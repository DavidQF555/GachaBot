package com.david.gachabot;

import java.util.*;

import com.david.gachabot.commands.CommandAbstract;
import com.david.gachabot.data.*;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {

	@Override
	public void onShutdown(ShutdownEvent event) {
		FileUtil.saveAnimeList();
		FileUtil.saveCharacterList();
		FileUtil.saveUserData();
		System.out.println("Shutting Down");
		System.exit(0);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		User user = event.getAuthor();
		long id = user.getIdLong();
		Message m = event.getMessage();
		CommandAbstract c = detect(m);
		if(c != null && !user.isBot()) {
			UserData data = Bot.userData.get(id);
			if(data == null) {
				data = new UserData(id, new HashMap<Integer, CharacterInstanceData>());
				Bot.userData.put(id, data);
			}
			if(c.hasPermission(m)) {	
				if(!c.allowInBattle() && data.getBattleOpponent() != null) {
					event.getChannel().sendMessage(user.getAsMention() + " This command cannot be used while in battle").queue();
				}
				else if(c.correctFormat(m)) {
					c.onCommand(m);
				}
				else {
					event.getChannel().sendMessage(user.getAsMention() + " Incorrect format. Correct Format: `" + c.getFormat() + "`").queue();
				}
			}
			else {
				event.getChannel().sendMessage(user.getAsMention() + " You do not have permission to use this command").queue();
			}
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		User user = event.getAuthor();
		long id = user.getIdLong();
		Message m = event.getMessage();
		CommandAbstract c = detect(m);
		if(c != null && !user.isBot()) {
			UserData data = Bot.userData.get(id);
			if(data == null) {
				data = new UserData(id, new HashMap<Integer, CharacterInstanceData>());
				Bot.userData.put(id, data);
			}
			if(c.hasPermission(m)) {	
				if(!c.allowInBattle() && data.getBattleOpponent() != null) {
					event.getChannel().sendMessage(user.getAsMention() + " This command cannot be used while in battle").queue();
				}
				else if(c.correctFormat(m)) {
					c.onPrivateMessage(m);
				}
				else {
					event.getChannel().sendMessage(user.getAsMention() + " Incorrect format. Correct Format: `" + c.getFormat() + "`").queue();
				}
			}
			else {
				event.getChannel().sendMessage(user.getAsMention() + " You do not have permission to use this command").queue();
			}
		}
	}

	private CommandAbstract detect(Message m) {
		String s = m.getContentRaw();
		if(s.toLowerCase().startsWith(Reference.COMMAND.toLowerCase())) {
			for(CommandAbstract c : Bot.commands) {
				if(s.toLowerCase().startsWith(c.getActivatingName().toLowerCase(), Reference.COMMAND.length())) {
					return c;
				}
			}
			for(CommandAbstract c : Bot.commands) {
				for(String pref : c.getAlternativeNames()) {
					if(s.toLowerCase().startsWith(pref.toLowerCase(), Reference.COMMAND.length())) {
						return c;
					}
				}
			}
		}
		return null;
	}
}