package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;

/**
 * Обработчик пользовательского ввода.
 * Поддерживает обработку движения и действий (например, стрельбы).
 */
public class InputController {
    private static final float INPUT_COOLDOWN = 0.1f;
    private float timeSinceLastInput = 0f;

    /**
     * Определяет направление движения на основе нажатых клавиш.
     */
    public Direction getMovementDirection() {
        timeSinceLastInput += Gdx.graphics.getDeltaTime();
        if (timeSinceLastInput < INPUT_COOLDOWN) {
            return null;
        }

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)
                || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            return resetCooldown(Direction.UP);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)
                || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            return resetCooldown(Direction.LEFT);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)
                || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            return resetCooldown(Direction.DOWN);
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)
                || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            return resetCooldown(Direction.RIGHT);
        }
        return null;
    }

    private Direction resetCooldown(Direction direction) {
        timeSinceLastInput = 0f;
        return direction;
    }

    /**
     * Проверяет, нажата ли клавиша действия (например, стрельбы).
     */
    public boolean isActionPressed(Action action) {
        switch (action) {
            case SHOOT:
                return Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
            default:
                return false;
        }
    }

    /**
     * Перечисление возможных действий в игре.
     */
    public enum Action {
        SHOOT
    }
}