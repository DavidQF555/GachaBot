package io.github.davidqf555.gachabot.commands;

import io.github.davidqf555.gachabot.Reference;
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
        return Reference.COMMAND + getCommandType().getActivatingName();
    }

    public List<String> getAlternativeNames() {
        return new ArrayList<>();
    }

    public abstract String getDescription();

    public abstract CommandType getCommandType();
}
