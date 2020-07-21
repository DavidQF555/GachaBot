package com.david.gachabot.commands;

import java.util.ArrayList;

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
		m.getChannel().sendMessage(Util.createMessage("Searching for `" + input + "` and all related series").build()).queue();
		Anime a = JikanRetriever.animeSearch(input);
		String mes = "Removed the following: ```";
		LocalAnimeData an = Bot.anime.get(a.mal_id);
		if(an == null) {
			m.getChannel().sendMessage(Util.createFailedMessage(a.title + " has not been not added yet").build()).queue();
			return;
		}
		SeriesData series = an.getSeries();
		for(LocalAnimeData data : series.getAnime()) {
			for(int id : new ArrayList<Integer>(Bot.anime.keySet())) {
				if(data.getID() == id) {
					Bot.anime.remove(id);
					mes += "\n" + data.getTitle();
				}
			}
		}
		for(LocalCharacterData data : series.getCharacters()) {
			for(int id : new ArrayList<Integer>(Bot.characters.keySet())) {
				if(data.getID() == id) {
					Bot.characters.remove(id);
				}
			}
		}
		Bot.updateExistingCharactersList(null, null);
		m.getChannel().sendMessage(Util.createMessage(mes + "```").build()).queue();
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
