package ru.mipt.bit.platformer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса InputController
 */
class InputControllerTest {
  private InputController inputController;
  private Input mockInput;

  @BeforeEach
  void setUp() {
    // Создание мока для Gdx.input
    mockInput = mock(Input.class);
    Gdx.input = mockInput;

    // Создание контроллера ввода
    inputController = new InputController();
  }

  @AfterEach
  void tearDown() {
    // Очистка статического поля после теста
    Gdx.input = null;
  }

  @Test
  void testGetMovementDirection_Up() {
    // Настройка мока - клавиша UP нажата
    when(mockInput.isKeyPressed(Input.Keys.UP)).thenReturn(true);

    // Действие
    Direction direction = inputController.getMovementDirection();

    // Проверка
    assertEquals(
        Direction.UP, direction, "Должно возвращаться направление UP при нажатии клавиши UP");
  }

  @Test
  void testGetMovementDirection_Down() {
    // Настройка мока - клавиша DOWN нажата
    when(mockInput.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);

    // Действие
    Direction direction = inputController.getMovementDirection();

    // Проверка
    assertEquals(
        Direction.DOWN, direction, "Должно возвращаться направление DOWN при нажатии клавиши DOWN");
  }

  @Test
  void testGetMovementDirection_Left() {
    // Настройка мока - клавиша LEFT нажата
    when(mockInput.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);

    // Действие
    Direction direction = inputController.getMovementDirection();

    // Проверка
    assertEquals(
        Direction.LEFT, direction, "Должно возвращаться направление LEFT при нажатии клавиши LEFT");
  }

  @Test
  void testGetMovementDirection_Right() {
    // Настройка мока - клавиша RIGHT нажата
    when(mockInput.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);

    // Действие
    Direction direction = inputController.getMovementDirection();

    // Проверка
    assertEquals(Direction.RIGHT, direction,
        "Должно возвращаться направление RIGHT при нажатии клавиши RIGHT");
  }

  @Test
  void testGetMovementDirection_WASD() {
    // Проверка альтернативных клавиш управления

    when(mockInput.isKeyPressed(Input.Keys.W)).thenReturn(true);
    assertEquals(Direction.UP, inputController.getMovementDirection(),
        "Клавиша W должна соответствовать направлению UP");

    when(mockInput.isKeyPressed(Input.Keys.S)).thenReturn(true);
    assertEquals(Direction.DOWN, inputController.getMovementDirection(),
        "Клавиша S должна соответствовать направлению DOWN");

    when(mockInput.isKeyPressed(Input.Keys.A)).thenReturn(true);
    assertEquals(Direction.LEFT, inputController.getMovementDirection(),
        "Клавиша A должна соответствовать направлению LEFT");

    when(mockInput.isKeyPressed(Input.Keys.D)).thenReturn(true);
    assertEquals(Direction.RIGHT, inputController.getMovementDirection(),
        "Клавиша D должна соответствовать направлению RIGHT");
  }

  @Test
  void testGetMovementDirection_NoKeyPressed() {
    // Настройка мока - никакие клавиши не нажаты
    when(mockInput.isKeyPressed(anyInt())).thenReturn(false);

    // Действие
    Direction direction = inputController.getMovementDirection();

    // Проверка
    assertNull(direction, "Должен возвращаться null при отсутствии нажатых клавиш движения");
  }

  @Test
  void testIsActionPressed_Shoot() {
    // Настройка мока - клавиша SPACE нажата
    when(mockInput.isKeyPressed(Input.Keys.SPACE)).thenReturn(true);

    // Действие
    boolean result = inputController.isActionPressed(InputController.Action.SHOOT);

    // Проверка
    assertTrue(result, "Метод должен возвращать true при нажатии SPACE для действия SHOOT");
  }

  @Test
  void testIsActionPressed_ShootNotPressed() {
    // Настройка мока - клавиша SPACE не нажата
    when(mockInput.isKeyPressed(Input.Keys.SPACE)).thenReturn(false);

    // Действие
    boolean result = inputController.isActionPressed(InputController.Action.SHOOT);

    // Проверка
    assertFalse(result, "Метод должен возвращать false когда SPACE не нажата");
  }

  @Test
  void testIsActionPressed_UnknownAction() {
    // Действие - передача неизвестного действия (в текущей реализации)
    // В будущем можно добавить другие действия

    // Проверка - для неизвестного действия должно возвращаться false
    assertFalse(
        inputController.isActionPressed(null), "Для null действия должно возвращаться false");
  }
}