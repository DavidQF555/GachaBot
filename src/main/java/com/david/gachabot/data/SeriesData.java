package com.david.gachabot.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SeriesData implements Serializable {

    private static final long serialVersionUID = 10;
    private final List<LocalAnimeData> anime;
    private final List<LocalCharacterData> characters;

    public SeriesData() {
        anime = new ArrayList<>();
        characters = new ArrayList<>();
    }

    public List<LocalAnimeData> getAnime() {
        return anime;
    }

    public List<LocalCharacterData> getCharacters() {
        return characters;
    }
}
