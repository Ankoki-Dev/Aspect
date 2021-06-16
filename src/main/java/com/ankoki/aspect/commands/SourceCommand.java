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
        event.replyEmbeds(Utils.simpleEmbed(event.getUser(), "Aspect-Bot Source",
                "Aspect is an open source project, and is available to see on GitHub!",
                "Find it at <https://www.github.com/Ankoki-Dev/Aspect/>! Any contributions are welcome:)")).setEphemeral(true)
                .queue();
    }
}
