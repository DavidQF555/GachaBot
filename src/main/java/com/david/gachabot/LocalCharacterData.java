package com.david.gachabot;

public class LocalCharacterData {

	private final int id;
	private double rate;
	private String name;
	private String image_url;
	private int member_favorites;

	public LocalCharacterData(int id, int member_favorites, double rate, String image_url, String name) {
		this.id = id;
		this.rate = rate;
		this.name = name;
		this.image_url = image_url;
		this.member_favorites = member_favorites;
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
