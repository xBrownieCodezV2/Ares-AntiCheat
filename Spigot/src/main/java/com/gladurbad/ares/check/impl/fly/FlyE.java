package com.gladurbad.ares.check.impl.fly;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.attribute.AttributeType;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.nms.NmsUtil;

@CheckInfo(name = "Fly E")
public class FlyE extends MotionCheck {

    public FlyE(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        if (from.getY() > to.getY()) return;

        double delta = motion.getY();
        double motion = NmsUtil.getMotionValues(data.getPlayer()).getY();

        if (velocityTicks.occurred(1) || teleportTicks.occurred(3)) return;

        double threshold = 0.42F + attributes.get(AttributeType.JUMP).getLevel() * 0.1;

        if (delta > threshold && motion == 0.0) this.fail(
                new Debug<>("delta", delta),
                new Debug<>("motion", motion)
        );
    }
}
