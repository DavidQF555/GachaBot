package com.david.gachabot.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.david.gachabot.Bot;

import net.dv8tion.jda.api.entities.Message;

@Command
public class HelpCommand extends CommandAbstract {

	@Override
	public void onCommand(Message m) {
		String out = "```";
		for(CommandAbstract c : Bot.commands) {
			out += "\n" + c.getFormat() + ": " + c.getDescription();
		}
		m.getChannel().sendMessage(out + "```").queue();
	}

	@Override
	public String getActivatingName() {
		return "help";
	}
	
	@Override
	public List<String> getAlternativeNames() {
		return new ArrayList<String>(Arrays.asList("h"));
	}

	@Override
	public String getDescription() {
		return "Retrieves a list and description of all commands";
	}

}
