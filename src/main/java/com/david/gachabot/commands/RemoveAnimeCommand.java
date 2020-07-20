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
		m.getChannel().sendMessage(m.getAuthor().getAsMention() + " Searching for `" + input + "` and all related series").queue();
		Anime a = JikanRetriever.animeSearch(input);
		List<Anime> related = AddAnimeCommand.getAllRelated(a, new ArrayList<Anime>());
		String mes = m.getAuthor().getAsMention() + " Removed the following: ```";
		Set<Integer> rem = new HashSet<Integer>();
		for(Anime an : related) {
			List<LocalAnimeData> anime = new ArrayList<LocalAnimeData>(Bot.anime.values());
			for(LocalAnimeData data : anime) {
				int id = data.getID();
				if(an.mal_id == id) {
					rem.add(id);
					Bot.anime.remove(id);
					mes += "\n" + data.getTitle();
					break;
				}
			}
		}
		if(rem.isEmpty()) {
			m.getChannel().sendMessage(m.getAuthor().getAsMention() + " " + a.title + " has not been not added").queue();
			return;
		}
		List<LocalCharacterData> vals = new ArrayList<LocalCharacterData>(Bot.characters.values());
		for(LocalCharacterData data : vals) {
			for(int id : data.getAnimeography()) {
				if(rem.contains(id)) {
					Bot.characters.remove(data.getID());
				}
			}
		}
		m.getChannel().sendMessage(mes + "```").queue();
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
