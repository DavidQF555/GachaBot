package io.github.davidqf555.gachabot.data;

public class LocalAnimeData {

    private final String title;
    private final int id;
    private SeriesData series;

    public LocalAnimeData(SeriesData series, String title, int id) {
        this.series = series;
        series.getAnime().add(this);
        this.title = title;
        this.id = id;
    }

    public SeriesData getSeries() {
        return series;
    }

    public void setSeries(SeriesData series) {
        series.getAnime().remove(this);
        this.series = series;
        series.getAnime().add(this);
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
