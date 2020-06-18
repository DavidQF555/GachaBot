package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.*;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.support.related.*;

import net.dv8tion.jda.api.entities.Message;

public class AddAnimeCommand extends Command {

	@Override
	public void onCommand(Message m) {
		String input = m.getContentRaw().substring(Reference.COMMAND.length() + getActivatingName().length() + 1);
		System.out.println("Getting all related to: " + input);
		m.getChannel().sendMessage(m.getAuthor().getAsMention() + " Searching for " + input + " and all related series").queue();
		Anime a = Util.animeSearch(input);
		List<Anime> uniq = getAllRelated(a, new ArrayList<Anime>());
		List<Anime> all = uniq.subList(0, uniq.size());
		for(int i = uniq.size() - 1; i >= 0; i --) {
			for(int id : Bot.anime) {
				if(uniq.get(i).mal_id == id) {
					uniq.remove(i);
					break;
				}
			}
		}
		if(uniq.isEmpty()) {
			m.getChannel().sendMessage(m.getAuthor().getAsMention() + " ```Everything is already added```").queue();
			return;
		}
		Bot.addBestCharactersFromAnime(all);
		String out = m.getAuthor().getAsMention() + " Added the following series: ```";
		for(Anime an : uniq) {
			Bot.anime.add(an.mal_id);
			out += "\n" + an.title;
		}
		m.getChannel().sendMessage(out + "```").queue();
	}

	@Override
	public void onPrivateMessage(Message m) {
		onCommand(m);
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

	@Override
	public String getActivatingName() {
		return "addanime";
	}

	@Override
	public String getDescription() {
		return "Adds a new anime series and characters";
	}

	private List<Anime> getAllRelated(Anime st, List<Anime> vis) {
		vis.add(st);
		Related rel = st.related;
		ArrayList<RelatedType> pre = rel.prequel;
		if(pre != null) {
			check:
				for(RelatedType type : pre) {
					for(Anime a : vis) {
						if(type.mal_id == a.mal_id) {
							continue check;
						}
					}
					getAllRelated(Util.animeSearch(type.mal_id), vis);
				}
		}
		ArrayList<RelatedType> seq = rel.sequels;
		if(seq != null) {
			check:
				for(RelatedType type : seq) {
					for(Anime a : vis) {
						if(type.mal_id == a.mal_id) {
							continue check;
						}
					}
					getAllRelated(Util.animeSearch(type.mal_id), vis);
				}
		}
		return vis;
	}
}
