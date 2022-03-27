package com.gladurbad.ares.gui.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.gui.ClickableItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerInfoGUI implements InventoryProvider {

    private final Player player;
    private final int rows, columns;

    public PlayerInfoGUI(Player player, int rows, int columns) {
        this.player = player;
        this.rows = rows;
        this.columns = columns;
    }

    public static SmartInventory getInventory(Player player) {
        return SmartInventory.builder()
                .id("playerInfoGUI")
                .provider(new PlayerInfoGUI(player, 3, 9))
                .size(3, 9)
                .title(ChatColor.DARK_RED + "Information for " + player.getName())
                .manager(Ares.INSTANCE.getGuiManager())
                .build();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        PlayerData subject = Ares.INSTANCE.getPlayerDataManager().get(this.player);

        if (subject != null) {
            ClickableItem vlCount = new ClickableItemBuilder(Material.PAPER)
                    .name("&cTotal violation level")
                    .description("&7" + subject.getCheckData().getTotalVl())
                    .build();

            ClickableItem checkCount = new ClickableItemBuilder(Material.PAPER)
                    .name("&cRegistered checks")
                    .description("&7" + subject.getCheckData().getChecksList().size())
                    .build();

            inventoryContents
                    .set(1, 1, vlCount)
                    .set(1, 4, checkCount);
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
