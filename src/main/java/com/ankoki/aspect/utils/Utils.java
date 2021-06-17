package com.ankoki.aspect.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
}