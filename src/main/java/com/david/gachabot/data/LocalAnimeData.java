package com.david.gachabot.data;

import java.io.Serializable;
import java.util.*;

public class LocalAnimeData implements Serializable {

	private static final long serialVersionUID = 2;
	private String title;
	private List<LocalAnimeData> related;
	private final int id;

	public LocalAnimeData(String title, int id) {
		this.title = title;
		related = new ArrayList<LocalAnimeData>();
		this.id = id;
	}

	public List<LocalAnimeData> getRelated() {
		return related;
	}

	public int getID() {
		return id;
	}

	public String getTitle() {
		return title;
	}

}
