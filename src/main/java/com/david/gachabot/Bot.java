package com.david.gachabot;

import java.io.*;
import java.util.*;

import javax.security.auth.login.LoginException;

import com.david.gachabot.commands.*;
import com.github.doomsdayrs.jikan4java.core.Connector;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.User;

public class Bot {

	public final static Set<Command> commands = new HashSet<Command>();
	public final static Set<Integer> anime = new HashSet<Integer>();
	public final static Map<Integer, LocalCharacterData> characters = new HashMap<Integer, LocalCharacterData>();
	public final static Map<Long, Map<Integer, CharacterInstanceData>> userData = new HashMap<Long, Map<Integer, CharacterInstanceData>>();
	public static JDA jda;
	public static Connector connector = new Connector();
	public static User owner;

	public static void main(String[] args) {
		try {
			jda = new JDABuilder(Reference.TOKEN).build();
		} 
		catch (LoginException e) {
			System.out.println("Invalid Token");
			System.exit(0);
		}
		owner = jda.retrieveUserById(Reference.OWNER_ID).complete();
		jda.addEventListener(new EventListener());
		readAnimeList();
		readCharacterList();
		readUserData();
		updateCharactersList(null);

		commands.add(new ShutdownCommand());
		commands.add(new SaveCommand());
		commands.add(new AddAnimeCommand());
		commands.add(new CharacterListCommand());
		commands.add(new GachaRollCommand());
		commands.add(new RetrieveUserCharacterListCommand());
		commands.add(new HelpCommand());
	}

	public static void saveUserData() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter("userdata.txt")));
		}
		catch (Exception e) {}
		for(long id : userData.keySet()) {
			for(CharacterInstanceData data : userData.get(id).values()) {
				pw.println(data.getOwnerId() + " " + data.getCharacterId() + " " + data.getStars() + " " + data.getLevel() + " " + data.getExperience());	
			}
		}
		pw.close();
		System.out.println("Saved user data");
	}

	public static void saveAnimeList() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter("animelist.txt")));
		} 
		catch (Exception e) {}
		for(int id : anime) {
			pw.println(id);
		}
		pw.close();
		System.out.println("Saved anime list");
	}

	public static void saveCharacterList() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter("characterlist.txt")));
		} 
		catch (Exception e) {}
		for(int id : characters.keySet()) {
			LocalCharacterData data = characters.get(id);
			pw.println(data.getID() + " " + data.getMemberFavorites() + " " + data.getRate() + " " + data.getImageUrl() + " " + data.getName());
		}
		pw.close();
		System.out.println("Saved character list");
	}

	private static void readUserData() {
		System.out.println("Reading user data");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("userdata.txt"));
		}
		catch (FileNotFoundException e) {
			System.out.println("userdata.txt could not be found");
			System.exit(0);
		}
		try {
			while(true) {
				String[] ar = br.readLine().split(" ");
				long id = Long.parseLong(ar[0]);
				if(!userData.containsKey(id)) {
					userData.put(id, new HashMap<Integer, CharacterInstanceData>());
				}
				int chId = Integer.parseInt(ar[1]);
				userData.get(id).put(chId, new CharacterInstanceData(id, chId, Integer.parseInt(ar[2]), Integer.parseInt(ar[3]), Integer.parseInt(ar[4])));
			}
		}
		catch(Exception e) {}
		try {
			br.close();
		}
		catch (Exception e) {}
	}

	private static void readAnimeList() {
		System.out.println("Reading anime list");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("animelist.txt"));
		}
		catch (FileNotFoundException e) {
			System.out.println("animelist.txt could not be found");
			System.exit(0);
		}
		try {
			while(true) {
				anime.add(Integer.parseInt(br.readLine()));
			}
		}
		catch(Exception e) {}
		try {
			br.close();
		}
		catch (Exception e) {}
	}

	private static void readCharacterList() {
		System.out.println("Reading character list");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("characterlist.txt"));
		}
		catch (FileNotFoundException e) {
			System.out.println("characterlist.txt could not be found");
			System.exit(0);
		}
		try {
			while(true) {
				String[] ar = br.readLine().split(" ");
				int id = Integer.parseInt(ar[0]);
				String name = "";
				for(int i = 4; i < ar.length; i ++) {
					name += ar[i] + " ";
				}
				characters.put(id, new LocalCharacterData(id, Integer.parseInt(ar[1]), Double.parseDouble(ar[2]), ar[3], name.substring(0, name.length() - 1)));
			}
		}
		catch(Exception e) {}
		try {
			br.close();
		}
		catch (Exception e) {}
	}

	//updates by getting new data from MAL page
	private static void updateCharactersList(List<com.github.doomsdayrs.jikan4java.types.main.character.Character> add) {
		System.out.println("Updating character list");
		List<com.github.doomsdayrs.jikan4java.types.main.character.Character> chars = new ArrayList<com.github.doomsdayrs.jikan4java.types.main.character.Character>();
		double totalInv = 0;
		for(int id : characters.keySet()) {
			com.github.doomsdayrs.jikan4java.types.main.character.Character c = Util.getCharacter(id);
			totalInv += 1.0 / c.member_favorites;
			chars.add(c);
		}
		if(add != null) {
			check:
				for(com.github.doomsdayrs.jikan4java.types.main.character.Character c : add) {
					for(com.github.doomsdayrs.jikan4java.types.main.character.Character ch : chars) {
						if(c.mal_id == ch.mal_id) {
							continue check;
						}
					}
					totalInv += 1.0 / c.member_favorites;
					chars.add(c);
				}
		}
		for(com.github.doomsdayrs.jikan4java.types.main.character.Character c : chars) {
			double rate = 1.0 / c.member_favorites / totalInv;
			LocalCharacterData data = characters.get(c.mal_id);
			data.setMemberFavorites(c.member_favorites);
			data.setRate(rate);
			data.setImageUrl(c.image_url);
			data.setName(c.name);
		}
		System.out.println("Finished updating character list");
	}

	//updates without looking for MAL page changes
	public static void updateExistingCharactersList(List<com.github.doomsdayrs.jikan4java.types.main.character.Character> add) {
		List<Integer> chars = new ArrayList<Integer>();
		double totalInv = 0;
		for(int id : characters.keySet()) {
			chars.add(id);
			totalInv += 1.0 / characters.get(id).getMemberFavorites();
		}
		if(add != null) {
			check:
				for(int i = add.size() - 1; i >= 0; i --) {
					for(int id : chars) {
						if(add.get(i).mal_id == id) {
							continue check;
						}
					}
					totalInv += 1.0 / add.get(i).member_favorites;
				}
		}
		for(int id : characters.keySet()) {
			LocalCharacterData data = characters.get(id);
			data.setRate(1.0 / data.getMemberFavorites() / totalInv); 
		}
		for(com.github.doomsdayrs.jikan4java.types.main.character.Character c : add) {
			double rate = 1.0 / c.member_favorites / totalInv;
			characters.put(c.mal_id, new LocalCharacterData(c.mal_id, c.member_favorites, rate, c.image_url, c.name));
		}
		System.out.println("Finished updating character list");
	}
}
