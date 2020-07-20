package com.david.gachabot.data;

import java.io.Serializable;

public class LocalAnimeData implements Serializable {

	private static final long serialVersionUID = 2;
	private String title;
	private final int set;
	private final int id;

	public LocalAnimeData(String title, int set, int id) {
		this.title = title;
		this.set = set;
		this.id = id;
	}

	public int getSet() {
		return set;
	}

	public int getID() {
		return id;
	}

	public String getTitle() {
		return title;
	}

}
