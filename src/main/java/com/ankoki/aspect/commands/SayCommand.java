package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class SayCommand extends SlashCommand {

    public SayCommand() {
        super(new CommandData("say", "Makes me say something.")
                .addOptions(new OptionData(STRING, "text", "What am I saying?")
                        .setRequired(true))
                .addOptions(new OptionData(BOOLEAN, "ephemeral", "Whether the message can be seen only by you. Defaults to false.")),
                Permission.ADMINISTRATOR);
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        String text = event.getOption("text").getAsString();
        OptionMapping ephemeralMapping = event.getOption("ephemeral");
        event.reply(text).setEphemeral(ephemeralMapping != null && ephemeralMapping.getAsBoolean()).queue();
    }
}
