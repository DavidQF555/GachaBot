package com.david.gachabot.data;

import java.io.Serializable;
import java.util.*;

public class UserData implements Serializable {

	private static final long serialVersionUID = 4;
	private final long id;
	private final Map<Integer, CharacterInstanceData> characters;
	private UserData battleOpponent;
	private List<Integer> team;
	private long gems;

	public UserData(long id, Map<Integer, CharacterInstanceData> characters) {
		this.id = id;
		this.characters = characters;
		battleOpponent = null;
		team = new ArrayList<Integer>();
		gems = 500;
	}

	public long getID() {
		return id;
	}

	public Map<Integer, CharacterInstanceData> getCharacters(){
		return characters;
	}

	public UserData getBattleOpponent() {
		return battleOpponent;
	}

	public List<Integer> getTeam(){
		return team;
	}

	public long getGems() {
		return gems;
	}

	public void setBattleOpponent(UserData d) {
		battleOpponent = d;
	}

	public void setGems(long g) {
		gems = g;
	}
}
