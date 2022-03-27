package com.gladurbad.ares.bungee.dispatcher;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public interface Dispatcher {
    ByteArrayDataOutput dispatch(ByteArrayDataInput input);
    String getListenedChannel();
}
