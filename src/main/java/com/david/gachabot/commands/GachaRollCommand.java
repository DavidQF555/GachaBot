package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.*;
import com.david.gachabot.data.*;

import net.dv8tion.jda.api.entities.Message;

@Command
public class GachaRollCommand extends CommandAbstract {

	@Override
	public void onCommand(Message m) {
		double roll = Math.random();
		double chance = 0.0;
		for(LocalCharacterData data : Bot.characters.values()) {
			chance += data.getRate();
			if(roll < chance) {
				long userId = m.getAuthor().getIdLong();
				Map<Integer, CharacterInstanceData> chData = Bot.userData.get(userId).getCharacters();
				if(chData.containsKey(data.getID())) {
					CharacterInstanceData inst = chData.get(data.getID());
					inst.setStars(inst.getStars() + 1);
				}
				else {
					chData.put(data.getID(), new CharacterInstanceData(userId, data.getID()));
				}
				m.getChannel().sendMessage(m.getAuthor().getAsMention() + " You pulled " + data.getName() + "\n" + data.getImageUrl()).queue();
				return;
			}
		}
	}

	@Override
	public boolean allowInBattle() {
		return false;
	}

	@Override
	public String getActivatingName() {
		return "roll";
	}

	@Override
	public List<String> getAlternativeNames() {
		return new ArrayList<String>(Arrays.asList("r"));
	}

	@Override
	public String getDescription() {
		return "Randomly gives the user a character";
	}

}
