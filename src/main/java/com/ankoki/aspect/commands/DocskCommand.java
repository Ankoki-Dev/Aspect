package com.ankoki.aspect.commands;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.gitignore.Secrets;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class DocskCommand extends SlashCommand {

    public DocskCommand() {
        super(new CommandData("docsk", "Gets documentation from SkUnity based on the query.")
                .addOptions(new OptionData(STRING, "query", "The query you want to search.")
                    .setRequired(true)));
    }

    private static final JSONParser JSON_PARSER = new JSONParser();

    @Override
    public void onCommand(SlashCommandEvent event) {
        event.deferReply(true).queue();
        String query = event.getOption("query").getAsString();
        Map<String, String> data = new LinkedHashMap<>() {{
            put("key", Secrets.SKUNITY_API_KEY);
            put("function", "doSearch");
            put("query", query.replace(" ", "%20"));
        }};;
        CompletableFuture<JSONObject> future = CompletableFuture.supplyAsync(() -> {
            try {
                StringJoiner joiner = new StringJoiner("&");
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    joiner.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) +
                            "=" +
                            URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
                }
                Utils.debug(joiner);
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://docs.skunity.com/api/"))
                        .POST(BodyPublishers.ofString(joiner.toString()))
                        .build();
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                String resp = response.body();
                return (JSONObject) JSON_PARSER.parse(resp);
            } catch (IOException | InterruptedException | ParseException ex) {
                ex.printStackTrace();
                return null;
            }
        });
        while (!future.isDone()) {}
        JSONObject json;
        try {
            json = future.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        InteractionHook hook = event.getHook();
        if (json.get("response").equals("success")) {
            JSONArray array = (JSONArray) json.get("records");
            JSONObject res = (JSONObject) array.get(0);
            hook.editOriginalEmbeds(Utils.simpleEmbed(event.getUser(), "*Name*: " + res.get("name"),
                    "*Type*: " + res.get("doc"),
                    "*Description*: " + res.get("desc"),
                    "```vb\n" + res.get("example") + "```")).queue();
        } else {
            hook.editOriginalEmbeds(Utils.simpleEmbed(event.getUser(), "Error",
                    "There was an error fetching this query.",
                    "**Error**: ```" + json.get("result") + "```",
                    "The corresponding JSON has been put in console.")).queue();
            Utils.debug(json.toString());
        }
    }
}
