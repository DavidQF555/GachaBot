package com.davidqf.gachabot.commands;

import com.davidqf.gachabot.Reference;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandAbstract {

    public void onCommand(Message m, String content) {
    }

    public void onPrivateMessage(Message m, String content) {
        onCommand(m, content);
    }

    public boolean hasPermission(Message m) {
        return true;
    }

    public boolean correctFormat(String m) {
        return true;
    }

    public String getFormat() {
        return Reference.COMMAND + getActivatingName();
    }

    public List<String> getAlternativeNames() {
        return new ArrayList<>();
    }

    public boolean allowInBattle() {
        return true;
    }

    public abstract String getActivatingName();

    public abstract String getDescription();

}
