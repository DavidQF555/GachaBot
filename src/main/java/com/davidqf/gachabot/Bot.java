package com.davidqf.gachabot;

import java.util.*;
import java.util.Map.Entry;

import javax.security.auth.login.LoginException;

import com.davidqf.gachabot.abilities.Ability;
import com.davidqf.gachabot.abilities.AbilityAbstract;
import com.davidqf.gachabot.commands.AddAnimeCommand;
import com.davidqf.gachabot.commands.Command;
import com.davidqf.gachabot.commands.CommandAbstract;
import com.davidqf.gachabot.data.LocalAnimeData;
import com.davidqf.gachabot.data.LocalCharacterData;
import com.davidqf.gachabot.data.SeriesData;
import com.davidqf.gachabot.data.UserData;
import org.reflections.Reflections;

import com.github.doomsdayrs.jikan4java.core.Connector;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.character.Animeography;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;

public class Bot {

    public final static Set<CommandAbstract> commands = new HashSet<>();
    public final static List<AbilityAbstract> abilities = new ArrayList<>();
    public static Map<Integer, LocalAnimeData> anime = new LinkedHashMap<>();
    public static Map<Integer, LocalCharacterData> characters = new LinkedHashMap<>();
    public static Map<Long, UserData> userData = new LinkedHashMap<>();
    public final static Connector connector = new Connector();
    public static JDA jda;
    public static User owner;
    public static int lastID = Integer.MIN_VALUE;

    public static void main(String[] args) {

        Reflections reflCom = new Reflections("com.davidqf.gachabot.commands");
        for (Class<?> c : reflCom.getTypesAnnotatedWith(Command.class)) {
            try {
                commands.add((CommandAbstract) c.getConstructor().newInstance());
            } catch (Exception ignored) {
            }
        }
        Reflections reflAb = new Reflections("com.davidqf.gachabot.abilities");
        for (Class<?> c : reflAb.getTypesAnnotatedWith(Ability.class)) {
            try {
                abilities.add((AbilityAbstract) c.getConstructor().newInstance());
            } catch (Exception ignored) {
            }
        }

        FileUtil.readSeriesData();
        FileUtil.readUserData();

        for (LocalAnimeData data : anime.values()) {
            int id = data.getSeries().getID();
            if (id > lastID) {
                lastID = id;
            }
        }

        updateCharactersList();

        for (UserData data : userData.values()) {
            data.setBattleOpponent(null);
        }

        try {
            jda = new JDABuilder(Reference.TOKEN).setActivity(Activity.watching(" people waste money")).build();
        } catch (LoginException e) {
            System.out.println("Invalid Token");
            System.exit(0);
        }
        owner = jda.retrieveUserById(Reference.OWNER_ID).complete();
        jda.addEventListener(new EventListener());
        jda.addEventListener(new BattleListener());
    }

    //updates series data from MAL, chars must be the list of all current characters
    private static void updateSeriesData(List<com.github.doomsdayrs.jikan4java.types.main.character.Character> chars) {
        System.out.println("Updating Series Data");
        //anime
        List<SeriesData> vis = new ArrayList<>();
        for (LocalAnimeData data : anime.values()) {
            SeriesData series = data.getSeries();
            if (!vis.contains(series)) {
                vis.add(series);
                List<Anime> related = AddAnimeCommand.getAllRelated(JikanRetriever.animeSearch(data.getID()), new ArrayList<>());
                check:
                for (int i = series.getAnime().size() - 1; i >= 0; i--) {
                    LocalAnimeData an = series.getAnime().get(i);
                    for (Anime a : related) {
                        if (a.mal_id == an.getID()) {
                            continue check;
                        }
                    }
                    List<Anime> rel = AddAnimeCommand.getAllRelated(JikanRetriever.animeSearch(an.getID()), new ArrayList<>());
                    for (LocalAnimeData d : anime.values()) {
                        for (Anime ani : rel) {
                            if (d.getID() == ani.mal_id) {
                                an.setSeries(d.getSeries());
                                continue check;
                            }
                        }
                    }
                    an.setSeries(new SeriesData());
                }
            }

        }
        //characters
        characters:
        for (com.github.doomsdayrs.jikan4java.types.main.character.Character c : chars) {
            List<Animeography> anime = c.animeography;
            LocalCharacterData data = characters.get(c.mal_id);
            for (Animeography ani : anime) {
                for (LocalAnimeData a : data.getSeries().getAnime()) {
                    if (ani.mal_id == a.getID()) {
                        continue characters;
                    }
                }
                for (LocalAnimeData an : Bot.anime.values()) {
                    if (ani.mal_id == an.getID()) {
                        data.setSeries(an.getSeries());
                        continue characters;
                    }
                }
            }
            data.setSeries(new SeriesData());
        }
    }

    //updates by getting new data from MAL page
    private static void updateCharactersList() {
        System.out.println("Updating character list");
        List<com.github.doomsdayrs.jikan4java.types.main.character.Character> chars = new ArrayList<>();
        double totalInv = 0;
        for (int id : characters.keySet()) {
            com.github.doomsdayrs.jikan4java.types.main.character.Character c = JikanRetriever.getCharacter(id);
            totalInv += 1.0 / c.member_favorites;
            chars.add(c);
        }
        for (com.github.doomsdayrs.jikan4java.types.main.character.Character c : chars) {
            double rate = 1.0 / c.member_favorites / totalInv;
            LocalCharacterData data = characters.get(c.mal_id);
            data.setMemberFavorites(c.member_favorites);
            data.setRate(rate);
            data.setImageUrl(c.image_url);
            data.setName(c.name);
            for (Animeography a : c.animeography) {
                data.getAnimeography().add(a.mal_id);
            }
        }
        updateSeriesData(chars);
        adjustRates();
        System.out.println("Finished updating character list");
    }

    //updates without looking for MAL page changes, all in add must be in the given series
    public static void updateExistingCharactersList(List<com.github.doomsdayrs.jikan4java.types.main.character.Character> add, SeriesData series) {
        List<Integer> chars = new ArrayList<>();
        double totalInv = 0;
        for (int id : characters.keySet()) {
            chars.add(id);
            totalInv += 1.0 / characters.get(id).getMemberFavorites();
        }
        if (add != null) {
            check:
            for (int i = add.size() - 1; i >= 0; i--) {
                for (int id : chars) {
                    if (add.get(i).mal_id == id) {
                        continue check;
                    }
                }
                totalInv += 1.0 / add.get(i).member_favorites;
            }
            for (com.github.doomsdayrs.jikan4java.types.main.character.Character c : add) {
                double rate = 1.0 / c.member_favorites / totalInv;
                LocalCharacterData data = new LocalCharacterData(series, c.mal_id, c.member_favorites, rate, c.image_url, c.name);
                for (Animeography a : c.animeography) {
                    data.getAnimeography().add(a.mal_id);
                }
                characters.put(c.mal_id, data);
            }
        }
        for (int id : chars) {
            LocalCharacterData data = characters.get(id);
            data.setRate(1.0 / data.getMemberFavorites() / totalInv);
        }
        //sort
        List<Entry<Integer, LocalCharacterData>> list = new ArrayList<>(characters.entrySet());
        list.sort(Entry.comparingByValue());
        Map<Integer, LocalCharacterData> sorted = new LinkedHashMap<>();
        for (Entry<Integer, LocalCharacterData> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }
        Bot.characters = sorted;

        adjustRates();
        System.out.println("Finished updating character list");
    }

    @SuppressWarnings("unchecked")
    private static void adjustRates() {
        System.out.println("Adjusting rates");
        double cut = 2.5 / characters.size();
        List<LocalCharacterData> high = new ArrayList<>();
        for (LocalCharacterData data : characters.values()) {
            if (data.getRate() > cut) {
                high.add(data);
            }
        }
        ArrayList<LocalCharacterData> newHigh = new ArrayList<>();
        do {
            newHigh.clear();
            for (LocalCharacterData data : high) {
                while (data.getRate() > cut) {
                    double per = 0.1 / (characters.size() - high.size());
                    for (LocalCharacterData low : characters.values()) {
                        if (!high.contains(low)) {
                            double change = per * low.getRate();
                            data.setRate(data.getRate() - change);
                            low.setRate(low.getRate() + change);
                            if (low.getRate() > cut && !newHigh.contains(low)) {
                                newHigh.add(low);
                            }
                        }
                    }
                }
            }
            high = (ArrayList<LocalCharacterData>) newHigh.clone();
        } while (!newHigh.isEmpty());
    }
}
