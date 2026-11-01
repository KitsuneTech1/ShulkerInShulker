package com.kitsune.shulkerinshulker;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShulkerListenerTest {

    private ServerMock server;
    private ShulkerInShulker plugin;
    private PlayerMock player;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(ShulkerInShulker.class);
        player = server.addPlayer();
        player.addAttachment(plugin, "shulkerinshulker.use", true);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void clickIntoShulkerIsAllowedForColoredShulkers() {
        Inventory inventory = openShulkerInventory();
        InventoryClickEvent event = new InventoryClickEvent(
                player.getOpenInventory(),
                InventoryType.SlotType.CONTAINER,
                0,
                ClickType.LEFT,
                InventoryAction.PLACE_ALL);
        event.setCursor(new ItemStack(Material.RED_SHULKER_BOX));
        event.setCancelled(true);

        server.getPluginManager().callEvent(event);

        assertFalse(event.isCancelled());
        assertTrue(inventory.getType() == InventoryType.SHULKER_BOX);
    }

    @Test
    void dragIntoShulkerIsAllowedAndShowsSuccessMessage() {
        openShulkerInventory();
        configure("show-nesting-message", true);
        configure("nesting-message", "&aNested");

        InventoryDragEvent event = new InventoryDragEvent(
                player.getOpenInventory(),
                new ItemStack(Material.SHULKER_BOX),
                new ItemStack(Material.SHULKER_BOX),
                false,
                Map.of(0, new ItemStack(Material.SHULKER_BOX)));
        event.setCancelled(true);

        server.getPluginManager().callEvent(event);

        assertFalse(event.isCancelled());
        assertTrue(player.nextMessage().contains("Nested"));
    }

    @Test
    void filledShulkersAreRejectedWhenDisabled() {
        openShulkerInventory();
        configure("allow-filled-shulkers", false);

        InventoryClickEvent event = clickWithCursor(filledShulker());
        event.setCancelled(true);

        server.getPluginManager().callEvent(event);

        assertTrue(event.isCancelled());
    }

    @Test
    void nestingDepthCanBeLimitedAndBypassed() {
        openShulkerInventory();
        configure("max-nesting-depth", 1);

        InventoryClickEvent event = clickWithCursor(shulkerContaining(new ItemStack(Material.SHULKER_BOX)));
        event.setCancelled(true);
        server.getPluginManager().callEvent(event);
        assertTrue(event.isCancelled());

        player.setOp(true);
        event = clickWithCursor(shulkerContaining(new ItemStack(Material.SHULKER_BOX)));
        event.setCancelled(true);
        server.getPluginManager().callEvent(event);
        assertFalse(event.isCancelled());
    }

    @Test
    void adminPermissionIsDeclared() {
        assertTrue(plugin.getDescription().getPermissions().stream()
                .anyMatch(permission -> permission.getName().equals("shulkerinshulker.admin")));
    }

    private Inventory openShulkerInventory() {
        Inventory inventory = server.createInventory(null, InventoryType.SHULKER_BOX);
        player.openInventory(inventory);
        return inventory;
    }

    private void configure(String key, Object value) {
        plugin.getConfig().set(key, value);
        plugin.saveConfig();
        plugin.loadConfiguration();
    }

    private InventoryClickEvent clickWithCursor(ItemStack item) {
        return new InventoryClickEvent(
                player.getOpenInventory(),
                InventoryType.SlotType.CONTAINER,
                0,
                ClickType.LEFT,
                InventoryAction.PLACE_ALL) {{
            setCursor(item);
        }};
    }

    private ItemStack filledShulker() {
        return shulkerContaining(new ItemStack(Material.DIAMOND));
    }

    private ItemStack shulkerContaining(ItemStack item) {
        ItemStack shulker = new ItemStack(Material.SHULKER_BOX);
        BlockStateMeta meta = (BlockStateMeta) shulker.getItemMeta();
        ShulkerBox state = (ShulkerBox) meta.getBlockState();
        state.getInventory().setItem(0, item);
        meta.setBlockState(state);
        shulker.setItemMeta(meta);
        return shulker;
    }
}
