package io.github.davidqf555.gachabot.data;

import io.github.davidqf555.gachabot.abilities.AbilityType;

import java.util.HashSet;
import java.util.Set;

public class LocalCharacterData implements Comparable<LocalCharacterData> {

    private final int id;
    private final Set<Integer> animeography;
    private final double baseHP;
    private final double baseDefense;
    private final double baseAttack;
    private final AbilityType ability;
    private SeriesData series;
    private double rate;
    private String name;
    private String image_url;
    private int member_favorites;

    public LocalCharacterData(SeriesData series, int id, int member_favorites, double rate, String image_url, String name) {
        this.series = series;
        series.getCharacters().add(this);
        this.id = id;
        this.rate = rate;
        this.name = name;
        this.image_url = image_url;
        this.member_favorites = member_favorites;
        animeography = new HashSet<>();

        double rHP = Math.random() * 0.8 + 0.2;
        double rDefense = Math.random() * 0.8 + 0.2;
        double rAttack = Math.random() * 0.8 + 0.2;
        double total = rHP + rDefense + rAttack;
        baseHP = rHP / total;
        baseDefense = rDefense / total;
        baseAttack = rAttack / total;

        AbilityType[] abilities = AbilityType.values();
        ability = abilities[(int) (Math.random() * abilities.length)];
    }

    public LocalCharacterData(SeriesData series, int id, int member_favorites, double rate, String image_url, String name, int hp, int attack, int defense, AbilityType ability, Set<Integer> animeography) {
        this.series = series;
        series.getCharacters().add(this);
        this.id = id;
        this.member_favorites = member_favorites;
        this.rate = rate;
        this.image_url = image_url;
        this.name = name;
        baseHP = hp;
        baseAttack = attack;
        baseDefense = defense;
        this.ability = ability;
        this.animeography = animeography;
    }

    public SeriesData getSeries() {
        return series;
    }

    public void setSeries(SeriesData series) {
        series.getCharacters().remove(this);
        this.series = series;
        series.getCharacters().add(this);
    }

    public int getID() {
        return id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return image_url;
    }

    public void setImageUrl(String url) {
        image_url = url;
    }

    public int getMemberFavorites() {
        return member_favorites;
    }

    public void setMemberFavorites(int fav) {
        member_favorites = fav;
    }

    public Set<Integer> getAnimeography() {
        return animeography;
    }

    public int getBaseHP() {
        return (int) (getBaseTotal() * baseHP + 0.5);
    }

    public int getBaseDefense() {
        return (int) (getBaseTotal() * baseDefense + 0.5);
    }

    public int getBaseAttack() {
        return (int) (getBaseTotal() * baseAttack + 0.5);
    }

    public AbilityType getAbilityType() {
        return ability;
    }

    @Override
    public int compareTo(LocalCharacterData o) {
        return name.compareTo(o.getName());
    }

    private int getBaseTotal() {
        return 100 + (int) (100 / rate + 0.5);
    }
}
