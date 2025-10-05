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
 */
public class GameDesktopLauncher implements ApplicationListener {
  // Основные компоненты
  private Batch batch;
  private TiledMap level;
  private MapRenderer levelRenderer;
  private TileMovement tileMovement;
  private GraphicsManager graphicsManager;

  // Игровые объекты
  private Tank tank;
  private List<Obstacle> obstacles;

  // Контроллеры
  private InputController inputController;

  // Константы
  private static final float MOVEMENT_SPEED = 0.4f;

  @Override
  public void create() {
    // Инициализация компонентов
    batch = new SpriteBatch();
    obstacles = new ArrayList<>();
    graphicsManager = new GraphicsManager();
    inputController = new InputController();

    // Загрузка и настройка карты уровня
    level = new TmxMapLoader().load("level.tmx");
    levelRenderer = createSingleLayerMapRenderer(level, batch);
    TiledMapTileLayer groundLayer = getSingleLayer(level);
    tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

    // Создание танка
    Texture blueTankTexture = new Texture("images/tank_blue.png");
    tank = new Tank(
        new TextureRegion(blueTankTexture), new GridPoint2(1, 1), MOVEMENT_SPEED, tileMovement);

    // Создание препятствий
    Texture greenTreeTexture = new Texture("images/greenTree.png");
    TextureRegion treeGraphics = new TextureRegion(greenTreeTexture);
    Rectangle treeBounds = createBoundingRectangle(treeGraphics);

    GridPoint2 treePosition = new GridPoint2(1, 3);
    obstacles.add(new Obstacle(treeGraphics, treePosition, treeBounds));
    moveRectangleAtTileCenter(groundLayer, treeBounds, treePosition);
  }

  @Override
  public void render() {
    // Очистка экрана
    Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
    Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

    // Получение направления движения от контроллера
    Direction movementDirection = inputController.getMovementDirection();

    // Обновление состояния игровых объектов
    tank.update(Gdx.graphics.getDeltaTime(), obstacles, movementDirection);

    // Проверка действий (например, стрельбы)
    if (inputController.isActionPressed(InputController.Action.SHOOT)) {
      // TODO: Реализовать логику стрельбы
      Gdx.app.log("Input", "Shoot action detected!");
    }

    // Отрисовка игры
    levelRenderer.render();
    batch.begin();
    tank.render(batch);
    for (Obstacle obstacle : obstacles) {
      graphicsManager.drawObstacle(batch, obstacle);
    }
    batch.end();
  }

  @Override
  public void resize(int width, int height) {
    // Не используется
  }

  @Override
  public void pause() {
    // Не используется
  }

  @Override
  public void resume() {
    // Не используется
  }

  @Override
  public void dispose() {
    // Освобождение ресурсов
    batch.dispose();
    level.dispose();
    // Текстуры освобождаются в отдельных объектах
  }

  public static void main(String[] args) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    config.setWindowedMode(1280, 1024);
    new Lwjgl3Application(new GameDesktopLauncher(), config);
  }
}