package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;

/**
 * Обработчик пользовательского ввода.
 * Поддерживает обработку движения и действий (например, стрельбы).
 */
public class InputController {
  /**
   * Определяет направление движения на основе нажатых клавиш.
   */
  public Direction getMovementDirection() {
    if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)
        || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
      return Direction.UP;
    }
    if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)
        || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
      return Direction.LEFT;
    }
    if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)
        || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
      return Direction.DOWN;
    }
    if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)
        || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
      return Direction.RIGHT;
    }
    return null;
  }

  /**
   * Проверяет, нажата ли клавиша действия (например, стрельбы).
   */
  public boolean isActionPressed(Action action) {
    switch (action) {
      case SHOOT:
        return Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);
      // Добавьте другие действия здесь
      default:
        return false;
    }
  }

  /**
   * Перечисление возможных действий в игре.
   */
  public enum Action {
    SHOOT
    // Можно добавить: RELOAD, SPECIAL_ABILITY, PAUSE и т.д.
  }
}