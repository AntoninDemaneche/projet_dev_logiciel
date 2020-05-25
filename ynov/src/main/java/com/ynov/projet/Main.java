package com.ynov.projet;


import com.ynov.projet.features.PlayerData.PlayerInfo;
import com.ynov.projet.features.chat.ChatFormat;
import com.ynov.projet.features.commands.anothers.Commands;
import com.ynov.projet.features.commands.profil.ProfilRegister;
import com.ynov.projet.features.listener.Listener;
import com.ynov.projet.features.utils.Channel;
import lombok.Getter;
import com.ynov.projet.features.data.DBManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    @Getter
    private static Logger spigotLogger;

    public static Main plugin(){
        return Main.getPlugin(Main.class);
    }

    private static Logger LOG;
    public static FileConfiguration CONFIG;

    private static List<String> CURR_CONFIG_PATH;
    private static List<String> COMMANDS_ALLOW;
    private static int CURR_CONFIG_DEPTH;
    private static HashMap<String, Object> configMap;

    public static ArrayList<String> loadingList = new ArrayList<>();
    public static DBManager dbManager;
    public static boolean serverOpen;


    private void setupManaLoop() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (PlayerInfo pInfo : PlayerInfo.getInstanceList().values()) {
                if (pInfo.getMana() != pInfo.getMaxMana()) {
                    regenMana(pInfo);
                }
            }
        }, 0, 20 * 2);
    }

    private static void regenMana(PlayerInfo pInfo) {
        Player p = pInfo.getPlayer();
        float progression = p.getExp() + (1/60f * (pInfo.getMaxMana() / 100f));
        if(progression >= 1.0f) {
            p.setExp(0.0f);
            pInfo.addMana(1);
        }
        else {
            p.setExp(progression);
        }
    }

    @Override
    public void onEnable() {
        COMMANDS_ALLOW = new ArrayList<>();
        setupConfiguration();
        registerFeatures();
        notifyEnd();
        dbManager = new DBManager(this);
        dbManager.getConnection();
        serverOpen = true;
    }

    @Override
    public void onDisable() {
        LOG.info("---> Disabling plugin <---");
        serverOpen = false;
        Channel.saveMJlist();
        Bukkit.getScheduler().cancelTasks(this);
    }

    public void setupConfiguration(){
        CONFIG = this.getConfig();
        LOG = this.getLogger();
        CONFIG.options().copyDefaults(true);
        CURR_CONFIG_PATH = new ArrayList<>();
        CURR_CONFIG_PATH.add("features");
        configMap = new HashMap<>();
        COMMANDS_ALLOW.add("lost");
    }

    private void registerFeatures(){
        new Channel().register();
        new ChatFormat().register();
        new Listener().register();
        new Commands().register();
        new ProfilRegister().register();
        System.out.println("---> Enabling Plugin <---");
        serverOpen = true;
        spigotLogger = Bukkit.getLogger();

        spigotLogger.info("Loading data...");
        dbManager = new DBManager(this);
        dbManager.getConnection();

        spigotLogger.info("Loading sort");
        setupManaLoop();
        spigotLogger.info("---> Plugin enabled <---");
    }

    public void notifyEnd() {
        saveConfig();
        PluginDescriptionFile pdfFile = this.getDescription();
        LOG.log(Level.INFO, "ChatPlugin (v{0}) successfully loaded.", pdfFile.getVersion());
    }

    public static String buildConfigCurrentPath() {
        StringBuilder b = new StringBuilder();
        for(String s : CURR_CONFIG_PATH)
        {
            b.append(s).append('.');
        }
        return b.toString();
    }

    public static String initConfigFor(String name){
        CURR_CONFIG_PATH.add(name);
        CURR_CONFIG_DEPTH++;
        String path = buildConfigCurrentPath();
        CONFIG.addDefault(path + ".activated", true);
        return path;
    }

    public static boolean isActivated(String path) {
        return CONFIG.getBoolean(path + ".activated");
    }

    public static void exitConfigFor() {
        CURR_CONFIG_PATH.remove(CURR_CONFIG_PATH.size()-1);
        CURR_CONFIG_DEPTH--;
    }

    public static void addConfig(String s, Object value, String path) {
        CONFIG.addDefault(path + s, value);
        configMap.put(path + s, CONFIG.get(path + s));
    }

    public static Object getConfig(String s, String path) {
        return configMap.get(path + s);
    }

    public static void log(Level level, String log) {
        StringBuilder b = new StringBuilder();
        for(int i = 0; i< CURR_CONFIG_DEPTH-1; i++, b.append("  ")){}
        b.append("- ").append(log);
        LOG.log(level, b.toString());
    }

    public static abstract class Command{
        protected final PluginCommand command;
        protected String commandName;
        protected int index;
        protected final String path;

        protected Command(){
            commandName = getClass().getSimpleName();
            commandName = commandName.toLowerCase().substring(0, commandName.length()-Command.class.getSimpleName().length());
            this.command = Main.plugin().getCommand(commandName);
            this.index = 0;
            path = Main.initConfigFor(commandName);
        }

        public void register(){
            if (Main.isActivated(path)){
                this.command.setExecutor(this.getExecutor());
                this.command.setTabCompleter(this.getTabCompleter());
                Main.log(Level.INFO, "Enabled command " + this.command.getName());
            }
            Main.exitConfigFor();
        }

        private CommandExecutor getExecutor(){
            return (sender, command, label, args) -> {
                if (!sender.isOp() && Command.this.isOpOnly(commandName)){
                    sender.sendMessage("§cCommande pour les OPs seulement.");
                    return true;
                }
                Command.this.myOnCommand(sender, command, label, args);
                return true;
            };
        }

        private TabCompleter getTabCompleter() {
            return (sender, command, alias, args) -> {
                if (!sender.isOp() && Command.this.isOpOnly(commandName))
                {
                    return new ArrayList<>();
                }
                return Command.this.myOnTabComplete(sender, command, alias, args);
            };
        }

        protected void complete(List<String> completion, String target, String arg) {
            if(target.toLowerCase().startsWith(arg.toLowerCase()))
            {
                completion.add(target);
            }
        }

        protected boolean isOpOnly(String cmd){
            return !COMMANDS_ALLOW.contains(cmd);
        }

        public static class DisabledCompleter implements TabCompleter {
            public DisabledCompleter(){}
            public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args)
            {
                return new ArrayList();
            }
        }

        public static class DisabledCommand implements CommandExecutor {
            public DisabledCommand(){}
            public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
            {
                return true;
            }
        }

        protected abstract void myOnCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] split);
        protected abstract List<String> myOnTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] split);
    }
}
