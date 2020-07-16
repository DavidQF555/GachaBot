package com.david.gachabot.data;

import java.util.*;

import com.david.gachabot.BattleListener;

import net.dv8tion.jda.api.entities.Message;

public class BattleData {

	private int turn;
	private boolean user1Turn;
	private Message message;
	private UserData user1;
	private UserData user2;
	private LinkedHashMap<CharacterInstanceData, int[]> user1Stats;
	private LinkedHashMap<CharacterInstanceData, int[]> user2Stats;
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
		user1Stats = new LinkedHashMap<CharacterInstanceData, int[]>();
		for(Integer id : user1.getTeam()) {
			CharacterInstanceData data = user1.getCharacters().get(id);
			user1Stats.put(data, new int[] {data.getHP(), data.getDefense(), data.getAttack(), 1});
		}
		user2Stats = new LinkedHashMap<CharacterInstanceData, int[]>();
		for(Integer id : user2.getTeam()) {
			CharacterInstanceData data = user2.getCharacters().get(id);
			user2Stats.put(data, new int[] {data.getHP(), data.getDefense(), data.getAttack(), 1});
		}
	}

	public int getTurn() {
		return turn;
	}

	public Message getMessage() {
		return message;
	}

	public boolean isUser1Turn() {
		return user1Turn;
	}

	public int getUser1Out() {
		return user1Out;
	}

	public int getUser2Out() {
		return user2Out;
	}

	public void setTurn(int t) {
		turn = t;
	}

	public void setMessage(Message m) {
		message = m;
	}

	public void setIsUser1Turn(boolean b){
		user1Turn = b;
	}

	public void setUser1Out(int slot) {
		user1Out = slot;
	}

	public void setUser2Out(int slot) {
		user2Out = slot;
	}

	public UserData getUser1() {
		return user1;
	}

	public UserData getUser2() {
		return user2;
	}

	public Map<CharacterInstanceData, int[]> getUser1Stats(){
		return user1Stats;
	}

	public Map<CharacterInstanceData, int[]> getUser2Stats(){
		return user2Stats;
	}

	public void endBattle(int winner) {
		user1.setBattleOpponent(null);
		user2.setBattleOpponent(null);
		BattleListener.battleData.remove(this);
		BattleListener.timers.get(this).cancel();
		if(winner == 1) {
			user1.setGems(user1.getGems() + 200);
			user2.setGems(user2.getGems() + 100);
		}
		else if(winner == 2) {
			user1.setGems(user1.getGems() + 100);
			user2.setGems(user2.getGems() + 200);
		}
		else {
			user1.setGems(user1.getGems() + 150);
			user2.setGems(user2.getGems() + 150);
		}
	}
}
