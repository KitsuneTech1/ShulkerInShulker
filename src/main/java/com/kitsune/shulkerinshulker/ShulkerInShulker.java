package com.kitsune.shulkerinshulker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ShulkerInShulker extends JavaPlugin {

    private static ShulkerInShulker instance;
    private int maxNestingDepth;
    private boolean showNestingMessage;
    private String nestingMessage;
    private boolean allowFilledShulkers;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadConfiguration();

        getServer().getPluginManager().registerEvents(new ShulkerListener(this), this);
        
        getLogger().info("ShulkerInShulker has been enabled!");
        getLogger().info("Players can now put shulker boxes inside other shulker boxes!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ShulkerInShulker has been disabled!");
    }

    public void loadConfiguration() {
        reloadConfig();
        maxNestingDepth = getConfig().getInt("max-nesting-depth", 0);
        showNestingMessage = getConfig().getBoolean("show-nesting-message", false);
        nestingMessage = ChatColor.translateAlternateColorCodes('&', 
            getConfig().getString("nesting-message", "&aShulker box nested!"));
        allowFilledShulkers = getConfig().getBoolean("allow-filled-shulkers", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("shulkerinshulker")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("shulkerinshulker.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to do this!");
                    return true;
                }
                loadConfiguration();
                sender.sendMessage(ChatColor.GREEN + "ShulkerInShulker configuration reloaded!");
                return true;
            }
            
            sender.sendMessage(ChatColor.GOLD + "=== ShulkerInShulker v" + getDescription().getVersion() + " ===");
            sender.sendMessage(ChatColor.YELLOW + "This plugin allows you to put shulker boxes inside other shulker boxes!");
            sender.sendMessage(ChatColor.GRAY + "Max nesting depth: " + (maxNestingDepth == 0 ? "Unlimited" : maxNestingDepth));
            sender.sendMessage(ChatColor.GRAY + "/shulkerinshulker reload - Reload config");
            return true;
        }
        return false;
    }

    public static ShulkerInShulker getInstance() {
        return instance;
    }

    public int getMaxNestingDepth() {
        return maxNestingDepth;
    }

    public boolean shouldShowNestingMessage() {
        return showNestingMessage;
    }

    public String getNestingMessage() {
        return nestingMessage;
    }

    public boolean isAllowFilledShulkers() {
        return allowFilledShulkers;
    }
}
