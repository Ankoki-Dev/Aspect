package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Lookup;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class LookupCommand extends SlashCommand {

    public LookupCommand() {
        super(new CommandData("lookup", "Gets the history and information of a member.")
                .addOptions(new OptionData(USER, "member", "The member you want to look up."))
                .addOptions(new OptionData(INTEGER, "id", "The ID of the member you want to look up.")));
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        OptionMapping memberMapping = event.getOption("member");
        OptionMapping idMapping = event.getOption("id");
        if (memberMapping == null && idMapping == null) {
            event.reply("You need to provide a member or ID!").setEphemeral(true).queue();
        } else if (memberMapping != null && idMapping != null) {
            if (memberMapping.getAsMember().getIdLong() != idMapping.getAsLong()) {
                event.reply("You can only provide one member or one id!").queue();
            } else {
                Lookup lookup = Lookup.performLookup(memberMapping.getAsMember());
                event.replyEmbeds(lookup.getEmbed()).queue();
            }
        } else if (memberMapping != null) {
            Lookup lookup = Lookup.performLookup(memberMapping.getAsMember());
            event.replyEmbeds(lookup.getEmbed()).queue();
        } else {
            event.deferReply(true).queue();
            event.getGuild().retrieveMemberById(idMapping.getAsLong())
                    .queue(member -> {
                        if (member == null) {
                            event.getHook().editOriginalEmbeds(Utils.simpleEmbed(event.getUser(), "Invalid ID",
                                    "This wasn't a valid discord ID. Did you get it right?"))
                                    .queue();
                            return;
                        }
                        Lookup lookup = Lookup.performLookup(member);
                        event.getHook().editOriginalEmbeds(lookup.getEmbed())
                                .queue();
                    });
        }
    }
}
