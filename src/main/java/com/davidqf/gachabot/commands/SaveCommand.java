package com.davidqf.gachabot.commands;

import com.davidqf.gachabot.FileUtil;
import com.davidqf.gachabot.Reference;
import com.davidqf.gachabot.Util;
import net.dv8tion.jda.api.entities.Message;

@Command
public class SaveCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m) {
        FileUtil.saveSeriesData();
        FileUtil.saveUserData();
        m.getChannel().sendMessage(Util.createMessage("Saved all data").build()).queue();
    }

    @Override
    public boolean hasPermission(Message m) {
        return m.getAuthor().getIdLong() == Reference.OWNER_ID;
    }

    @Override
    public String getActivatingName() {
        return "save";
    }

    @Override
    public String getDescription() {
        return "Saves all data";
    }

}
