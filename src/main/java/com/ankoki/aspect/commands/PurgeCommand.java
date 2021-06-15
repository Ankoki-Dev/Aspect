package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class PurgeCommand extends SlashCommand {

    public PurgeCommand() {
        super(new CommandData("purge", "Purges messages from a channel.")
                .addOptions(new OptionData(INTEGER, "amount", "Amount of messages to delete, defaults to 100."))
                .addOptions(new OptionData(CHANNEL, "channel", "Channel to delete messages in, defaults to your current channel.")),
                Permission.MESSAGE_MANAGE);
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        OptionMapping amountMapping = event.getOption("amount");
        OptionMapping channelMapping = event.getOption("channel");
        int amount = Math.max(2, Math.min(200, amountMapping == null ? 100 : (int) amountMapping.getAsLong()));
        MessageChannel channel = channelMapping == null ? event.getChannel() : channelMapping.getAsMessageChannel();
        assert channel != null;
        channel.getIterableHistory()
                .takeAsync(amount)
                .thenAccept(channel::purgeMessages);
        event.replyEmbeds(Utils.simpleEmbed(event.getUser(), "**Successful Purge**",
                "The previous " + amount + " messages have been purged.")).setEphemeral(true).queue();
    }
}
