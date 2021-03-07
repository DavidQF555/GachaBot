package com.davidqf.gachabot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Queue;

public class EventListener extends ListenerAdapter {

    public static final Queue<Message> messages = new LinkedList<>();

    @Override
    public void onShutdown(@Nonnull ShutdownEvent event) {
        FileUtil.saveSeriesData();
        FileUtil.saveUserData();
        System.out.println("Shutting Down");
        System.exit(0);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        messages.add(event.getMessage());
    }
}