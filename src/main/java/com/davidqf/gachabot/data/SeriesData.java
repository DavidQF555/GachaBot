package com.davidqf.gachabot.data;

import com.davidqf.gachabot.Bot;

import java.util.ArrayList;
import java.util.List;

public class SeriesData {

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
