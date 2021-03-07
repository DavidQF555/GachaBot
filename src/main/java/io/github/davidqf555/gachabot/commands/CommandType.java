package io.github.davidqf555.gachabot.commands;

import java.util.ArrayList;
import java.util.List;

public enum CommandType {

    ADD_ANIME("addanime", new AddAnimeCommand(), true, true),
    CHARACTER_LIST("characters", new CharacterListCommand(), true, false),
    ROLL("roll", new GachaRollCommand(), false, false),
    HELP("help", new HelpCommand(), true, false),
    REMOVE_ANIME("removeanime", new RemoveAnimeCommand(), true, false),
    RETRIEVE_TEAM("myteam", new RetrieveTeamCommand(), true, false),
    USER_CHARACTER_LIST("mylist", new RetrieveUserCharacterListCommand(), true, false),
    SAVE("save", new SaveCommand(), true, false),
    SHUTDOWN("shut", new ShutdownCommand(), true, false),
    START_BATTLE("battle", new StartBattleCommand(), false, false),
    CHANGE_TEAM("team", new TeamChangeCommand(), false, false);

    private final String name;
    private final CommandAbstract command;
    private final boolean battle;
    private final boolean retrieves;

    CommandType(String name, CommandAbstract command, boolean battle, boolean retrieves) {
        this.name = name;
        this.command = command;
        this.battle = battle;
        this.retrieves = retrieves;
    }

    public static List<CommandType> getRetrievalCommands() {
        List<CommandType> commands = new ArrayList<>();
        for (CommandType command : values()) {
            if (command.retrieves()) {
                commands.add(command);
            }
        }
        return commands;
    }

    public String getActivatingName() {
        return name;
    }

    public boolean allowInBattle() {
        return battle;
    }

    public CommandAbstract getCommand() {
        return command;
    }

    public boolean retrieves() {
        return retrieves;
    }
}
