package com.david.gachabot.commands;

import com.david.gachabot.*;

import net.dv8tion.jda.api.entities.Message;

@Command
public class ShutdownCommand extends CommandAbstract {

	@Override
	public void onCommand(Message m) {
		m.getChannel().sendMessage(Util.createMessage("Shutting Down")).queue();
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
