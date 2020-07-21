package com.david.gachabot.commands;

import com.david.gachabot.*;

import net.dv8tion.jda.api.entities.Message;

@Command
public class SaveCommand extends CommandAbstract {

	@Override
	public void onCommand(Message m) {
		FileUtil.saveAnimeList();
		FileUtil.saveCharacterList();
		FileUtil.saveUserData();
		m.getChannel().sendMessage(Util.createMessage("Saved all data")).queue();
	}

	@Override
	public boolean hasPermission(Message m) {
		if(m.getAuthor().getIdLong() == Reference.OWNER_ID) {
			return true;
		}
		return false;
	}

	@Override
	public String getActivatingName() {
		return "save";
	}

	@Override
	public String getDescription() {
		return "Saves all data";
	}

}
