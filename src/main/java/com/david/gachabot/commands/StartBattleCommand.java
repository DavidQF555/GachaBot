package com.david.gachabot.commands;

import com.david.gachabot.*;
import com.david.gachabot.data.*;

import net.dv8tion.jda.api.entities.*;

public class StartBattleCommand extends Command {

	@Override
	public void onCommand(Message m) {
		User u1 = m.getAuthor();
		UserData user1 = Bot.userData.get(u1.getIdLong());
		if(user1.getTeam().isEmpty()) {
			m.getChannel().sendMessage(u1.getAsMention() + " ```Your team is empty```").queue();
			return;
		}
		String name = m.getContentRaw().substring(Reference.COMMAND.length() + getActivatingName().length() + 1);
		User u2 = null;
		UserData user2 = null;
		for(Member mem : m.getGuild().getMembers()) {
			if(mem.getNickname().equalsIgnoreCase(name) || mem.getEffectiveName().equalsIgnoreCase(name)) {
				u2 = mem.getUser();
				user2 = Bot.userData.get(mem.getIdLong());
			}
		}
		if(u2 == null) {
			m.getChannel().sendMessage(u1.getAsMention() + " ```Could not find " + name + " in this server```").queue();
			return;
		}
		else if(user2 == null) {
			m.getChannel().sendMessage(u1.getAsMention() + " ```" + u2.getName() + " has not started using GachaBot yet```").queue();
			return;
		}
		Message mes = m.getChannel().sendMessage(u1.getAsMention() + u2.getAsMention() + " ```A battle has started between " + u1.getName() + " and " + u2.getName() + "! " + u1.getName() + "has the first move!```").complete();
		mes.addReaction(Reference.ATTACK_CODEPOINTS);
		mes.addReaction(Reference.WAIT_CODEPOINTS);
		for(int i = 0; i < user1.getTeam().size(); i ++) {
			m.addReaction(Reference.SWAP_CODEPOINTS.get(i));
		}
		user1.setBattleOpponent(user2);
		user2.setBattleOpponent(user1);
		BattleListener.battleData.add(new BattleData(user1, 1, user2, 1, mes));
	}

	@Override
	public void onPrivateMessage(Message m) {
		m.getChannel().sendMessage(m.getAuthor().getAsMention() + " ```You cannot battle in private chat```").queue();
	}

	@Override
	public boolean allowInBattle() {
		return false;
	}

	@Override
	public String getActivatingName() {
		return "battle";
	}

	@Override
	public String getDescription() {
		return "Starts a battle";
	}

	@Override
	public boolean correctFormat(Message m) {
		return m.getContentRaw().split(" ").length > 1;
	}

	@Override
	public String getFormat() {
		return Reference.COMMAND + getActivatingName() + " [opponent name]";
	}

}
