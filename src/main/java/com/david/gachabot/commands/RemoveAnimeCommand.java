package com.david.gachabot.commands;

import java.util.ArrayList;

import com.david.gachabot.Bot;
import com.david.gachabot.JikanRetriever;
import com.david.gachabot.Reference;
import com.david.gachabot.Util;
import com.david.gachabot.data.LocalAnimeData;
import com.david.gachabot.data.LocalCharacterData;
import com.david.gachabot.data.SeriesData;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;

import net.dv8tion.jda.api.entities.Message;

@Command
public class RemoveAnimeCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m) {
        String input = m.getContentRaw().substring(Reference.COMMAND.length() + getActivatingName().length() + 1);
        System.out.println("Removing all related to: " + input);
        m.getChannel().sendMessage(Util.createMessage("Searching for `" + input + "` and all related series").build()).queue();
        Anime a = JikanRetriever.animeSearch(input);
        StringBuilder mes = new StringBuilder("Removed the following: ```");
        LocalAnimeData an = Bot.anime.get(a.mal_id);
        if (an == null) {
            m.getChannel().sendMessage(Util.createFailedMessage(a.title + " has not been not added yet").build()).queue();
            return;
        }
        SeriesData series = an.getSeries();
        for (LocalAnimeData data : series.getAnime()) {
            for (int id : new ArrayList<>(Bot.anime.keySet())) {
                if (data.getID() == id) {
                    Bot.anime.remove(id);
                    mes.append("\n").append(data.getTitle());
                }
            }
        }
        for (LocalCharacterData data : series.getCharacters()) {
            for (int id : new ArrayList<>(Bot.characters.keySet())) {
                if (data.getID() == id) {
                    Bot.characters.remove(id);
                }
            }
        }
        Bot.updateExistingCharactersList(null, null);
        m.getChannel().sendMessage(Util.createMessage(mes + "```").build()).queue();
    }

    @Override
    public String getActivatingName() {
        return "removeanime";
    }

    @Override
    public String getDescription() {
        return "Removes all related anime and characters";
    }

    @Override
    public boolean correctFormat(Message m) {
        return m.getContentRaw().contains(" ");
    }

    @Override
    public boolean hasPermission(Message m) {
        return m.getAuthor().getIdLong() == Reference.OWNER_ID;
    }

    @Override
    public String getFormat() {
        return Reference.COMMAND + getActivatingName() + " [anime name]";
    }

}
