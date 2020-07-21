package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.*;
import com.david.gachabot.data.LocalCharacterData;

import net.dv8tion.jda.api.entities.Message;

@Command
public class CharacterListCommand extends CommandAbstract {

	private static int PAGE_ENTRIES = 15;
	private static int NAME_LENGTH = 25;
	private static int RATE_LENGTH = 15;
	private static int HP_LENGTH = 7;
	private static int ATTACK_LENGTH = 7;
	private static int DEFENSE_LENGTH = 7;
	private static int ABILITY_LENGTH = 15;

	@Override
	public void onCommand(Message m) {
		int size = Bot.characters.size();
		if(size < 1) {
			m.getChannel().sendMessage(Util.createFailedMessage("No characters are currently added").build()).queue();
			return;
		}
		int totalPages = size / PAGE_ENTRIES;
		if(size % PAGE_ENTRIES > 0) {
			totalPages ++;
		}
		String[] s = m.getContentRaw().split(" ");
		int page = 1;
		if(s.length >= 2) {
			try {
				page = Integer.parseInt(s[1]);
			}
			catch(Exception e) {}
		}
		if(page > totalPages) {
			page = totalPages;
		}
		else if(page < 1) {
			page = 1;
		}
		String out = "```";
		String descName = "Name";
		while(descName.length() < NAME_LENGTH) {
			descName += " ";
		}
		String descRate = "Rate";
		while(descRate.length() < RATE_LENGTH) {
			descRate += " ";
		}
		String descHP = "HP";
		while(descHP.length() < HP_LENGTH) {
			descHP += " ";
		}
		String descAtt = "Attack";
		while(descAtt.length() < ATTACK_LENGTH) {
			descAtt += " ";
		}
		String descDef = "Defense";
		while(descDef.length() < DEFENSE_LENGTH) {
			descDef += " ";
		}
		String descAb = "Ability";
		while(descAb.length() < ABILITY_LENGTH) {
			descAb += " ";
		}
		out += "\n" + descName.substring(0, NAME_LENGTH) + " " + descRate.substring(0, RATE_LENGTH) + " " + descHP.substring(0, HP_LENGTH) + " " + descAtt.substring(0, ATTACK_LENGTH) + " " + descDef.substring(0, DEFENSE_LENGTH) + " " + descAb.substring(0, ABILITY_LENGTH);
		String div = "\n";
		while(div.length() < NAME_LENGTH + RATE_LENGTH + HP_LENGTH + ATTACK_LENGTH + DEFENSE_LENGTH + ABILITY_LENGTH + 6) {
			div += "-";
		}
		out += div;
		List<LocalCharacterData> d = new ArrayList<LocalCharacterData>(Bot.characters.values());
		int max = page * PAGE_ENTRIES;
		if(max > d.size()) {
			max = d.size();
		}
		for(int i = (page - 1) * PAGE_ENTRIES; i < max; i ++) {
			LocalCharacterData data = d.get(i);
			String name = data.getName();
			while(name.length() < NAME_LENGTH) {
				name += " ";
			}
			String rate = getRoundedPercent(data.getRate(), 2);
			while(rate.length() < RATE_LENGTH) {
				rate += " ";
			}
			String hp = "" + data.getBaseHP();
			while(hp.length() < HP_LENGTH) {
				hp += " ";
			}
			String attack = "" + data.getBaseAttack();
			while(attack.length() < ATTACK_LENGTH) {
				attack += " ";
			}
			String defense = "" + data.getBaseDefense();
			while(defense.length() < DEFENSE_LENGTH) {
				defense += " ";
			}
			String ability = data.getAbility().getName();
			while(ability.length() < ABILITY_LENGTH) {
				ability += " ";
			}
			out += "\n" + name.substring(0, NAME_LENGTH) + " " + rate.substring(0, RATE_LENGTH) + " " + hp.substring(0, HP_LENGTH) + " " + attack.substring(0, ATTACK_LENGTH) + " " + defense.substring(0, DEFENSE_LENGTH) + " " + ability.substring(0, ABILITY_LENGTH);
		}
		m.getChannel().sendMessage(out + div + "\nPage " + page + "/" + totalPages + "```").queue();
	}

	@Override
	public String getActivatingName() {
		return "characters";
	}

	@Override
	public String getDescription() {
		return "Retrieves a list of all characters";
	}

	@Override
	public String getFormat() {
		return super.getFormat() + " [page number]";
	}

	@Override
	public List<String> getAlternativeNames() {
		return new ArrayList<String>(Arrays.asList("c"));
	}

	private String getRoundedPercent(double d, int decDigits) {
		return (int) (d * Math.pow(10, decDigits + 2) + 0.5) / Math.pow(10, decDigits) + "%";
	}
}
