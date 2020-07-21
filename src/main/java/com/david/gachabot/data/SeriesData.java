package com.david.gachabot.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SeriesData implements Serializable {

	private static final long serialVersionUID = 10;
	private List<LocalAnimeData> anime;
	private List<LocalCharacterData> characters;

	public SeriesData() {
		anime = new ArrayList<LocalAnimeData>();
		characters = new ArrayList<LocalCharacterData>();
	}

	public List<LocalAnimeData> getAnime() {
		return anime;
	}

	public List<LocalCharacterData> getCharacters() {
		return characters;
	}
}
