package ru.mipt.bit.platformer;

import static com.badlogic.gdx.Input.Keys.*;

import com.badlogic.gdx.Gdx;

/**
 * Обработчик пользовательского ввода.
 * Отвечает за преобразование нажатий клавиш в игровые команды.
 */
public class InputController {
  /**
   * Определяет направление движения на основе нажатых клавиш.
   * Приоритет направлений: Вверх → Влево → Вниз → Вправо.
   *
   * @return направление движения или null, если клавиши не нажаты
   */
  public Direction getInputDirection() {
    // Проверка движения вверх
    if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(W)) {
      return Direction.UP;
    }
    // Проверка движения влево
    if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(A)) {
      return Direction.LEFT;
    }
    // Проверка движения вниз
    if (Gdx.input.isKeyPressed(DOWN) || Gdx.input.isKeyPressed(S)) {
      return Direction.DOWN;
    }
    // Проверка движения вправо
    if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(D)) {
      return Direction.RIGHT;
    }
    // Если ни одна из клавиш не нажата
    return null;
  }

  /**
   * Проверяет, нажата ли конкретная клавиша в данный момент.
   *
   * @param key код клавиши (из com.badlogic.gdx.Input.Keys)
   * @return true если клавиша нажата
   */
  public boolean isKeyPressed(int key) {
    return Gdx.input.isKeyPressed(key);
  }
}
