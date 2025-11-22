package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Контроллер для управления AI-танками с использованием шаблонов "Команда" и "Стратегия"
 * Теперь поддерживает dependency injection через конструктор
 */
public class AIController {
    private final List<ru.mipt.bit.platformer.Tank> aiTanks;
    private final List<ru.mipt.bit.platformer.Collidable> collidables;
    private final int levelWidth;
    private final int levelHeight;
    private final Random random;
    private final List<ru.mipt.bit.platformer.Command> commandHistory;

    private static final float SHOOT_PROBABILITY = 0.3f; // Вероятность выстрела вместо движения

    /**
     * Конструктор для Spring DI - все зависимости передаются через параметры
     */
    public AIController(List<ru.mipt.bit.platformer.Tank> aiTanks, List<ru.mipt.bit.platformer.Collidable> collidables, int levelWidth, int levelHeight) {
        this.aiTanks = new ArrayList<>(aiTanks);
        this.collidables = collidables;
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.random = new Random();
        this.commandHistory = new ArrayList<>();
    }

    /**
     * Обновляет состояние всех AI-танков
     */
    public void update() {
        for (ru.mipt.bit.platformer.Tank tank : aiTanks) {
            if (!tank.isMoving() && tank.isAlive()) {
                generateRandomAction(tank);
            }
        }
    }

    /**
     * Генерирует случайное действие (движение или стрельбу) для танка
     */
    private void generateRandomAction(ru.mipt.bit.platformer.Tank tank) {
        // Случайно выбираем между движением и стрельбой
        if (random.nextFloat() < SHOOT_PROBABILITY && isValidShoot(tank)) {
            // Создаем команду стрельбы
            ru.mipt.bit.platformer.Command shootCommand = new ru.mipt.bit.platformer.command.ShootCommand(tank);
            shootCommand.execute();
            commandHistory.add(shootCommand);
        } else {
            // Генерируем движение как раньше
            generateRandomMoveCommand(tank);
        }
    }

    /**
     * Генерирует случайную команду движения для танка
     */
    private void generateRandomMoveCommand(ru.mipt.bit.platformer.Tank tank) {
        ru.mipt.bit.platformer.Direction[] directions = ru.mipt.bit.platformer.Direction.values();
        List<ru.mipt.bit.platformer.Direction> possibleDirections = new ArrayList<>();

        // Проверяем все возможные направления на валидность
        for (ru.mipt.bit.platformer.Direction direction : directions) {
            if (isValidMove(tank, direction)) {
                possibleDirections.add(direction);
            }
        }

        // Если есть возможные направления движения
        if (!possibleDirections.isEmpty()) {
            ru.mipt.bit.platformer.Direction randomDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
            ru.mipt.bit.platformer.Command moveCommand = new ru.mipt.bit.platformer.MoveCommand(tank, randomDirection, collidables);
            moveCommand.execute();
            commandHistory.add(moveCommand);
        }
    }

    /**
     * Проверяет, является ли движение в данном направлении валидным
     */
    private boolean isValidMove(ru.mipt.bit.platformer.Tank tank, ru.mipt.bit.platformer.Direction direction) {
        GridPoint2 currentPos = tank.getPosition();
        GridPoint2 newPos = direction.applyTo(currentPos);

        // Проверка границ уровня
        if (newPos.x < 0 || newPos.x >= levelWidth || newPos.y < 0 || newPos.y >= levelHeight) {
            return false;
        }

        // Проверка столкновений с другими объектами
        for (ru.mipt.bit.platformer.Collidable collidable : collidables) {
            if (collidable != tank && collidable.blocksMovement()) {
                if (collidable instanceof ru.mipt.bit.platformer.Tank) {
                    // Для танков проверяем все занимаемые клетки
                    ru.mipt.bit.platformer.Tank otherTank = (ru.mipt.bit.platformer.Tank) collidable;
                    if (otherTank.occupiesPosition(newPos)) {
                        return false;
                    }
                } else {
                    // Для остальных объектов проверяем только текущую позицию
                    if (collidable.getPosition().equals(newPos)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Проверяет, может ли танк выстрелить (направление не упирается в стену)
     */
    private boolean isValidShoot(ru.mipt.bit.platformer.Tank tank) {
        GridPoint2 shootPosition = ru.mipt.bit.platformer.Direction.fromRotation(tank.getRotation()).applyTo(tank.getPosition());

        // Проверка границ уровня
        if (shootPosition.x < 0 || shootPosition.x >= levelWidth ||
            shootPosition.y < 0 || shootPosition.y >= levelHeight) {
            return false;
        }

        // Проверка столкновений с препятствиями
        for (ru.mipt.bit.platformer.Collidable collidable : collidables) {
            if (collidable != tank && collidable.blocksMovement() &&
                collidable.getPosition().equals(shootPosition)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Добавляет новый AI-танк
     */
    public void addAITank(ru.mipt.bit.platformer.Tank tank) {
        if (!aiTanks.contains(tank)) {
            aiTanks.add(tank);
        }
    }

    /**
     * Удаляет AI-танк
     */
    public void removeAITank(ru.mipt.bit.platformer.Tank tank) {
        aiTanks.remove(tank);
    }

    public List<ru.mipt.bit.platformer.Command> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }

    public List<ru.mipt.bit.platformer.Tank> getAITanks() {
        return new ArrayList<>(aiTanks);
    }
}
