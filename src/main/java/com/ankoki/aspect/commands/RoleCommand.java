package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class RoleCommand extends SlashCommand {

    public RoleCommand() {
        super(new CommandData("role", "Gives a person a role.")
                .addOptions(new OptionData(USER, "user", "Who to give the role to.")
                        .setRequired(true))
                .addOptions(new OptionData(ROLE, "role", "What role to give the person.")
                        .setRequired(true)));
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        Member member = event.getOption("member").getAsMember();
        Role role = event.getOption("role").getAsRole();
        Guild guild = event.getGuild();
        assert member != null && guild != null;
        guild.addRoleToMember(member, role).queue();
        event.replyEmbeds(Utils.simpleEmbed(event.getUser(), "Role Assigned",
                member.getEffectiveName() + " has been assigned the role " + role.getAsMention())).queue();
    }
}
