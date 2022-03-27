package com.gladurbad.ares.util.move;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * For anyone reading this; this is going to be used as a fail safe for the collisions that are already inside of
 * ares. This can be used on it's own although not particularly recommended. Don't judge.
 */

public final class BlockSnapshot {
    public static List<Material> blocked = new ArrayList<>();

    public static final Set<Byte> BLOCK_SOLID_PASS_SET;
    private static final Set<Byte> BLOCK_STAIRS_SET;
    private static final Set<Byte> BLOCK_LIQUID_SET;
    private static final Set<Byte> BLOCK_WEBS_SET;
    private static final Set<Byte> BLOCK_ICE_SET;
    private static final Set<Byte> BLOCK_CARPET_SET;
    private static final Set<Byte> BLOCK_SLIME_SET;

    static {
        BLOCK_SOLID_PASS_SET = new HashSet<>();
        BLOCK_STAIRS_SET = new HashSet<>();
        BLOCK_LIQUID_SET = new HashSet<>();
        BLOCK_WEBS_SET = new HashSet<>();
        BLOCK_ICE_SET = new HashSet<>();
        BLOCK_CARPET_SET = new HashSet<>();
        BLOCK_SLIME_SET = new HashSet<>();

        BLOCK_SOLID_PASS_SET.add((byte) 0);
        BLOCK_SOLID_PASS_SET.add((byte) 6);
        BLOCK_SOLID_PASS_SET.add((byte) 8);
        BLOCK_SOLID_PASS_SET.add((byte) 9);
        BLOCK_SOLID_PASS_SET.add((byte) 10);
        BLOCK_SOLID_PASS_SET.add((byte) 11);
        BLOCK_SOLID_PASS_SET.add((byte) 27);
        BLOCK_SOLID_PASS_SET.add((byte) 28);
        BLOCK_SOLID_PASS_SET.add((byte) 30);
        BLOCK_SOLID_PASS_SET.add((byte) 31);
        BLOCK_SOLID_PASS_SET.add((byte) 32);
        BLOCK_SOLID_PASS_SET.add((byte) 37);
        BLOCK_SOLID_PASS_SET.add((byte) 38);
        BLOCK_SOLID_PASS_SET.add((byte) 39);
        BLOCK_SOLID_PASS_SET.add((byte) 40);
        BLOCK_SOLID_PASS_SET.add((byte) 50);
        BLOCK_SOLID_PASS_SET.add((byte) 51);
        BLOCK_SOLID_PASS_SET.add((byte) 55);
        BLOCK_SOLID_PASS_SET.add((byte) 59);
        BLOCK_SOLID_PASS_SET.add((byte) 63);
        BLOCK_SOLID_PASS_SET.add((byte) 66);
        BLOCK_SOLID_PASS_SET.add((byte) 68);
        BLOCK_SOLID_PASS_SET.add((byte) 69);
        BLOCK_SOLID_PASS_SET.add((byte) 70);
        BLOCK_SOLID_PASS_SET.add((byte) 72);
        BLOCK_SOLID_PASS_SET.add((byte) 75);
        BLOCK_SOLID_PASS_SET.add((byte) 76);
        BLOCK_SOLID_PASS_SET.add((byte) 77);
        BLOCK_SOLID_PASS_SET.add((byte) 78);
        BLOCK_SOLID_PASS_SET.add((byte) 83);
        BLOCK_SOLID_PASS_SET.add((byte) 90);
        BLOCK_SOLID_PASS_SET.add((byte) 104);
        BLOCK_SOLID_PASS_SET.add((byte) 105);
        BLOCK_SOLID_PASS_SET.add((byte) 115);
        BLOCK_SOLID_PASS_SET.add((byte) 119);
        BLOCK_SOLID_PASS_SET.add((byte) (-124));
        BLOCK_SOLID_PASS_SET.add((byte) (-113));
        BLOCK_SOLID_PASS_SET.add((byte) (-81));

        BLOCK_STAIRS_SET.add((byte) 53);
        BLOCK_STAIRS_SET.add((byte) 67);
        BLOCK_STAIRS_SET.add((byte) 108);
        BLOCK_STAIRS_SET.add((byte) 109);
        BLOCK_STAIRS_SET.add((byte) 114);
        BLOCK_STAIRS_SET.add((byte) (-128));
        BLOCK_STAIRS_SET.add((byte) (-122));
        BLOCK_STAIRS_SET.add((byte) (-121));
        BLOCK_STAIRS_SET.add((byte) (-120));
        BLOCK_STAIRS_SET.add((byte) (-100));
        BLOCK_STAIRS_SET.add((byte) (-93));
        BLOCK_STAIRS_SET.add((byte) (-92));
        BLOCK_STAIRS_SET.add((byte) (-76));
        BLOCK_STAIRS_SET.add((byte) (-74));
        BLOCK_STAIRS_SET.add((byte) 44);
        BLOCK_STAIRS_SET.add((byte) 78);
        BLOCK_STAIRS_SET.add((byte) 181);
        BLOCK_STAIRS_SET.add((byte) 99);
        BLOCK_STAIRS_SET.add((byte) (-112));
        BLOCK_STAIRS_SET.add((byte) (-115));
        BLOCK_STAIRS_SET.add((byte) (-116));
        BLOCK_STAIRS_SET.add((byte) (-105));
        BLOCK_STAIRS_SET.add((byte) (-108));
        BLOCK_STAIRS_SET.add((byte) 125);
        BLOCK_STAIRS_SET.add((byte) 126);
        BLOCK_STAIRS_SET.add((byte) 100);

        BLOCK_LIQUID_SET.add((byte) 8);
        BLOCK_LIQUID_SET.add((byte) 9);
        BLOCK_LIQUID_SET.add((byte) 10);
        BLOCK_LIQUID_SET.add((byte) 11);

        BLOCK_WEBS_SET.add((byte) 30);

        BLOCK_ICE_SET.add((byte) 79);
        BLOCK_ICE_SET.add((byte) (-82));

        BLOCK_CARPET_SET.add((byte) (-85));

        BLOCK_SLIME_SET.add((byte) 165);

        blocked.add(Material.ACTIVATOR_RAIL);
        blocked.add(Material.ANVIL);
        blocked.add(Material.BED_BLOCK);
        blocked.add(Material.POTATO);
        blocked.add(Material.POTATO_ITEM);
        blocked.add(Material.CARROT);
        blocked.add(Material.CARROT_ITEM);
        blocked.add(Material.BIRCH_WOOD_STAIRS);
        blocked.add(Material.BREWING_STAND);
        blocked.add(Material.BOAT);
        blocked.add(Material.BRICK_STAIRS);
        blocked.add(Material.BROWN_MUSHROOM);
        blocked.add(Material.CAKE_BLOCK);
        blocked.add(Material.CARPET);
        blocked.add(Material.CAULDRON);
        blocked.add(Material.COBBLESTONE_STAIRS);
        blocked.add(Material.COBBLE_WALL);
        blocked.add(Material.DARK_OAK_STAIRS);
        blocked.add(Material.DIODE);
        blocked.add(Material.DIODE_BLOCK_ON);
        blocked.add(Material.DIODE_BLOCK_OFF);
        blocked.add(Material.DEAD_BUSH);
        blocked.add(Material.DETECTOR_RAIL);
        blocked.add(Material.DOUBLE_PLANT);
        blocked.add(Material.DOUBLE_STEP);
        blocked.add(Material.DRAGON_EGG);
        blocked.add(Material.PAINTING);
        blocked.add(Material.FLOWER_POT);
        blocked.add(Material.GOLD_PLATE);
        blocked.add(Material.HOPPER);
        blocked.add(Material.STONE_PLATE);
        blocked.add(Material.IRON_PLATE);
        blocked.add(Material.HUGE_MUSHROOM_1);
        blocked.add(Material.HUGE_MUSHROOM_2);
        blocked.add(Material.IRON_DOOR_BLOCK);
        blocked.add(Material.IRON_DOOR);
        blocked.add(Material.FENCE);
        blocked.add(Material.IRON_FENCE);
        blocked.add(Material.IRON_PLATE);
        blocked.add(Material.ITEM_FRAME);
        blocked.add(Material.JUKEBOX);
        blocked.add(Material.JUNGLE_WOOD_STAIRS);
        blocked.add(Material.LADDER);
        blocked.add(Material.LEVER);
        blocked.add(Material.LONG_GRASS);
        blocked.add(Material.NETHER_FENCE);
        blocked.add(Material.NETHER_STALK);
        blocked.add(Material.NETHER_WARTS);
        blocked.add(Material.MELON_STEM);
        blocked.add(Material.PUMPKIN_STEM);
        blocked.add(Material.QUARTZ_STAIRS);
        blocked.add(Material.RAILS);
        blocked.add(Material.RED_MUSHROOM);
        blocked.add(Material.RED_ROSE);
        blocked.add(Material.SAPLING);
        blocked.add(Material.SEEDS);
        blocked.add(Material.SIGN);
        blocked.add(Material.SIGN_POST);
        blocked.add(Material.SKULL);
        blocked.add(Material.SMOOTH_STAIRS);
        blocked.add(Material.NETHER_BRICK_STAIRS);
        blocked.add(Material.SPRUCE_WOOD_STAIRS);
        blocked.add(Material.STAINED_GLASS_PANE);
        blocked.add(Material.REDSTONE_COMPARATOR);
        blocked.add(Material.REDSTONE_COMPARATOR_OFF);
        blocked.add(Material.REDSTONE_COMPARATOR_ON);
        blocked.add(Material.REDSTONE_LAMP_OFF);
        blocked.add(Material.REDSTONE_LAMP_ON);
        blocked.add(Material.REDSTONE_TORCH_OFF);
        blocked.add(Material.REDSTONE_TORCH_ON);
        blocked.add(Material.REDSTONE_WIRE);
        blocked.add(Material.SANDSTONE_STAIRS);
        blocked.add(Material.STEP);
        blocked.add(Material.ACACIA_STAIRS);
        blocked.add(Material.SUGAR_CANE);
        blocked.add(Material.SUGAR_CANE_BLOCK);
        blocked.add(Material.ENCHANTMENT_TABLE);
        blocked.add(Material.SOUL_SAND);
        blocked.add(Material.TORCH);
        blocked.add(Material.TRAP_DOOR);
        blocked.add(Material.TRIPWIRE);
        blocked.add(Material.TRIPWIRE_HOOK);
        blocked.add(Material.WALL_SIGN);
        blocked.add(Material.VINE);
        blocked.add(Material.WATER_LILY);
        blocked.add(Material.WEB);
        blocked.add(Material.WOOD_DOOR);
        blocked.add(Material.WOOD_DOUBLE_STEP);
        blocked.add(Material.WOOD_PLATE);
        blocked.add(Material.WOOD_STAIRS);
        blocked.add(Material.WOOD_STEP);
        blocked.add(Material.HOPPER);
        blocked.add(Material.WOODEN_DOOR);
        blocked.add(Material.YELLOW_FLOWER);
        blocked.add(Material.LAVA);
        blocked.add(Material.WATER);
        blocked.add(Material.STATIONARY_WATER);
        blocked.add(Material.STATIONARY_LAVA);
        blocked.add(Material.CACTUS);
        blocked.add(Material.CHEST);
        blocked.add(Material.PISTON_BASE);
        blocked.add(Material.PISTON_MOVING_PIECE);
        blocked.add(Material.PISTON_EXTENSION);
        blocked.add(Material.PISTON_STICKY_BASE);
        blocked.add(Material.TRAPPED_CHEST);
        blocked.add(Material.SNOW);
        blocked.add(Material.ENDER_CHEST);
        blocked.add(Material.THIN_GLASS);
        blocked.add(Material.ENDER_PORTAL_FRAME);
    }

    public static boolean isOnStairs(final Location location, final int down) {
        return isUnderBlock(location, BLOCK_STAIRS_SET, down);
    }

    public static boolean isOnLiquid(final Location location, final int down) {
        return isUnderBlock(location, BLOCK_LIQUID_SET, down);
    }

    public static boolean isOnWeb(final Location location, final int down) {
        return isUnderBlock(location, BLOCK_WEBS_SET, down);
    }

    public static boolean isOnSlime(final Location location, final int down) {
        return isUnderBlock(location, BLOCK_SLIME_SET, down);
    }

    public static boolean isOnIce(final Location location, final int down) {
        return isUnderBlock(location, BLOCK_ICE_SET, down);
    }

    public static boolean isOnCarpet(final Location location, final int down) {
        return isUnderBlock(location, BLOCK_CARPET_SET, down);
    }

    public static boolean isSlab(final Player player) {
        return blocked.contains(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType());
    }

    public static boolean isBlockFaceAir(final Player player) {
        final Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        return block.getType().equals(Material.AIR) && block.getRelative(BlockFace.WEST).getType().equals(Material
                .AIR) && block.getRelative(BlockFace.NORTH).getType().equals(Material.AIR) && block.getRelative
                (BlockFace.EAST).getType().equals(Material.AIR) && block.getRelative(BlockFace.SOUTH).getType()
                .equals(Material.AIR);
    }

    public static boolean isUnderBlock(final Location location, final Set<Byte> itemIDs, final int down) {
        final double posX = location.getX();
        final double posZ = location.getZ();

        final double fracX = (posX % 1.0 > 0.0) ? Math.abs(posX % 1.0) : (1.0 - Math.abs(posX % 1.0));
        final double fracZ = (posZ % 1.0 > 0.0) ? Math.abs(posZ % 1.0) : (1.0 - Math.abs(posZ % 1.0));

        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY() - down;
        final int blockZ = location.getBlockZ();

        final World world = location.getWorld();

        if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ).getTypeId())) {
            return true;
        }

        if (fracX < 0.3) {
            if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId())) {
                return true;
            }

            if (fracZ < 0.3) {
                if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }

                if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }

                return itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {
                if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }

                if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }

                return itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracX > 0.7) {
            if (itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId())) {
                return true;
            }

            if (fracZ < 0.3) {
                if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }

                if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }

                return itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {
                if (itemIDs.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }

                if (itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
                    return true;
                }

                return itemIDs.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracZ < 0.3) {
            return itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId());
        } else return fracZ > 0.7 && itemIDs.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId());

        return false;
    }

    public static boolean isOnGround(final Location location, final int down) {
        final double posX = location.getX();
        final double posZ = location.getZ();

        final double fracX = (posX % 1.0 > 0.0) ? Math.abs(posX % 1.0) : (1.0 - Math.abs(posX % 1.0));
        final double fracZ = (posZ % 1.0 > 0.0) ? Math.abs(posZ % 1.0) : (1.0 - Math.abs(posZ % 1.0));

        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY() - down;
        final int blockZ = location.getBlockZ();

        final World world = location.getWorld();

        if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX, blockY, blockZ).getTypeId())) {
            return true;
        }

        if (fracX < 0.3) {
            if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId())) {
                return true;
            }

            if (fracZ < 0.3) {
                if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1)
                        .getTypeId())) {
                    return true;
                }

                if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1)
                        .getTypeId())) {
                    return true;
                }

                return !BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {
                if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1)
                        .getTypeId())) {
                    return true;
                }

                if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1)
                        .getTypeId())) {
                    return true;
                }

                return !BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracX > 0.7) {
            if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId()
            )) {
                return true;
            }

            if (fracZ < 0.3) {
                if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ - 1)
                        .getTypeId())) {
                    return true;
                }

                if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
                    return true;
                }

                return !BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId());
            } else if (fracZ > 0.7) {
                if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX - 1, blockY, blockZ + 1)
                        .getTypeId())) {
                    return true;
                }

                if (!BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1)
                        .getTypeId())) {
                    return true;
                }

                return !BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId());
            }
        } else if (fracZ < 0.3) {
            return !BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId());
        } else
            return fracZ > 0.7 && !BLOCK_SOLID_PASS_SET.contains((byte) world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId());

        return false;
    }
}
