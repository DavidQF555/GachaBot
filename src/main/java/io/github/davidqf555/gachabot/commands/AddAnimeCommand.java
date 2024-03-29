package io.github.davidqf555.gachabot.commands;

import io.github.davidqf555.gachabot.Bot;
import io.github.davidqf555.gachabot.JikanRetriever;
import io.github.davidqf555.gachabot.Reference;
import io.github.davidqf555.gachabot.Util;
import io.github.davidqf555.gachabot.data.LocalAnimeData;
import io.github.davidqf555.gachabot.data.SeriesData;
import com.github.doomsdayrs.jikan4java.types.main.anime.Anime;
import com.github.doomsdayrs.jikan4java.types.main.anime.character_staff.AnimeCharacter;
import com.github.doomsdayrs.jikan4java.types.support.related.Related;
import com.github.doomsdayrs.jikan4java.types.support.related.RelatedType;
import net.dv8tion.jda.api.entities.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddAnimeCommand extends CommandAbstract {

    public static List<Anime> getAllRelated(Anime st, List<Anime> vis) {
        vis.add(st);
        Related rel = st.related;
        ArrayList<RelatedType> pre = rel.prequel;
        if (pre != null) {
            check:
            for (RelatedType type : pre) {
                for (Anime a : vis) {
                    if (type.mal_id == a.mal_id) {
                        continue check;
                    }
                }
                getAllRelated(JikanRetriever.animeSearch(type.mal_id), vis);
            }
        }
        ArrayList<RelatedType> seq = rel.sequels;
        if (seq != null) {
            check:
            for (RelatedType type : seq) {
                for (Anime a : vis) {
                    if (type.mal_id == a.mal_id) {
                        continue check;
                    }
                }
                getAllRelated(JikanRetriever.animeSearch(type.mal_id), vis);
            }
        }
        return vis;
    }

    private static void addBestCharactersFromAnime(List<Anime> an, SeriesData series) {
        List<AnimeCharacter> pos = new ArrayList<>();
        double avg = 0;
        int total = 0;
        for (Anime a : an) {
            if (!a.status.equals("Not yet aired")) {
                int hours = 0;
                int minutes = 0;
                String[] s = a.duration.split(" ");
                for (int i = 0; i < s.length; i++) {
                    if (s[i].equals("hr")) {
                        hours = Integer.parseInt(s[i - 1]);
                    } else if (s[i].equals("min")) {
                        minutes = Integer.parseInt(s[i - 1]);
                    }
                }
                int episodes = a.episodes;
                if (a.airing && episodes < 1) {
                    String[] date = a.aired.from.substring(0, 10).split("-");
                    SimpleDateFormat form = new SimpleDateFormat("yyyy MM dd");
                    String[] cur = form.format(new Date(System.currentTimeMillis())).split(" ");
                    int days = (Integer.parseInt(cur[0]) - Integer.parseInt(date[0])) * 365 + (Integer.parseInt(cur[1]) - Integer.parseInt(date[1])) * 30 + Integer.parseInt(cur[2]) - Integer.parseInt(date[2]);
                    episodes = days / 10;
                }
                long time = episodes * (hours * 60 + minutes);
                avg += a.popularity * time;
                total += time;
            }
        }
        avg /= total;
        double limit = avg / 150000 / an.size() + 5;
        for (Anime a : an) {
            List<AnimeCharacter> chs = JikanRetriever.animeCharacters(a);
            int count = 0;
            check:
            for (AnimeCharacter c : chs) {
                for (AnimeCharacter check : pos) {
                    if (c.mal_id == check.mal_id) {
                        continue check;
                    }
                }
                pos.add(c);
                count++;
                if (count >= limit) {
                    break;
                }
            }
        }
        List<com.github.doomsdayrs.jikan4java.types.main.character.Character> chars = new ArrayList<>();
        double cut = 0;
        for (AnimeCharacter ac : pos) {
            com.github.doomsdayrs.jikan4java.types.main.character.Character c = JikanRetriever.getCharacter(ac.mal_id);
            if (c != null && c.member_favorites > 100) {
                chars.add(c);
                cut += c.member_favorites;
            }
        }
        cut /= chars.size();
        double charMin = avg / 500000 + 2;
        List<com.github.doomsdayrs.jikan4java.types.main.character.Character> out = new ArrayList<>();
        while (out.size() < charMin && !chars.isEmpty()) {
            for (int i = chars.size() - 1; i >= 0; i--) {
                com.github.doomsdayrs.jikan4java.types.main.character.Character c = chars.get(i);
                if (c.member_favorites >= cut) {
                    out.add(c);
                    chars.remove(i);
                }
            }
            cut *= 0.95;
        }
        Bot.updateExistingCharactersList(out, series);
    }

    @Override
    public void onCommand(Message m, String content) {
        String input = content.substring(Reference.COMMAND.length() + getCommandType().getActivatingName().length() + 1);
        System.out.println("Adding all related to: " + input);
        m.getChannel().sendMessage(Util.createMessage("Searching for `" + input + "` and all related series").build()).queue();
        Anime a = JikanRetriever.animeSearch(input);
        List<Anime> uniq = getAllRelated(a, new ArrayList<>());
        List<Anime> all = uniq.subList(0, uniq.size());
        for (int i = uniq.size() - 1; i >= 0; i--) {
            if (Bot.anime.get(uniq.get(i).mal_id) != null) {
                uniq.remove(i);
            }
        }
        if (uniq.isEmpty()) {
            m.getChannel().sendMessage(Util.createFailedMessage("Everything related to " + a.title + " is already added").build()).queue();
            return;
        }
        StringBuilder out = new StringBuilder("Added the following series: ```");
        SeriesData series = new SeriesData();
        series:
        for (LocalAnimeData data : Bot.anime.values()) {
            for (Anime an : all) {
                if (data.getID() == an.mal_id) {
                    series = data.getSeries();
                    break series;
                }
            }
        }
        for (Anime an : uniq) {
            LocalAnimeData data = new LocalAnimeData(series, an.title, an.mal_id);
            Bot.anime.put(an.mal_id, data);
            out.append("\n").append(an.title);
        }
        addBestCharactersFromAnime(all, series);
        m.getChannel().sendMessage(Util.createMessage(out + "```").build()).queue();
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

    @Override
    public List<String> getAlternativeNames() {
        return new ArrayList<>(Collections.singletonList("aa"));
    }

    @Override
    public String getDescription() {
        return "Adds a new anime series and characters";
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.ADD_ANIME;
    }
}