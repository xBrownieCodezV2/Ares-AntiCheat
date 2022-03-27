package com.gladurbad.ares.gui.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.check.AbstractCheck;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LogsGUI implements InventoryProvider {

    private final Player player;
    private final int rows, columns;

    public LogsGUI(Player player, int rows, int columns) {
        this.player = player;
        this.rows = rows;
        this.columns = columns;
    }

    public static SmartInventory getInventory(Player player) {
        return SmartInventory.builder()
                .id("logsGUI")
                .provider(new LogsGUI(player, 3, 9))
                .size(3, 9)
                .title(ChatColor.DARK_RED + "Logs for " + player.getName())
                .manager(Ares.INSTANCE.getGuiManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        PlayerData data = Ares.INSTANCE.getPlayerDataManager().get(this.player);

        if (data != null) {
            Pagination pagination = inventoryContents.pagination();

            List<AbstractCheck> checks = data.getCheckData().getChecksList()
                    .stream()
                    .filter(check -> check.getAlertManager().getVl() > 0)
                    .sorted(Comparator.comparingDouble(check -> check.getAlertManager().getVl()))
                    .collect(Collectors.toList());

            Collections.reverse(checks);

            ClickableItem[] items = new ClickableItem[checks.size()];

            for (int i = 0; i < items.length; i++) {
                AbstractCheck check = checks.get(i);
                double vl = check.getAlertManager().getVl();

                ClickableItem checkItem = new ClickableItemBuilder(Material.PAPER)
                        .name("&cCheck: " + check.getName())
                        .description("&cCurrent violation level: " + MathUtil.FORMATTER.format(vl))
                        .build();

                items[i] = checkItem;
            }

            pagination.setItems(items);
            pagination.setItemsPerPage(columns * 3);

            pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

            ClickableItem back = new ClickableItemBuilder(Material.ARROW)
                    .name("&cPrevious page")
                    .consume(event -> getInventory(this.player).open(player, pagination.previous().getPage()))
                    .build();

            ClickableItem forward = new ClickableItemBuilder(Material.ARROW)
                    .name("&cNext page")
                    .consume(event -> getInventory(this.player).open(player, pagination.next().getPage()))
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
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}