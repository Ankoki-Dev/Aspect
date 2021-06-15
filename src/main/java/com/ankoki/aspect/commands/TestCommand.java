package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class TestCommand extends SlashCommand {

    public TestCommand() {
        super(new CommandData("test", "Simple test command."),
                Permission.ADMINISTRATOR);
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        event.reply("Hey, this is a test command.").setEphemeral(true).queue();
    }
}
