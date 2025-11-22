package ru.mipt.bit.platformer.levelloaders;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Генератор случайных уровней
 */
public class RandomLevelGenerator implements LevelGenerator {
    private final int width;
    private final int height;
    private final float obstacleDensity;
    private final Random random;

    /**
     * Создает генератор случайных уровней с указанными параметрами
     * @param width ширина уровня в клетках
     * @param height высота уровня в клетках
     * @param obstacleDensity плотность препятствий (0.0 - 1.0)
     */
    public RandomLevelGenerator(int width, int height, float obstacleDensity) {
        this.width = width;
        this.height = height;
        this.obstacleDensity = obstacleDensity;
        this.random = new Random();
    }

    /**
     * Создает генератор случайных уровней с плотностью по умолчанию
     */
    public RandomLevelGenerator(int width, int height) {
        this(width, height, 0.3f); // 30% плотность по умолчанию
    }

    @Override
    public Level generateLevel() {
        List<GridPoint2> obstacles = new ArrayList<>();

        // Генерируем препятствия
        int totalCells = width * height;
        int obstacleCount = (int) (totalCells * obstacleDensity);

        // Минимальное количество свободных клеток для игрока и AI-танков
        int minFreeCells = 10;
        obstacleCount = Math.min(obstacleCount, totalCells - minFreeCells);

        while (obstacles.size() < obstacleCount) {
            GridPoint2 pos = new GridPoint2(
                random.nextInt(width),
                random.nextInt(height)
            );
            // Проверяем, что позиция не занята
            if (!obstacles.contains(pos)) {
                obstacles.add(pos);
            }
        }

        // Генерируем стартовую позицию игрока на свободной клетке
        GridPoint2 playerStart = findFreePosition(obstacles);

        return new Level(playerStart, obstacles, width, height);
    }
    
    @Override
    public String getName() {
        return String.format("RandomLevelGenerator(%dx%d, density=%.2f)", width, height, obstacleDensity);
    }

    /**
     * Находит свободную позицию на карте
     */
    private GridPoint2 findFreePosition(List<GridPoint2> occupiedPositions) {
        int maxAttempts = width * height * 2;

        for (int i = 0; i < maxAttempts; i++) {
            GridPoint2 candidate = new GridPoint2(
                random.nextInt(width),
                random.nextInt(height)
            );
            if (!occupiedPositions.contains(candidate)) {
                return candidate;
            }
        }

        // Если не нашли свободную позицию, используем первую доступную
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                GridPoint2 candidate = new GridPoint2(x, y);
                if (!occupiedPositions.contains(candidate)) {
                    return candidate;
                }
            }
        }

        throw new RuntimeException("No free positions available for player start");
    }
}
