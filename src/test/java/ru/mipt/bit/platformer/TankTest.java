package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mipt.bit.platformer.util.TileMovement;

@ExtendWith(MockitoExtension.class)
class TankTest {
    @Mock
    private TextureRegion textureRegion;
    
    @Mock
    private TextureRegion bulletTextureRegion;

    @Mock
    private TileMovement tileMovement;

    @Mock
    private ObservableLevel observableLevel;

    private Tank tank;
    private static final float MOVEMENT_SPEED = 0.4f;
    private static final int LEVEL_WIDTH = 10;
    private static final int LEVEL_HEIGHT = 8;
    private static final float TILE_WIDTH = 1.0f;
    private static final float TILE_HEIGHT = 1.0f;

    @BeforeEach
    void setUp() {
        // Mock Gdx.app to avoid NPE in shoot() method
        Gdx.app = mock(com.badlogic.gdx.Application.class);

        GridPoint2 startPosition = new GridPoint2(2, 2);
        tank = new Tank(textureRegion, startPosition, tileMovement, MOVEMENT_SPEED,
                       LEVEL_WIDTH, LEVEL_HEIGHT, observableLevel,
                       bulletTextureRegion, TILE_WIDTH, TILE_HEIGHT);
    }

    @Test
    void testTankCreation() {
        assertEquals(new GridPoint2(2, 2), tank.getPosition());
        assertEquals(1f, tank.getMovementProgress());
        assertFalse(tank.isMoving());
        assertTrue(tank.blocksMovement());
        assertTrue(tank.isAlive());
        assertEquals(100, tank.getHealth());
        assertEquals(100, tank.getMaxHealth());
    }

    @Test
    void testTryMoveSuccess() {
        List<Collidable> collidables = Arrays.asList();

        boolean result = tank.tryMove(Direction.RIGHT, collidables);

        assertTrue(result);
        assertTrue(tank.isMoving());
        assertEquals(0f, tank.getMovementProgress());
    }

    @Test
    void testTryMoveCollisionWithObstacle() {
        Obstacle obstacle = mock(Obstacle.class);
        when(obstacle.getPosition()).thenReturn(new GridPoint2(3, 2));
        when(obstacle.blocksMovement()).thenReturn(true);

        List<Collidable> collidables = Arrays.asList(obstacle);

        boolean result = tank.tryMove(Direction.RIGHT, collidables);

        assertFalse(result);
        assertFalse(tank.isMoving());
    }

    @Test
    void testTryMoveCollisionWithOtherTank() {
        Tank otherTank = mock(Tank.class);
        when(otherTank.blocksMovement()).thenReturn(true);
        when(otherTank.occupiesPosition(any())).thenReturn(true);

        List<Collidable> collidables = Arrays.asList(otherTank);

        boolean result = tank.tryMove(Direction.RIGHT, collidables);

        assertFalse(result);
        assertFalse(tank.isMoving());
    }

    @Test
    void testTryMoveOutOfBounds() {
        Tank edgeTank = new Tank(textureRegion, new GridPoint2(0, 0), tileMovement, 
                               MOVEMENT_SPEED, LEVEL_WIDTH, LEVEL_HEIGHT, observableLevel,
                               bulletTextureRegion, TILE_WIDTH, TILE_HEIGHT);
        List<Collidable> collidables = Arrays.asList();

        assertFalse(edgeTank.tryMove(Direction.LEFT, collidables));
        assertFalse(edgeTank.tryMove(Direction.DOWN, collidables));
    }

    @Test
    void testTryMoveWhileMoving() {
        List<Collidable> collidables = Arrays.asList();
        tank.tryMove(Direction.RIGHT, collidables);

        boolean result = tank.tryMove(Direction.UP, collidables);

        assertFalse(result);
    }

    @Test
    void testGetOccupiedCells() {
        List<Collidable> collidables = Arrays.asList();
        tank.tryMove(Direction.RIGHT, collidables);

        List<GridPoint2> occupiedCells = tank.getOccupiedCells();

        assertEquals(2, occupiedCells.size());
        assertTrue(occupiedCells.contains(new GridPoint2(2, 2)));
        assertTrue(occupiedCells.contains(new GridPoint2(3, 2)));
    }

    @Test
    void testOccupiesPosition() {
        List<Collidable> collidables = Arrays.asList();
        tank.tryMove(Direction.RIGHT, collidables);

        assertTrue(tank.occupiesPosition(new GridPoint2(2, 2)));
        assertTrue(tank.occupiesPosition(new GridPoint2(3, 2)));
        assertFalse(tank.occupiesPosition(new GridPoint2(4, 2)));
    }

    @Test
    void testSetPosition() {
        tank.setPosition(new GridPoint2(5, 5));

        assertEquals(new GridPoint2(5, 5), tank.getPosition());
        assertEquals(1f, tank.getMovementProgress());
    }

    @Test
    void testHealthManagement() {
        tank.setHealth(75);
        assertEquals(75, tank.getHealth());

        tank.takeDamage(25);
        assertEquals(50, tank.getHealth());

        tank.takeDamage(100);
        assertEquals(0, tank.getHealth());
        assertFalse(tank.isAlive());
    }

    @Test
    void testShoot() {
        // Настраиваем мок для observableLevel, чтобы избежать NPE при вызове notifyObjectAdded
        doNothing().when(observableLevel).notifyObjectAdded(any(Bullet.class));
        // Проверяем, что метод shoot вызывается без ошибок
        assertDoesNotThrow(() -> tank.shoot());
        // Проверяем, что notifyObjectAdded был вызван
        verify(observableLevel, times(1)).notifyObjectAdded(any(Bullet.class));
    }

    @Test
    void testDirectionFromRotation() {
        assertEquals(Direction.UP, Direction.fromRotation(90f));
        assertEquals(Direction.DOWN, Direction.fromRotation(-90f));
        assertEquals(Direction.LEFT, Direction.fromRotation(-180f));
        assertEquals(Direction.RIGHT, Direction.fromRotation(0f));
    }

    @Test
    void testUpdateIncrementsShotCooldown() {
        float initialTime = getTimeSinceLastShot(tank);
        tank.update(0.1f);
        float newTime = getTimeSinceLastShot(tank);
        assertTrue(newTime > initialTime);
    }

    // Вспомогательный метод для доступа к приватному полю через рефлексию
    private float getTimeSinceLastShot(Tank tank) {
        try {
            java.lang.reflect.Field field = Tank.class.getDeclaredField("timeSinceLastShot");
            field.setAccessible(true);
            return (float) field.get(tank);
        } catch (Exception e) {
            fail("Failed to access timeSinceLastShot field: " + e.getMessage());
            return 0f;
        }
    }
}
