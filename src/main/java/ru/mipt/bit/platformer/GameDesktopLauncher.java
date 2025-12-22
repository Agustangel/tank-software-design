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
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.mipt.bit.platformer.config.SpringConfig;
import ru.mipt.bit.platformer.controller.AIController;
import ru.mipt.bit.platformer.controller.InputController;
import ru.mipt.bit.platformer.levelloaders.LevelGenerator;
import ru.mipt.bit.platformer.levelloaders.RandomLevelGenerator;
import ru.mipt.bit.platformer.levelloaders.FileLevelGenerator;

/**
 * Главный класс игры, реализующий игровой цикл.
 * Spring используется только для неграфических компонентов
 */

// 1. Нарушение SRP
// Класс реализует ApplicationListener И LevelObserver одновременно
// Класс отвечает за управление игровым циклом И за обработку событий уровня.
// Это две разные ответственности.
// РЕШЕНИЕ: Создать отдельный класс GameLevelObserver, который будет имплементировать интерфейс LevelObserver.
// Его как поле будет хранить GameDesktopLauncher и при необходимости дергать его методы.
public class GameDesktopLauncher implements ApplicationListener, LevelObserver {
    // 2. Нарушение OCP
    // Конфигурационные константы жестко закодированы в коде
    // Код закрыт для расширения - нельзя менять значения без перекомпиляции.
    // Невозможны разные конфигурации для разных сценариев или уровней.
    // РЕШЕНИЕ: Перенести в конфигурационный файл (resources/application.yaml) 
    // и подтягивать через spring (@Value)
    private static final float TANK_MOVEMENT_SPEED = 0.4f;
    private static final float AI_MOVEMENT_SPEED = 0.4f;
    private static final int AI_TANK_COUNT = 3;

    private final boolean useRandomGeneration;
    private final String levelFilePath;

    private Level levelData;
    private LevelGenerator levelGenerator;
    private InputController inputController;
    private AIController aiController;
    private ObservableLevel observableLevel;
    
    private Batch batch;
    private TiledMap level;
    private MapRenderer levelRenderer;
    private TileMovement tileMovement;
    private Tank playerTank;
    
    // 3. Нарушение SRP
    // Инициализация и управление множеством коллекций в одном классе
    // GameDesktopLauncher отвечает за слишком много:
    // - управление танками (aiTanks)
    // - управление всеми игровыми объектами (gameObjects)
    // - управление коллизиями (collidables)
    // - управление декораторами здоровья (healthDecorators)
    // - управление отрисовкой (renderableObjects)
    // - управление пулями (bullets)
    // Это нарушает принцип единственной ответственности.
    // РЕШЕНИЕ: За управлением этими объектами должен отвечать другой класс - GameWorld (например).
    // Это должен быть класс отличный от GameDesktopLauncher, который:
    // - хранит все объекты
    // - управляет добавлением/удалением объектов
    // - обеспечивает взаимодействие объектов
    private List<Tank> aiTanks;
    private List<GameObject> gameObjects;
    private List<Collidable> collidables;
    private List<HealthBarDecorator> healthDecorators;
    private List<GameObject> renderableObjects;
    private List<Bullet> bullets;
    private Texture bulletTexture;
    private TextureRegion bulletGraphics;
    private float tileWidth;
    private float tileHeight;
    
    private int levelWidth;
    private int levelHeight;

    private AnnotationConfigApplicationContext context;

    public GameDesktopLauncher() {
        this.useRandomGeneration = true;
        this.levelFilePath = "levels/level1.lvl";
        
        this.aiTanks = new ArrayList<>();
        this.gameObjects = new ArrayList<>();
        this.collidables = new ArrayList<>();
        this.healthDecorators = new ArrayList<>();
        this.renderableObjects = new ArrayList<>();
        this.bullets = new ArrayList<>();
    }

    public GameDesktopLauncher(boolean useRandomGeneration, String levelFilePath) {
        this.useRandomGeneration = useRandomGeneration;
        this.levelFilePath = levelFilePath;
        
        this.aiTanks = new ArrayList<>();
        this.gameObjects = new ArrayList<>();
        this.collidables = new ArrayList<>();
        this.healthDecorators = new ArrayList<>();
        this.renderableObjects = new ArrayList<>();
        this.bullets = new ArrayList<>();
    }

    @Override
    public void create() {
        // 4. Нарушение DIP
        // Получаем Spring бины через Service Locator pattern
        // Класс явно создает зависимость от Spring контекста.
        // Вместо инжекции зависимостей в конструктор, мы "достаем" бины из контекста.
        // Это нарушает DIP - класс зависит от конкретной реализации (Spring контекст),
        // а не от абстракции.
        // РЕШЕНИЕ: Эти бины нужно создавать в файле Config.java через @Bean методы, 
        // после чего инжектить в конструкторы классов через Constructor Injection.
        context = new AnnotationConfigApplicationContext(SpringConfig.class);

        this.levelData = context.getBean(Level.class);
        this.levelGenerator = context.getBean(LevelGenerator.class);
        this.inputController = context.getBean(InputController.class);
        this.aiController = context.getBean(AIController.class);
        this.observableLevel = context.getBean(ObservableLevel.class);

        batch = new SpriteBatch();
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        tileWidth = groundLayer.getTileWidth();
        tileHeight = groundLayer.getTileHeight();
        Gdx.app.log("Game", "Tile dimensions: " + tileWidth + "x" + tileHeight);

        this.levelWidth = levelData.getWidth();
        this.levelHeight = levelData.getHeight();

        observableLevel.addObserver(this);


        try {
            Gdx.app.log("Game", "Creating fallback texture for testing");
            createFallbackBulletTexture();
        } catch (Exception e) {
            Gdx.app.error("Game", "Failed to load bullet texture: " + e.getMessage());
            createFallbackBulletTexture();
        }

        Texture blueTankTexture = new Texture("images/tank_blue.png");
        TextureRegion tankGraphics = new TextureRegion(blueTankTexture);
        
        playerTank = new Tank(tankGraphics, levelData.getPlayerStart(), tileMovement, 
                            TANK_MOVEMENT_SPEED, levelWidth, levelHeight, 
                            observableLevel, bulletGraphics, tileWidth, tileHeight);

        HealthBarDecorator playerHealthDecorator = new HealthBarDecorator(playerTank);
        healthDecorators.add(playerHealthDecorator);
        renderableObjects.add(playerHealthDecorator);
        collidables.add(playerTank);

        Rectangle playerBounds = createBoundingRectangle(playerTank.getGraphics());
        moveRectangleAtTileCenter(groundLayer, playerBounds, levelData.getPlayerStart());
        playerTank.getBounds().set(playerBounds);

        createObstacles(groundLayer);
        
        createAITanks(levelData, groundLayer);

        ToggleHealthDisplayCommand healthCommand = new ToggleHealthDisplayCommand(healthDecorators);
        inputController.setHealthDisplayCommand(healthCommand);

        Gdx.app.log("Level", "Level generation mode: " + 
            (useRandomGeneration ? "RANDOM" : "FILE: " + levelFilePath));
        Gdx.app.log("Game", "Created " + aiTanks.size() + " AI tanks");
        Gdx.app.log("Spring", "Game initialized with Spring IoC container (non-graphics only)");
    }


    private void createObstacles(TiledMapTileLayer groundLayer) {
        Texture greenTreeTexture = new Texture("images/greenTree.png");
        TextureRegion treeGraphics = new TextureRegion(greenTreeTexture);

        for (GridPoint2 obstaclePos : levelData.getObstaclePositions()) {
            Rectangle treeBounds = createBoundingRectangle(treeGraphics);
            Obstacle tree = new Obstacle(treeGraphics, obstaclePos, treeBounds, true);
            gameObjects.add(tree);
            renderableObjects.add(tree);
            collidables.add(tree);
            moveRectangleAtTileCenter(groundLayer, treeBounds, obstaclePos);
        }
    }

    private boolean isTextureMostlyTransparent(Texture texture) {
        return texture.getWidth() < 10 || texture.getHeight() < 10;
    }

    // 5. Нарушение SRP
    // createFallbackBulletTexture() находится в основном классе
    // GameDesktopLauncher не должен отвечать за создание графических ресурсов.
    // Это отделение интереса - создание текстур это отдельная задача от управления игровым циклом.
    // РЕШЕНИЕ: За это должен отвечать отдельный утилитарный класс (например BulletTextureFactory).
    private void createFallbackBulletTexture() {
        try {
            int size = 32;
            Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);

            pixmap.setColor(0, 0, 0, 0);
            pixmap.fill();

            pixmap.setColor(1, 1, 0, 1); // Ярко-желтый
            pixmap.fillCircle(size/2, size/2, size/3);

            pixmap.setColor(1, 0, 0, 1); // Красный
            pixmap.fillCircle(size/2, size/2, size/6);

            pixmap.setColor(0, 0, 0, 1); // Черный
            pixmap.drawCircle(size/2, size/2, size/3);

            bulletTexture = new Texture(pixmap);
            bulletGraphics = new TextureRegion(bulletTexture);
            pixmap.dispose();
            Gdx.app.log("Game", "Created proper bullet texture: yellow circle with red center");
        } catch (Exception e2) {
            Gdx.app.error("Game", "Failed to create proper bullet texture: " + e2.getMessage());
            createSimpleRedTexture();
        }
    }

    // Нарушение SRP
    // Метод создания fallback текстуры также находится в основном классе (часть проблемы)
    // РЕШЕНИЕ: За это должен отвечать отдельный утилитарный класс (например BulletTextureFactory).
    private void createSimpleRedTexture() {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 1); // Красный
        pixmap.fill();
        bulletTexture = new Texture(pixmap);
        bulletGraphics = new TextureRegion(bulletTexture);
        pixmap.dispose();
        Gdx.app.log("Game", "Created simple red bullet texture");
    }

    private Level loadLevelData() {
        LevelGenerator generator;

        if (useRandomGeneration) {
            generator = new RandomLevelGenerator(10, 8, 0.25f);
            Gdx.app.log("Level", "Using generator: " + generator.getName());
        } else {
            generator = new FileLevelGenerator(levelFilePath);
            Gdx.app.log("Level", "Using generator: " + generator.getName());
        }

        return generator.generateLevel();
    }

    // 7. Нарушение SRP
    // Логика заполнения уровня находится в основном классе
    // GameDesktopLauncher отвечает за создание врагов и поиск свободных позиций.
    // Это отдельная ответственность, которая не связана с управлением игровым циклом.
    // РЕШЕНИЕ: Стоит вынести логику в отдельный обработчик/инициализатор уровня.
    // Создать класс LevelPopulator или LevelInitializer, который будет отвечать за:
    // - создание AI танков
    // - поиск свободных позиций
    // нужно вынести: createAITanks, findFreePosition
    private void createAITanks(Level levelData, TiledMapTileLayer groundLayer) {
        Texture redTankTexture = new Texture("images/tank_red.png");
        List<GridPoint2> occupiedPositions = new ArrayList<>();

        occupiedPositions.add(levelData.getPlayerStart());
        occupiedPositions.addAll(levelData.getObstaclePositions());

        for (int i = 0; i < AI_TANK_COUNT; i++) {
            GridPoint2 aiPosition = findFreePosition(occupiedPositions);
            if (aiPosition != null) {
                Tank aiTank = new Tank(new TextureRegion(redTankTexture), aiPosition,
                                    tileMovement, AI_MOVEMENT_SPEED, levelWidth, levelHeight,
                                    observableLevel, bulletGraphics, tileWidth, tileHeight);
                aiTanks.add(aiTank);
                collidables.add(aiTank);
                occupiedPositions.add(aiPosition);

                HealthBarDecorator aiHealthDecorator = new HealthBarDecorator(aiTank);
                healthDecorators.add(aiHealthDecorator);
                renderableObjects.add(aiHealthDecorator);

                Rectangle aiBounds = createBoundingRectangle(aiTank.getGraphics());
                moveRectangleAtTileCenter(groundLayer, aiBounds, aiPosition);
                aiTank.getBounds().set(aiBounds);
                Gdx.app.log("TankSetup", "Tank bounds after setup: " + aiTank.getBounds());

                Gdx.app.log("AI", "Created AI tank at position: " + aiPosition);
            } else {
                Gdx.app.error("AI", "Could not find position for AI tank " + i);
            }
        }
    }

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
        clearScreen();

        Direction movementDirection = inputController.getMovementDirection();
        updateGameState(movementDirection);

        renderGame();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void updateGameState(Direction movementDirection) {
        inputController.handleSpecialActions();
        // 6. Нарушение DIP
        // Класс GameDesktopLauncher знает про внутренности InputController (InputController.Action.SHOOT)
        // GameDesktopLauncher зависит от конкретной реализации InputController (его enum Action).
        // Если структура InputController изменится, придется менять GameDesktopLauncher.
        // Это нарушает DIP - высокоуровневый модуль (GameDesktopLauncher) зависит от низкоуровневого (InputController).
        // РЕШЕНИЕ: Нужно создать интерфейс (например InputHandler), который будет абстрагировать работу с вводом.
        if (inputController.isActionPressed(InputController.Action.SHOOT)) {
            Gdx.app.log("Input", "Shoot button pressed");
            playerTank.shoot();
        }

        if (movementDirection != null) {
            playerTank.tryMove(movementDirection, collidables);
        }

        aiController.update();

        float deltaTime = Gdx.graphics.getDeltaTime();
        for (GameObject gameObject : renderableObjects) {
            gameObject.update(deltaTime);
        }

        updateBullets();
    }

    private void updateBullets() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(Gdx.graphics.getDeltaTime());

            if (checkBulletCollision(bullet)) {
                observableLevel.notifyObjectRemoved(bullet);
            }
        }
    }

    private boolean checkBulletCollision(Bullet bullet) {
        if (bullet.isDestroyed()) {
            return true;
        }

        if (!bullet.hasBeenRendered()) {
            return false;
        }

        GridPoint2 bulletPos = bullet.getPosition();

        for (Collidable collidable : collidables) {
            if (collidable == bullet.getOwner()) {
                continue;
            }
            if (collidable.getPosition().equals(bulletPos)) {
                if (collidable instanceof Tank) {
                    Tank tank = (Tank) collidable;
                    tank.takeDamage(bullet.getDamage());
                    Gdx.app.log("Collision", "Bullet hit tank! Health: " + tank.getHealth());
                    if (!tank.isAlive()) {
                        // Танк уничтожен - удаляем его
                        Gdx.app.log("Collision", "Tank destroyed!");
                        observableLevel.notifyObjectRemoved(tank);
                    }
                }
                bullet.destroy();
                return true;
            }
        }

        return false;
    }

    private void renderGame() {
        levelRenderer.render();
        batch.begin();

        for (GameObject gameObject : renderableObjects) {
            gameObject.render(batch);
        }
        batch.end();
        updateBullets();
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
        batch.dispose();
        level.dispose();
        if (bulletTexture != null) {
            bulletTexture.dispose();
        }

        if (context != null) {
            context.close();
        }

        for (GameObject obj : renderableObjects) {
            if (obj instanceof HealthBarDecorator) {
                HealthBarDecorator decorator = (HealthBarDecorator) obj;
                decorator.getTank().getGraphics().getTexture().dispose();
            } else if (obj instanceof Obstacle) {
                ((Obstacle) obj).getGraphics().getTexture().dispose();
            }
        }
    }


    // Нарушение SRP
    // Реализация методов LevelObserver в этом же классе (часть проблемы)
    // РЕШЕНИЕ: Создать отдельный класс GameLevelObserver, который будет имплементировать интерфейс LevelObserver.
    // Его как поле будет хранить GameDesktopLauncher и при необходимости дергать его методы.
    @Override
    public void objectAdded(GameObject object) {
        if (object instanceof Bullet) {
            Bullet bullet = (Bullet) object;
            bullets.add(bullet);
            renderableObjects.add(bullet);
            if (object instanceof Collidable) {
                collidables.add((Collidable) object);
            }
        }
    }

    @Override
    public void objectRemoved(GameObject object) {
        if (object instanceof Bullet) {
            Bullet bullet = (Bullet) object;
            bullets.remove(bullet);
            renderableObjects.remove(bullet);
            if (object instanceof Collidable) {
                collidables.remove((Collidable) object);
            }
            Gdx.app.log("Observer", "Bullet removed at position: " + bullet.getPosition() +
                    ". Total objects now: " + renderableObjects.size());
        } else if (object instanceof Tank) {
            Tank tank = (Tank) object;
            renderableObjects.removeIf(obj -> {
                if (obj instanceof HealthBarDecorator) {
                    return ((HealthBarDecorator) obj).getTank() == tank;
                }
                return obj == tank;
            });
            collidables.remove(tank);
            aiTanks.remove(tank);

            healthDecorators.removeIf(decorator -> decorator.getTank() == tank);
        }
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);

        CommandLineArgs parsedArgs = parseCommandLineArgs(args);

        System.out.println("Level generation mode: " + 
            (parsedArgs.useRandomGeneration ? "RANDOM" : "FILE: " + parsedArgs.levelFilePath));

        GameDesktopLauncher game = new GameDesktopLauncher(
            parsedArgs.useRandomGeneration, 
            parsedArgs.levelFilePath
        );

        new Lwjgl3Application(game, config);
    }

    private static class CommandLineArgs {
        boolean useRandomGeneration = true;
        String levelFilePath = "levels/level1.lvl";
    }

    private static CommandLineArgs parseCommandLineArgs(String[] args) {
        CommandLineArgs result = new CommandLineArgs();

        if (args.length == 0) {
            return result;
        }

        List<String> argsList = Arrays.asList(args);

        if (argsList.contains("--random")) {
            result.useRandomGeneration = true;
            System.out.println("Using random level generation");
        }

        int fileIndex = argsList.indexOf("--file");
        if (fileIndex != -1 && fileIndex + 1 < argsList.size()) {
            result.useRandomGeneration = false;
            result.levelFilePath = argsList.get(fileIndex + 1);
            System.out.println("Loading level from file: " + result.levelFilePath);
        }

        if (argsList.contains("--help") || argsList.contains("-h")) {
            printHelp();
            System.exit(0);
        }

        return result;
    }

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