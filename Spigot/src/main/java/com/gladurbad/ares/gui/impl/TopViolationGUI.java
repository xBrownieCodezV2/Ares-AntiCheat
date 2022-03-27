package com.gladurbad.ares.gui.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.gui.ClickableItemBuilder;
import com.gladurbad.ares.util.math.MathUtil;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopViolationGUI implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("violationGUI")
            .provider(new TopViolationGUI())
            .size(6, 9)
            .title(ChatColor.DARK_RED + "Ares AntiCheat Top Violators")
            .manager(Ares.INSTANCE.getGuiManager())
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        Pagination pagination = inventoryContents.pagination();

        List<PlayerData> flaggedPlayers = Ares.INSTANCE.getPlayerDataManager().getData()
                .stream()
                .filter(data -> data.getCheckData().getTotalVl() > 0)
                .sorted(Comparator.comparingDouble(data -> data.getCheckData().getTotalVl()))
                .collect(Collectors.toList());

        ClickableItem[] items = new ClickableItem[flaggedPlayers.size()];

        for (int i = 0; i < items.length; i++) {
            PlayerData data = flaggedPlayers.get(i);
            String name = data.getPlayer().getName();

            double totalVl = data.getCheckData().getTotalVl();

            items[i] = new ClickableItemBuilder(Material.PAPER)
                    .name("&c" + name)
                    .description("&7Current violation level: " + MathUtil.FORMATTER.format(totalVl))
                    .build();
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(INVENTORY.getColumns() * 3);

        pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        ClickableItem back = new ClickableItemBuilder(Material.ARROW)
                .name("&cPrevious page")
                .consume(event -> {
                    if (!pagination.isFirst()) {
                        INVENTORY.open(player, pagination.previous().getPage());
                    } else {
                        player.sendMessage(ChatColor.RED + "There is no previous page!");
                    }
                })
                .build();

        ClickableItem forward = new ClickableItemBuilder(Material.ARROW)
                .name("&cNext page")
                .consume(event -> {
                    if (!pagination.isLast()) {
                        INVENTORY.open(player, pagination.previous().getPage());
                    } else {
                        player.sendMessage(ChatColor.RED + "There is no next page!");
                    }
                })
                .build();
        ClickableItem main = new ClickableItemBuilder(Material.BARRIER)
                .name("&cBack to main menu")
                .consume(event -> MenuGUI.INVENTORY.open(player))
                .build();

        inventoryContents
                .set(4, 4, main)
                .set(4, 1, back)
                .set(4, 7, forward);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
