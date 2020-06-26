package com.david.gachabot.data;

import java.io.Serializable;
import java.util.*;

public class LocalCharacterData implements Serializable {

	private static final long serialVersionUID = 3;
	private final int set;
	private final int id;
	private double rate;
	private String name;
	private String image_url;
	private int member_favorites;
	private Set<Integer> animeography;

	public LocalCharacterData(int set, int id, int member_favorites, double rate, String image_url, String name) {
		this.set = set;
		this.id = id;
		this.rate = rate;
		this.name = name;
		this.image_url = image_url;
		this.member_favorites = member_favorites;
		animeography = new HashSet<Integer>();
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

	public void setRate(double rate) {
		this.rate = rate;
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
}
