package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.command.ShootCommand;

/**
 * Обработчик пользовательского ввода.
 * Поддерживает обработку движения, действий и специальных команд.
 */
public class InputController {
    private static final float INPUT_COOLDOWN = 0.1f;
    private float timeSinceLastInput = 0f;

    // Команда для переключения отображения здоровья
    private Command healthDisplayCommand;

    public void setHealthDisplayCommand(Command healthDisplayCommand) {
        this.healthDisplayCommand = healthDisplayCommand;
    }

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

    public Command getShootCommand(Tank tank) {
        if (isActionPressed(Action.SHOOT) && timeSinceLastInput >= INPUT_COOLDOWN) {
            timeSinceLastInput = 0f;
            return new ShootCommand(tank);
        }
        return null;
    }

    private Direction resetCooldown(Direction direction) {
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
