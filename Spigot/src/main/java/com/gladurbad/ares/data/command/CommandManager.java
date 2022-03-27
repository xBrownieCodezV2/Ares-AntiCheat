package com.gladurbad.ares.data.command;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.command.impl.*;

import java.util.*;

public class CommandManager {

    private final PlayerData data;

    private final Map<String, Command> commands;

    public CommandManager(PlayerData data) {
        this.data = data;

        this.commands = new HashMap<>();

        commands.put("alerts", new AlertCommand(data, new String[]{}));
        commands.put("gui", new GUICommand(data, new String[]{}));
        commands.put("logs", new LogsCommand(data, new String[]{"<player>"}));
        commands.put("top", new TopCommand(data, new String[]{}));
        commands.put("reload", new ReloadCommand(data, new String[]{}));
        commands.put("playerinfo", new PlayerInfoCommand(data, new String[]{"<player>"}));
        commands.put("checks", new ChecksCommand(data, new String[]{}));
        commands.put("info", new InfoCommand(data, new String[]{}));
    }

    public void handle(String[] args) {
        // Split message by spaces into separate arguments, and make them lowercase for easier parsing.
        List<String> arguments = new ArrayList<>(Arrays.asList(args));

        if (arguments.size() == 0) {
            sendCommands();
            return;
        }

        // The wanted command should always be the first argument.
        String inputCommand = arguments.get(0);

        // Attempt to get the command from the command map.
        Command executable = commands.get(inputCommand);

        // Check if the command is not null so you can handle it.
        if (executable != null) {

            // Remove the wanted command argument from the list.
            arguments.remove(0);

            // Check if the amount of arguments is valid.
            String[] commandArgs = executable.getArguments();

            if (commandArgs == null || commandArgs.length == arguments.size()) {
                executable.handle(arguments);
            } else {
                // Tell the sender that they did not input the correct arguments.
                data.message(Ares.INSTANCE.getConfigManager().getInvalidArguments());

                for (String argument : executable.getArguments()) {
                    data.message(Ares.INSTANCE.getConfigManager().getArgumentResponseColor() + argument);
                }
            }
        } else {
            sendCommands();
        }
    }

    private void sendCommands() {
        data.message(Ares.INSTANCE.getConfigManager().getCommandResponseHeader());
        commands.forEach((name, command) -> data.message(Ares.INSTANCE.getConfigManager().getCommandResponseColor()
                + "/ares " + name + " " + String.join(", ", command.getArguments())));
    }

}
