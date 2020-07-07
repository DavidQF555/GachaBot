package com.david.gachabot;

import java.util.*;

import com.david.gachabot.data.*;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BattleListener extends ListenerAdapter {

	public static List<BattleData> battleData = new ArrayList<BattleData>();

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		User u = event.getUser();
		if(!u.isBot()) {
			long id = u.getIdLong();
			String emote = event.getReactionEmote().getAsCodepoints();
			TextChannel ch = event.getChannel();
			List<String> swap = Reference.SWAP_CODEPOINTS;
			for(BattleData data : battleData) {
				UserData user1 = data.getUser1();
				UserData user2 = data.getUser2();
				User u1 = Bot.jda.getUserById(user1.getID());
				User u2 = Bot.jda.getUserById(user2.getID());
				MessageReaction react = event.getReaction();
				if(react.getMessageIdLong() == data.getMessage().getIdLong() && (id == user1.getID() || id == user2.getID())) {
					List<CharacterInstanceData> user1Characters = new ArrayList<CharacterInstanceData> (data.getUser1Stats().keySet());
					List<CharacterInstanceData> user2Characters = new ArrayList<CharacterInstanceData> (data.getUser2Stats().keySet());
					CharacterInstanceData char1 = user1Characters.get(data.getUser1Out() - 1);
					CharacterInstanceData char2 = user2Characters.get(data.getUser2Out() - 1);
					List<int[]> stats1 = new ArrayList<int[]> (data.getUser1Stats().values());
					List<int[]> stats2 = new ArrayList<int[]> (data.getUser2Stats().values());
					int[] out1 = stats1.get(data.getUser1Out() - 1);
					int[] out2 = stats2.get(data.getUser2Out() - 1);
					boolean swapped = false;
					if(data.isUser1Turn() && id == user1.getID()) {}
					else if(!data.isUser1Turn() && id == user2.getID()) {
						//swap
						UserData tempD = user1;
						user1 = user2;
						user2 = tempD;
						User tempU = u1;
						u1 = u2;
						u2 = tempU;
						List<CharacterInstanceData> tempCh = user1Characters;
						user1Characters = user2Characters;
						user2Characters = tempCh;
						List<int[]> tempSt = stats1;
						stats1 = stats2;
						stats2 = tempSt;
						CharacterInstanceData tempC = char1;
						char1 = char2;
						char2 = tempC;
						int[] tempAr = out1;
						out1 = out2;
						out2 = tempAr;
						swapped = true;
					}
					else {
						return;
					}
					if(emote.equals(Reference.ATTACK_CODEPOINTS) && out1[3] > 0) {
						int damage = (int) (out1[1] / 10.0 + 1.0 * out1[1] / out2[2] + 0.5);
						if(damage <= 0) {
							damage = 1;
						}
						out2[0] -= damage;
						ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + Bot.characters.get(char1.getCharacterId()).getName() + " has dealt " + damage + " damage to " + Bot.characters.get(char2.getCharacterId()).getName() + "```").queue();
						boolean lose1 = true;
						boolean lose2 = true;
						for(int i = 0; i < stats2.size(); i ++) {
							int[] stats = stats2.get(i);
							if(stats[0] <= 0) {
								if(stats[3] > 0) {
									stats[3] *= -1;
									ch.sendMessage(u2.getName() + "'s " + Bot.characters.get(user2Characters.get(i).getCharacterId()).getName() + " has fainted!").queue();
								}
							}
							else {
								lose2 = false;
							}
						}
						for(int i = 0; i < stats1.size(); i ++) {
							int[] stats = stats1.get(i);
							if(stats[0] <= 0) {
								if(stats[3] > 0) {
									stats[3] *= -1;
									ch.sendMessage(u1.getName() + "'s " + Bot.characters.get(user1Characters.get(i).getCharacterId()).getName() + " has fainted!").queue();
								}
							}
							else {
								lose1 = false;
							}
						}
						if(lose1 || lose2) {
							if(lose1 && lose2) {
								ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```You have tied!```").queue();
							}
							else if(lose1) {
								ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + u2.getName() + " wins!```").queue();
							}
							else if(lose2) {
								ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + u1.getName() + " wins!```").queue();
							}
							battleData.remove(data);
							user1.setBattleOpponent(null);
							user2.setBattleOpponent(null);
							return;
						}
					}
					else if(swap.contains(emote)) {
						for(int i = 0; i < swap.size(); i ++) {
							if(swap.get(i).equals(emote)) {
								if(!swapped) {
									data.setUser1Out(i + 1);
								}
								else {
									data.setUser2Out(i + 1);
								}
								ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + u1.getName() + " has swapped to " + Bot.characters.get(user1Characters.get(i).getCharacterId()).getName() + "```").queue();
								break;
							}
						}
					}
					else if(emote.equals(Reference.WAIT_CODEPOINTS) && out1[3] > 0) {
						ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + u1.getName() + " has waited```").queue();
					}
					else {
						return;
					}
					Message m = ch.sendMessage(u2.getAsMention() + "What do you do?").complete();
					if(out2[3] > 0) {
						m.addReaction(Reference.ATTACK_CODEPOINTS).queue();
						m.addReaction(Reference.WAIT_CODEPOINTS).queue();
					}
					for(int i = 0; i < user2.getTeam().size(); i ++) {
						int[] stats = stats2.get(i);
						if(stats[3] > 0 && !stats.equals(out2)) {
							m.addReaction(swap.get(i)).queue();
						}
					}
					data.setIsUser1Turn(!data.isUser1Turn());
					data.setMessage(m);
				}
			}
		}
	}
}
