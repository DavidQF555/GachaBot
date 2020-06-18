package com.david.gachabot.commands;

import com.david.gachabot.Reference;

import net.dv8tion.jda.api.entities.*;

public abstract class Command {

	public void onCommand(Message m) {}

	public void onPrivateMessage(Message m) {
		m.getChannel().sendMessage(m.getAuthor().getAsMention() + " Cannot use this command in private chat").queue();
	}

	public boolean hasPermission(Message m) {
		return true;
	}

	public boolean correctFormat(Message m) {
		return true;
	}

	public String getFormat() {
		return Reference.COMMAND + getActivatingName();	
	}

	public abstract String getActivatingName();

	public abstract String getDescription();

}
