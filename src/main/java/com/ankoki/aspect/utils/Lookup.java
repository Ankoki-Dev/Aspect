package com.ankoki.aspect.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageEmbed.Footer;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.EnumSet;

public class Lookup {
    private final String discordName, discordTag, guildNickname, avatarUrl;
    private final long discordId;
    private final Color colour;
    private final EnumSet<Permission> permissions;
    private final OffsetDateTime timeJoined, timeCreated;

    // In the future add punishments and ways to grab them.
    public Lookup(String discordName,
                  String discordTag,
                  String guildNickname,
                  long discordId,
                  String avatarUrl,
                  Color colour,
                  EnumSet<Permission> permissions,
                  OffsetDateTime timeJoined,
                  OffsetDateTime timeCreated) {
        this.discordName = discordName;
        this.discordTag = discordTag;
        this.guildNickname = guildNickname == null ? discordName : guildNickname;
        this.discordId = discordId;
        this.avatarUrl = avatarUrl;
        this.colour = colour;
        this.permissions = permissions;
        this.timeJoined = timeJoined;
        this.timeCreated = timeCreated;
    }

    public static Lookup performLookup(Member member) {
        return new Lookup(member.getEffectiveName(),
                member.getUser().getAsTag(),
                member.getNickname(),
                member.getIdLong(),
                member.getUser().getAvatarUrl(),
                member.getColor(),
                member.getPermissions(),
                member.getTimeJoined(),
                member.getTimeCreated());
    }

    public MessageEmbed getEmbed() {
        return new MessageEmbed(null, "Lookup: " + discordTag,
                "Discord Name: `" + discordName + "`\n" +
                        "Discord ID: `" + discordId + "`\n" +
                        "Guild Nickname: `" + guildNickname + "`\n" +
                        "Guild Permissions: " + Utils.readablePermissions(permissions.toArray(new Permission[0])) + "\n" +
                        "Date Joined: `" + getOrganisedTime(timeJoined) + "`\n" +
                        "Date Created: `" + getOrganisedTime(timeCreated) + "`\n",
                null, null, colour.getRGB(), null, null, null, null,
                new Footer(discordTag + " • " + Utils.formattedNow(), avatarUrl, null),
                null, null);
    }

    private static String getOrganisedTime(OffsetDateTime dateTime) {
        return dateTime.getDayOfMonth() + "/" + dateTime.getMonthValue() + "/" + dateTime.getYear() + " at " +
                dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond();
    }
}
