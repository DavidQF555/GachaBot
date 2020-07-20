package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.Bot;
import com.david.gachabot.data.CharacterInstanceData;

import net.dv8tion.jda.api.entities.Message;

@Command
public class RetrieveUserCharacterListCommand extends CommandAbstract {

	@Override
	public void onCommand(Message m) {
		long id = m.getAuthor().getIdLong();
		if(Bot.userData.containsKey(id)) {
			String out = m.getAuthor().getAsMention() + " You own the following: ```";
			for(CharacterInstanceData data : Bot.userData.get(id).getCharacters().values()) {
				out += "\n" + data.getCharacterData().getName();
			}
			m.getChannel().sendMessage(out + "```").queue();
			return;
		}
		m.getChannel().sendMessage(m.getAuthor().getAsMention() + " You have nothing").queue();
	}

	@Override
	public String getActivatingName() {
		return "mylist";
	}

	@Override
	public List<String> getAlternativeNames() {
		return new ArrayList<String>(Arrays.asList("ml"));
	}

	@Override
	public String getDescription() {
		return "Retrieves the user's character list";
	}

}
