package com.david.gachabot.commands;

import com.david.gachabot.Bot;
import com.david.gachabot.Reference;
import com.david.gachabot.Util;
import net.dv8tion.jda.api.entities.Message;

@Command
public class ShutdownCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m) {
        m.getChannel().sendMessage(Util.createMessage("Shutting Down").build()).queue();
        Bot.jda.shutdown();
    }

    @Override
    public String getActivatingName() {
        return "shut";
    }

    @Override
    public boolean hasPermission(Message m) {
        return m.getAuthor().getIdLong() == Reference.OWNER_ID;
    }

    @Override
    public String getDescription() {
        return "Shuts down this bot";
    }

}
