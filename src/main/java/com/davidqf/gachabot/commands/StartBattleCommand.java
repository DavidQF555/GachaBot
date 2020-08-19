package com.davidqf.gachabot.commands;

import com.davidqf.gachabot.BattleListener;
import com.davidqf.gachabot.Bot;
import com.davidqf.gachabot.Reference;
import com.davidqf.gachabot.Util;
import com.davidqf.gachabot.data.UserData;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.Timer;
import java.util.TimerTask;

@Command
public class StartBattleCommand extends CommandAbstract {

    private final static Timer inv = new Timer();

    @Override
    public void onCommand(Message m) {
        User u1 = m.getAuthor();
        UserData user1 = Bot.userData.get(u1.getIdLong());
        if (user1.getTeam().isEmpty()) {
            m.getChannel().sendMessage(Util.createFailedMessage(u1.getName() + ", your team is empty").build()).queue();
            return;
        }
        String name = m.getContentRaw().substring(Reference.COMMAND.length() + getActivatingName().length() + 1);
        User u2 = null;
        UserData user2 = null;
        for (Member mem : m.getGuild().getMembers()) {
            if (mem.getEffectiveName().equalsIgnoreCase(name) || (mem.getNickname() != null && mem.getNickname().equalsIgnoreCase(name))) {
                if (mem.getOnlineStatus() == OnlineStatus.OFFLINE) {
                    m.getChannel().sendMessage(Util.createFailedMessage(mem.getNickname() + " is currently offline").build()).queue();
                    return;
                }
                u2 = mem.getUser();
                user2 = Bot.userData.get(mem.getIdLong());
            }
        }
        if (u2 == null) {
            m.getChannel().sendMessage(Util.createFailedMessage("Could not find `" + name + "` in this server").build()).queue();
            return;
        } else if (user2 == null) {
            m.getChannel().sendMessage(Util.createFailedMessage(u2.getName() + " has not started using GachaBot yet").build()).queue();
            return;
        }
        Message mes = m.getChannel().sendMessage(Util.createMessage(u2.getAsMention() + " " + u1.getName() + " has challenged you to a battle! Do you accept?").build()).complete();
        long mesId = mes.getIdLong();
        BattleListener.invitations.put(mesId, new UserData[]{user1, user2});
        mes.addReaction(Reference.ACCEPT_CODEPOINTS).queue();
        mes.addReaction(Reference.DECLINE_CODEPOINTS).queue();
        inv.schedule(new TimerTask() {
            @Override
            public void run() {
                if (BattleListener.invitations.containsKey(mesId)) {
                    mes.delete().queue();
                    BattleListener.invitations.remove(mesId);
                }
            }
        }, 300000);
    }

    @Override
    public void onPrivateMessage(Message m) {
        m.getChannel().sendMessage(Util.createFailedMessage("You cannot battle in private chat").build()).queue();
    }

    @Override
    public boolean allowInBattle() {
        return false;
    }

    @Override
    public String getActivatingName() {
        return "battle";
    }

    @Override
    public String getDescription() {
        return "Starts a battle";
    }

    @Override
    public boolean correctFormat(Message m) {
        return m.getContentRaw().split(" ").length > 1;
    }

    @Override
    public String getFormat() {
        return super.getFormat() + " [opponent name]";
    }

}