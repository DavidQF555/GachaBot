package com.david.gachabot.data;

import java.util.LinkedHashMap;

import net.dv8tion.jda.api.entities.Message;

public class BattleData {

	private boolean user1Turn;
	private Message message;
	private UserData user1;
	private UserData user2;
	private LinkedHashMap<CharacterInstanceData, int[]> user1Stats;
	private LinkedHashMap<CharacterInstanceData, int[]> user2Stats;
	private int user1Out;
	private int user2Out;

	public BattleData(UserData user1, int user1Out, UserData user2, int user2Out, Message m) {
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

	public LinkedHashMap<CharacterInstanceData, int[]> getUser1Stats(){
		return user1Stats;
	}

	public LinkedHashMap<CharacterInstanceData, int[]> getUser2Stats(){
		return user2Stats;
	}

}
