package com.david.gachabot.data;

import java.io.Serializable;
import java.util.*;

import com.david.gachabot.Bot;
import com.david.gachabot.abilities.AbilityAbstract;

public class LocalCharacterData implements Serializable {

	private static final long serialVersionUID = 3;
	private final int set;
	private final int id;
	private double rate;
	private String name;
	private String image_url;
	private int member_favorites;
	private Set<Integer> animeography;
	private int baseHP;
	private int baseDefense;
	private int baseAttack;
	private AbilityAbstract ability;

	public LocalCharacterData(int set, int id, int member_favorites, double rate, String image_url, String name) {
		this.set = set;
		this.id = id;
		this.rate = rate;
		this.name = name;
		this.image_url = image_url;
		this.member_favorites = member_favorites;
		animeography = new HashSet<Integer>();

		int totalBase = getBaseTotal();
		int base = totalBase / 6;
		baseHP = base;
		baseAttack = base;
		baseDefense = base;
		totalBase -= base * 3;
		while(totalBase > getBaseTotal() / 10) {
			int hp = (int) (Math.random() * totalBase + 1);
			int attack = (int) (Math.random() * totalBase + 1);
			int defense = (int) (Math.random() * totalBase + 1);
			if(hp + attack + defense <= totalBase) {
				baseHP += hp;
				baseAttack += attack;
				baseDefense += defense;
				totalBase -= hp + attack + defense;
			}
		}
		int change = totalBase / 3;
		baseAttack += change;
		baseDefense += change;
		baseHP += totalBase - 2 * change;

		ability = Bot.abilities.get((int) (Math.random() * Bot.abilities.size())); 
	}

	public int getSet() {
		return set;
	}

	public int getID() {
		return id;
	}

	public double getRate() {
		return rate;
	}

	public String getName() {
		return name;
	}

	public String getImageUrl() {
		return image_url;
	}

	public int getMemberFavorites() {
		return member_favorites;
	}

	public Set<Integer> getAnimeography(){
		return animeography;
	}

	public int getBaseHP() {
		return baseHP;
	}

	public int getBaseDefense() {
		return baseDefense;
	}

	public int getBaseAttack() {
		return baseAttack;
	}

	public AbilityAbstract getAbility() {
		return ability;
	}

	public void setRate(double rate) {
		this.rate = rate;
		updateStats();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImageUrl(String url) {
		image_url = url;
	}

	public void setMemberFavorites(int fav) {
		member_favorites = fav;
	}

	private void updateStats() {
		int total = baseHP + baseAttack + baseDefense;
		double def = baseDefense * 1.0 / total;
		double att = baseAttack * 1.0 / total;
		int base = getBaseTotal();
		baseDefense = (int) (base * def);
		baseAttack = (int) (base * att);
		baseHP = base - baseDefense - baseAttack;
	}

	private int getBaseTotal() {
		return 100 + (int) (100 / rate + 0.5);
	}
}
