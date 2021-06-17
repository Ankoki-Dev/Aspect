package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class SuggestCommand extends SlashCommand {

    public SuggestCommand() {
        super(new CommandData("suggest", "Give a suggestion for any plugin ideas/improvements, " +
                "or discord improvements.")
                .addOptions(new OptionData(STRING, "suggestion", "What do you want to suggest?")
                        .setRequired(true)));
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        String suggestion = event.getOption("suggestion").getAsString();
        JDA jda = event.getJDA();
        Guild guild = jda.getGuildById(747818545466966176L);
        assert guild != null;
        TextChannel channel = guild.getTextChannelById(801123202419130368L);
        assert channel != null;
        channel.sendMessage(Utils.simpleEmbed(event.getUser(), "New Suggestion", suggestion)).queue();
        event.reply("Your suggestion has been sent!").setEphemeral(true).queue();
    }
}
