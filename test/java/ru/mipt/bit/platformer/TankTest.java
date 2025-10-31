package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

/**
 * Тесты для класса Tank
 */
@ExtendWith(MockitoExtension.class)
class TankTest {

    @Mock
    private TextureRegion textureRegion;

    @Mock
    private TileMovement tileMovement;

    private Tank tank;
    private static final float MOVEMENT_SPEED = 0.4f;
    private static final int LEVEL_WIDTH = 10;
    private static final int LEVEL_HEIGHT = 8;

    @BeforeEach
    void setUp() {
        GridPoint2 startPosition = new GridPoint2(2, 2);
        tank = new Tank(textureRegion, startPosition, tileMovement, MOVEMENT_SPEED, LEVEL_WIDTH, LEVEL_HEIGHT);
    }

    @Test
    void testTankCreation() {
        // Проверка
        assertEquals(new GridPoint2(2, 2), tank.getPosition());
        assertEquals(1f, tank.getMovementProgress());
        assertFalse(tank.isMoving());
        assertTrue(tank.blocksMovement());
    }

    @Test
    void testTryMoveSuccess() {
        // Подготовка
        List<Collidable> collidables = Arrays.asList();

        // Действие
        boolean result = tank.tryMove(Direction.RIGHT, collidables);

        // Проверка
        assertTrue(result);
        assertTrue(tank.isMoving());
        assertEquals(0f, tank.getMovementProgress());
    }

    @Test
    void testTryMoveCollisionWithObstacle() {
        // Подготовка
        Obstacle obstacle = mock(Obstacle.class);
        when(obstacle.getPosition()).thenReturn(new GridPoint2(3, 2));
        when(obstacle.blocksMovement()).thenReturn(true);

        List<Collidable> collidables = Arrays.asList(obstacle);

        // Действие
        boolean result = tank.tryMove(Direction.RIGHT, collidables);

        // Проверка
        assertFalse(result);
        assertFalse(tank.isMoving());
    }

    @Test
    void testTryMoveCollisionWithOtherTank() {
        // Подготовка
        Tank otherTank = mock(Tank.class);
        when(otherTank.getPosition()).thenReturn(new GridPoint2(3, 2));
        when(otherTank.blocksMovement()).thenReturn(true);
        when(otherTank.occupiesPosition(any())).thenReturn(true);

        List<Collidable> collidables = Arrays.asList(otherTank);

        // Действие
        boolean result = tank.tryMove(Direction.RIGHT, collidables);

        // Проверка
        assertFalse(result);
        assertFalse(tank.isMoving());
    }

    @Test
    void testTryMoveOutOfBounds() {
        // Подготовка
        Tank edgeTank = new Tank(textureRegion, new GridPoint2(0, 0), tileMovement,
                               MOVEMENT_SPEED, LEVEL_WIDTH, LEVEL_HEIGHT);
        List<Collidable> collidables = Arrays.asList();

        // Действие & Проверка - движение за левую границу
        assertFalse(edgeTank.tryMove(Direction.LEFT, collidables));

        // Действие & Проверка - движение за нижнюю границу
        assertFalse(edgeTank.tryMove(Direction.DOWN, collidables));
    }

    @Test
    void testTryMoveWhileMoving() {
        // Подготовка
        List<Collidable> collidables = Arrays.asList();
        tank.tryMove(Direction.RIGHT, collidables); // Начинаем движение

        // Действие - попытка движения во время движения
        boolean result = tank.tryMove(Direction.UP, collidables);

        // Проверка
        assertFalse(result);
    }

    @Test
    void testGetOccupiedCells() {
        // Подготовка
        List<Collidable> collidables = Arrays.asList();
        tank.tryMove(Direction.RIGHT, collidables);

        // Действие
        List<GridPoint2> occupiedCells = tank.getOccupiedCells();

        // Проверка
        assertEquals(2, occupiedCells.size());
        assertTrue(occupiedCells.contains(new GridPoint2(2, 2))); // Начальная позиция
        assertTrue(occupiedCells.contains(new GridPoint2(3, 2))); // Целевая позиция
    }

    @Test
    void testOccupiesPosition() {
        // Подготовка
        List<Collidable> collidables = Arrays.asList();
        tank.tryMove(Direction.RIGHT, collidables);

        // Проверка
        assertTrue(tank.occupiesPosition(new GridPoint2(2, 2))); // Начальная позиция
        assertTrue(tank.occupiesPosition(new GridPoint2(3, 2))); // Целевая позиция
        assertFalse(tank.occupiesPosition(new GridPoint2(4, 2))); // Другая позиция
    }

    @Test
    void testSetPosition() {
        // Действие
        tank.setPosition(new GridPoint2(5, 5));

        // Проверка
        assertEquals(new GridPoint2(5, 5), tank.getPosition());
        assertEquals(1f, tank.getMovementProgress()); // Должен сбросить прогресс движения
    }

    @Test
    void testIsPlayer() {
        // Проверка - танк с скоростью 0.4f должен определяться как игрок
        assertTrue(tank.isPlayer());

        // Подготовка - танк с другой скоростью
        Tank aiTank = new Tank(textureRegion, new GridPoint2(0, 0), tileMovement,
                             0.3f, LEVEL_WIDTH, LEVEL_HEIGHT);

        // Проверка
        assertFalse(aiTank.isPlayer());
    }
}
