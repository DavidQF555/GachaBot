package com.david.gachabot.data;

import java.io.Serializable;
import java.util.*;

public class UserData implements Serializable {

	private static final long serialVersionUID = 4;
	private final long id;
	private final Map<LocalCharacterData, CharacterInstanceData> characters;
	private UserData battleOpponent;
	private List<CharacterInstanceData> team;
	private long gems;

	public UserData(long id, Map<LocalCharacterData, CharacterInstanceData> characters) {
		this.id = id;
		this.characters = characters;
		battleOpponent = null;
		team = new ArrayList<CharacterInstanceData>();
		gems = 500;
	}

	public long getID() {
		return id;
	}

	public Map<LocalCharacterData, CharacterInstanceData> getCharacters(){
		return characters;
	}

	public UserData getBattleOpponent() {
		return battleOpponent;
	}

	public List<CharacterInstanceData> getTeam(){
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
