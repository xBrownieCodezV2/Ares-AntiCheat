package com.gladurbad.ares.task.type;

import com.gladurbad.ares.task.Task;

public interface PreTickTask extends Task {
    void handlePreTick();
}
