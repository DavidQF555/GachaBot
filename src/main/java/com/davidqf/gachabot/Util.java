package com.davidqf.gachabot;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;

public class Util {

    public static EmbedBuilder createMessage(String desc, Color c) {
        return createMessage(desc).setColor(c);
    }

    public static EmbedBuilder createMessage(String desc) {
        return new EmbedBuilder().setDescription(desc).setColor(Color.GREEN);
    }

    public static EmbedBuilder createFailedMessage(String desc) {
        return createMessage(desc, Color.RED);
    }
}
