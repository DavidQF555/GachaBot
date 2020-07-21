package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.Bot;
import com.david.gachabot.Util;
import com.david.gachabot.data.CharacterInstanceData;

import net.dv8tion.jda.api.entities.Message;

@Command
public class RetrieveUserCharacterListCommand extends CommandAbstract {

	@Override
	public void onCommand(Message m) {
		long id = m.getAuthor().getIdLong();
		if(Bot.userData.containsKey(id)) {
			String out = m.getAuthor().getName() + ", you own the following: ```";
			for(CharacterInstanceData data : Bot.userData.get(id).getCharacters().values()) {
				out += "\n" + data.getCharacterData().getName();
			}
			m.getChannel().sendMessage(Util.createMessage(out + "```").build()).queue();
			return;
		}
		m.getChannel().sendMessage(Util.createMessage(m.getAuthor().getName() + ", you have nothing").build()).queue();
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
