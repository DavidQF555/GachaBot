package com.david.gachabot.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.david.gachabot.*;

import net.dv8tion.jda.api.entities.Message;

public class CharacterListCommand extends Command {

	@Override
	public void onCommand(Message m) {
		String out = m.getAuthor().getAsMention() + " The following are all the characters: ```";
		for(LocalCharacterData data : Bot.characters.values()) {
			String name = data.getName();
			while(name.length() < 25) {
				name += " ";
			}
			out += "\n" + name.substring(0, 25) + " " + getRoundedPercent(data.getRate(), 2);
		}
		m.getChannel().sendMessage(out + "```").queue();
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
	public List<String> getAlternativeNames() {
		return new ArrayList<String>(Arrays.asList("c"));
	}

	private String getRoundedPercent(double d, int decDigits) {
		return (int) (d * Math.pow(10, decDigits + 2) + 0.5) / Math.pow(10, decDigits) + "%";
	}
}
