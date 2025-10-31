package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Контроллер для управления AI-танками с использованием шаблона "Команда"
 */
public class AIController {
    private final List<Tank> aiTanks;
    private final List<Collidable> collidables;
    private final int levelWidth;
    private final int levelHeight;
    private final Random random;
    private final List<Command> commandHistory;

    public AIController(List<Tank> aiTanks, List<Collidable> collidables, int levelWidth, int levelHeight) {
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
        for (Tank tank : aiTanks) {
            if (!tank.isMoving()) {
                generateRandomMoveCommand(tank);
            }
        }
    }

    /**
     * Генерирует случайную команду движения для танка
     */
    private void generateRandomMoveCommand(Tank tank) {
        Direction[] directions = Direction.values();
        List<Direction> possibleDirections = new ArrayList<>();

        // Проверяем все возможные направления на валидность
        for (Direction direction : directions) {
            if (isValidMove(tank, direction)) {
                possibleDirections.add(direction);
            }
        }

        // Если есть возможные направления движения
        if (!possibleDirections.isEmpty()) {
            Direction randomDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
            Command moveCommand = new MoveCommand(tank, randomDirection, collidables);
            moveCommand.execute();
            commandHistory.add(moveCommand);
        }
    }

    /**
     * Проверяет, является ли движение в данном направлении валидным
     */
    private boolean isValidMove(Tank tank, Direction direction) {
        GridPoint2 currentPos = tank.getPosition();
        GridPoint2 newPos = direction.applyTo(currentPos);

        // Проверка границ уровня
        if (newPos.x < 0 || newPos.x >= levelWidth || newPos.y < 0 || newPos.y >= levelHeight) {
            return false;
        }

        // Проверка столкновений с другими объектами
        for (Collidable collidable : collidables) {
            if (collidable != tank && collidable.blocksMovement()) {
                if (collidable instanceof Tank) {
                    // Для танков проверяем все занимаемые клетки
                    Tank otherTank = (Tank) collidable;
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
     * Добавляет новый AI-танк
     */
    public void addAITank(Tank tank) {
        if (!aiTanks.contains(tank)) {
            aiTanks.add(tank);
        }
    }

    /**
     * Удаляет AI-танк
     */
    public void removeAITank(Tank tank) {
        aiTanks.remove(tank);
    }

    public List<Command> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }

    public List<Tank> getAITanks() {
        return new ArrayList<>(aiTanks);
    }
}
