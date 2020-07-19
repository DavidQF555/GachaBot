package com.david.gachabot;

import java.util.*;

import com.david.gachabot.abilities.AbilityAbstract;
import com.david.gachabot.data.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BattleListener extends ListenerAdapter {

	public static List<BattleData> battleData = new ArrayList<BattleData>();
	public static Map<Long, UserData[]> invitations = new HashMap<Long, UserData[]>();
	public static Map<BattleData, Timer> timers = new HashMap<BattleData, Timer>();

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		User u = event.getUser();
		if(!u.isBot()) {
			long id = u.getIdLong();
			String emote = event.getReactionEmote().getAsCodepoints();
			TextChannel ch = event.getChannel();
			long mesId = event.getMessageIdLong();
			if(invitations.containsKey(mesId)) {
				UserData[] usDa = invitations.get(mesId);
				if(usDa[1].equals(Bot.userData.get(id))) {
					if(emote.equals(Reference.ACCEPT_CODEPOINTS)) {
						if(usDa[0].getBattleOpponent() == null && usDa[1].getBattleOpponent() == null) {
							invitations.remove(mesId);
							BattleData data = new BattleData(usDa[0], 1, usDa[1], 1, null);
							User u1 = Bot.jda.getUserById(usDa[0].getID());
							User u2 = Bot.jda.getUserById(usDa[1].getID());
							Message mes = ch.sendMessage(BattleListener.generateBattleMessage(data, "A battle has started between " + u1.getName() + " and " + u2.getName() + "! " + u1.getName() + " gets the first move!")).complete();
							data.setMessage(mes);
							mes.addReaction(Reference.ATTACK_CODEPOINTS).queue();
							mes.addReaction(Reference.WAIT_CODEPOINTS).queue();
							for(int i = 1; i < usDa[0].getTeam().size(); i ++) {
								mes.addReaction(Reference.SWAP_CODEPOINTS.get(i)).queue();
							}
							usDa[0].setBattleOpponent(usDa[1]);
							usDa[1].setBattleOpponent(usDa[0]);
							battleData.add(data);
							Timer t = new Timer();
							t.schedule(new TimerTask() {
								@Override
								public void run() {
									if(data.getTurn() == 1 && data.isUser1Turn()) {
										ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " " + u1.getName() + " has timed out! " + u2.getName() + " wins!").queue();
										data.endBattle(2);
									}
								}
							}, 600000);
							timers.put(data, t);
						}
					}
					else if(emote.equals(Reference.DECLINE_CODEPOINTS)) {
						ch.retrieveMessageById(mesId).complete().delete().queue();
						invitations.remove(mesId);
					}
				}
				return;
			}
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
					String desc = "";
					if(emote.equals(Reference.ATTACK_CODEPOINTS) && out1[3] > 0) {
						LocalCharacterData data1 = Bot.characters.get(char1.getCharacterId());
						LocalCharacterData data2 = Bot.characters.get(char2.getCharacterId());
						AbilityAbstract ab1 = data1.getAbility();
						AbilityAbstract ab2 = data2.getAbility();
						int damage = ab1.calculateDamage(out1, stats1, out2, stats2, ab2);
						out2[0] -= damage;
						ab1.attackEffect(damage, out1, stats1, out2, stats2);
						ab2.defenseEffect(damage, out1, stats1, out2, stats2);
						desc += u1.getName() + "'s " + data1.getName() + " has dealt " + damage + " damage to " + u2.getName() + "'s " + data2.getName() + "! ";
					}
					else if(Reference.SWAP_CODEPOINTS.contains(emote)) {
						for(int i = 0; i < Reference.SWAP_CODEPOINTS.size(); i ++) {
							if(Reference.SWAP_CODEPOINTS.get(i).equals(emote)) {
								if(!swapped) {
									data.setUser1Out(i + 1);
								}
								else {
									data.setUser2Out(i + 1);
								}
								desc += u1.getName() + " has swapped to " + Bot.characters.get(user1Characters.get(i).getCharacterId()).getName() + ". ";
								break;
							}
						}
					}
					else if(emote.equals(Reference.WAIT_CODEPOINTS) && out1[3] > 0) {
						desc += u1.getName() + " has waited. ";
					}
					else {
						return;
					}
					boolean lose1 = true;
					boolean lose2 = true;
					for(int i = 0; i < stats2.size(); i ++) {
						int[] stats = stats2.get(i);
						if(stats[0] <= 0) {
							if(stats[3] > 0) {
								stats[3] *= -1;
								desc += u2.getName() + "'s " + Bot.characters.get(user2Characters.get(i).getCharacterId()).getName() + " has fainted! ";
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
								desc += u1.getName() + "'s " + Bot.characters.get(user1Characters.get(i).getCharacterId()).getName() + " has fainted! ";
							}
						}
						else {
							lose1 = false;
						}
					}
					if(lose1 || lose2) {
						if(lose1 && lose2) {
							ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + desc + "You have tied! ```").queue();
							data.endBattle(0);
						}
						else if(lose1) {
							ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + desc + u2.getName() + " wins! ```").queue();
							if(swapped) {
								data.endBattle(1);
							}
							else {
								data.endBattle(2);
							}
						}
						else if(lose2) {
							ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " ```" + desc + u1.getName() + " wins! ```").queue();
							if(swapped) {
								data.endBattle(2);
							}
							else {
								data.endBattle(1);
							}
						}
						return;
					}
					Message m = ch.sendMessage(generateBattleMessage(data, desc + "It's " + u2.getName() + "'s turn! ")).complete();
					if(out2[3] > 0) {
						m.addReaction(Reference.ATTACK_CODEPOINTS).queue();
						m.addReaction(Reference.WAIT_CODEPOINTS).queue();
					}
					for(int i = 0; i < user2.getTeam().size(); i ++) {
						int[] stats = stats2.get(i);
						if(stats[3] > 0 && !stats.equals(out2)) {
							m.addReaction(Reference.SWAP_CODEPOINTS.get(i)).queue();
						}
					}
					data.setIsUser1Turn(!data.isUser1Turn());
					if(data.isUser1Turn()) {
						data.setTurn(data.getTurn() + 1);
					}
					data.setMessage(m);
					timers.get(data).schedule(new TimerTask() {

						private final int turn = data.getTurn();
						private final boolean isUser1 = data.isUser1Turn();

						@Override
						public void run() {
							if(data.getTurn() == turn && data.isUser1Turn() == isUser1) {
								User u1 = Bot.jda.getUserById(data.getUser1().getID());
								User u2 = Bot.jda.getUserById(data.getUser2().getID());
								if(isUser1) {
									ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " " + u1.getName() + " has timed out! " + u2.getName() + " wins!").queue();
									data.endBattle(2);
								}
								else {
									ch.sendMessage(u1.getAsMention() + u2.getAsMention() + " " + u2.getName() + " has timed out! " + u1.getName() + " wins!").queue();
									data.endBattle(1);
								}
							}
						}
					}, 120000);
				}
			}
		}
	}

	public static MessageEmbed generateBattleMessage(BattleData data, String desc) {
		UserData user1 = data.getUser1();
		UserData user2 = data.getUser2();
		String u1 = Bot.jda.getUserById(user1.getID()).getName();
		String u2 = Bot.jda.getUserById(user2.getID()).getName();
		int outID1 = user1.getTeam().get(data.getUser1Out() - 1);
		int outID2 = user2.getTeam().get(data.getUser2Out() - 1);
		LocalCharacterData out1 = Bot.characters.get(outID1);
		LocalCharacterData out2 = Bot.characters.get(outID2);
		int[] stats1 = data.getUser1Stats().get(user1.getCharacters().get(outID1));
		int[] stats2 = data.getUser2Stats().get(user2.getCharacters().get(outID2));
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Battle between " + u1 + " and " + u2)
				.addField(u1 + "'s " + out1.getName(), "HP: " + stats1[0] + "\nAttack: " + stats1[1] + "\nDefense: " + stats1[2], true)
				.addField(u2 + "'s " + out2.getName(), "HP: " + stats2[0] + "\nAttack: " + stats2[1] + "\nDefense: " + stats2[2], true)
				.setDescription(desc);
		return eb.build();
	}
}