package com.david.gachabot;

import java.util.List;

import com.david.gachabot.commands.Command;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {

	@Override
	public void onShutdown(ShutdownEvent event) {
		Bot.saveAnimeList();
		Bot.saveCharacterList();
		Bot.saveUserData();
		System.out.println("Shutting Down");
		System.exit(0);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		Message m = event.getMessage();
		String s = m.getContentRaw().toLowerCase();
		if(!event.getAuthor().isBot()) {
			for(Command c : Bot.commands) {
				if(s.startsWith(Reference.COMMAND + c.getActivatingName()) || startsWith(s.substring(Reference.COMMAND.length()), c.getAlternativeNames())) {
					if(c.hasPermission(m)) {
						if(c.correctFormat(m)) {
							c.onCommand(m);
						}
						else {
							event.getChannel().sendMessage(event.getAuthor().getAsMention() + "\n```Incorrect format. Correct Format: " + c.getFormat() + "```").queue();
						}
					}
					else {
						event.getChannel().sendMessage(event.getAuthor().getAsMention() + "\n```You do not have permission to use this command```").queue();
					}
					break;
				}
			}
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		Message m = event.getMessage();
		String s = m.getContentRaw().toLowerCase();
		if(!event.getAuthor().isBot()) {
			for(Command c : Bot.commands) {
				if(s.startsWith(Reference.COMMAND + c.getActivatingName()) || startsWith(s.substring(Reference.COMMAND.length()), c.getAlternativeNames())) {
					if(c.hasPermission(m)) {
						if(c.correctFormat(m)) {
							c.onPrivateMessage(m);
						}
						else {
							event.getChannel().sendMessage(event.getAuthor().getAsMention() + "\n```Incorrect format. Correct Format: " + c.getFormat() + "```").queue();
						}
					}
					else {
						event.getChannel().sendMessage(event.getAuthor().getAsMention() + "\n```You do not have permission to use this command```").queue();
					}
					break;
				}
			}
		}
	}

	private boolean startsWith(String s, List<String> alt) {
		for(String a : alt) {
			if(s.startsWith(a)) {
				return true;
			}
		}
		return false;
	}
}