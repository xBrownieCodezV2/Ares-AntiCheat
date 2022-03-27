package com.gladurbad.ares.gui.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.util.gui.ClickableItemBuilder;
import com.gladurbad.ares.util.math.MathUtil;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InfoGUI implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("infoGUI")
            .provider(new InfoGUI())
            .size(4, 9)
            .title(ChatColor.DARK_RED + "Ares AntiCheat Info")
            .manager(Ares.INSTANCE.getGuiManager())
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        ClickableItem lag = new ClickableItemBuilder(Material.COMPASS)
                .name("&cLag stats")
                .description("&7Last lag tick: " + Ares.INSTANCE.getTaskManager().getLagTick(), "&7TPS: " + MathUtil.FORMATTER.format(Ares.INSTANCE.getTaskManager().getTps()))
                .build();

        double totalAresVl = Ares.INSTANCE.getPlayerDataManager().getData()
                .stream()
                .mapToDouble(data -> data.getCheckData().getTotalVl())
                .sum();

        ClickableItem playerCount = new ClickableItemBuilder(Material.DIAMOND_HELMET)
                .name("&cPlayer stats")
                .description("&7Player count: " + Ares.INSTANCE.getPlayerDataManager().getData().size(), "&7Total ares violation level: " + MathUtil.FORMATTER.format(totalAresVl))
                .build();

        ClickableItem credits = new ClickableItemBuilder(Material.PAPER)
                .name("&cCredits")
                .description("&7Developed by GladUrBad, Rhys, Elevated, and Tecnio", "&7Owned by Mommy and Fabio")
                .build();

        ClickableItem main = new ClickableItemBuilder(Material.BARRIER)
                .name("&cBack to main menu")
                .consume(event -> MenuGUI.INVENTORY.open(player))
                .build();

        inventoryContents
                .set(1, 1, lag)
                .set(1, 4, playerCount)
                .set(1, 7, credits)
                .set(3, 4, main);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
