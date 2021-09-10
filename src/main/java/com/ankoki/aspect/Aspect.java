package com.ankoki.aspect;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.gitignore.Secrets;
import com.ankoki.aspect.listeners.CommandListener;
import com.ankoki.aspect.listeners.JoinListener;
import com.ankoki.aspect.utils.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.reflections.Reflections;

import java.util.*;

// This is Aspect, the new bot for the Aspect discord.
public class Aspect {
    private static Aspect instance;
    private final List<SlashCommand> commands = new ArrayList<>();
    private static JDA jda;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Utils.log("Bot is starting");
        instance = new Aspect();

        // Logs into the discord bot.
        try {
            jda = JDABuilder.createDefault(Secrets.BOT_TOKEN).build();
            jda = jda.awaitReady();
            JDABuilder.createLight(Secrets.BOT_TOKEN, Arrays.asList(GatewayIntent.values()))
                    .addEventListeners(new CommandListener(),
                            new JoinListener())
                    .setActivity(Activity.watching("you guys!"))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        Utils.log(String.format("I was enabled in %sms. Registering slash commands.", System.currentTimeMillis() - start));

        // Loads and registers all command classes. Uses the Reflections api.
        Reflections reflections = new Reflections("com.ankoki.aspect.commands");
        Set<Class<? extends SlashCommand>> classes = reflections.getSubTypesOf(SlashCommand.class);
        List<CommandData> allData = new ArrayList<>();
        for (Class<? extends SlashCommand> clazz : classes) {
            try {
                SlashCommand command = clazz.newInstance();
                allData.add(command.getData());
                instance.commands.add(command);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Updates commands, instead of globally pushing updates (which can take up to an hour),
        // update the guilds we are in (which is immediate)
        jda.getGuilds().forEach(guild -> {
            CommandListUpdateAction commands = guild.updateCommands();
            commands.addCommands(allData).queue();
        });
    }

    public List<SlashCommand> getCommands() {
        return commands;
    }

    public static Aspect getInstance() {
        return instance;
    }
}