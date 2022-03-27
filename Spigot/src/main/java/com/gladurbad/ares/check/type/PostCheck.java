package com.gladurbad.ares.check.type;

import ac.artemis.packet.wrapper.Packet;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

import java.util.function.Predicate;

public abstract class PostCheck extends PacketCheck {

    private final Predicate<Packet> predicate;

    private boolean sent = false;

    public long lastFlying, lastPacket;
    public double buffer = 0.0;

    public PostCheck(final PlayerData data, final Predicate<Packet> predicate) {
        super(data);

        this.predicate = predicate;
    }

    @Override
    public void handle(final Packet packet) {
        if (packet instanceof PacketPlayClientFlying) {
            final long now = System.currentTimeMillis();
            final long delay = now - this.lastPacket;

            if (this.sent) {
                if (delay > 40L && delay < 100L) {
                    this.buffer += 0.25;

                    if (this.buffer > 0.5) {
                        this.fail(new Debug<>("buffer", buffer));
                    }
                } else {
                    this.buffer = Math.max(this.buffer - 0.025, 0);
                }

                this.sent = false;
            }

            this.lastFlying = now;
        } else if (this.predicate.test(packet)) {
            final long now = System.currentTimeMillis();
            final long delay = now - this.lastFlying;

            if (delay < 10L) {
                this.lastPacket = now;
                this.sent = true;
            } else {
                this.buffer = Math.max(this.buffer - 0.025, 0.0);
            }
        }
    }
}