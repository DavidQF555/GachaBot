package com.david.gachabot.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.david.gachabot.*;

import net.dv8tion.jda.api.entities.Message;

public class SaveCommand extends Command {

	@Override
	public void onCommand(Message m) {
		Bot.saveAnimeList();
		Bot.saveCharacterList();
		Bot.saveUserData();
		m.getChannel().sendMessage(m.getAuthor().getAsMention() + " Saved all data").queue();
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
	public List<String> getAlternativeNames() {
		return new ArrayList<String>(Arrays.asList("s"));
	}

	@Override
	public String getDescription() {
		return "Saves all data";
	}

}
