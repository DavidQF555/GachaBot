package com.davidqf.gachabot;

import com.davidqf.gachabot.commands.CommandAbstract;
import com.davidqf.gachabot.data.UserData;
import net.dv8tion.jda.api.entities.*;

import java.util.HashMap;
import java.util.TimerTask;

public class CommandProcessorTask extends TimerTask {

    @Override
    public void run() {
        if (!EventListener.messages.isEmpty() && !Bot.commands.isEmpty()) {
            for (int i = EventListener.messages.size() - 1; i >= 0; i--) {
                Message message = EventListener.messages.get(i);
                User user = message.getAuthor();
                long id = user.getIdLong();
                CommandAbstract c = detect(message);
                if (c != null && !user.isBot()) {
                    UserData data = Bot.userData.get(id);
                    if (data == null) {
                        data = new UserData(id, new HashMap<>());
                        Bot.userData.put(id, data);
                    }
                    TextChannel channel = message.getTextChannel();
                    if (c.hasPermission(message)) {
                        if (!c.allowInBattle() && data.getBattleOpponent() != null) {
                            channel.sendMessage(Util.createFailedMessage(user.getName() + ", this command cannot be used while in battle").build()).queue();
                        } else if (c.correctFormat(message)) {
                            if (channel instanceof PrivateChannel) {
                                c.onPrivateMessage(message);
                            } else {
                                c.onCommand(message);
                            }
                        } else {
                            message.getChannel().sendMessage(Util.createFailedMessage(user.getName() + ", incorrect format. Correct Format: `" + c.getFormat() + "`").build()).queue();
                        }
                    } else {
                        message.getChannel().sendMessage(Util.createFailedMessage(user.getName() + ", you do not have permission to use this command").build()).queue();
                    }
                }
                EventListener.messages.remove(message);
            }
        }
    }

    private CommandAbstract detect(Message m) {
        String s = m.getContentRaw();
        if (s.toLowerCase().startsWith(Reference.COMMAND.toLowerCase())) {
            for (CommandAbstract c : Bot.commands) {
                if (s.toLowerCase().startsWith(c.getActivatingName().toLowerCase(), Reference.COMMAND.length())) {
                    return c;
                }
            }
            for (CommandAbstract c : Bot.commands) {
                for (String pref : c.getAlternativeNames()) {
                    if (s.toLowerCase().startsWith(pref.toLowerCase(), Reference.COMMAND.length())) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

}
