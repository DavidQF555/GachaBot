package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.*;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.anime.character_staff.AnimeCharacter;
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
			if(Bot.anime.get(uniq.get(i).mal_id) != null) {
				uniq.remove(i);
			}
		}
		if(uniq.isEmpty()) {
			m.getChannel().sendMessage(m.getAuthor().getAsMention() + " ```Everything is already added```").queue();
			return;
		}
		Bot.current ++;
		addBestCharactersFromAnime(all);
		String out = m.getAuthor().getAsMention() + " Added the following series: ```";
		for(Anime an : uniq) {
			Bot.anime.put(an.mal_id, new LocalAnimeData(Bot.current, an.mal_id));
			out += "\n" + an.title;
		}
		m.getChannel().sendMessage(out + "```").queue();
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

	private static void addBestCharactersFromAnime(List<Anime> an){
		List<AnimeCharacter> pos = new ArrayList<AnimeCharacter>();
		double avg = 0;
		int aired = 0;
		for(Anime a : an) {
			if(!a.status.equals("Not yet aired")) {
				avg += a.popularity;
				aired ++;
			}
		}
		avg /= aired;
		double limit = avg / 150000 / an.size() + 5;
		for(Anime a : an) {
			List<AnimeCharacter> chs = Util.animeCharacters(a);
			int count = 0;
			check:
				for(AnimeCharacter c : chs) {
					for(AnimeCharacter check : pos) {
						if(c.mal_id == check.mal_id) {
							continue check;
						}
					}
					pos.add(c);
					count ++;
					if(count >= limit) {
						break;
					}
				}
		}
		List<com.github.doomsdayrs.jikan4java.types.main.character.Character> chars = new ArrayList<com.github.doomsdayrs.jikan4java.types.main.character.Character>();
		double cut = 0;
		for(AnimeCharacter ac : pos) {
			com.github.doomsdayrs.jikan4java.types.main.character.Character c = Util.getCharacter(ac.mal_id);
			if(c.member_favorites > 100) {
				chars.add(c);
				cut += c.member_favorites;
			}
		}
		cut /= chars.size();
		double charMin = avg / 500000 + 2;
		List<com.github.doomsdayrs.jikan4java.types.main.character.Character> out = new ArrayList<com.github.doomsdayrs.jikan4java.types.main.character.Character>();
		while(out.size() < charMin && !chars.isEmpty()) {
			for(int i = chars.size() - 1; i >= 0; i --) {
				com.github.doomsdayrs.jikan4java.types.main.character.Character c = chars.get(i);
				if(c.member_favorites >= cut) {
					out.add(c);
					chars.remove(i);
				}
			}
			cut *= 0.95;
		}
		Bot.updateExistingCharactersList(out);
	}
}