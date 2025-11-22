package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.levelloaders.RandomLevelGenerator;

class RandomLevelGeneratorTest {

    @Test
    void testRandomLevelGeneratorCreation() {
        RandomLevelGenerator generator = new RandomLevelGenerator(10, 8, 0.25f);

        assertEquals("RandomLevelGenerator(10x8, density=0.25)", generator.getName());
    }

    @Test
    void testGenerateLevel() {
        RandomLevelGenerator generator = new RandomLevelGenerator(5, 5, 0.2f);

        Level levelData = generator.generateLevel();

        assertNotNull(levelData);
        assertEquals(5, levelData.getWidth());
        assertEquals(5, levelData.getHeight());
        assertNotNull(levelData.getPlayerStart());
        assertNotNull(levelData.getObstaclePositions());

        assertFalse(levelData.getObstaclePositions().contains(levelData.getPlayerStart()));

        assertTrue(levelData.getPlayerStart().x >= 0 && levelData.getPlayerStart().x < 5);
        assertTrue(levelData.getPlayerStart().y >= 0 && levelData.getPlayerStart().y < 5);
    }

    @Test
    void testGenerateLevelWithDefaultDensity() {
        RandomLevelGenerator generator = new RandomLevelGenerator(8, 6);

        Level levelData = generator.generateLevel();

        assertNotNull(levelData);
        assertEquals(8, levelData.getWidth());
        assertEquals(6, levelData.getHeight());
    }
}
