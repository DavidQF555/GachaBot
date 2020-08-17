package com.david.gachabot.data;

import com.david.gachabot.Bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SeriesData implements Serializable {

    private static final long serialVersionUID = 10;
    private final static List<SeriesData> series = new ArrayList<>();
    private final int id;
    private final List<LocalAnimeData> anime;
    private final List<LocalCharacterData> characters;

    public SeriesData() {
        anime = new ArrayList<>();
        characters = new ArrayList<>();
        Bot.lastID++;
        this.id = Bot.lastID;
        series.add(this);
    }

    public static List<SeriesData> getSeries() {
        return series;
    }

    public List<LocalAnimeData> getAnime() {
        return anime;
    }

    public List<LocalCharacterData> getCharacters() {
        return characters;
    }

    public int getID() {
        return id;
    }
}
