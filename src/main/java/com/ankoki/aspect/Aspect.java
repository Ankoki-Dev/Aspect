package com.ankoki.aspect;

import com.ankoki.aspect.api.SlashCommand;
import com.ankoki.aspect.gitignore.Secrets;
import com.ankoki.aspect.listeners.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

//This is Aspect, the new bot for the Aspect discord.
public class Aspect {
    private static Aspect instance;
    private final List<SlashCommand> commands = new ArrayList<>();
    private static JDA jda;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println("aspect is enabling");
        instance = new Aspect();

        //Logs into the discord bot.
        try {
            jda = JDABuilder.createDefault(Secrets.BOT_TOKEN).build();
            jda = jda.awaitReady();
            JDABuilder.createLight(Secrets.BOT_TOKEN, Arrays.asList(GatewayIntent.values()))
                    .addEventListeners(new CommandListener())
                    .setActivity(Activity.listening("k-pop"))
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        //Loads and registers all command classes.
        List<Class> classes = instance.getClasses("com.ankoki.aspect.commands");
        List<CommandData> allData = new ArrayList<>();
        for (Class clazz : classes) {
            try {
                if (SlashCommand.class.isAssignableFrom(clazz)) {
                    SlashCommand command = (SlashCommand) clazz.getDeclaredConstructor().newInstance();
                    allData.add(command.getData());
                    instance.commands.add(command);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        jda.getGuilds().forEach(guild -> {
            CommandListUpdateAction commands = guild.updateCommands();
            commands.addCommands(allData).queue();
        });

        System.out.printf("aspect was enabled in %sms%n", System.currentTimeMillis() - start);
    }

    private List<Class> getClasses(String packageName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(path);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private List<Class> findClasses(File directory, String packageName) {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                try {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return classes;
    }

    public List<SlashCommand> getCommands() {
        return commands;
    }

    public static Aspect getInstance() {
        return instance;
    }
}
