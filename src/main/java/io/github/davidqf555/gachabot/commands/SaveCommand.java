package io.github.davidqf555.gachabot.commands;

import io.github.davidqf555.gachabot.FileUtil;
import io.github.davidqf555.gachabot.Reference;
import io.github.davidqf555.gachabot.Util;
import net.dv8tion.jda.api.entities.Message;

public class SaveCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m, String content) {
        FileUtil.saveSeriesData();
        FileUtil.saveUserData();
        m.getChannel().sendMessage(Util.createMessage("Saved all data").build()).queue();
    }

    @Override
    public boolean hasPermission(Message m) {
        return m.getAuthor().getIdLong() == Reference.OWNER_ID;
    }

    @Override
    public String getDescription() {
        return "Saves all data";
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.SAVE;
    }

}
