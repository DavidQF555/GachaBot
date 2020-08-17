package com.david.gachabot.commands;

import com.david.gachabot.Bot;
import com.david.gachabot.Util;
import com.david.gachabot.data.CharacterInstanceData;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Command
public class RetrieveTeamCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m) {
        List<CharacterInstanceData> team = Bot.userData.get(m.getAuthor().getIdLong()).getTeam();
        if (!team.isEmpty()) {
            StringBuilder mes = new StringBuilder(m.getAuthor().getName() + ", your team: ```");
            for (CharacterInstanceData data : team) {
                mes.append("\n").append(data.getCharacterData().getName());
            }
            m.getChannel().sendMessage(Util.createMessage(mes + "```").build()).queue();
            return;
        }
        m.getChannel().sendMessage(Util.createFailedMessage(m.getAuthor().getName() + ", your team is empty").build()).queue();
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
        return new ArrayList<>(Collections.singletonList("mt"));
    }

}
