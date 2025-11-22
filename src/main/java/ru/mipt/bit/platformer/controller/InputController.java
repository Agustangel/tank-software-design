package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.*;

/**
 * Обработчик пользовательского ввода.
 * Поддерживает обработку движения, действий и специальных команд.
 */
public class InputController {
    private static final float INPUT_COOLDOWN = 0.1f;
    private float timeSinceLastInput = 0f;

    // Команда для переключения отображения здоровья
    private ru.mipt.bit.platformer.Command healthDisplayCommand;

    public void setHealthDisplayCommand(ru.mipt.bit.platformer.Command healthDisplayCommand) {
        this.healthDisplayCommand = healthDisplayCommand;
    }

    /**
     * Определяет направление движения на основе нажатых клавиш.
     */
    public ru.mipt.bit.platformer.Direction getMovementDirection() {
        timeSinceLastInput += Gdx.graphics.getDeltaTime();
        if (timeSinceLastInput < INPUT_COOLDOWN) {
            return null;
        }

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)
                || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            return resetCooldown(ru.mipt.bit.platformer.Direction.UP);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)
                || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            return resetCooldown(ru.mipt.bit.platformer.Direction.LEFT);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)
                || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            return resetCooldown(ru.mipt.bit.platformer.Direction.DOWN);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)
                || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            return resetCooldown(ru.mipt.bit.platformer.Direction.RIGHT);
        }
        return null;
    }

    public ru.mipt.bit.platformer.Command getShootCommand(ru.mipt.bit.platformer.Tank tank) {
        if (isActionPressed(Action.SHOOT) && timeSinceLastInput >= INPUT_COOLDOWN) {
            timeSinceLastInput = 0f;
            return new ru.mipt.bit.platformer.command.ShootCommand(tank);
        }
        return null;
    }

    private ru.mipt.bit.platformer.Direction resetCooldown(ru.mipt.bit.platformer.Direction direction) {
        timeSinceLastInput = 0f;
        return direction;
    }

    /**
     * Проверяет, нажата ли клавиша действия.
     */
    public boolean isActionPressed(Action action) {
        switch (action) {
            case SHOOT:
                return Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
            case TOGGLE_HEALTH:
                return Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.L);
            default:
                return false;
        }
    }

    /**
     * Обрабатывает специальные действия (например, переключение здоровья)
     */
    public void handleSpecialActions() {
        if (isActionPressed(Action.TOGGLE_HEALTH) && healthDisplayCommand != null) {
            healthDisplayCommand.execute();
            // Небольшая задержка чтобы избежать многократного срабатывания
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Перечисление возможных действий в игре.
     */
    public enum Action {
        SHOOT,
        TOGGLE_HEALTH
    }
}
