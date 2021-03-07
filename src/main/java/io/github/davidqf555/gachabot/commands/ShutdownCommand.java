package io.github.davidqf555.gachabot.commands;

import io.github.davidqf555.gachabot.Bot;
import io.github.davidqf555.gachabot.Reference;
import io.github.davidqf555.gachabot.Util;
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
