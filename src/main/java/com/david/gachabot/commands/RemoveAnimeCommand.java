package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.*;
import com.david.gachabot.data.*;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;

import net.dv8tion.jda.api.entities.Message;

@Command
public class RemoveAnimeCommand extends CommandAbstract {

	@Override
	public void onCommand(Message m) {
		String input = m.getContentRaw().substring(Reference.COMMAND.length() + getActivatingName().length() + 1);
		System.out.println("Removing all related to: " + input);
		m.getChannel().sendMessage(Util.createMessage("Searching for `" + input + "` and all related series")).queue();
		Anime a = JikanRetriever.animeSearch(input);
		String mes = "Removed the following: ```";
		LocalAnimeData an = Bot.anime.get(a.mal_id);
		if(an == null) {
			m.getChannel().sendMessage(Util.createMessage(a.title + " has not been not added yet")).queue();
			return;
		}
		List<LocalAnimeData> rel = an.getRelated();
		List<Integer> rem = new ArrayList<Integer>();
		for(int id : new ArrayList<Integer>(Bot.anime.keySet())) {
			LocalAnimeData data = Bot.anime.get(id);
			if(rel.contains(data)) {
				Bot.anime.remove(id);
				mes += data.getTitle();
				rem.add(id);
			}
		}
		List<LocalCharacterData> vals = new ArrayList<LocalCharacterData>(Bot.characters.values());
		for(LocalCharacterData data : vals) {
			for(int id : data.getAnimeography()) {
				if(rem.contains(id)) {
					Bot.characters.remove(data.getID());
				}
			}
		}
		m.getChannel().sendMessage(Util.createMessage(mes + "```")).queue();
	}

	@Override
	public String getActivatingName() {
		return "removeanime";
	}

	@Override
	public String getDescription() {
		return "Removes all related anime and characters";
	}

	@Override
	public boolean correctFormat(Message m) {
		if(m.getContentRaw().indexOf(" ") != -1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasPermission(Message m) {
		if(m.getAuthor().getIdLong() != Reference.OWNER_ID) {
			return false;
		}
		return true;
	}

	@Override
	public String getFormat() {
		return Reference.COMMAND + getActivatingName() + " [anime name]";
	}

}
