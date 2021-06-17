package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.InputStream;

public class WelcomeCommand extends SlashCommand {

    public WelcomeCommand() {
        super(new CommandData("welcome", "Sends the welcome screen."),
                Permission.ADMINISTRATOR);
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        InputStream is = getClass().getResourceAsStream("/aspect_logo.png");
        assert is != null;
        event.reply("").addFile(is, "aspect_logo.png").queue();
        TextChannel channel = event.getTextChannel();
        channel.sendMessage(Utils.simpleEmbed(event.getUser(), "Welcome to the Aspect discord server.",
                "To keep this community safe, there are rules you need to follow. Not following these rules " +
                "will lead to punishment.",
                "`Rule 1` • No derogatory behaviour.",
                "`Rule 2` • Do not doxx or release harmful information about a person.",
                "`Rule 3` • Do not spam.",
                "`Rule 4` • Receiving help is a privilege, not a right.",
                "`Rule 5` • Don't advertise, whether this is in guild or DMs.",
                "*Use common sense and everything will be fine:)*")).queue();
    }
}
