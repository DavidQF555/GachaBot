package com.davidqf.gachabot.commands;

import com.davidqf.gachabot.Bot;
import com.davidqf.gachabot.Reference;
import com.davidqf.gachabot.Util;
import net.dv8tion.jda.api.entities.Message;

public class ShutdownCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m, String content) {
        m.getChannel().sendMessage(Util.createMessage("Shutting Down").build()).queue();
        Bot.jda.shutdown();
    }

    @Override
    public boolean hasPermission(Message m) {
        return m.getAuthor().getIdLong() == Reference.OWNER_ID;
    }

    @Override
    public String getDescription() {
        return "Shuts down this bot";
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.SHUTDOWN;
    }

}
