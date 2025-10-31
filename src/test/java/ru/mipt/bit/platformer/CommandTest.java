package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Тесты для команд
 */
@ExtendWith(MockitoExtension.class)
class CommandTest {

    @Mock
    private Tank tank;
    
    @Mock
    private List<Collidable> collidables;

    @Test
    void testMoveCommandCreation() {
        // Подготовка
        Direction direction = Direction.UP;

        // Действие
        MoveCommand command = new MoveCommand(tank, direction, collidables);

        // Проверка
        assertEquals(tank, command.getTank());
        assertEquals(direction, command.getDirection());
    }

    @Test
    void testMoveCommandExecute() {
        // Подготовка
        Direction direction = Direction.RIGHT;
        MoveCommand command = new MoveCommand(tank, direction, collidables);

        when(tank.tryMove(direction, collidables)).thenReturn(true);

        // Действие
        command.execute();

        // Проверка
        verify(tank).tryMove(direction, collidables);
    }

    @Test
    void testCommandInterface() {
        // Проверка что MoveCommand реализует интерфейс Command
        Command command = new MoveCommand(tank, Direction.LEFT, collidables);
        assertTrue(command instanceof Command);
    }
}
