package com.david.gachabot.commands;

import com.david.gachabot.*;

import net.dv8tion.jda.api.entities.Message;

public class ShutdownCommand extends Command {

	@Override
	public void onCommand(Message m) {
		m.getChannel().sendMessage(m.getAuthor().getAsMention() + " Shutting down").queue();
		Bot.jda.shutdown();
	}

	@Override
	public String getActivatingName() {
		return "shut";
	}

	@Override
	public boolean hasPermission(Message m) {
		if(m.getAuthor().getIdLong() != Reference.OWNER_ID) {
			return false;
		}
		return true;
	}

	@Override
	public String getDescription() {
		return "Shuts down this bot";
	}

}
