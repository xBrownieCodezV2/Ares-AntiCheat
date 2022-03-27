package com.gladurbad.ares.bungee.dispatcher.punishment;

import com.gladurbad.ares.bungee.dispatcher.Dispatcher;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PunishmentDispatcher implements Dispatcher {

    @Override
    public ByteArrayDataOutput dispatch(ByteArrayDataInput input) {
        String command = input.readUTF();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF(command);

        return output;
    }

    @Override
    public String getListenedChannel() {
        return "punish";
    }
}
