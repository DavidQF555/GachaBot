package com.david.gachabot.commands;

import java.util.*;

import com.david.gachabot.Bot;

import net.dv8tion.jda.api.entities.Message;

public class RetrieveTeamCommand extends Command {

	@Override
	public void onCommand(Message m) {
		List<Integer> team = Bot.userData.get(m.getAuthor().getIdLong()).getTeam();
		if(!team.isEmpty()) {
			String mes = m.getAuthor().getAsMention() + " Your team: ```";
			for(int id : team) {
				mes += "\n" + Bot.characters.get(id).getName();
			}
			m.getChannel().sendMessage(mes + "```").queue();
			return;
		}
		m.getChannel().sendMessage(m.getAuthor().getAsMention() + " ```Your team is empty```").queue();
	}

	@Override
	public String getActivatingName() {
		return "myteam";
	}

	@Override
	public String getDescription() {
		return "Retrieves your team";
	}

	@Override
	public List<String> getAlternativeNames() {
		return new ArrayList<String>(Arrays.asList("mt"));
	}

}
