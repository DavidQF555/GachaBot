package com.david.gachabot;

import java.io.*;
import java.util.*;

import com.david.gachabot.data.*;

public class FileUtil {

	public static void saveUserData() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("userdata.txt");
		} 
		catch (FileNotFoundException e) {
			System.out.println("Could not find userdata.txt");
			return;
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(Bot.userData);
			oos.close();
			fos.close();
		} 
		catch (IOException e) {}
		System.out.println("Saved User Data");
	}

	public static void saveAnimeList() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("animelist.txt");
		} 
		catch (FileNotFoundException e) {
			System.out.println("Could not find animelist.txt");
			return;
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(Bot.anime);
			oos.close();
			fos.close();
		} 
		catch (IOException e) {}
		System.out.println("Saved Anime List");
	}

	public static void saveCharacterList() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("characterlist.txt");
		} 
		catch (FileNotFoundException e) {
			System.out.println("Could not find characterlist.txt");
			return;
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(Bot.characters);
			oos.close();
			fos.close();
		} 
		catch (IOException e) {}
		System.out.println("Saved Character List");
	}

	@SuppressWarnings("unchecked")
	public static void readUserData() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("userdata.txt");
		} 
		catch (FileNotFoundException e) {
			System.out.println("Could not find userdata.txt");
			return;
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(fis);
			Map<Long, UserData> data = (Map<Long, UserData>) ois.readObject();
			ois.close();
			fis.close();
			if(data != null) {
				Bot.userData = data;
			}
		}
		catch (Exception e) {}
	}

	@SuppressWarnings("unchecked")
	public static void readAnimeList() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("animelist.txt");
		} 
		catch (FileNotFoundException e) {
			System.out.println("Could not find animelist.txt");
			return;
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(fis);
			Map<Integer, LocalAnimeData> data = (Map<Integer, LocalAnimeData>) ois.readObject();
			ois.close();
			fis.close();
			if(data != null) {
				Bot.anime = data;
			}
		}
		catch (Exception e) {}
	}

	@SuppressWarnings("unchecked")
	public static void readCharacterList() {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("characterlist.txt");
		} 
		catch (FileNotFoundException e) {
			System.out.println("Could not find characterlist.txt");
			return;
		}
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(fis);
			LinkedHashMap<Integer, LocalCharacterData> data = (LinkedHashMap<Integer, LocalCharacterData>) ois.readObject();
			ois.close();
			fis.close();
			if(data != null) {
				Bot.characters = data;
			}
		}
		catch (Exception e) {}
	}
}
