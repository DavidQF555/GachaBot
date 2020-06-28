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
		long id = event.getUserIdLong();
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
				boolean isUser1Fainted = data.isUser1Fainted();
				boolean isUser2Fainted = data.isUser2Fainted();
				boolean swapped = false;
				if(data.isUser1Turn() && id == user1.getID()) {}
				else if(!data.isUser1Turn() && id == user2.getID()) {
					swap(user1, user2);
					swap(u1, u2);
					swap(user1Characters, user2Characters);
					swap(stats1, stats2);
					swap(char1, char2);
					swap(out1, out2);
					isUser2Fainted = isUser1Fainted;
					isUser1Fainted = data.isUser2Fainted();
					swapped = true;
				}
				else {
					return;
				}
				if(emote.equals(Reference.ATTACK_CODEPOINTS) && !isUser1Fainted) {
					int damage = out1[1] - out2[2];
					if(damage < 0) {
						damage = 0;
					}
					out2[0] -= damage;
					ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + Bot.characters.get(char1.getCharacterId()).getName() + " has dealt " + damage + " damage to " + Bot.characters.get(char2.getCharacterId()) + "```").queue();
					boolean lose1 = true;
					boolean lose2 = true;
					if(out2[0] <= 0) {
						ch.sendMessage(u2.getName() + "'s " + Bot.characters.get(char2.getCharacterId()).getName() + " has fainted!").queue();
						for(int[] stats : stats2) {
							if(stats[0] > 0) {
								lose2 = false;
							}
						}
						if(!swapped) {
							data.setUser2Faint(true);
						}
						else {
							data.setUser1Faint(true);
						}
					}
					if(out1[0] <= 0) {
						ch.sendMessage(u1.getName() + "'s " + Bot.characters.get(char1.getCharacterId()).getName() + " has fainted!").queue();
						for(int[] stats : stats1) {
							if(stats[0] > 0) {
								lose1 = false;
							}
						}
						if(!swapped) {
							data.setUser1Faint(true);
						}
						else {
							data.setUser2Faint(true);
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
							ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + u1.getName() + " has swapped to " + Bot.characters.get(user1Characters.get(i + 1).getCharacterId()).getName() + "```").queue();
							break;
						}
					}
					if(!swapped) {
						data.setUser1Faint(false);
					}
					else {
						data.setUser2Faint(false);
					}
				}
				else if(emote.equals(Reference.WAIT_CODEPOINTS) && !isUser1Fainted) {
					ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + u1.getName() + " has waited```").queue();
				}
				else {
					return;
				}
				Message m = ch.sendMessage(u2.getAsMention() + "What do you do?").complete();
				if(!isUser2Fainted) {
					m.addReaction(Reference.ATTACK_CODEPOINTS);
					m.addReaction(Reference.WAIT_CODEPOINTS);
				}
				for(int i = 0; i < user2.getTeam().size(); i ++) {
					if(stats2.get(i)[0] > 0) {
						m.addReaction(swap.get(i));
					}
				}
				data.setIsUser1Turn(!data.isUser1Turn());
				data.setMessage(m);
			}
		}
	}

	private void swap(Object o1, Object o2) {
		Object o3 = o1;
		o1 = o2;
		o2 = o3;
	}
}
