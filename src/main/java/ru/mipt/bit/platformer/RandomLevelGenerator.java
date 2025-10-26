package ru.mipt.bit.platformer;

import com.badlogic.gdx.math.GridPoint2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Генератор случайных уровней
 */
public class RandomLevelGenerator {
    
    /**
     * Генерирует случайный уровень
     * @param width ширина уровня в клетках
     * @param height высота уровня в клетках
     * @param obstacleDensity плотность препятствий (0.0 - 1.0)
     * @return данные уровня
     */
    public static LevelData generateRandomLevel(int width, int height, float obstacleDensity) {
        Random random = new Random();
        List<GridPoint2> obstacles = new ArrayList<>();
        
        // Генерируем препятствия
        int totalCells = width * height;
        int obstacleCount = (int) (totalCells * obstacleDensity);
        
        // Минимальное количество свободных клеток для игрока
        int minFreeCells = 5;
        obstacleCount = Math.min(obstacleCount, totalCells - minFreeCells);
        
        while (obstacles.size() < obstacleCount) {
            GridPoint2 pos = new GridPoint2(
                random.nextInt(width),
                random.nextInt(height)
            );
            
            // Проверяем, что позиция не занята и оставляем место вокруг для движения
            if (!obstacles.contains(pos)) {
                obstacles.add(pos);
            }
        }
        
        // Генерируем стартовую позицию игрока на свободной клетке
        GridPoint2 playerStart;
        do {
            playerStart = new GridPoint2(
                random.nextInt(width),
                random.nextInt(height)
            );
        } while (obstacles.contains(playerStart));
        
        return new LevelData(playerStart, obstacles, width, height);
    }
    
    /**
     * Перегруженный метод с плотностью по умолчанию
     */
    public static LevelData generateRandomLevel(int width, int height) {
        return generateRandomLevel(width, height, 0.3f); // 30% плотность по умолчанию
    }
}