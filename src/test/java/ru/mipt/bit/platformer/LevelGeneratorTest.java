package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Тесты для интерфейса генератора уровней
 */
class LevelGeneratorTest {

    @Test
    void testLevelGeneratorInterface() {
        // Проверка что реализации корректно реализуют интерфейс

        // Подготовка
        LevelGenerator randomGenerator = new RandomLevelGenerator(5, 5);
        LevelGenerator fileGenerator = new FileLevelGenerator("test.lvl");

        // Действие & Проверка
        assertNotNull(randomGenerator.getName());
        assertNotNull(fileGenerator.getName());

        // Проверка что оба генератора реализуют интерфейс
        assertTrue(randomGenerator instanceof LevelGenerator);
        assertTrue(fileGenerator instanceof LevelGenerator);
    }

    @Test
    void testGeneratorNames() {
        // Подготовка
        LevelGenerator randomGenerator = new RandomLevelGenerator(8, 6, 0.2f);
        LevelGenerator fileGenerator = new FileLevelGenerator("levels/custom.lvl");

        // Проверка
        assertEquals("RandomLevelGenerator(8x6, density=0.20)", randomGenerator.getName());
        assertEquals("FileLevelGenerator(levels/custom.lvl)", fileGenerator.getName());
    }
}
