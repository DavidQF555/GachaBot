package com.david.gachabot.data;

import java.io.Serializable;

import com.david.gachabot.Bot;

public class CharacterInstanceData implements Serializable {

	private static final long serialVersionUID = 1;
	private int character_id;
	private long owner_id;
	private int stars;
	private int level;
	private int exp;

	public CharacterInstanceData(long owner_id, int character_id) {
		this(owner_id, character_id, 1, 0, 0);
	}

	public CharacterInstanceData(long owner_id, int character_id, int stars, int level, int exp) {
		this.character_id = character_id;
		this.owner_id = owner_id;
		this.stars = stars;
		this.level = level;
		this.exp = exp;
	}

	public int getCharacterId() {
		return character_id;
	}

	public long getOwnerId() {
		return owner_id;
	}

	public int getStars() {
		return stars;
	}

	public int getLevel() {
		return level;
	}

	public int getExperience() {
		return exp;
	}

	public int getHP() {
		int base = Bot.characters.get(character_id).getBaseHP();
		return (int) (base + base * 0.1 * level);
	}

	public int getDefense() {
		int base = Bot.characters.get(character_id).getBaseDefense();
		return (int) (base + base * 0.1 * level);
	}

	public int getAttack() {
		int base = Bot.characters.get(character_id).getBaseAttack();
		return (int) (base + base * 0.1 * level);
	}

	public void setOwnerId(long id) {
		owner_id = id;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setExperience(int exp) {
		this.exp = exp;
	}
}
