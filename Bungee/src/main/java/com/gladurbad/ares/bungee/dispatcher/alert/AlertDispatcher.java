package com.gladurbad.ares.bungee.dispatcher.alert;

import com.gladurbad.ares.bungee.dispatcher.Dispatcher;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class AlertDispatcher implements Dispatcher {

    @Override
    public ByteArrayDataOutput dispatch(ByteArrayDataInput input) {
        String log = input.readUTF();
        String hover = input.readUTF();
        String command = input.readUTF();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF(log);
        output.writeUTF(hover);
        output.writeUTF(command);

        return output;
    }

    @Override
    public String getListenedChannel() {
        return "alert";
    }
}
