package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Lookup;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class LookupCommand extends SlashCommand {

    public LookupCommand() {
        super(new CommandData("lookup", "Gets the history and information of a member.")
                .addOptions(new OptionData(USER, "member", "The member you want to look up.")
                        .setRequired(true)));
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        Lookup lookup = Lookup.performLookup(event.getOption("member").getAsMember());
        event.replyEmbeds(lookup.getEmbed()).queue();
    }
}
