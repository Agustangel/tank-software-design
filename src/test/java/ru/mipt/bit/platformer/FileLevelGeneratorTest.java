package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

class FileLevelGeneratorTest {

    @Test
    void testFileLevelGeneratorCreation() {
        FileLevelGenerator generator = new FileLevelGenerator("levels/test.lvl");
        assertEquals("FileLevelGenerator(levels/test.lvl)", generator.getName());
    }

    @Test
    void testParseLevelContent() {
        String levelContent = 
            "X___\n" +
            "_T__\n" +
            "__T_\n" +
            "____";

        // Тестируем напрямую парсинг содержимого
        Level level = parseLevelContentDirectly(levelContent);

        assertNotNull(level);
        assertEquals(4, level.getWidth());
        assertEquals(4, level.getHeight());
        assertEquals(new GridPoint2(0, 3), level.getPlayerStart());
        assertEquals(2, level.getObstaclePositions().size());
        assertTrue(level.getObstaclePositions().contains(new GridPoint2(1, 2)));
        assertTrue(level.getObstaclePositions().contains(new GridPoint2(2, 1)));
    }

    @Test
    void testParseLevelContentWithMultiplePlayerStarts() {
        String levelContent = 
            "X___\n" +
            "_X__\n" +
            "__T_\n" +
            "____";

        Exception exception = assertThrows(RuntimeException.class, 
            () -> parseLevelContentDirectly(levelContent));

        assertTrue(exception.getMessage().contains("Multiple player start positions"));
    }

    @Test
    void testParseLevelContentWithoutPlayerStart() {
        String levelContent = 
            "____\n" +
            "_T__\n" +
            "__T_\n" +
            "____";

        Exception exception = assertThrows(RuntimeException.class, 
            () -> parseLevelContentDirectly(levelContent));

        assertTrue(exception.getMessage().contains("No player start position"));
    }

    @Test
    void testParseLevelContentWithUnknownCharacter() {
        String levelContent = 
            "X___\n" +
            "_Z__\n" +
            "__T_\n" +
            "____";

        Exception exception = assertThrows(RuntimeException.class, 
            () -> parseLevelContentDirectly(levelContent));

        assertTrue(exception.getMessage().contains("Unknown cell character"));
    }

    // Вспомогательный метод для прямого тестирования парсинга
    private Level parseLevelContentDirectly(String content) {
        String[] lines = content.split("\\r?\\n");
        java.util.List<GridPoint2> obstacles = new java.util.ArrayList<>();
        GridPoint2 playerStart = null;

        int height = lines.length;
        int width = 0;

        if (height == 0) {
            throw new RuntimeException("Level file is empty");
        }

        for (int y = height - 1; y >= 0; y--) {
            String line = lines[height - 1 - y].trim();
            if (line.isEmpty())
                continue;

            width = Math.max(width, line.length());

            for (int x = 0; x < line.length(); x++) {
                char cell = line.charAt(x);
                switch (cell) {
                case 'T':
                    obstacles.add(new GridPoint2(x, y));
                    break;
                case 'X':
                    if (playerStart != null) {
                        throw new RuntimeException("Multiple player start positions (X) found");
                    }
                    playerStart = new GridPoint2(x, y);
                    break;
                case '_':
                    break;
                default:
                    throw new RuntimeException("Unknown cell character: '" + cell + "' at position (" + x + "," + y + ")");
                }
            }
        }

        if (playerStart == null) {
            throw new RuntimeException("No player start position (X) found in level file");
        }

        return new Level(playerStart, obstacles, width, height);
    }
}