package com.ankoki.aspect.api;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class SlashCommand {
    private final CommandData data;
    private final Permission[] permissions;

    public SlashCommand(CommandData commandData, Permission... commandPermissions) {
        this.data = commandData;
        this.permissions = commandPermissions == null ? new Permission[0] : commandPermissions;
    }

    public CommandData getData() {
        return data;
    }

    public Permission[] getPermissions() {
        return permissions;
    }

    public abstract void onCommand(SlashCommandEvent event);
}
