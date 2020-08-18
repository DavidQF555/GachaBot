package com.davidqf.gachabot.commands;

import com.davidqf.gachabot.Bot;
import com.davidqf.gachabot.Util;
import com.davidqf.gachabot.data.CharacterInstanceData;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Command
public class RetrieveUserCharacterListCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m) {
        long id = m.getAuthor().getIdLong();
        if (Bot.userData.containsKey(id)) {
            StringBuilder out = new StringBuilder(m.getAuthor().getName() + ", you own the following: ```");
            for (CharacterInstanceData data : Bot.userData.get(id).getCharacters().values()) {
                out.append("\n").append(data.getCharacterData().getName());
            }
            m.getChannel().sendMessage(Util.createMessage(out + "```").build()).queue();
            return;
        }
        m.getChannel().sendMessage(Util.createMessage(m.getAuthor().getName() + ", you have nothing").build()).queue();
    }

    @Override
    public String getActivatingName() {
        return "mylist";
    }

    @Override
    public List<String> getAlternativeNames() {
        return new ArrayList<>(Collections.singletonList("ml"));
    }

    @Override
    public String getDescription() {
        return "Retrieves the user's character list";
    }

}
