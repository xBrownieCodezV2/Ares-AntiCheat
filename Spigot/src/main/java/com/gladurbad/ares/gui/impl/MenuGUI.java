package com.gladurbad.ares.gui.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.gui.ClickableItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MenuGUI implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("menuGUI")
            .provider(new MenuGUI())
            .size(3, 9)
            .title(ChatColor.DARK_RED + "Ares AntiCheat Console")
            .manager(Ares.INSTANCE.getGuiManager())
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        PlayerData data = Ares.INSTANCE.getPlayerDataManager().get(player);

        if (data != null) {
            ClickableItem checks = new ClickableItemBuilder(Material.BOOKSHELF)
                    .name("&cChecks GUI")
                    .description("&7Set the configuration of the checks.")
                    .consume(event -> ChecksGUI.INVENTORY.open(player))
                    .build();

            ClickableItem info = new ClickableItemBuilder(Material.COMPASS)
                    .name("&cInfo GUI")
                    .description("&7View information about Ares and yourself.")
                    .consume(event -> InfoGUI.INVENTORY.open(player))
                    .build();

            ClickableItem violations = new ClickableItemBuilder(Material.PAPER)
                    .name("&cViolations GUI")
                    .description("&7View the cheaters with the highest violations.")
                    .consume(event -> TopViolationGUI.INVENTORY.open(player))
                    .build();

            inventoryContents
                    .set(1, 1, checks)
                    .set(1, 4, info)
                    .set(1, 7, violations);

            inventoryContents.fillBorders(new ClickableItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData()).build());
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
