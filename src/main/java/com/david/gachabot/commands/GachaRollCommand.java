package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.*;
import com.david.gachabot.data.*;

import net.dv8tion.jda.api.entities.Message;

@Command
public class GachaRollCommand extends CommandAbstract {

	private final static long COST = 100;

	@Override
	public void onCommand(Message m) {
		long userId = m.getAuthor().getIdLong();
		UserData user = Bot.userData.get(userId);
		long gems = user.getGems();
		if(gems >= COST) {
			user.setGems(gems - COST);
			double roll = Math.random();
			double chance = 0.0;
			for(LocalCharacterData data : Bot.characters.values()) {
				chance += data.getRate();
				if(roll < chance) {
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
		else {
			m.getChannel().sendMessage(m.getAuthor().getAsMention() + " You do not have enough gems").queue();
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
		return "Uses " + COST + " gems to randomly a character";
	}
}