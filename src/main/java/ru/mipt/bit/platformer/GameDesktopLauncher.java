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
    // Конфигурационные константы
    private static final float TANK_MOVEMENT_SPEED = 0.4f;

    // Основные компоненты
    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    // Игровые объекты
    private Tank tank;
    private List<GameObject> gameObjects;
    private List<Collidable> collidables;

    // Контроллеры
    private InputController inputController;

    @Override
    public void create() {
        // Инициализация компонентов
        batch = new SpriteBatch();
        gameObjects = new ArrayList<>();
        collidables = new ArrayList<>();
        inputController = new InputController();

        // Загрузка и настройка карты уровня
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        // Создание танка
        Texture blueTankTexture = new Texture("images/tank_blue.png");
        tank = new Tank(new TextureRegion(blueTankTexture), new GridPoint2(1, 1),
                       tileMovement, TANK_MOVEMENT_SPEED);
        gameObjects.add(tank);

        // Создание препятствий
        Texture greenTreeTexture = new Texture("images/greenTree.png");
        TextureRegion treeGraphics = new TextureRegion(greenTreeTexture);
        Rectangle treeBounds = createBoundingRectangle(treeGraphics);

        GridPoint2 treePosition = new GridPoint2(1, 3);
        Obstacle tree = new Obstacle(treeGraphics, treePosition, treeBounds, true);
        gameObjects.add(tree);
        collidables.add(tree);

        moveRectangleAtTileCenter(groundLayer, treeBounds, treePosition);
    }

    @Override
    public void render() {
        // Очистка экрана
        clearScreen();

        // Обработка ввода
        Direction movementDirection = inputController.getMovementDirection();

        // Обновление состояния игровых объектов
        updateGameState(movementDirection);

        // Отрисовка игры
        renderGame();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void updateGameState(Direction movementDirection) {
        // Обновление танка с обработкой ввода и столкновений
        if (movementDirection != null) {
            tank.tryMove(movementDirection, collidables);
        }

        // Обновление всех игровых объектов
        float deltaTime = Gdx.graphics.getDeltaTime();
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }

        // Проверка действий (например, стрельбы)
        if (inputController.isActionPressed(InputController.Action.SHOOT)) {
            handleShootAction();
        }
    }

    private void handleShootAction() {
        // TODO: Реализовать логику стрельбы
        Gdx.app.log("Input", "Shoot action detected!");
    }

    private void renderGame() {
        levelRenderer.render();
        batch.begin();
        for (GameObject gameObject : gameObjects) {
            gameObject.render(batch);
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
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}