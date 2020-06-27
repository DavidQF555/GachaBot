package com.david.gachabot;

import java.util.*;

import com.david.gachabot.commands.Command;
import com.david.gachabot.data.*;

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
		User user = event.getAuthor();
		long id = user.getIdLong();
		Message m = event.getMessage();
		String s = m.getContentRaw().toLowerCase();
		if(!user.isBot()) {
			for(Command c : Bot.commands) {
				if(s.startsWith(Reference.COMMAND) && (s.substring(Reference.COMMAND.length()).startsWith(c.getActivatingName()) || startsWith(s.substring(Reference.COMMAND.length()), c.getAlternativeNames()))) {
					if(c.hasPermission(m)) {
						UserData data = Bot.userData.get(id);
						if(data == null) {
							data = new UserData(id, new HashMap<Integer, CharacterInstanceData>());
							Bot.userData.put(id, data);
						}
						if(!c.allowInBattle() && data.getBattleOpponent() != null) {
							event.getChannel().sendMessage(user.getAsMention() + "```This command cannot be used while in battle").queue();
						}
						else if(c.correctFormat(m)) {
							c.onCommand(m);
						}
						else {
							event.getChannel().sendMessage(user.getAsMention() + "\n```Incorrect format. Correct Format: " + c.getFormat() + "```").queue();
						}
					}
					else {
						event.getChannel().sendMessage(user.getAsMention() + "\n```You do not have permission to use this command```").queue();
					}
					break;
				}
			}
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		User user = event.getAuthor();
		long id = user.getIdLong();
		Message m = event.getMessage();
		String s = m.getContentRaw().toLowerCase();
		if(!user.isBot()) {
			for(Command c : Bot.commands) {
				if(s.startsWith(Reference.COMMAND) && (s.substring(Reference.COMMAND.length()).startsWith(c.getActivatingName()) || startsWith(s.substring(Reference.COMMAND.length()), c.getAlternativeNames()))) {
					if(c.hasPermission(m)) {
						UserData data = Bot.userData.get(id);
						if(data == null) {
							data = new UserData(id, new HashMap<Integer, CharacterInstanceData>());
							Bot.userData.put(id, data);
						}
						if(!c.allowInBattle() && data.getBattleOpponent() != null) {
							event.getChannel().sendMessage(user.getAsMention() + "```This command cannot be used while in battle").queue();
						}
						else if(c.correctFormat(m)) {
							c.onPrivateMessage(m);
						}
						else {
							event.getChannel().sendMessage(user.getAsMention() + "\n```Incorrect format. Correct Format: " + c.getFormat() + "```").queue();
						}
					}
					else {
						event.getChannel().sendMessage(user.getAsMention() + "\n```You do not have permission to use this command```").queue();
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