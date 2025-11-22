package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

class ObstacleTest {

    @Test
    void testInitialization() {
        TextureRegion textureRegion = mock(TextureRegion.class);
        GridPoint2 position = new GridPoint2(3, 4);
        Rectangle bounds = new Rectangle(0, 0, 1, 1);

        Obstacle obstacle = new Obstacle(textureRegion, position, bounds);

        assertEquals(position, obstacle.getPosition());
        assertTrue(obstacle.blocksMovement());
        assertEquals(textureRegion, obstacle.getGraphics());
        assertEquals(bounds, obstacle.getBounds());
    }

    @Test
    void testObstacleCreationWithBlocksMovement() {
        TextureRegion textureRegion = mock(TextureRegion.class);
        GridPoint2 position = new GridPoint2(3, 4);
        Rectangle bounds = new Rectangle(0, 0, 1, 1);

        Obstacle obstacle = new Obstacle(textureRegion, position, bounds, false);

        assertFalse(obstacle.blocksMovement());
    }

    @Test
    void testUpdateDoesNothing() {
        TextureRegion textureRegion = mock(TextureRegion.class);
        Obstacle obstacle = new Obstacle(textureRegion, new GridPoint2(1, 1), new Rectangle());

        // Не должно быть исключений
        assertDoesNotThrow(() -> obstacle.update(0.1f));
        assertDoesNotThrow(() -> obstacle.update(1.0f));
    }

    @Test
    void testRender() {
        TextureRegion textureRegion = mock(TextureRegion.class);
        Batch batch = mock(Batch.class);
        Obstacle obstacle = new Obstacle(textureRegion, new GridPoint2(1, 1), new Rectangle());

        // Не должно быть исключений
        assertDoesNotThrow(() -> obstacle.render(batch));
    }

    @Test
    void testGetPosition() {
        TextureRegion textureRegion = mock(TextureRegion.class);
        GridPoint2 position = new GridPoint2(5, 5);
        Obstacle obstacle = new Obstacle(textureRegion, position, new Rectangle());

        assertEquals(position, obstacle.getPosition());
    }

    @Test
    void testGetBounds() {
        TextureRegion textureRegion = mock(TextureRegion.class);
        Rectangle bounds = new Rectangle(1, 1, 2, 2);
        Obstacle obstacle = new Obstacle(textureRegion, new GridPoint2(1, 1), bounds);

        assertEquals(bounds, obstacle.getBounds());
    }

    @Test
    void testGetGraphics() {
        TextureRegion textureRegion = mock(TextureRegion.class);
        Obstacle obstacle = new Obstacle(textureRegion, new GridPoint2(1, 1), new Rectangle());

        assertEquals(textureRegion, obstacle.getGraphics());
    }
}
