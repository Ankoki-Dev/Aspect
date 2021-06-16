package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class HostingCommand extends SlashCommand {

    public HostingCommand() {
        super(new CommandData("hosting", "Details on the host we use to keep this bot alive"));
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        event.replyEmbeds(Utils.simpleEmbed(event.getUser(), "Hosting Details",
                "This bot is kept up by ChargedHosting, who have very cheap prices!",
                "If you are interested in using their services, find them at <https://panel.chargedhosting.net/>!"))
                .setEphemeral(true)
                .queue();
    }
}
