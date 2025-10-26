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
import java.util.Arrays;
import java.util.List;
import ru.mipt.bit.platformer.util.TileMovement;

/**
 * Главный класс игры, реализующий игровой цикл.
 */
public class GameDesktopLauncher implements ApplicationListener {
    // Конфигурационные константы
    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    
    // Флаги генерации уровня
    private final boolean useRandomGeneration;
    private final String levelFilePath;

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

    /**
     * Конструктор по умолчанию - для обратной совместимости
     */
    public GameDesktopLauncher() {
        this.useRandomGeneration = true; // значение по умолчанию
        this.levelFilePath = "levels/level1.lvl";
    }

    /**
     * Конструктор с параметрами для передачи из командной строки
     */
    public GameDesktopLauncher(boolean useRandomGeneration, String levelFilePath) {
        this.useRandomGeneration = useRandomGeneration;
        this.levelFilePath = levelFilePath;
    }

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

        // Загрузка данных уровня с учетом параметров командной строки
        LevelData levelData = loadLevelData();

        // Создание танка
        Texture blueTankTexture = new Texture("images/tank_blue.png");
        tank = new Tank(new TextureRegion(blueTankTexture), levelData.getPlayerStart(),
                       tileMovement, TANK_MOVEMENT_SPEED);
        gameObjects.add(tank);

        // Создание препятствий
        Texture greenTreeTexture = new Texture("images/greenTree.png");
        TextureRegion treeGraphics = new TextureRegion(greenTreeTexture);

        for (GridPoint2 obstaclePos : levelData.getObstaclePositions()) {
            Rectangle treeBounds = createBoundingRectangle(treeGraphics);
            Obstacle tree = new Obstacle(treeGraphics, obstaclePos, treeBounds, true);
            gameObjects.add(tree);
            collidables.add(tree);
            moveRectangleAtTileCenter(groundLayer, treeBounds, obstaclePos);
        }

        // Логируем выбранный режим
        Gdx.app.log("Level", "Level generation mode: " + 
            (useRandomGeneration ? "RANDOM" : "FILE: " + levelFilePath));
    }

    /**
     * Загружает данные уровня выбранным способом
     */
    private LevelData loadLevelData() {
        if (useRandomGeneration) {
            Gdx.app.log("Level", "Generating random level");
            return RandomLevelGenerator.generateRandomLevel(10, 8, 0.25f);
        } else {
            Gdx.app.log("Level", "Loading level from file: " + levelFilePath);
            return LevelLoader.loadFromFile(levelFilePath);
        }
    }

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
        
        // Парсинг аргументов командной строки
        CommandLineArgs parsedArgs = parseCommandLineArgs(args);
        
        // Выводим информацию о выбранном режиме в консоль
        System.out.println("Level generation mode: " + 
            (parsedArgs.useRandomGeneration ? "RANDOM" : "FILE: " + parsedArgs.levelFilePath));
        
        // Создание экземпляра игры с параметрами из командной строки
        GameDesktopLauncher game = new GameDesktopLauncher(
            parsedArgs.useRandomGeneration, 
            parsedArgs.levelFilePath
        );
        
        new Lwjgl3Application(game, config);
    }

    /**
     * Вспомогательный класс для хранения распарсенных аргументов
     */
    private static class CommandLineArgs {
        boolean useRandomGeneration = true; // значение по умолчанию
        String levelFilePath = "levels/level1.lvl"; // значение по умолчанию
    }

    /**
     * Парсит аргументы командной строки
     */
    private static CommandLineArgs parseCommandLineArgs(String[] args) {
        CommandLineArgs result = new CommandLineArgs();
        
        if (args.length == 0) {
            // Аргументы не переданы - используем значения по умолчанию
            return result;
        }

        List<String> argsList = Arrays.asList(args);
        
        // Проверяем флаг --random
        if (argsList.contains("--random")) {
            result.useRandomGeneration = true;
            System.out.println("Using random level generation");
        }
        
        // Проверяем флаг --file с путем к файлу
        int fileIndex = argsList.indexOf("--file");
        if (fileIndex != -1 && fileIndex + 1 < argsList.size()) {
            result.useRandomGeneration = false;
            result.levelFilePath = argsList.get(fileIndex + 1);
            System.out.println("Loading level from file: " + result.levelFilePath);
        }
        
        // Проверяем флаг --help
        if (argsList.contains("--help") || argsList.contains("-h")) {
            printHelp();
            System.exit(0);
        }

        return result;
    }

    /**
     * Выводит справку по использованию
     */
    private static void printHelp() {
        System.out.println("Tank Game - Command Line Options:");
        System.out.println("  --random              Generate random level (default)");
        System.out.println("  --file <path>         Load level from specified file");
        System.out.println("  --help, -h            Show this help message");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  ./gradlew run                         # Random level");
        System.out.println("  ./gradlew run --args=\"--random\"      # Random level");
        System.out.println("  ./gradlew run --args=\"--file levels/custom.lvl\"  # Load from file");
    }
}