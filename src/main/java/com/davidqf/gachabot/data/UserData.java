package com.davidqf.gachabot.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserData {

    private final long id;
    private final Map<LocalCharacterData, CharacterInstanceData> characters;
    private UserData battleOpponent;
    private final List<CharacterInstanceData> team;
    private long gems;

    public UserData(long id, Map<LocalCharacterData, CharacterInstanceData> characters) {
        this.id = id;
        this.characters = characters;
        battleOpponent = null;
        team = new ArrayList<>();
        gems = 500;
    }

    public long getID() {
        return id;
    }

    public Map<LocalCharacterData, CharacterInstanceData> getCharacters() {
        return characters;
    }

    public UserData getBattleOpponent() {
        return battleOpponent;
    }

    public List<CharacterInstanceData> getTeam() {
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