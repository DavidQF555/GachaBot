package com.davidqf.gachabot.commands;

import com.davidqf.gachabot.Bot;
import com.davidqf.gachabot.Util;
import com.davidqf.gachabot.data.LocalCharacterData;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterListCommand extends CommandAbstract {

    private final static int PAGE_ENTRIES = 15;
    private final static int NAME_LENGTH = 25;
    private final static int RATE_LENGTH = 15;
    private final static int HP_LENGTH = 7;
    private final static int ATTACK_LENGTH = 7;
    private final static int DEFENSE_LENGTH = 7;
    private final static int ABILITY_LENGTH = 15;

    @Override
    public void onCommand(Message m, String content) {
        int size = Bot.characters.size();
        if (size < 1) {
            m.getChannel().sendMessage(Util.createFailedMessage("No characters are currently added").build()).queue();
            return;
        }
        int totalPages = size / PAGE_ENTRIES;
        if (size % PAGE_ENTRIES > 0) {
            totalPages++;
        }
        String[] s = content.split(" ");
        int page = 1;
        if (s.length >= 2) {
            try {
                page = Integer.parseInt(s[1]);
            } catch (Exception ignored) {
            }
        }
        if (page > totalPages) {
            page = totalPages;
        } else if (page < 1) {
            page = 1;
        }
        StringBuilder out = new StringBuilder("```");
        StringBuilder descName = new StringBuilder("Name");
        while (descName.length() < NAME_LENGTH) {
            descName.append(" ");
        }
        StringBuilder descRate = new StringBuilder("Rate");
        while (descRate.length() < RATE_LENGTH) {
            descRate.append(" ");
        }
        StringBuilder descHP = new StringBuilder("HP");
        while (descHP.length() < HP_LENGTH) {
            descHP.append(" ");
        }
        StringBuilder descAtt = new StringBuilder("Attack");
        while (descAtt.length() < ATTACK_LENGTH) {
            descAtt.append(" ");
        }
        StringBuilder descDef = new StringBuilder("Defense");
        while (descDef.length() < DEFENSE_LENGTH) {
            descDef.append(" ");
        }
        StringBuilder descAb = new StringBuilder("Ability");
        while (descAb.length() < ABILITY_LENGTH) {
            descAb.append(" ");
        }
        out.append("\n").append(descName.substring(0, NAME_LENGTH)).append(" ").append(descRate.substring(0, RATE_LENGTH)).append(" ").append(descHP.substring(0, HP_LENGTH)).append(" ").append(descAtt.substring(0, ATTACK_LENGTH)).append(" ").append(descDef.substring(0, DEFENSE_LENGTH)).append(" ").append(descAb.substring(0, ABILITY_LENGTH));
        StringBuilder div = new StringBuilder("\n");
        while (div.length() < NAME_LENGTH + RATE_LENGTH + HP_LENGTH + ATTACK_LENGTH + DEFENSE_LENGTH + ABILITY_LENGTH + 6) {
            div.append("-");
        }
        out.append(div);
        List<LocalCharacterData> d = new ArrayList<>(Bot.characters.values());
        int max = page * PAGE_ENTRIES;
        if (max > d.size()) {
            max = d.size();
        }
        for (int i = (page - 1) * PAGE_ENTRIES; i < max; i++) {
            LocalCharacterData data = d.get(i);
            StringBuilder name = new StringBuilder(data.getName());
            while (name.length() < NAME_LENGTH) {
                name.append(" ");
            }
            StringBuilder rate = new StringBuilder(getRoundedPercent(data.getRate(), 2));
            while (rate.length() < RATE_LENGTH) {
                rate.append(" ");
            }
            StringBuilder hp = new StringBuilder("" + data.getBaseHP());
            while (hp.length() < HP_LENGTH) {
                hp.append(" ");
            }
            StringBuilder attack = new StringBuilder("" + data.getBaseAttack());
            while (attack.length() < ATTACK_LENGTH) {
                attack.append(" ");
            }
            StringBuilder defense = new StringBuilder("" + data.getBaseDefense());
            while (defense.length() < DEFENSE_LENGTH) {
                defense.append(" ");
            }
            StringBuilder ability = new StringBuilder(data.getAbilityType().toString());
            while (ability.length() < ABILITY_LENGTH) {
                ability.append(" ");
            }
            out.append("\n").append(name.substring(0, NAME_LENGTH)).append(" ").append(rate.substring(0, RATE_LENGTH)).append(" ").append(hp.substring(0, HP_LENGTH)).append(" ").append(attack.substring(0, ATTACK_LENGTH)).append(" ").append(defense.substring(0, DEFENSE_LENGTH)).append(" ").append(ability.substring(0, ABILITY_LENGTH));
        }
        m.getChannel().sendMessage(out.toString() + div + "\nPage " + page + "/" + totalPages + "```").queue();
    }

    @Override
    public String getDescription() {
        return "Retrieves a list of all characters";
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.CHARACTER_LIST;
    }

    @Override
    public String getFormat() {
        return super.getFormat() + " [page number]";
    }

    @Override
    public List<String> getAlternativeNames() {
        return new ArrayList<>(Collections.singletonList("c"));
    }

    private String getRoundedPercent(double d, int decDigits) {
        return (int) (d * Math.pow(10, decDigits + 2) + 0.5) / Math.pow(10, decDigits) + "%";
    }
}
