package com.david.gachabot;

import com.david.gachabot.data.*;

import java.io.*;
import java.util.Map;

public class FileUtil {

    public static void saveUserData() {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("userdata.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Could not find userdata.txt");
            return;
        }
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(fos);
            oos.writeObject(Bot.userData);
            oos.close();
            fos.close();
        } catch (IOException ignored) {
        }
        System.out.println("Saved User Data");
    }

    public static void saveAnimeList() {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("animelist.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Could not find animelist.txt");
            return;
        }
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(fos);
            oos.writeObject(Bot.anime);
            oos.close();
            fos.close();
        } catch (IOException ignored) {
        }
        System.out.println("Saved Anime List");
    }

    public static void saveCharacterList() {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("characterlist.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Could not find characterlist.txt");
            return;
        }
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(fos);
            oos.writeObject(Bot.characters);
            oos.close();
            fos.close();
        } catch (IOException ignored) {
        }
        System.out.println("Saved Character List");
    }

    @SuppressWarnings("unchecked")
    public static void readUserData() {
        FileInputStream fis;
        try {
            fis = new FileInputStream("userdata.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Could not find userdata.txt");
            return;
        }
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(fis);
            Map<Long, UserData> data = (Map<Long, UserData>) ois.readObject();
            ois.close();
            fis.close();
            if (data != null) {
                Bot.userData = data;
            }
        } catch (Exception ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    public static void readAnimeList() {
        FileInputStream fis;
        try {
            fis = new FileInputStream("animelist.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Could not find animelist.txt");
            return;
        }
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(fis);
            Map<Integer, LocalAnimeData> data = (Map<Integer, LocalAnimeData>) ois.readObject();
            ois.close();
            fis.close();
            if (data != null) {
                Bot.anime = data;
            }
        } catch (Exception ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    public static void readCharacterList() {
        FileInputStream fis;
        try {
            fis = new FileInputStream("characterlist.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Could not find characterlist.txt");
            return;
        }
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(fis);
            Map<Integer, LocalCharacterData> data = (Map<Integer, LocalCharacterData>) ois.readObject();
            ois.close();
            fis.close();
            if (data != null) {
                Bot.characters = data;
            }
        } catch (Exception ignored) {
        }
    }
}
