package com.david.gachabot.data;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

import javax.imageio.ImageIO;

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
	private BufferedImage image;

	public LocalCharacterData(int set, int id, int member_favorites, double rate, String image_url, String name) {
		this.set = set;
		this.id = id;
		this.rate = rate;
		this.name = name;
		this.image_url = image_url;
		this.member_favorites = member_favorites;
		animeography = new HashSet<Integer>();

		updateImage();

		int totalBase = member_favorites;
		baseHP = (int) (Math.random() * totalBase * 0.7 + totalBase / 10.0);
		totalBase -= baseHP;
		baseAttack = (int) (Math.random() * totalBase * 0.9 + totalBase / 10.0);
		baseDefense = totalBase - baseAttack;
		if(baseDefense <= 0) {
			baseDefense = 1;
		}
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

	public BufferedImage getImage() {
		return image;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImageUrl(String url) {
		image_url = url;
		updateImage();
	}

	public void setMemberFavorites(int fav) {
		member_favorites = fav;
		updateStats();
	}

	public void setBaseHP(int hp) {
		baseHP = hp;
	}

	public void setBaseDefense(int def) {
		baseDefense = def;
	}

	public void setBaseAttack(int att) {
		baseAttack = att;
	}

	private void updateStats() {
		int total = baseHP + baseAttack + baseDefense;
		double hp = baseHP * 1.0 / total;
		double att = baseAttack * 1.0 / total;
		baseHP = (int) (member_favorites * hp);
		baseAttack = (int) (member_favorites * att);
		baseDefense = member_favorites - baseHP - baseAttack;
	}

	private void updateImage() {
		try {
			image = ImageIO.read(new URL(image_url));
		}
		catch(Exception e) {}
	}
}
