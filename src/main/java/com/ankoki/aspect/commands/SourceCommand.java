package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SourceCommand extends SlashCommand {

    public SourceCommand() {
        super(new CommandData("source", "Information on the source code of Aspect."));
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        event.replyEmbeds(Utils.simpleEmbed(event.getUser(), "*Aspect* • Source",
                "Unfortunately, Aspect is not open source as of yet. However it does plan to be in the future, " +
                        "keep an eye on <https://www.github.com/Ankoki-Dev/> to see if it's put up!")).setEphemeral(true)
                .queue();
    }
}
