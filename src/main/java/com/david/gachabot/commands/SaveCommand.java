package com.david.gachabot.commands;

import com.david.gachabot.FileUtil;
import com.david.gachabot.Reference;
import com.david.gachabot.Util;
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
