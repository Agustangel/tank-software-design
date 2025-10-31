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
import java.util.Random;
import ru.mipt.bit.platformer.util.TileMovement;

/**
 * Главный класс игры, реализующий игровой цикл.
 */
public class GameDesktopLauncher implements ApplicationListener {
    // Конфигурационные константы
    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final float AI_MOVEMENT_SPEED = 0.4f;
    private static final int AI_TANK_COUNT = 3;
    
    // Флаги генерации уровня
    private final boolean useRandomGeneration;
    private final String levelFilePath;

    // Основные компоненты
    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;

    // Игровые объекты
    private Tank playerTank;
    private List<Tank> aiTanks;
    private List<GameObject> gameObjects;
    private List<Collidable> collidables;

    // Контроллеры
    private InputController inputController;
    private AIController aiController;

    // Данные уровня
    private int levelWidth;
    private int levelHeight;

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
        aiTanks = new ArrayList<>();
        inputController = new InputController();

        // Загрузка данных уровня с учетом параметров командной строки
        Level levelData = loadLevelData();
        levelWidth = levelData.getWidth();
        levelHeight = levelData.getHeight();

        // Загрузка и настройка карты уровня
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        // Создание танка игрока
        Texture blueTankTexture = new Texture("images/tank_blue.png");
        playerTank = new Tank(new TextureRegion(blueTankTexture), levelData.getPlayerStart(),
                             tileMovement, TANK_MOVEMENT_SPEED, levelWidth, levelHeight);
        gameObjects.add(playerTank);
        collidables.add(playerTank);

        // Размещаем графику танка игрока
        Rectangle playerBounds = createBoundingRectangle(playerTank.getGraphics());
        moveRectangleAtTileCenter(groundLayer, playerBounds, levelData.getPlayerStart());
        playerTank.getBounds().set(playerBounds);

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

        // Создание AI-танков
        createAITanks(levelData, groundLayer);

        // Создание AI-контроллера
        aiController = new AIController(aiTanks, collidables, levelWidth, levelHeight);

        // Логируем выбранный режим
        Gdx.app.log("Level", "Level generation mode: " + 
            (useRandomGeneration ? "RANDOM" : "FILE: " + levelFilePath));
        Gdx.app.log("Game", "Created " + aiTanks.size() + " AI tanks");
    }

    /**
     * Загружает данные уровня выбранным способом
     */
    private Level loadLevelData() {
        if (useRandomGeneration) {
            Gdx.app.log("Level", "Generating random level");
            return RandomLevelGenerator.generateRandomLevel(10, 8, 0.25f);
        } else {
            Gdx.app.log("Level", "Loading level from file: " + levelFilePath);
            return FileLevelGenerator.loadFromFile(levelFilePath);
        }
    }

    /**
     * Создает AI-танки на свободных позициях уровня
     */
    private void createAITanks(Level levelData, TiledMapTileLayer groundLayer) {
        Texture redTankTexture = new Texture("images/tank_red.png");
        List<GridPoint2> occupiedPositions = new ArrayList<>();

        // Собираем все занятые позиции
        occupiedPositions.add(levelData.getPlayerStart());
        occupiedPositions.addAll(levelData.getObstaclePositions());

        // Создаем AI-танки
        for (int i = 0; i < AI_TANK_COUNT; i++) {
            GridPoint2 aiPosition = findFreePosition(occupiedPositions);
            if (aiPosition != null) {
                Tank aiTank = new Tank(new TextureRegion(redTankTexture), aiPosition,
                                      tileMovement, AI_MOVEMENT_SPEED, levelWidth, levelHeight);
                aiTanks.add(aiTank);
                gameObjects.add(aiTank);
                collidables.add(aiTank);
                occupiedPositions.add(aiPosition);

                // Размещаем графику AI-танка
                Rectangle aiBounds = createBoundingRectangle(aiTank.getGraphics());
                moveRectangleAtTileCenter(groundLayer, aiBounds, aiPosition);
                aiTank.getBounds().set(aiBounds);

                Gdx.app.log("AI", "Created AI tank at position: " + aiPosition);
            } else {
                Gdx.app.error("AI", "Could not find position for AI tank " + i);
            }
        }
    }

    /**
     * Находит свободную позицию на карте
     */
    private GridPoint2 findFreePosition(List<GridPoint2> occupiedPositions) {
        Random random = new Random();
        int maxAttempts = levelWidth * levelHeight * 2;

        for (int i = 0; i < maxAttempts; i++) {
            GridPoint2 candidate = new GridPoint2(
                random.nextInt(levelWidth),
                random.nextInt(levelHeight)
            );
            if (!occupiedPositions.contains(candidate)) {
                return candidate;
            }
        }

        return null;
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
        // Обновление танка игрока с обработкой ввода и столкновений
        if (movementDirection != null) {
            playerTank.tryMove(movementDirection, collidables);
        }

        // Обновление AI-танков
        aiController.update();

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

        // Освобождение текстур танков
        for (GameObject obj : gameObjects) {
            if (obj instanceof Tank) {
                ((Tank) obj).getGraphics().getTexture().dispose();
            } else if (obj instanceof Obstacle) {
                ((Obstacle) obj).getGraphics().getTexture().dispose();
            }
        }
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
