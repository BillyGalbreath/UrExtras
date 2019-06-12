package net.pl3x.bukkit.urextras.configuration;

import com.google.common.base.Throwables;
import net.pl3x.bukkit.urextras.UrExtras;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Lang {
    public static String COMMAND_NO_PERMISSION ;
    public static String COMMAND_NO_PERMISSION_PORTAL;
    public static String PLAYER_COMMAND;

    public static String UREXTRAS_PORTAL_INVENTORY_TITLE;
    public static String UREXTRAS_PORTAL_INVENTORY_CLOSED;
    public static String NO_TREEE;
    public static String TREEE;
    public static String HAND_NOT_EMPTY;
    public static String GIVE_TREEE_SPAWNER_TOOL;

    public static String SET_DIAMOND_AXE_TITLE;
    public static String SET_DIAMOND_AXE_INFO;
    public static String TREEE_LIST_INVENTORY_TITLE;
    public static String CANNOT_SPAWN_TREEE_HERE;
    public static String TREEE_SPAWNER_ACACIA;

    private static void init() {
        COMMAND_NO_PERMISSION = getString("command-no-permission", "&4You do not have permission for that command!");
        COMMAND_NO_PERMISSION_PORTAL = getString("command-no-permission-portal", "&cYou do not have permission to use the&4 {getClicked}&c!");
        PLAYER_COMMAND = getString("player-command", "&4This command is only available to players!");

        UREXTRAS_PORTAL_INVENTORY_TITLE = getString("urextras-portal-inventory-title", "UrExtras Portal");
        UREXTRAS_PORTAL_INVENTORY_CLOSED = getString("urextras-portal-inventory-closed","&7You closed &4{getInventoryName}&7.");
        NO_TREEE = getString("no-treee","&4Treee Spawner Tool");
        TREEE = getString("treee","&2Treee Spawner Tool");
        HAND_NOT_EMPTY = getString("hand-not-empty","&dPlease empty your hand before clicking the &7{getClicked}&d again.");
        GIVE_TREEE_SPAWNER_TOOL = getString("give-treee-spawner-tool","&7You received a &2{getToolName}&7.");

        SET_DIAMOND_AXE_TITLE = getString("set-diamond-axe-title", "Treee Spawner Tool");
        SET_DIAMOND_AXE_INFO = getString("set-diamond-axe-info","&7Spawn Any treee.");
        CANNOT_SPAWN_TREEE_HERE = getString("cannot-spawn-treee-here","&5=====================================================\n" +
                                                                                    "&7You cannot use the &4{getToolName} &7here.\n" +
                                                                                    "\n" +
                                                                                    "&7Please click on one of the following materials:\n" +
                                                                                    "  &ddirt&6, &dCoarse Dirt&6, &dGrass Block&6, &dGrass Path&6, &dPodzol&6, &dFarmland&6." +
                                                                                    "\n&5=====================================================\n");
        TREEE_LIST_INVENTORY_TITLE = getString("treee-list-inventory-title","Treee List");
        TREEE_SPAWNER_ACACIA = getString("treee-spawner-acacia","&7You spawned a {getToolName}&7.");
    }

    // ############################  DO NOT EDIT BELOW THIS LINE  ############################

    /**
     * Reload the language file
     */
    @SuppressWarnings("deprecation")
    public static void reload() {
        UrExtras plugin = UrExtras.getInstance();
        File configFile = new File(plugin.getDataFolder(), Config.LANGUAGE_FILE);
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load " + Config.LANGUAGE_FILE + ", please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header("This is the main language file for " + plugin.getName());
        config.options().copyDefaults(true);

        Lang.init();

        try {
            config.save(configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
        }
    }

    private static YamlConfiguration config;

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    /**
     * Sends a message to a recipient
     *
     * @param recipient Recipient of message
     * @param message   Message to send
     */
    public static void send(CommandSender recipient, String message) {
        if (recipient != null) {
            for (String part : colorize(message).split("\n")) {
                recipient.sendMessage(part);
            }
        }
    }

    /**
     * Broadcast a message to server
     *
     * @param message Message to broadcast
     */
    public static void broadcast(String message) {
        for (String part : colorize(message).split("\n")) {
            Bukkit.getOnlinePlayers().forEach(recipient -> recipient.sendMessage(part));
            Bukkit.getConsoleSender().sendMessage(part);
        }
    }

    /**
     * Colorize a String
     *
     * @param str String to colorize
     * @return Colorized String
     */
    public static String colorize(String str) {
        if (str == null) {
            return "";
        }
        str = ChatColor.translateAlternateColorCodes('&', str);
        if (ChatColor.stripColor(str).isEmpty()) {
            return "";
        }
        return str;
    }
}