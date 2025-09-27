package ru.mipt.bit.platformer;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.List;
import ru.mipt.bit.platformer.util.TileMovement;

/**
 * Главный класс игры, реализующий игровой цикл.
 * Координирует работу всех компонентов: ввод, обновление состояния, отрисовку.
 */
public class GameDesktopLauncher implements ApplicationListener {
  // Основные компоненты графики
  private Batch batch; // Пакетный отрисовщик спрайтов
  private TiledMap level; // Загруженная карта уровня
  private MapRenderer levelRenderer; // Отрисовщик карты
  private TileMovement tileMovement; // Механика перемещения по тайлам

  // Игровые объекты
  private Player player; // Управляемый игрок
  private Texture blueTankTexture; // Текстура танка

  // Препятствия
  private Texture greenTreeTexture; // Текстура дерева
  private TextureRegion treeGraphics; // Графическое представление дерева
  private Rectangle treeBounds; // Прямоугольник для отрисовки дерева
  private List<GridPoint2> obstacles; // Список позиций всех препятствий

  // Константы
  private static final float MOVEMENT_SPEED = 0.4f; // Скорость движения игрока

  /**
   * Инициализация игры: загрузка ресурсов, создание объектов.
   */
  @Override
  public void create() {
    // Инициализация графической системы
    batch = new SpriteBatch();
    obstacles = new ArrayList<>();

    // Загрузка и настройка карты уровня
    level = new TmxMapLoader().load("level.tmx");
    levelRenderer = createSingleLayerMapRenderer(level, batch);
    TiledMapTileLayer groundLayer = getSingleLayer(level);
    tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

    // Создание системы ввода
    InputController inputController = new InputController();

    // Создание игрока
    blueTankTexture = new Texture("images/tank_blue.png");
    player = new Player(new TextureRegion(blueTankTexture), new GridPoint2(1, 1), MOVEMENT_SPEED,
        tileMovement, inputController);

    // Создание препятствий
    greenTreeTexture = new Texture("images/greenTree.png");
    treeGraphics = new TextureRegion(greenTreeTexture);
    treeBounds = createBoundingRectangle(treeGraphics);

    // Размещение дерева на карте
    GridPoint2 treePosition = new GridPoint2(1, 3);
    obstacles.add(treePosition);
    moveRectangleAtTileCenter(groundLayer, treeBounds, treePosition);
  }

  /**
   * Главный игровой цикл. Вызывается каждый кадр.
   * Порядок выполнения: обработка ввода → обновление состояния → отрисовка.
   */
  @Override
  public void render() {
    // Очистка экрана темно-синим цветом
    Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
    Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

    // Обновление состояния игрока
    player.update(Gdx.graphics.getDeltaTime(), obstacles);

    // Отрисовка игры
    levelRenderer.render(); // Сначала рисуем карту

    batch.begin(); // Начинаем пакетную отрисовку спрайтов
    player.render(batch); // Рисуем игрока
    drawTextureRegionUnscaled(batch, treeGraphics, treeBounds, 0f); // Рисуем дерево
    batch.end(); // Завершаем отрисовку и отправляем данные на GPU
  }

  /**
   * Обработка изменения размера окна (не используется).
   */
  @Override
  public void resize(int width, int height) {
    // В данной игре не реагируем на изменение размера окна
  }

  /**
   * Вызывается при сворачивании приложения (не используется).
   */
  @Override
  public void pause() {
    // Игра не поддерживает паузу
  }

  /**
   * Вызывается при восстановлении приложения (не используется).
   */
  @Override
  public void resume() {
    // Игра не поддерживает паузу
  }

  /**
   * Освобождение ресурсов при завершении игры.
   * Важно для предотвращения утечек памяти.
   */
  @Override
  public void dispose() {
    // Освобождаем все загруженные текстуры
    greenTreeTexture.dispose();
    blueTankTexture.dispose();

    // Освобождаем карту и графические ресурсы
    level.dispose();
    batch.dispose();
  }

  /**
   * Точка входа в приложение.
   *
   * @param args аргументы командной строки
   */
  public static void main(String[] args) {
    // Настройка окна приложения
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setWindowedMode(1280, 1024); // Размер окна: 1280x1024 пикселей

    // Запуск игрового приложения
    new Lwjgl3Application(new GameDesktopLauncher(), config);
  }
}
