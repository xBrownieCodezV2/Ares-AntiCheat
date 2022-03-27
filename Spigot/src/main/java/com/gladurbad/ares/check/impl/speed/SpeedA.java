package com.gladurbad.ares.check.impl.speed;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.attribute.Attribute;
import com.gladurbad.ares.util.attribute.AttributeType;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.math.MathUtil;

@CheckInfo(name = "Speed A")
public class SpeedA extends MotionCheck {

    public SpeedA(PlayerData data) {
        super(data);
    }

    private int lastGround, lastAir;

    @Override
    public void handle() {
        // Increment the air ticks and ground ticks.
        ++lastAir;
        ++lastGround;

        // Get the speed and jump attributes.
        Attribute speed = attributes.get(AttributeType.SPEED);
        Attribute jump = attributes.get(AttributeType.JUMP);

        // Get the speed and jump amplifiers.
        int levelSpeed = speed.getLevel();
        int levelJump = jump.getLevel();

        // Get the ticks since the last speed and jump amplifier change.
        int speedChangeTicks = speed.getTicks();
        int jumpChangeTicks = jump.getTicks();

        // The speed will still hang for a few ticks after the potion runs out, exempt this.
        if (speedChangeTicks < 10 || jumpChangeTicks < 10) return;

        // Get the expected jump motion.
        double expectedJump = 0.42F + (levelJump * 0.1F);

        // Get the players walk speed. TODO: Lag compensate this value.
        double walkSpeed = attributes.get(AttributeType.WALK_SPEED).getLevel() * 2;

        // Return if the player is teleporting or taking velocity or if the walkSpeed is less than the base.
        if (teleportTicks.occurred(1) || walkSpeed < 0.2F) return;

        // Get the client ground status. We will use this for the base speed as it's more accurate.
        boolean onGround = to.isOnGround();

        // Set the last ground tick and last air tick based on the client ground status.
        if (onGround) lastGround = 0;
        else lastAir = 0;

        // Create the base speed based on the client ground status.
        double baseSpeed = onGround ? 0.3 : 0.36;

        // Get the current and last delta y, we use this to tell if the player is jumping or landing.
        double deltaY = motion.getY();
        double lastDeltaY = lastMotion.getY();

        // Check if the player is under a block and compensate the extra speed boost.
        boolean underBlock = underBlockTicks.occurred(20);

        // Check if the player jumped or landed to compensate the extra speed boost.
        boolean jumped = lastDeltaY <= 0.0 && (underBlock ? deltaY > 0 : deltaY >= expectedJump);
        boolean landed = deltaY >= lastDeltaY && lastAir < 5 && lastGround < 3;

        // Compensate extra speed based on collisions and other factors.
        if (jumped) baseSpeed *= 1.75;
        if (landed) baseSpeed *= 1.37;
        if (iceTicks.occurred(30)) baseSpeed *= 1.6;
        if (underBlock) baseSpeed *= 1.5;
        if (halfBlockTicks.occurred(10)) baseSpeed *= 1.5;
        if (slimeTicks.occurred(20)) baseSpeed *= 1.3;
        if (placeAffectMotionTicks.occurred(5)) baseSpeed *= 1.5;

        // Add the factor of the walk speed, I created a regression to find out the best way to compensate.
        baseSpeed *= (5.0 * walkSpeed);
        baseSpeed += ((0.06 * (walkSpeed / 0.2) * levelSpeed));

        // Add the velocity modifier.
        if (velocityTicks.occurred(20)) baseSpeed += MathUtil.hypot(velocity.getX(), velocity.getZ());

        // Create a ratio to see if the player goes faster than the limit, >1.0 is faster.
        double ratio = motion.getHorizontalDistance() / baseSpeed;

        // Fail and increase the VL with more severity the higher the ratio.
        if (ratio > 1.01 && !isFlying() && !wasFlying()) {
            if (increaseBuffer(ratio) > 3) {
                fail(ratio,
                        new Debug<>("ratio", ratio),
                        new Debug<>("motion", motion.getHorizontalDistance()),
                        new Debug<>("limit", baseSpeed),
                        new Debug<>("jumped", jumped),
                        new Debug<>("landed", landed),
                        new Debug<>("ice", iceTicks.occurred(30)),
                        new Debug<>("under", underBlock),
                        new Debug<>("halfBlock", halfBlockTicks.occurred(10)),
                        new Debug<>("slime", slimeTicks.occurred(10)),
                        new Debug<>("walkSpeed", walkSpeed),
                        new Debug<>("speed", levelSpeed),
                        new Debug<>("jump", levelJump),
                        new Debug<>("teleportTicks", teleportTicks.getTicks()));
            }
        } else {
            decreaseBuffer(0.01);
        }
    }
}
