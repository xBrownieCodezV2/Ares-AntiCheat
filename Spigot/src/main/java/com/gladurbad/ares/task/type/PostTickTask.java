package com.gladurbad.ares.task.type;

import com.gladurbad.ares.task.Task;

public interface PostTickTask extends Task {
    void handlePostTick();
}
