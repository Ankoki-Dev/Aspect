package com.ankoki.aspect.listeners;

import com.ankoki.aspect.Aspect;
import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        //We don't want to continue if there is no guild.
        if (event.getGuild() == null) return;
        //Gets all the commands that have been registered
        for (SlashCommand command : Aspect.getInstance().getCommands()) {
            //Checks if the command is the same its alias.
            if (command.getData().getName().equalsIgnoreCase(event.getName())) {
                Permission[] permissions = command.getPermissions();
                if (permissions == null || event.getMember().hasPermission(permissions)) {
                    //It matches and the player has the permission! We now execute this command
                    command.onCommand(event);
                } else {
                    //If they do not have the permission, tell them this!
                    event.reply("*You don't have permission to execute this!*\n" +
                            "You require **one** of the following permissions:\n" +
                            Utils.readablePermissions(permissions)).setEphemeral(true).queue();
                }
            }
        }
    }
}