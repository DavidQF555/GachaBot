package com.davidqf.gachabot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.davidqf.gachabot.Bot;

import net.dv8tion.jda.api.entities.Message;

@Command
public class HelpCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m) {
        StringBuilder out = new StringBuilder("```");
        for (CommandAbstract c : Bot.commands) {
            out.append("\n").append(c.getFormat()).append(": ").append(c.getDescription());
        }
        m.getChannel().sendMessage(out + "```").queue();
    }

    @Override
    public String getActivatingName() {
        return "help";
    }

    @Override
    public List<String> getAlternativeNames() {
        return new ArrayList<String>(Collections.singletonList("h"));
    }

    @Override
    public String getDescription() {
        return "Retrieves a list and description of all commands";
    }

}
