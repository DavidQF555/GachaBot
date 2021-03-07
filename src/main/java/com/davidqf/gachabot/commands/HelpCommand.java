package com.davidqf.gachabot.commands;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m, String content) {
        StringBuilder out = new StringBuilder("```");
        for (CommandType type : CommandType.values()) {
            CommandAbstract c = type.getCommand();
            out.append("\n").append(c.getFormat()).append(": ").append(c.getDescription());
        }
        m.getChannel().sendMessage(out + "```").queue();
    }

    @Override
    public List<String> getAlternativeNames() {
        return new ArrayList<String>(Collections.singletonList("h"));
    }

    @Override
    public String getDescription() {
        return "Retrieves a list and description of all commands";
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.HELP;
    }

}
