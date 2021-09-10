package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class NicknameCommand extends SlashCommand {

    public NicknameCommand() {
        super(new CommandData("nickname", "Changes a members nickname.")
                .addOptions(new OptionData(USER, "member", "The member.")
                        .setRequired(true))
                .addOptions(new OptionData(STRING, "nick", "The new nickname.")
                        .setRequired(true)),
                Permission.NICKNAME_MANAGE);
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        Member member = event.getOption("member").getAsMember();
        assert member != null;
        Member self = event.getGuild().getSelfMember();
        if (!self.canInteract(member)) {
            event.reply("You cannot change " + member.getUser().getName() + "'s nickname.").setEphemeral(true).queue();
            return;
        }
        member.modifyNickname(event.getOption("nick").getAsString()).queue();
        event.reply("You've changed " + member.getUser().getName() + "'s nickname.").setEphemeral(true).queue();
    }
}
