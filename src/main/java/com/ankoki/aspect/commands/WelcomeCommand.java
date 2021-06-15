package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class WelcomeCommand extends SlashCommand {

    public WelcomeCommand() {
        super(new CommandData("welcome", "Sends the welcome screen."), Permission.ADMINISTRATOR);
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        event.reply("").addFile(Utils.getImage("aspect_logo.png")).queue();
    }
}
