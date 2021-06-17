package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class StatusCommand extends SlashCommand {

    public StatusCommand() {
        super(new CommandData("status", "Sets the status of the bot.")
                        .addOptions(new OptionData(STRING, "type", "Listening, competing, playing, or watching.")
                                .setRequired(true))
                        .addOptions(new OptionData(STRING, "text", "Text to go in.")
                                .setRequired(true)),
                Permission.ADMINISTRATOR);
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        String type = event.getOption("type").getAsString();
        String activity = event.getOption("text").getAsString();
        JDA jda = event.getJDA();
        switch (type.toUpperCase()) {
            case "LISTENING":
                jda.getPresence().setPresence(Activity.listening(activity), true);
                event.reply(":white_check_mark: Status has been updated!").setEphemeral(true).queue();
                break;
            case "COMPETING":
                jda.getPresence().setPresence(Activity.competing(activity), true);
                event.reply(":white_check_mark: Status has been updated!").setEphemeral(true).queue();
                break;
            case "PLAYING":
                jda.getPresence().setPresence(Activity.playing(activity), true);
                event.reply(":white_check_mark: Status has been updated!").setEphemeral(true).queue();
                break;
            case "WATCHING":
                jda.getPresence().setPresence(Activity.watching(activity), true);
                event.reply(":white_check_mark: Status has been updated!").setEphemeral(true).queue();
                break;
            default:
                event.reply(":no_entry_sign: This is not a valid option!").setEphemeral(true).queue();
        }
    }
}
