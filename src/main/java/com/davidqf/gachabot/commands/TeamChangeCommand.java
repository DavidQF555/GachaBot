package com.davidqf.gachabot.commands;

import java.util.List;

import com.davidqf.gachabot.Bot;
import com.davidqf.gachabot.Util;
import com.davidqf.gachabot.data.CharacterInstanceData;
import com.davidqf.gachabot.data.LocalCharacterData;
import com.davidqf.gachabot.data.UserData;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

@Command
public class TeamChangeCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m, String content) {
        String[] s = content.split(" ");
        User user = m.getAuthor();
        UserData userData = Bot.userData.get(user.getIdLong());
        List<CharacterInstanceData> team = userData.getTeam();
        if (s[1].equalsIgnoreCase("add")) {
            if (team.size() < 5) {
                StringBuilder name = new StringBuilder();
                for (int i = 2; i < s.length; i++) {
                    name.append(s[i]).append(" ");
                }
                name = new StringBuilder(name.substring(0, name.length() - 1));
                for (LocalCharacterData data : Bot.characters.values()) {
                    if (data.getName().equalsIgnoreCase(name.toString())) {
                        if (userData.getCharacters().containsKey(data)) {
                            CharacterInstanceData inst = userData.getCharacters().get(data);
                            if (team.contains(inst)) {
                                m.getChannel().sendMessage(Util.createFailedMessage(user.getName() + ", `" + data.getName() + "` is already on your team").build()).queue();
                            } else {
                                team.add(inst);
                                m.getChannel().sendMessage(Util.createMessage(user.getName() + ", added `" + data.getName() + "` to your team").build()).queue();
                            }
                        } else {
                            m.getChannel().sendMessage(Util.createFailedMessage(user.getName() + ", you do not have `" + data.getName() + "`").build()).queue();
                        }
                        return;
                    }
                }
                m.getChannel().sendMessage(Util.createFailedMessage("Could not find `" + name + "`").build()).queue();
            } else {
                m.getChannel().sendMessage(Util.createFailedMessage(user.getName() + ", Your team is already full").build()).queue();
            }
        } else if (s[1].equalsIgnoreCase("remove")) {
            if (!team.isEmpty()) {
                StringBuilder name = new StringBuilder();
                for (int i = 2; i < s.length; i++) {
                    name.append(s[i]).append(" ");
                }
                name = new StringBuilder(name.substring(0, name.length() - 1));
                for (int i = 0; i < team.size(); i++) {
                    String dataName = team.get(i).getCharacterData().getName();
                    if (dataName.equalsIgnoreCase(name.toString())) {
                        team.remove(i);
                        m.getChannel().sendMessage(Util.createMessage(user.getName() + ", removed `" + dataName + "` from your team").build()).queue();
                        return;
                    }
                }
                m.getChannel().sendMessage(Util.createFailedMessage(user.getName() + ", could not find `" + name + "` on your team").build()).queue();
            } else {
                m.getChannel().sendMessage(Util.createFailedMessage(user.getName() + ", your team is already empty").build()).queue();
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
    public boolean correctFormat(String s) {
        String[] split = s.split(" ");
        return split.length >= 3 && (split[1].equalsIgnoreCase("add") || split[1].equalsIgnoreCase("remove"));
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
