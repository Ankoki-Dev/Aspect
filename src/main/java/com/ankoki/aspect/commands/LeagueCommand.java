package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.gitignore.Secrets;
import com.ankoki.aspect.utils.Champion;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.champion_mastery.dto.ChampionMastery;
import net.rithms.riot.api.endpoints.league.dto.LeagueEntry;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class LeagueCommand extends SlashCommand {

    private static final RiotApi RIOT_API;
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");

    static {
        ApiConfig config = new ApiConfig().setKey(Secrets.RIOT_API_KEY);
        RIOT_API = new RiotApi(config);
    }

    public LeagueCommand() {
        super(new CommandData("league", "Gets league data based on input.")
                .addOptions(new OptionData(STRING, "region", "The region the given summoner is in.")
                        .setRequired(true))
                .addOptions(new OptionData(STRING, "summoner", "The summoners name.")
                        .setRequired(true))
                .addOptions(new OptionData(STRING, "champion", "Information on a champion based on the summoner."))
                .addOptions(new OptionData(BOOLEAN, "public", "True if everyone can see the result, false if only you.")));
    }

    @Override
    public void onCommand(SlashCommandEvent event) {
        InteractionHook hook = event.getHook();
        String region = event.getOption("region").getAsString();
        String summonerName = event.getOption("summoner").getAsString();
        String championName = "<none>";
        boolean isChampInfo = false;
        OptionMapping option = event.getOption("champion");
        OptionMapping optionTwo = event.getOption("public");
        boolean ephemeral = true;
        if (option != null) {
            championName = option.getAsString();
            isChampInfo = true;
        }
        if (optionTwo != null) {
            ephemeral = !optionTwo.getAsBoolean();
        }
        event.deferReply(ephemeral).queue();
        Platform platform = getPlatformByName(region);
        if (platform == null) {
            hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Invalid Region",
                            "*The region `" + region + "` is not a valid region.*",
                            "The valid regions are:```" + Arrays.toString(Platform.values())
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replaceAll("\\d", "") + "```"))
                    .queue();
            return;
        }
        Summoner summoner = null;
        try {
            summoner = RIOT_API.getSummonerByName(platform, summonerName);
        } catch (Exception ex) {
            if (ex.getMessage().equalsIgnoreCase("404: Data not found - summoner not found")) {
                hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Invalid Summoner",
                                "This summoner does not exist.",
                                "Are you sure you put the right region?"))
                        .queue();
            } else {
                ex.printStackTrace();
                hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Error",
                                "There was an error fetching the information.",
                                "**Error**: ```" + ex.getMessage() + "```" +
                                        "The corresponding stacktrace has been put in console."))
                        .queue();
                return;
            }
        }
        assert summoner != null;
        if (isChampInfo) {
            Champion champion = Champion.fromName(championName);
            if (champion == null) {
                hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Invalid Region",
                                "*The champion `" + championName + "` is not a valid champion.*",
                                "```Please note that not all newer champions may be updated. If a current champion is not there, please report this.```"))
                        .queue();
                return;
            }
            try {
                ChampionMastery mastery = RIOT_API.getChampionMasteriesBySummonerByChampion(platform, summoner.getId(), champion.getId());
                if (mastery == null) {
                    hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Invalid Champion",
                                    "This champion has no mastery. Have you played them before?"))
                            .queue();
                } else {
                    hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Champion Mastery - " + champion,
                                    "**Summoner Name**: `" + summoner.getName() + "`",
                                    "**Mastery Level**: `" + mastery.getChampionLevel() + "`",
                                    "**Mastery Points**: `" + NumberFormat.getInstance().format(mastery.getChampionPoints()) + "`",
                                    "**Points Since Last Level**: `" + NumberFormat.getInstance().format(mastery.getChampionPointsSinceLastLevel()) + "`",
                                    "**Points Until Next level**: `" + NumberFormat.getInstance().format(mastery.getChampionPointsUntilNextLevel()) + "`",
                                    "**Tokens Earned**: `" + mastery.getTokensEarned() + "`",
                                    "**Chest Earned**: `" + Utils.titleCaseConversion(String.valueOf(mastery.isChestGranted())) + "`",
                                    "**Last Played**: `" + DATE_FORMATTER.format(new Date(mastery.getLastPlayTime())) + "`"))
                            .queue();
                }
            } catch (RiotApiException ex) {
                ex.printStackTrace();
                if (ex.getMessage().equalsIgnoreCase("404: Not Found")) {
                    hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Invalid Champion",
                                    "This champion has no mastery. Have you played them before?"))
                            .queue();
                } else {
                    hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Error",
                                    "There was an error fetching the information.",
                                    "**Error**: ```" + ex.getMessage() + "```" +
                                            "The corresponding stacktrace has been put in console."))
                            .queue();
                }
            }
        } else {
            try {
                int masteryScore = RIOT_API.getChampionMasteryScoresBySummoner(platform, summoner.getId());
                MatchList matchList = RIOT_API.getMatchListByAccountId(platform, summoner.getAccountId());
                Set<LeagueEntry> entries = RIOT_API.getLeagueEntriesBySummonerId(platform, summoner.getId());
                String lastChampion = "<none>";
                String lastPlayed = "Never Played";
                if (matchList != null) {
                    List<MatchReference> references = matchList.getMatches();
                    if (references.size() > 0) {
                        MatchReference reference = references.get(0);
                        Champion champ = Champion.fromId(reference.getChampion());
                        if (champ != null) lastChampion = champ.toString();
                        lastPlayed = DATE_FORMATTER.format(new Date(reference.getTimestamp()));
                    }
                }
                String rank = "Unranked";
                int wins = 0, losses = 0;
                for (LeagueEntry entry : entries) {
                    if (!entry.getQueueType().equalsIgnoreCase("RANKED_SOLO_5x5")) continue;
                    String tier = Utils.titleCaseConversion(entry.getTier());
                    String division = entry.getRank();
                    int lp = entry.getLeaguePoints();
                    if (tier == null || tier.isEmpty() || division == null || division.isEmpty()) break;
                    wins = entry.getWins();
                    losses = entry.getLosses();
                    rank = tier + " " + division + " with " + lp + " LP";
                }
                hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Summoner Information",
                                "**Name**: `" + summoner.getName() + "`",
                                "**Level**: `" + summoner.getSummonerLevel() + "`",
                                "**Mastery Level**: `" + masteryScore + "`",
                                "**Rank**: `" + rank + "`" + (rank.equalsIgnoreCase("Unranked") ? "" :
                                        "\n**Ranked Wins**: `" + wins + "`\n**Ranked Losses**: `" + losses + "`"),
                                "**Last Played**: `" + lastPlayed + "`",
                                "**Last Played Champion**: `" + lastChampion + "`"))
                        .queue();
            } catch (RiotApiException ex) {
                if (ex.getMessage().equalsIgnoreCase("404: Data not found - summoner not found")) {
                    hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Invalid Summoner",
                                    "This summoner does not exist.",
                                    "Are you sure you put the right region?"))
                            .queue();
                } else {
                    ex.printStackTrace();
                    hook.editOriginalEmbeds(Utils.leagueEmbed(event.getUser(), "Error",
                                    "There was an error fetching the information.",
                                    "**Error**: ```" + ex.getMessage() + "```" +
                                            "The corresponding stacktrace has been put in console."))
                            .queue();
                }
            }
        }
    }

    private Platform getPlatformByName(String region) {
        for (Platform platform : Platform.values()) {
            if (region.equalsIgnoreCase(platform.getName())) return platform;
        }
        return null;
    }
}
