package com.david.gachabot;

import java.io.Serializable;

public class LocalAnimeData implements Serializable {

	private static final long serialVersionUID = 2;
	private final int set;
	private final int id;

	public LocalAnimeData(int set, int id) {
		this.set = set;
		this.id = id;
	}

	public int getSet() {
		return set;
	}

	public int getID() {
		return id;
	}

}
