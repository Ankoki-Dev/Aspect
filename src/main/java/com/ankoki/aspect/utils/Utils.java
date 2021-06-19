package com.ankoki.aspect.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public final class Utils {
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    public static String joinedArray(String[] array, String joinBy) {
        StringBuilder builder = new StringBuilder();
        for (String string : array) {
            builder.append(string).append(joinBy);
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    public static String formattedNow() {
        return DATE_FORMATTER.format(new Date());
    }

    public static MessageEmbed simpleEmbed(User user, String title, String... lines) {
        return new MessageEmbed(null, title, joinedArray(lines, "\n"), null, null,
                new Color(196, 239, 231).getRGB(), null, null, null, null,
                new MessageEmbed.Footer(user.getAsTag() + " • " + formattedNow(), user.getAvatarUrl(), null),
                null, null);
    }

    public static String readablePermissions(Permission[] permissions) {
        StringBuilder builder = new StringBuilder();
        if (permissions.length < 1) return builder.toString();
        for (Permission permission : permissions) {
            builder.append("`").append(titleCaseConversion(permission.name())).append("`, ");
        }
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    public static String titleCaseConversion(String inputString) {
        if (inputString == null || inputString.isEmpty() || inputString.isBlank()) return "";
        if (inputString.length() == 1) return inputString.toUpperCase();
        inputString = inputString.replace("_", " ");

        StringBuffer resultPlaceholder = new StringBuffer(inputString.length());

        Stream.of(inputString.split(" ")).forEach(stringPart -> {
            char[] charArray = stringPart.toLowerCase().toCharArray();
            charArray[0] = Character.toUpperCase(charArray[0]);
            resultPlaceholder.append(new String(charArray)).append(" ");
        });
        resultPlaceholder.setLength(resultPlaceholder.length() - 1);
        return resultPlaceholder.toString();
    }

    public static void logCommand(SlashCommandEvent event) {
        Guild guild = event.getGuild();
        assert guild != null;
        if (guild.getIdLong() != 747818545466966176L) return;
        TextChannel channel = guild.getTextChannelById(855869714806997032L);
        List<OptionMapping> options = event.getOptions();
        StringBuilder builder = new StringBuilder();
        if (options.size() >= 1) {
            for (OptionMapping mapping : options) {
                builder.append("`").append(mapping.getAsString()).append("`, ");
            }
            builder.setLength(builder.length() - 2);
        }
        assert channel != null;
        channel.sendMessage(Utils.simpleEmbed(event.getUser(), "Command Used",
                "`User`: " + event.getUser().getAsTag(),
                "`Command`: **/" + event.getName().toLowerCase() + "**",
                "`Arguments`: " + (builder.length() >= 1 ? builder : "<none>"),
                "`Channel`: " + event.getTextChannel().getAsMention())).queue();
    }
}