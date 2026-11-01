package com.kitsune.shulkerinshulker;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class ShulkerListener implements Listener {

    private final ShulkerInShulker plugin;

    public ShulkerListener(ShulkerInShulker plugin) {
        this.plugin = plugin;
    }

    private boolean isShulkerBox(Material material) {
        return material != null && material.name().contains("SHULKER_BOX");
    }

    private boolean isShulkerBox(ItemStack item) {
        return item != null && isShulkerBox(item.getType());
    }

    private int getNestingDepth(ItemStack item) {
        if (!isShulkerBox(item)) return 0;
        if (!(item.getItemMeta() instanceof BlockStateMeta meta)) return 0;
        if (!(meta.getBlockState() instanceof ShulkerBox shulkerBox)) return 0;

        int maxDepth = 0;
        for (ItemStack content : shulkerBox.getInventory().getContents()) {
            if (content != null && isShulkerBox(content)) {
                int depth = getNestingDepth(content);
                maxDepth = Math.max(maxDepth, depth + 1);
            }
        }
        return maxDepth;
    }

    private boolean hasItems(ItemStack item) {
        if (!isShulkerBox(item)) return false;
        if (!(item.getItemMeta() instanceof BlockStateMeta meta)) return false;
        if (!(meta.getBlockState() instanceof ShulkerBox shulkerBox)) return false;

        for (ItemStack content : shulkerBox.getInventory().getContents()) {
            if (content != null && !content.getType().isAir()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether the player is allowed to nest this shulker box item, once permission,
     * the allow-filled-shulkers setting, and the max nesting depth are all accounted for.
     */
    private boolean canNest(Player player, ItemStack item) {
        if (!player.hasPermission("shulkerinshulker.use")) return false;
        if (!plugin.isAllowFilledShulkers() && hasItems(item)) return false;

        int maxDepth = plugin.getMaxNestingDepth();
        if (maxDepth > 0 && !player.hasPermission("shulkerinshulker.bypass")) {
            return getNestingDepth(item) < maxDepth;
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;
        if (clickedInventory.getType() != InventoryType.SHULKER_BOX) return;

        ItemStack cursorItem = event.getCursor();
        if (cursorItem == null || cursorItem.getType().isAir()) return;
        if (!isShulkerBox(cursorItem)) return;

        // Leave vanilla's own restriction (which cancels this) in place if nesting isn't allowed.
        if (!canNest(player, cursorItem)) return;

        event.setCancelled(false);

        if (plugin.shouldShowNestingMessage()) {
            player.sendMessage(plugin.getNestingMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.getType() != InventoryType.SHULKER_BOX) return;

        ItemStack draggedItem = event.getOldCursor();
        if (!isShulkerBox(draggedItem)) return;

        boolean affectsShulker = event.getRawSlots().stream()
                .anyMatch(slot -> slot < topInventory.getSize());
        if (!affectsShulker) return;

        if (!canNest(player, draggedItem)) return;

        event.setCancelled(false);

        if (plugin.shouldShowNestingMessage()) {
            player.sendMessage(plugin.getNestingMessage());
        }
    }
}
