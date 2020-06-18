package com.david.gachabot.commands;

import com.david.gachabot.Bot;

import net.dv8tion.jda.api.entities.Message;

public class HelpCommand extends Command {

	@Override
	public void onCommand(Message m) {
		String out = m.getAuthor().getAsMention() + " The following are all commands: ```";
		for(Command c : Bot.commands) {
			out += "\n" + c.getFormat() + ": " + c.getDescription();
		}
		m.getChannel().sendMessage(out + "```").queue();
	}

	@Override
	public String getActivatingName() {
		return "help";
	}


	@Override
	public void onPrivateMessage(Message m) {
		onCommand(m);
	}


	@Override
	public String getDescription() {
		return "Retrieves a list and description of all commands";
	}

}
