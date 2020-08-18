package com.davidqf.gachabot;

import com.davidqf.gachabot.abilities.AbilityAbstract;
import com.davidqf.gachabot.data.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileUtil {

    public static void saveUserData() {
        JSONObject out = new JSONObject();
        for (UserData data : Bot.userData.values()) {
            JSONObject ob = new JSONObject();
            ob.put("gems", data.getGems());
            JSONObject team = new JSONObject();
            List<CharacterInstanceData> t = data.getTeam();
            for (int i = 1; i <= t.size(); i++) {
                team.put(i, t.get(i - 1).getCharacterData().getID());
            }
            ob.put("team", team);
            JSONObject ch = new JSONObject();
            Map<LocalCharacterData, CharacterInstanceData> characters = data.getCharacters();
            for (LocalCharacterData local : characters.keySet()) {
                CharacterInstanceData inst = characters.get(local);
                JSONObject character = new JSONObject();
                character.put("stars", inst.getStars());
                character.put("level", inst.getLevel());
                character.put("exp", inst.getExperience());
                ch.put(local.getID(), character);
            }
            ob.put("characters", ch);
            out.put(data.getID(), ob);
        }

        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get("userdata.json"));
            out.writeJSONString(bw);
            System.out.println("Saved User Data");
            bw.close();
        } catch (IOException ignored) {
        }
    }

    public static void saveSeriesData() {
        JSONObject out = new JSONObject();
        for (SeriesData data : SeriesData.getSeries()) {
            JSONObject series = new JSONObject();
            JSONObject anime = new JSONObject();
            for (LocalAnimeData a : data.getAnime()) {
                JSONObject an = new JSONObject();
                an.put("title", a.getTitle());
                anime.put(a.getID(), an);
            }
            series.put("anime", anime);
            JSONObject characters = new JSONObject();
            for (LocalCharacterData c : data.getCharacters()) {
                JSONObject ch = new JSONObject();
                ch.put("name", c.getName());
                ch.put("rate", c.getRate());
                ch.put("ability", c.getAbility().getName());
                ch.put("hp", c.getBaseHP());
                ch.put("attack", c.getBaseAttack());
                ch.put("defense", c.getBaseDefense());
                ch.put("favorites", c.getMemberFavorites());
                ch.put("image", c.getImageUrl());
                ch.put("animeography", c.getAnimeography());
                characters.put(c.getID(), ch);
            }
            series.put("characters", characters);
            out.put(data.getID(), series);
        }
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get("seriesdata.json"));
            out.writeJSONString(bw);
            System.out.println("Saved Series Data");
            bw.close();
        } catch (IOException ignored) {
        }
    }

    public static void readUserData() {
        JSONObject ob;
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get("userdata.json"));
            JSONParser parser = new JSONParser();
            ob = (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            return;
        }
        Set<Map.Entry<String, JSONObject>> entrySet = ob.entrySet();
        for (Map.Entry<String, JSONObject> entry : entrySet) {
            long userId = Long.parseLong(entry.getKey());
            JSONObject value = entry.getValue();
            Map<LocalCharacterData, CharacterInstanceData> c = new HashMap<>();
            JSONObject characters = (JSONObject) value.get("characters");
            Set<Map.Entry<String, JSONObject>> entries = characters.entrySet();
            for (Map.Entry<String, JSONObject> en : entries) {
                JSONObject val = en.getValue();
                LocalCharacterData local = Bot.characters.get(Integer.parseInt(en.getKey()));
                CharacterInstanceData inst = new CharacterInstanceData(local, userId, (int) val.get("stars"), (int) val.get("level"), (int) val.get("exp"));
                c.put(local, inst);
            }
            UserData data = new UserData(userId, c);
            data.setGems((long) value.get("gems"));
            List<CharacterInstanceData> team = data.getTeam();
            team.clear();
            JSONObject t = (JSONObject) value.get("team");
            for (int i = 1; i <= 5; i++) {
                if (t.containsKey(i)) {
                    team.add(c.get(Bot.characters.get(t.get("i"))));
                    continue;
                }
                break;
            }
            Bot.userData.put(userId, data);
        }
        System.out.println("Finished Reading User Data");
    }

    public static void readSeriesData() {
        JSONObject ob;
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get("seriesdata.json"));
            JSONParser parser = new JSONParser();
            ob = (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            return;
        }
        for (Object o : ob.values()) {
            JSONObject value = (JSONObject) o;
            SeriesData series = new SeriesData();
            Set<Map.Entry<String, JSONObject>> anime = ((JSONObject) value.get("anime")).entrySet();
            for (Map.Entry<String, JSONObject> en : anime) {
                int id = Integer.parseInt(en.getKey());
                Bot.anime.put(id, new LocalAnimeData(series, (String) en.getValue().get("title"), id));
            }

            Set<Map.Entry<String, JSONObject>> characters = ((JSONObject) value.get("characters")).entrySet();
            for (Map.Entry<String, JSONObject> en : characters) {
                int id = Integer.parseInt(en.getKey());
                JSONObject val = en.getValue();
                Bot.characters.put(id, new LocalCharacterData(series, id, Integer.parseInt("" + val.get("favorites")), (double) val.get("rate"), (String) val.get("image"), (String) val.get("name"), Integer.parseInt("" + val.get("hp")), Integer.parseInt("" + val.get("attack")), Integer.parseInt("" + val.get("defense")), AbilityAbstract.get((String) val.get("ability")), new HashSet<Integer>((JSONArray) val.get("animeography"))));
            }
        }
        System.out.println("Finished Reading Series Data");
    }
}
