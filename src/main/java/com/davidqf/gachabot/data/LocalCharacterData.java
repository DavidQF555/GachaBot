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
    private int baseHP;
    private int baseDefense;
    private int baseAttack;
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

        int totalBase = getBaseTotal();
        int base = totalBase / 6;
        baseHP = base;
        baseAttack = base;
        baseDefense = base;
        totalBase -= base * 3;
        while (totalBase > getBaseTotal() / 10) {
            int hp = (int) (Math.random() * totalBase + 1);
            int attack = (int) (Math.random() * totalBase + 1);
            int defense = (int) (Math.random() * totalBase + 1);
            if (hp + attack + defense <= totalBase) {
                baseHP += hp;
                baseAttack += attack;
                baseDefense += defense;
                totalBase -= hp + attack + defense;
            }
        }
        int change = totalBase / 3;
        baseAttack += change;
        baseDefense += change;
        baseHP += change;

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
        return baseHP;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public AbilityAbstract getAbility() {
        return ability;
    }

    public void setRate(double rate) {
        this.rate = rate;
        updateStats();
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

    private void updateStats() {
        int total = baseHP + baseAttack + baseDefense;
        double hp = baseHP * 1.0 / total;
        double def = baseDefense * 1.0 / total;
        double att = baseAttack * 1.0 / total;
        int base = getBaseTotal();
        baseHP = (int) (base * hp + 0.5);
        baseDefense = (int) (base * def + 0.5);
        baseAttack = (int) (base * att + 0.5);
    }

    private int getBaseTotal() {
        return 100 + (int) (100 / rate + 0.5);
    }
}
