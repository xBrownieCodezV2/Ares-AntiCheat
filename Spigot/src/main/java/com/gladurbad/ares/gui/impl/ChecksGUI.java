package com.gladurbad.ares.gui.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.check.AbstractCheck;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.config.ConfigManager;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.gui.ClickableItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ChecksGUI implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("checksGUI")
            .provider(new ChecksGUI())
            .size(6, 9)
            .title(ChatColor.DARK_RED + "Ares AntiCheat Checks")
            .manager(Ares.INSTANCE.getGuiManager())
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        PlayerData data = Ares.INSTANCE.getPlayerDataManager().get(player);

        if (data != null) {
            Pagination pagination = inventoryContents.pagination();

            List<Class<? extends AbstractCheck>> checkClasses = Ares.INSTANCE.getCheckManager().getChecks()
                    .stream()
                    .sorted(Comparator.comparing(Class::getSimpleName))
                    .collect(Collectors.toList());

            ConfigManager configManager = Ares.INSTANCE.getConfigManager();

            ClickableItem[] items = new ClickableItem[checkClasses.size()];

            for (int i = 0; i < items.length; i++) {
                Class<? extends AbstractCheck> klass = checkClasses.get(i);

                String checkName = klass.getSimpleName();

                if (klass.isAnnotationPresent(CheckInfo.class)) {
                    checkName = klass.getAnnotation(CheckInfo.class).name();
                }

                boolean enabled = configManager.getEnabledChecks().contains(klass.getSimpleName());

                String finalCheckName = checkName;
                items[i] = new ClickableItemBuilder(enabled ? Material.EMERALD_BLOCK : Material.REDSTONE_BLOCK)
                        .name("&c" + checkName)
                        .description("&7This check is " + (enabled ? "enabled" : "disabled"))
                        .consume(event -> {
                            if (event.getAction() == InventoryAction.PICKUP_ALL) {
                                if (enabled) {
                                    Ares.INSTANCE.getConfigManager().getEnabledChecks().remove(klass.getSimpleName());
                                    Ares.INSTANCE.getPlayerDataManager().getData()
                                            .forEach(v -> v.getCheckData().getCheck(klass).getAlertManager().setEnabled(false));
                                } else {
                                    Ares.INSTANCE.getConfigManager().getEnabledChecks().add(klass.getSimpleName());
                                    Ares.INSTANCE.getPlayerDataManager().getData()
                                            .forEach(v -> v.getCheckData().getCheck(klass).getAlertManager().setEnabled(true));
                                }
                                INVENTORY.open(player);
                            }
                        })
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
                            data.getPlayer().sendMessage(ChatColor.RED + "There is no previous page!");
                        }
                    })
                    .build();

            ClickableItem forward = new ClickableItemBuilder(Material.ARROW)
                    .name("&cNext page")
                    .consume(event -> {
                        if (!pagination.isLast()) {
                            INVENTORY.open(player, pagination.previous().getPage());
                        } else {
                            data.getPlayer().sendMessage(ChatColor.RED + "There is no next page!");
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
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
