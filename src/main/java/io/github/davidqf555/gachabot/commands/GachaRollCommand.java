package io.github.davidqf555.gachabot.commands;

import io.github.davidqf555.gachabot.Bot;
import io.github.davidqf555.gachabot.Util;
import io.github.davidqf555.gachabot.data.CharacterInstanceData;
import io.github.davidqf555.gachabot.data.LocalCharacterData;
import io.github.davidqf555.gachabot.data.UserData;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GachaRollCommand extends CommandAbstract {

    private final static long COST = 100;

    @Override
    public void onCommand(Message m, String content) {
        long userId = m.getAuthor().getIdLong();
        UserData user = Bot.userData.get(userId);
        long gems = user.getGems();
        if (Bot.characters.size() > 0) {
            if (gems >= COST) {
                user.setGems(gems - COST);
                double roll = Math.random();
                double chance = 0.0;
                for (LocalCharacterData data : Bot.characters.values()) {
                    chance += data.getRate();
                    if (roll < chance) {
                        Map<LocalCharacterData, CharacterInstanceData> chData = Bot.userData.get(userId).getCharacters();
                        if (chData.containsKey(data)) {
                            CharacterInstanceData inst = chData.get(data);
                            inst.setStars(inst.getStars() + 1);
                        } else {
                            chData.put(data, new CharacterInstanceData(data, userId));
                        }
                        m.getChannel().sendMessage(Util.createMessage(m.getAuthor().getName() + ", you pulled " + data.getName() + "! ").setImage(data.getImageUrl()).build()).queue();
                        return;
                    }
                }
            } else {
                m.getChannel().sendMessage(Util.createFailedMessage(m.getAuthor().getName() + ", you do not have enough gems").build()).queue();
            }
        } else {
            m.getChannel().sendMessage(Util.createFailedMessage("There are currently no added characters").build()).queue();
        }
    }

    @Override
    public List<String> getAlternativeNames() {
        return new ArrayList<String>(Collections.singletonList("r"));
    }

    @Override
    public String getDescription() {
        return "Uses " + COST + " gems to randomly a character";
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.ROLL;
    }
}