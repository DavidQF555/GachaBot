package io.github.davidqf555.gachabot.data;

import io.github.davidqf555.gachabot.BattleListener;
import net.dv8tion.jda.api.entities.Message;

import java.util.LinkedHashMap;

public class BattleData {

    private final UserData user1;
    private final UserData user2;
    private final LinkedHashMap<CharacterInstanceData, int[]> user1Stats;
    private final LinkedHashMap<CharacterInstanceData, int[]> user2Stats;
    private int turn;
    private boolean user1Turn;
    private Message message;
    private int user1Out;
    private int user2Out;

    public BattleData(UserData user1, int user1Out, UserData user2, int user2Out, Message m) {
        turn = 1;
        user1Turn = true;
        message = m;
        this.user1 = user1;
        this.user2 = user2;
        this.user1Out = user1Out;
        this.user2Out = user2Out;
        user1Stats = new LinkedHashMap<>();
        for (CharacterInstanceData data : user1.getTeam()) {
            user1Stats.put(data, new int[]{data.getHP(), data.getDefense(), data.getAttack(), 1});
        }
        user2Stats = new LinkedHashMap<>();
        for (CharacterInstanceData data : user2.getTeam()) {
            user2Stats.put(data, new int[]{data.getHP(), data.getDefense(), data.getAttack(), 1});
        }
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int t) {
        turn = t;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message m) {
        message = m;
    }

    public boolean isUser1Turn() {
        return user1Turn;
    }

    public int getUser1Out() {
        return user1Out;
    }

    public void setUser1Out(int slot) {
        user1Out = slot;
    }

    public int getUser2Out() {
        return user2Out;
    }

    public void setUser2Out(int slot) {
        user2Out = slot;
    }

    public void setIsUser1Turn(boolean b) {
        user1Turn = b;
    }

    public UserData getUser1() {
        return user1;
    }

    public UserData getUser2() {
        return user2;
    }

    public LinkedHashMap<CharacterInstanceData, int[]> getUser1Stats() {
        return user1Stats;
    }

    public LinkedHashMap<CharacterInstanceData, int[]> getUser2Stats() {
        return user2Stats;
    }

    public void endBattle(int winner) {
        user1.setBattleOpponent(null);
        user2.setBattleOpponent(null);
        BattleListener.battleData.remove(this);
        BattleListener.timers.get(this).cancel();
        if (winner == 1) {
            user1.setGems(user1.getGems() + 200);
            user2.setGems(user2.getGems() + 100);
        } else if (winner == 2) {
            user1.setGems(user1.getGems() + 100);
            user2.setGems(user2.getGems() + 200);
        } else {
            user1.setGems(user1.getGems() + 150);
            user2.setGems(user2.getGems() + 150);
        }
    }
}
