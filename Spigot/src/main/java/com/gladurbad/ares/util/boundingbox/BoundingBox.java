package com.gladurbad.ares.util.boundingbox;

import com.gladurbad.ares.util.location.PlayerLocation;
import com.gladurbad.ares.util.math.MathUtil;
import com.gladurbad.ares.util.nms.MathHelper;
import com.gladurbad.ares.util.point.Point;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoundingBox {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public final long timestamp = System.currentTimeMillis();

    public BoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public BoundingBox(Location location, double expand, double height) {
        this.minX = location.getX();
        this.minY = location.getY();
        this.minZ = location.getZ();
        this.maxX = minX + expand;
        this.maxY = minY + height;
        this.maxZ = minZ + expand;
    }

    public BoundingBox(double x, double y, double z, float width, float height) {
        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = minX + width;
        this.maxY = minY + height;
        this.maxZ = minZ + width;
    }

    public boolean collides(BoundingBox other) {
        return other.maxX >= this.minX
                && other.minX <= this.maxX
                && other.maxY >= this.minY
                && other.minY <= this.maxY
                && other.maxZ >= this.minZ
                && other.minZ <= this.maxZ;
    }


    public BoundingBox clone() {
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public BoundingBox grow(double x, double y, double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public BoundingBox move(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public BoundingBox set(double x, double y, double z, double width, double height) {
        this.minX = x - width / 2.0;
        this.minY = y;
        this.minZ = z - width / 2.0;

        this.maxX = x + width / 2.0;
        this.maxY = y + height;
        this.maxZ = z + width / 2.0;

        return this;
    }

    public double getEyeDistance(float pitch, PlayerLocation pos) {
        double nearestX = MathUtil.clamp(pos.getX(), minX, maxX);
        double nearestZ = MathUtil.clamp(pos.getZ(), minZ, maxZ);

        double distX = pos.getX() - nearestX;
        double distZ = pos.getZ() - nearestZ;

        double dist = Math.hypot(distX, distZ);

        if (Math.abs(pitch) != 90) {
            dist /= Math.cos(Math.toRadians(pitch));
        }

        return dist;
    }


    public double distanceXZ(BoundingBox other) {
        double dx = this.posX() - other.posX();
        double dz = this.posZ() - other.posZ();

        return MathUtil.hypot(dx, dz);
    }

    public BoundingBox union(BoundingBox other) {
        double d0 = Math.min(this.minX, other.minX);
        double d1 = Math.min(this.minY, other.minY);
        double d2 = Math.min(this.minZ, other.minZ);
        double d3 = Math.max(this.maxX, other.maxX);
        double d4 = Math.max(this.maxY, other.maxY);
        double d5 = Math.max(this.maxZ, other.maxZ);
        return new BoundingBox(d0, d1, d2, d3, d4, d5);
    }

    public boolean collidesY(BoundingBox other) {
        return this.minY <= other.maxY || this.maxY >= other.minY;
    }

    public double posX() {
        return (maxX + minX) / 2.0;
    }

    public double posY() {
        return minY;
    }

    public double posZ() {
        return (maxZ + minZ) / 2.0;
    }

    public String toString() {
        return "x=" + posX() + " y=" + posY() + " posZ=" + posZ();
    }

    public List<Block> getColliding(World world) {
        List<Block> materials = new ArrayList<>();

        int x = MathHelper.floor(this.minX);
        int y = MathHelper.floor(this.minY);
        int z = MathHelper.floor(this.minZ);
        int x1 = MathHelper.floor(this.maxX + 1.0D);
        int y1 = MathHelper.floor(this.maxY + 1.0D);
        int z1 = MathHelper.floor(this.maxZ + 1.0D);

        for (int lx = x; lx < x1; lx++) {
            for (int lz = z; lz < z1; lz++) {
                if (world.isChunkLoaded(lx >> 4, lz >> 4)) {
                    for (int ly = y - 1; ly < y1; ly++) {
                        Block block = world.getBlockAt(lx, ly, lz);

                        if (block != null) {
                            materials.add(block);
                        }
                    }
                }
            }
        }

        return materials;
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateXOffset(BoundingBox other, double offsetX) {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (offsetX > 0.0D && other.maxX <= this.minX) {
                double d1 = this.minX - other.maxX;

                if (d1 < offsetX) {
                    offsetX = d1;
                }
            } else if (offsetX < 0.0D && other.minX >= this.maxX) {
                double d0 = this.maxX - other.minX;

                if (d0 > offsetX) {
                    offsetX = d0;
                }
            }

            return offsetX;
        } else {
            return offsetX;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateYOffset(BoundingBox other, double offsetY) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (offsetY > 0.0D && other.maxY <= this.minY) {
                double d1 = this.minY - other.maxY;

                if (d1 < offsetY) {
                    offsetY = d1;
                }
            } else if (offsetY < 0.0D && other.minY >= this.maxY) {
                double d0 = this.maxY - other.minY;

                if (d0 > offsetY) {
                    offsetY = d0;
                }
            }

            return offsetY;
        } else {
            return offsetY;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateZOffset(BoundingBox other, double offsetZ) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
            if (offsetZ > 0.0D && other.maxZ <= this.minZ) {
                double d1 = this.minZ - other.maxZ;

                if (d1 < offsetZ) {
                    offsetZ = d1;
                }
            } else if (offsetZ < 0.0D && other.minZ >= this.maxZ) {
                double d0 = this.maxZ - other.minZ;

                if (d0 > offsetZ) {
                    offsetZ = d0;
                }
            }

            return offsetZ;
        } else {
            return offsetZ;
        }
    }

    public BoundingBox addCoord(double x, double y, double z) {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;

        if (x < 0.0D) {
            d0 += x;
        } else if (x > 0.0D) {
            d3 += x;
        }

        if (y < 0.0D) {
            d1 += y;
        } else if (y > 0.0D) {
            d4 += y;
        }

        if (z < 0.0D) {
            d2 += z;
        } else if (z > 0.0D) {
            d5 += z;
        }

        return new BoundingBox(d0, d1, d2, d3, d4, d5);
    }

    /**
     * Checks if the specified vector is within the YZ dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInYZ(Point vec) {
        return vec != null && (vec.getY() >= this.minY && vec.getY() <= this.maxY && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ);
    }

    /**
     * Checks if the specified vector is within the XZ dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInXZ(Point vec) {
        return vec != null && (vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ);
    }

    /**
     * Checks if the specified vector is within the XY dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInXY(Point vec) {
        return vec != null && (vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getY() >= this.minY && vec.getY() <= this.maxY);
    }
}
