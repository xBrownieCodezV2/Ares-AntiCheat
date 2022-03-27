package com.gladurbad.ares.nms;

import ac.artemis.packet.protocol.ProtocolVersion;
import ac.artemis.packet.spigot.utils.ServerUtil;
import ac.artemis.packet.spigot.utils.factory.Factory;
import com.gladurbad.ares.nms.impl.NmsImplementation;
import com.gladurbad.ares.nms.impl.v_1_10_R1.NmsImplementation_v_1_10_R1;
import com.gladurbad.ares.nms.impl.v_1_11_R1.NmsImplementation_v_1_11_R1;
import com.gladurbad.ares.nms.impl.v_1_12_R1.NmsImplementation_v_1_12_R1;
import com.gladurbad.ares.nms.impl.v_1_8_R3.NmsImplementation_v_1_8_R3;
import com.gladurbad.ares.nms.impl.v_1_9_R2.NmsImplementation_v_1_9_R2;
import com.gladurbad.ares.util.exception.VersionIncompatibilityException;
import lombok.SneakyThrows;

public class NmsImplementationFactory implements Factory<NmsImplementation> {
    @SneakyThrows
    @Override
    public NmsImplementation build() {
        ProtocolVersion serverVersion = ServerUtil.getGameVersion();

        switch (serverVersion) {
            case V1_8_9: // There are more 1.8 revisions, tell them to use the latest or fuck off.
                return new NmsImplementation_v_1_8_R3();
            case V1_9_2: // There is another 1.9 revision, again, use latest or fuck off.
            case V1_9_4:
                return new NmsImplementation_v_1_9_R2();
            case V1_10:
            case V1_10_2:
                return new NmsImplementation_v_1_10_R1();
            case V1_11:
                return new NmsImplementation_v_1_11_R1();
            case V1_12:
                //case V1_12_1: For some reason this one is null, leave it out.
            case V1_12_2:
                return new NmsImplementation_v_1_12_R1();
            default:
                throw new VersionIncompatibilityException();
        }
    }
}
