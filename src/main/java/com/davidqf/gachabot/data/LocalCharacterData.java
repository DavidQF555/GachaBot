package com.davidqf.gachabot.data;

import java.util.HashSet;
import java.util.Set;

import com.davidqf.gachabot.Bot;
import com.davidqf.gachabot.abilities.AbilityAbstract;

public class LocalCharacterData implements Comparable<LocalCharacterData> {

    private SeriesData series;
    private final int id;
    private double rate;
    private String name;
    private String image_url;
    private int member_favorites;
    private final Set<Integer> animeography;
    private double baseHP;
    private double baseDefense;
    private double baseAttack;
    private final AbilityAbstract ability;

    public LocalCharacterData(SeriesData series, int id, int member_favorites, double rate, String image_url, String name) {
        this.series = series;
        series.getCharacters().add(this);
        this.id = id;
        this.rate = rate;
        this.name = name;
        this.image_url = image_url;
        this.member_favorites = member_favorites;
        animeography = new HashSet<>();

        baseHP = 0;
        baseDefense = 0;
        baseAttack = 0;
        double total = 1;
        do {
            double cHp = (Math.random() * (total * 0.9)) + total * 0.1;
            double cDefense = (Math.random() * (total * 0.9)) + total * 0.1;
            double cAttack = (Math.random() * (total * 0.9)) + total * 0.1;
            if (cHp + cDefense + cAttack <= 1) {
                baseHP += total * cHp;
                baseDefense += total * cDefense;
                baseAttack += total * cAttack;
                total = 1 - baseHP - baseDefense - baseAttack;
            }
        } while (baseHP + baseDefense + baseAttack < 0.8);
        double change = total / 3;
        baseHP += total - 2 * change;
        baseAttack += change;
        baseDefense += change;

        ability = Bot.abilities.get((int) (Math.random() * Bot.abilities.size()));
    }

    public LocalCharacterData(SeriesData series, int id, int member_favorites, double rate, String image_url, String name, int hp, int attack, int defense, AbilityAbstract ability, Set<Integer> animeography) {
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

    public int getID() {
        return id;
    }

    public double getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return image_url;
    }

    public int getMemberFavorites() {
        return member_favorites;
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

    public AbilityAbstract getAbility() {
        return ability;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String url) {
        image_url = url;
    }

    public void setMemberFavorites(int fav) {
        member_favorites = fav;
    }

    public void setSeries(SeriesData series) {
        series.getCharacters().remove(this);
        this.series = series;
        series.getCharacters().add(this);
    }

    @Override
    public int compareTo(LocalCharacterData o) {
        return name.compareTo(o.getName());
    }

    private int getBaseTotal() {
        return 100 + (int) (100 / rate + 0.5);
    }
}
