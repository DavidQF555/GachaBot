package com.david.gachabot;

import java.util.List;
import java.util.concurrent.*;

import com.github.doomsdayrs.jikan4java.core.search.animemanga.AnimeSearch;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.anime.character_staff.*;

public class Util {

	public static Anime animeSearch(int id) {
		AnimeSearch as = new AnimeSearch();
		CompletableFuture<Anime> cf = as.getByID(id);
		while(!cf.isDone()) {};
		Anime a = cf.join();
		try {
			TimeUnit.SECONDS.sleep(4);
		} 
		catch (Exception e) {}
		return a;
	}

	public static Anime animeSearch(String query) {
		AnimeSearch as = new AnimeSearch().setLimit(1).setQuery(query);
		CompletableFuture<Anime> cf = as.getFirst();
		while(!cf.isDone()) {}
		Anime a = cf.join();
		try {
			TimeUnit.SECONDS.sleep(4);
		} 
		catch (Exception e) {}
		return a;
	}

	public static List<AnimeCharacter> animeCharacters(Anime a) {
		CompletableFuture<Character_Staff> cf = a.getCharacterStaffs();
		while(!cf.isDone()) {}
		Character_Staff s = cf.join();
		try {
			TimeUnit.SECONDS.sleep(4);
		} 
		catch (Exception e) {}
		return s.animeCharacters;
	}

	public static com.github.doomsdayrs.jikan4java.types.main.character.Character getCharacter(int id) {
		CompletableFuture<com.github.doomsdayrs.jikan4java.types.main.character.Character> cf = Bot.connector.retrieveCharacter(id);
		while(!cf.isDone()) {}
		com.github.doomsdayrs.jikan4java.types.main.character.Character c = cf.join();
		try {
			TimeUnit.SECONDS.sleep(4);
		} 
		catch (Exception e) {}
		return c;
	}
}
