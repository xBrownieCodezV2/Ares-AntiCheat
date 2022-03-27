package com.gladurbad.ares.util.gui;

import com.gladurbad.ares.util.string.StringUtil;
import fr.minuskube.inv.ClickableItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClickableItemBuilder {

    private final ItemStack stack;
    private Consumer<InventoryClickEvent> eventConsumer;

    public ClickableItemBuilder(Material material) {
        stack = new ItemStack(material, 1, (short) 0);
    }

    public ClickableItemBuilder(Material material, int amount) {
        stack = new ItemStack(material, amount, (short) 0);
    }

    public ClickableItemBuilder(Material material, int amount, int damage) {
        stack = new ItemStack(material, amount, (short) damage);
    }

    public ClickableItemBuilder name(String name) {
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.setDisplayName(StringUtil.color(name));
        stack.setItemMeta(itemMeta);
        return this;
    }

    public ClickableItemBuilder description(String... desc) {
        ItemMeta meta = stack.getItemMeta();
        List<String> descs = stack.getItemMeta().getLore() == null ? new ArrayList<>() : stack.getItemMeta().getLore();
        descs.addAll(Stream.of(desc).map(StringUtil::color).collect(Collectors.toList()));
        meta.setLore(descs);
        stack.setItemMeta(meta);
        return this;
    }

    public ClickableItemBuilder consume(Consumer<InventoryClickEvent> clickEventConsumer) {
        this.eventConsumer = clickEventConsumer;
        return this;
    }

    public ClickableItem build() {
        return eventConsumer == null ? ClickableItem.empty(stack) : ClickableItem.of(stack, eventConsumer);
    }
}
