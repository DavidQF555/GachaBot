package com.david.gachabot.commands;

import java.util.List;

import com.david.gachabot.Bot;
import com.david.gachabot.data.*;

import net.dv8tion.jda.api.entities.*;

@Command
public class TeamChangeCommand extends CommandAbstract {

	@Override
	public void onCommand(Message m) {
		String[] s = m.getContentRaw().split(" ");
		User user = m.getAuthor();
		UserData userData = Bot.userData.get(user.getIdLong());
		List<Integer> team = userData.getTeam();
		if(s[1].equalsIgnoreCase("add")) {
			if(team.size() < 5) {
				String name = "";
				for(int i = 2; i < s.length; i ++) {
					name += s[i] + " ";
				}
				name = name.substring(0, name.length() - 1);
				for(LocalCharacterData data : Bot.characters.values()) {
					if(data.getName().equalsIgnoreCase(name)) {
						if(userData.getCharacters().keySet().contains(data.getID())) {
							team.add(data.getID());
							m.getChannel().sendMessage(user.getAsMention() + " Added `" + data.getName() + "` to your team").queue();
						}
						else {
							m.getChannel().sendMessage(user.getAsMention() + " You do not have `" + data.getName() + "`").queue();
						}
						return;
					}
				}
				m.getChannel().sendMessage(user.getAsMention() + " Could not find `" + name + "`").queue();
			}
			else {
				m.getChannel().sendMessage(user.getAsMention() + " ```Your team is already full```").queue();
			}
		}
		else if(s[1].equalsIgnoreCase("remove")) {
			if(!team.isEmpty()) {
				String name = "";
				for(int i = 2; i < s.length; i ++) {
					name += s[i] + " ";
				}
				name = name.substring(0, name.length() - 1);
				for(int i = 0; i < team.size(); i ++) {
					String dataName = Bot.characters.get(team.get(i)).getName();
					if(dataName.equalsIgnoreCase(name)) {
						team.remove(i);
						m.getChannel().sendMessage(user.getAsMention() + " Removed `" + dataName + "` from your team").queue();
						return;
					}
				}
				m.getChannel().sendMessage(user.getAsMention() + " Could not find `" + name + "` on your team").queue();
			}
			else {
				m.getChannel().sendMessage(user.getAsMention() + " Your team is already empty").queue();
			}
		}
	}

	@Override
	public String getActivatingName() {
		return "team";
	}

	@Override
	public boolean allowInBattle() {
		return false;
	}

	@Override
	public boolean correctFormat(Message m) {
		String[] s = m.getContentRaw().split(" ");
		if(s.length >= 3 && (s[1].equalsIgnoreCase("add") || s[1].equalsIgnoreCase("remove"))) {
			return true;
		}
		return false;
	}

	@Override
	public String getFormat() {
		return super.getFormat() + " [add/remove] [character name]";
	}

	@Override
	public String getDescription() {
		return "Adds or removes characters from your team";
	}
}
