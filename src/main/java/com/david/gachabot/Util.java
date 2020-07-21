package com.david.gachabot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Util {

	public static MessageEmbed createMessage(String desc, String imageurl) {
		return new EmbedBuilder().setTitle(desc).setImage(imageurl).build();
	}

	public static MessageEmbed createMessage(String desc) {
		return new EmbedBuilder().setDescription(desc).build();
	}
}
