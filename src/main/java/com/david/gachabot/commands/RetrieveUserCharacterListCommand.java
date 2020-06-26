package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.Bot;

import net.dv8tion.jda.api.entities.Message;

public class RetrieveUserCharacterListCommand extends Command {

	@Override
	public void onCommand(Message m) {
		long id = m.getAuthor().getIdLong();
		if(Bot.userData.containsKey(id)) {
			String out = m.getAuthor().getAsMention() + " You own the following: ```";
			for(int chId : Bot.userData.get(id).getCharacters().keySet()) {
				out += "\n" + Bot.characters.get(chId).getName();
			}
			m.getChannel().sendMessage(out + "```").queue();
			return;
		}
		m.getChannel().sendMessage(m.getAuthor().getAsMention() + " ```You have nothing```").queue();
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
