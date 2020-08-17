package com.david.gachabot.data;

import java.io.Serializable;

public class LocalAnimeData implements Serializable {

    private static final long serialVersionUID = 2;
    private SeriesData series;
    private final String title;
    private final int id;

    public LocalAnimeData(SeriesData series, String title, int id) {
        this.series = series;
        series.getAnime().add(this);
        this.title = title;
        this.id = id;
    }

    public SeriesData getSeries() {
        return series;
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setSeries(SeriesData series) {
        series.getAnime().remove(this);
        this.series = series;
        series.getAnime().add(this);
    }
}
