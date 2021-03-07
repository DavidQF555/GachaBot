package io.github.davidqf555.gachabot.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserData {

    private static final int STARTING_GEMS = 2500;
    private final long id;
    private final Map<LocalCharacterData, CharacterInstanceData> characters;
    private final List<CharacterInstanceData> team;
    private UserData battleOpponent;
    private long gems;

    public UserData(long id, Map<LocalCharacterData, CharacterInstanceData> characters) {
        this.id = id;
        this.characters = characters;
        battleOpponent = null;
        team = new ArrayList<>();
        gems = STARTING_GEMS;
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

    public void setBattleOpponent(UserData d) {
        battleOpponent = d;
    }

    public List<CharacterInstanceData> getTeam() {
        return team;
    }

    public long getGems() {
        return gems;
    }

    public void setGems(long g) {
        gems = g;
    }
}
