package com.david.gachabot.commands;

import com.david.gachabot.Reference;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandAbstract {

    public void onCommand(Message m) {
    }

    public void onPrivateMessage(Message m) {
        onCommand(m);
    }

    public boolean hasPermission(Message m) {
        return true;
    }

    public boolean correctFormat(Message m) {
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
