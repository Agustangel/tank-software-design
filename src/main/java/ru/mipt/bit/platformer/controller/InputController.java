package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.*;

/**
 * Обработчик пользовательского ввода.
 * Поддерживает обработку движения, действий и специальных команд.
 */

// 1. Нарушение SRP
// Класс отвечает за слишком много разных задач
// - Обнаруживает нажатия клавиш через Gdx.input
// - Управляет cooldown'ом (timeSinceLastInput)
// - Создает команды (ShootCommand)
// - Обрабатывает специальные действия (handleSpecialActions)
// - Хранит команды здоровья (healthDisplayCommand)
// РЕШЕНИЕ: Разделить на несколько классов, которые:
// - только обнаружение клавиш
// - только управление cooldown'ом
// - только создание команд
// - InputController - координирует взаимодействие вышеперечисленных
public class InputController {

    // 2. Нарушение OCP
    // Хардкодированная константа INPUT_COOLDOWN в коде
    // Код закрыт для расширения без перекомпиляции.
    // Невозможны разные cooldown'ы для разных действий (например, стрельба vs движение).
    // Невозможно менять значение во время работы приложения.
    // РЕШЕНИЕ: Перенести в application.yaml и инжектировать через @Value:
    private static final float INPUT_COOLDOWN = 0.1f;

    private float timeSinceLastInput = 0f;

    private ru.mipt.bit.platformer.Command healthDisplayCommand;

    public void setHealthDisplayCommand(ru.mipt.bit.platformer.Command healthDisplayCommand) {
        this.healthDisplayCommand = healthDisplayCommand;
    }

    /**
     * Определяет направление движения на основе нажатых клавиш.
     */
    // Нарушение OCP
    // Жестко закодированные клавиши в методе (UP, W, LEFT, A, DOWN, S, RIGHT, D)
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

    // 3. Нарушение DIP
    // Создание ShootCommand внутри метода getShootCommand()
    // - InputController зависит от конкретного ShootCommand
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

    // 4. VIOLATION: Нарушение OCP
    // Switch-case для каждого Action (SHOOT, TOGGLE_HEALTH)
    // - Код закрыт для расширения - каждый новый Action требует изменения этого метода
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

    // 5. Нарушение SRP
    // - Проверяет нажата ли клавиша
    // - Выполняет команду
    // - использует Thread.sleep(), что плохо. Это блокирует игровой поток. лучше использовать таймеры на основе delta time
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

    // Не нравится этот enum...
    public enum Action {
        SHOOT,
        TOGGLE_HEALTH
    }
}
