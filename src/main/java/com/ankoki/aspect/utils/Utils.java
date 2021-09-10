package com.ankoki.aspect.utils;

import net.dv8tion.jda.api.EmbedBuilder;
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
    private static final DateFormat LOG_FORMATTER = new SimpleDateFormat("hh:mm:ss");

    public static String formattedNow() {
        return DATE_FORMATTER.format(new Date());
    }

    public static MessageEmbed simpleEmbed(User user, String title, String... lines) {
        return new MessageEmbed(null, title, String.join("\n", lines), null, null,
                new Color(196, 239, 231).getRGB(), null, null, null, null,
                new MessageEmbed.Footer(user.getAsTag() + " • " + formattedNow(), user.getAvatarUrl(), null),
                null, null);
    }

    public static MessageEmbed leagueEmbed(User user, String title, String... lines) {
        return new MessageEmbed(null, title, String.join("\n", lines), null, null,
                new Color(196, 239, 231).getRGB(), new MessageEmbed.Thumbnail("https://cdn.discordapp.com/attachments/780462754724577310/885703251134144552/lol_logo.png",
                null, 0, 0), null, null, null,
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

    public static void debug(Object object) {
        System.out.println("[" + LOG_FORMATTER.format(new Date()) + "]" + ConsoleColors.YELLOW + " [DEBUG] " + ConsoleColors.CYAN + "Aspect-Bot | " + ConsoleColors.YELLOW + object);
    }

    public static void log(Object object) {
        System.out.println("[" + LOG_FORMATTER.format(new Date()) + "]" + ConsoleColors.CYAN + " [Aspect] Aspect-Bot | " + ConsoleColors.RESET + object);
    }

    @SuppressWarnings("unused")
    private static class ConsoleColors {
        // Reset
        public static final String RESET = "\033[0m";  // Text Reset

        // Regular Colors
        public static final String BLACK = "\033[0;30m";   // BLACK
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String PURPLE = "\033[0;35m";  // PURPLE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE = "\033[0;37m";   // WHITE

        // Bold
        public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
        public static final String RED_BOLD = "\033[1;31m";    // RED
        public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
        public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
        public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
        public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
        public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
        public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

        // Underline
        public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
        public static final String RED_UNDERLINED = "\033[4;31m";    // RED
        public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
        public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
        public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
        public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
        public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
        public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

        // Background
        public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
        public static final String RED_BACKGROUND = "\033[41m";    // RED
        public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
        public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
        public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
        public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
        public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
        public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

        // High Intensity
        public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
        public static final String RED_BRIGHT = "\033[0;91m";    // RED
        public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
        public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
        public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
        public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
        public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
        public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

        // Bold High Intensity
        public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
        public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
        public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
        public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
        public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
        public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
        public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
        public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

        // High Intensity backgrounds
        public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
        public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
        public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
        public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
        public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
        public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
        public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
    }
}