package com.davidqf.gachabot;

import com.davidqf.gachabot.commands.CommandAbstract;
import com.davidqf.gachabot.data.UserData;
import net.dv8tion.jda.api.entities.*;

import java.util.*;

public class CommandProcessorTask extends TimerTask {

    private final List<RetrievalThread> retrievalThreads;

    public CommandProcessorTask() {
        retrievalThreads = new ArrayList<>();
    }

    @Override
    public void run() {
        if (!Bot.commands.isEmpty()) {
            if (!retrievalThreads.isEmpty()) {
                RetrievalThread thread = retrievalThreads.get(0);
                if (!thread.isAlive()) {
                    thread.start();
                }
            }
            Message message;
            while ((message = EventListener.messages.poll()) != null) {
                User user = message.getAuthor();
                long id = user.getIdLong();
                String[] split = message.getContentRaw().split("\n");
                for (String s : split) {
                    CommandAbstract c = detect(s);
                    if (c != null && !user.isBot()) {
                        UserData data = Bot.userData.get(id);
                        if (data == null) {
                            data = new UserData(id, new HashMap<>());
                            Bot.userData.put(id, data);
                        }
                        MessageChannel channel = message.getChannel();
                        if (c.hasPermission(message)) {
                            if (!c.allowInBattle() && data.getBattleOpponent() != null) {
                                channel.sendMessage(Util.createFailedMessage(user.getName() + ", this command cannot be used while in battle").build()).queue();
                            } else if (c.correctFormat(s)) {
                                if (channel instanceof TextChannel) {
                                    if (Bot.retrievalCommands.contains(c)) {
                                        retrievalThreads.add(new RetrievalThread(c, message, s));
                                    } else {
                                        c.onCommand(message, s);
                                    }
                                } else if (channel instanceof PrivateChannel) {
                                    if (Bot.retrievalCommands.contains(c)) {
                                        retrievalThreads.add(new RetrievalThread(c, message, s));
                                    } else {
                                        c.onPrivateMessage(message, s);
                                    }
                                }
                            } else {
                                message.getChannel().sendMessage(Util.createFailedMessage(user.getName() + ", incorrect format. Correct Format: `" + c.getFormat() + "`").build()).queue();
                            }
                        } else {
                            message.getChannel().sendMessage(Util.createFailedMessage(user.getName() + ", you do not have permission to use this command").build()).queue();
                        }
                    }
                }
            }
        }
    }

    private CommandAbstract detect(String s) {
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

    private class RetrievalThread extends Thread {

        private final CommandAbstract command;
        private final Message message;
        private final String content;

        private RetrievalThread(CommandAbstract command, Message message, String content) {
            this.command = command;
            this.message = message;
            this.content = content;
        }

        @Override
        public void run() {
            command.onCommand(message, content);
            retrievalThreads.remove(this);
        }
    }

}