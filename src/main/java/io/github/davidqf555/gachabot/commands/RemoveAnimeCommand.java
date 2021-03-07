package io.github.davidqf555.gachabot.commands;

import io.github.davidqf555.gachabot.Bot;
import io.github.davidqf555.gachabot.JikanRetriever;
import io.github.davidqf555.gachabot.Reference;
import io.github.davidqf555.gachabot.Util;
import io.github.davidqf555.gachabot.data.LocalAnimeData;
import io.github.davidqf555.gachabot.data.LocalCharacterData;
import io.github.davidqf555.gachabot.data.SeriesData;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;

public class RemoveAnimeCommand extends CommandAbstract {

    @Override
    public void onCommand(Message m, String content) {
        String input = content.substring(Reference.COMMAND.length() + getCommandType().getActivatingName().length() + 1);
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
    public String getDescription() {
        return "Removes all related anime and characters";
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.REMOVE_ANIME;
    }

    @Override
    public boolean correctFormat(String s) {
        return s.contains(" ");
    }

    @Override
    public boolean hasPermission(Message m) {
        return m.getAuthor().getIdLong() == Reference.OWNER_ID;
    }

    @Override
    public String getFormat() {
        return super.getFormat() + " [anime name]";
    }

}
