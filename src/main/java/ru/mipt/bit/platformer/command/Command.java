package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.api.GameWorld;

public interface Command {
    void execute(GameWorld gameWorld);
}
