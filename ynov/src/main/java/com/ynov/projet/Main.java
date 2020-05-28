package com.ynov.projet;



import com.ynov.projet.Features.chat.ChatFormat;
import com.ynov.projet.Features.ability.Ability;
import com.ynov.projet.Features.ability.AbilityLoader;
import com.ynov.projet.Features.commands.anothers.Commands;
import com.ynov.projet.Features.commands.others.OthersCommandRegister;
import com.ynov.projet.Features.commands.profil.ProfilRegister;
import com.ynov.projet.Features.data.DBManager;
import com.ynov.projet.Features.listener.Listener;
import com.ynov.projet.Features.routines.Routines;
import com.ynov.projet.Features.skill.Skill;
import com.ynov.projet.Features.skill.SkillLoader;
import com.ynov.projet.Features.utils.Channel;
import lombok.Getter;
import com.ynov.projet.Features.PlayerData.PlayerClone;
import com.ynov.projet.Features.PlayerData.PlayerInfo;
import net.citizensnpcs.api.npc.NPC;
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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends JavaPlugin {
    @Getter
    private static ArrayList<Player> manaRegenList = new ArrayList<>();


    @Getter
    public static HashMap<String, UUID> IDninkenFromNamePlayer = new HashMap<>();

    @Getter
    public static HashMap<String, NPC> isSwitch = new HashMap<>();

    @Getter
    public static HashMap<String, Integer> currentSelectSkill = new HashMap<>();

    @Getter
    private static HashMap<String, String> stealChakra = new HashMap<>();

    @Getter
    private static Logger spigotLogger;

    @Getter
    public static HashMap<String, PlayerInfo> ficheMJ = new HashMap<>();



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

    public static Main plugin() {
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
        dbManager.getPlayerDB().saveFichePerso();
        LOG.info("--- Disabling Plugin  ---");
        serverOpen = false;
        Channel.saveMJlist();
        Bukkit.getScheduler().cancelTasks(this);


        LOG.info("--- Plugin  disabled ---");
    }

    public void setupConfiguration()
    {
        CONFIG = this.getConfig();
        LOG = this.getLogger();
        CONFIG.options().copyDefaults(true);
        CURR_CONFIG_PATH = new ArrayList<>();
        CURR_CONFIG_PATH.add("features");
        configMap = new HashMap<>();
        COMMANDS_ALLOW.add("roll");
        COMMANDS_ALLOW.add("boots");
        COMMANDS_ALLOW.add("hat");
        COMMANDS_ALLOW.add("walk");
        COMMANDS_ALLOW.add("changechatdeseisan");
        COMMANDS_ALLOW.add("lay");
        COMMANDS_ALLOW.add("sit");
        COMMANDS_ALLOW.add("gm");
        COMMANDS_ALLOW.add("renameitem");
        COMMANDS_ALLOW.add("lore");
        COMMANDS_ALLOW.add("antonin");
        COMMANDS_ALLOW.add("apparence");
        COMMANDS_ALLOW.add("clone");
        COMMANDS_ALLOW.add("target");
        COMMANDS_ALLOW.add("chakra");
        COMMANDS_ALLOW.add("jutsu");
        COMMANDS_ALLOW.add("lost");
        COMMANDS_ALLOW.add("transfer");
        COMMANDS_ALLOW.add("aprofil");
        COMMANDS_ALLOW.add("ninken");
        COMMANDS_ALLOW.add("competence");
        COMMANDS_ALLOW.add("resistance");
        COMMANDS_ALLOW.add("whois");
        COMMANDS_ALLOW.add("genre");
        COMMANDS_ALLOW.add("profil");
        COMMANDS_ALLOW.add("rollresistance");
        COMMANDS_ALLOW.add("stealchakra");
    }

    private void registerFeatures()
    {
        new Channel().register();
        new Listener().register();
        new ChatFormat().register();
        new Commands().register();
        new ProfilRegister().register();
        new OthersCommandRegister().register();
        new Routines().register();
        System.out.println("---> Enabling Plugin <---");
        serverOpen = true;
        spigotLogger = Bukkit.getLogger();

        spigotLogger.info("Loading data...");
        dbManager = new DBManager(this);
        dbManager.getConnection();
        PlayerClone.init();

        spigotLogger.info("Loading jutsu & compétences...");
        SkillLoader.loadSkillsFromConfig();
        AbilityLoader.loadAbilitiesFromConfig();
        spigotLogger.info(Ability.instanceList.size() + " abilities have been loaded !");
        spigotLogger.info(Skill.getInstanceList().size() + " jutsu have been loaded !");

        setupManaLoop();
        dbManager.getPlayerDB().loadPlayerFiche();

        spigotLogger.info("---> Plugin enabled <---");
    }

    public void notifyEnd()
    {
        saveConfig();
        PluginDescriptionFile pdfFile = this.getDescription();
        LOG.log(Level.INFO, "ChatPlugin (v{0}) successfully loaded.", pdfFile.getVersion());
    }

    public static String buildConfigCurrentPath()
    {
        StringBuilder b = new StringBuilder();
        for(String s : CURR_CONFIG_PATH)
        {
            b.append(s).append('.');
        }
        return b.toString();
    }

    public static String initConfigFor(String name)
    {
        CURR_CONFIG_PATH.add(name);
        CURR_CONFIG_DEPTH++;
        String path = buildConfigCurrentPath();
        CONFIG.addDefault(path + ".activated", true);
        return path;

    }

    public static boolean isActivated(String path)
    {
        return CONFIG.getBoolean(path + ".activated");
    }

    public static void exitConfigFor()
    {
        CURR_CONFIG_PATH.remove(CURR_CONFIG_PATH.size()-1);
        CURR_CONFIG_DEPTH--;
    }

    public static void addConfig(String s, Object value, String path)
    {
        CONFIG.addDefault(path + s, value);
        configMap.put(path + s, CONFIG.get(path + s));
    }

    public static Object getConfig(String s, String path)
    {
        return configMap.get(path + s);
    }

    public static void log(Level level, String log)
    {
        StringBuilder b = new StringBuilder();
        for(int i = 0; i< CURR_CONFIG_DEPTH-1; i++, b.append("  ")){}
        b.append("- ").append(log);
        LOG.log(level, b.toString());
    }

    public static void log(Level level, String log, int margin)
    {
        StringBuilder b = new StringBuilder();
        for(int i = 0; i< CURR_CONFIG_DEPTH-1+margin; i++, b.append("  ")){}
        b.append("- ").append(log);
        LOG.log(level, b.toString());
    }

    public static abstract class Command
    {
        protected final PluginCommand command;
        protected String commandName;
        protected int index;
        protected final String path;

        protected Command()
        {
            commandName = getClass().getSimpleName();
            commandName = commandName.toLowerCase().substring(0, commandName.length()-Command.class.getSimpleName().length() );
            this.command = Main.plugin().getCommand(commandName);
            this.index = 0;
            path = Main.initConfigFor(commandName);
        }

        public void register()
        {
            if(Main.isActivated(path))
            {
                this.command.setExecutor(this.getExecutor());
                this.command.setTabCompleter(this.getTabCompleter());
                Main.log(Level.INFO, "Enabled command " + this.command.getName());
            }
            else
            {
                this.command.setExecutor(new DisabledCommand());
                this.command.setTabCompleter(new DisabledCompleter());
                Main.log(Level.INFO, "Disabled command " + this.command.getName());
            }
            Main.exitConfigFor();
        }

        private CommandExecutor getExecutor()
        {
            return (sender, command, label, args) -> {
                if (!sender.isOp() && Command.this.isOpOnly(commandName))
                {
                    sender.sendMessage("§cHRP : Commande pour les OPs seulement.");
                    return true;
                }
                Command.this.myOnCommand(sender, command, label, args);
                return true;
            };
        }

        private TabCompleter getTabCompleter()
        {
            return (sender, command, alias, args) -> {
                if (!sender.isOp() && Command.this.isOpOnly(commandName))
                {
                    return new ArrayList<>();
                }
                return Command.this.myOnTabComplete(sender, command, alias, args);
            };
        }

        protected void complete(List<String> completion, String target, String arg)
        {
            if(target.toLowerCase().startsWith(arg.toLowerCase()))
            {
                completion.add(target);
            }
        }

        protected boolean isOpOnly(String cmd)
        {
            return !COMMANDS_ALLOW.contains(cmd);
        }

        public static class DisabledCompleter implements TabCompleter
        {
            public DisabledCompleter(){}
            public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args)
            {
                return new ArrayList();
            }
        }

        public static class DisabledCommand implements CommandExecutor
        {
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


