package ru.mipt.bit.platformer.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.config.SpringConfig;
import ru.mipt.bit.platformer.controller.InputHandler;
import ru.mipt.bit.platformer.controller.LevelController;
import ru.mipt.bit.platformer.controller.LevelControllerFactory;
import ru.mipt.bit.platformer.levelloaders.LevelGenerator;

import java.util.Queue;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

/**
 * Точка входа в игру
 * SRP: ТОЛЬКО управление игровым циклом LibGDX
 * Делегирует всю логику Controllers
 */
@Component
public class GameDesktopLauncher implements ApplicationListener {
    
    private Batch batch;
    private LevelController levelController;
    private InputHandler inputHandler;
    private LevelControllerFactory levelControllerFactory;
    private LevelGenerator levelGenerator;
    private AnnotationConfigApplicationContext context;
    
    // Для приватного использования
    private boolean useRandomGeneration;
    private String levelFilePath;
    
    public GameDesktopLauncher() {
        this(true, "levels/level1.lvl");
    }
    
    public GameDesktopLauncher(boolean useRandomGeneration, String levelFilePath) {
        this.useRandomGeneration = useRandomGeneration;
        this.levelFilePath = levelFilePath;
    }
    
    @Override
    public void create() {
        // DIP: Spring инъекция
        context = new AnnotationConfigApplicationContext(SpringConfig.class);
        
        levelControllerFactory = context.getBean(LevelControllerFactory.class);
        inputHandler = context.getBean(InputHandler.class);
        
        if (useRandomGeneration) {
            levelGenerator = context.getBean("randomLevelGenerator", 
                LevelGenerator.class);
        } else {
            levelGenerator = context.getBean("fileLevelGenerator",
                LevelGenerator.class);
        }
        
        batch = new SpriteBatch();
        
        // Загрузить уровень с Tiled
        TiledMap tiledMap = new TmxMapLoader().load("level.tmx");
        
        // Создать LevelController (делегирует всю работу)
        levelController = levelGenerator.generateLevel(
            tiledMap, batch, levelControllerFactory);
        
        Gdx.app.log("GameDesktopLauncher", 
            "Level generation mode: " + 
            (useRandomGeneration ? "RANDOM" : "FILE - " + levelFilePath));
    }
    
    @Override
    public void render() {
        clearScreen();
        
        // Обработать ввод
        Queue<Command> commands = inputHandler.handleInput(
            levelController.getTankController(),
            levelController.getBotControllers());
        
        levelController.handleCommands(commands);
        
        // Обновить
        float deltaTime = Gdx.graphics.getDeltaTime();
        levelController.update(deltaTime);
        
        // Отрисовать
        levelController.render(batch);
    }
    
    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }
    
    @Override
    public void resize(int width, int height) {
        // Не реагируем на изменение размера окна
    }
    
    @Override
    public void pause() {
        // Игра не паузируется
    }
    
    @Override
    public void resume() {
        // Игра не возобновляется
    }
    
    @Override
    public void dispose() {
        if (levelController != null) {
            levelController.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        if (context != null) {
            context.close();
        }
    }
    
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = 
            new Lwjgl3ApplicationConfiguration();
        
        config.setWindowedMode(1280, 1024);
        
        CommandLineArgs parsedArgs = parseCommandLineArgs(args);
        GameDesktopLauncher game = new GameDesktopLauncher(
            parsedArgs.useRandomGeneration,
            parsedArgs.levelFilePath);
        
        new Lwjgl3Application(game, config);
    }
    
    private static class CommandLineArgs {
        boolean useRandomGeneration = true;
        String levelFilePath = "levels/level1.lvl";
    }
    
    private static CommandLineArgs parseCommandLineArgs(String[] args) {
        CommandLineArgs result = new CommandLineArgs();
        
        if (args.length == 0) return result;
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--random")) {
                result.useRandomGeneration = true;
                System.out.println("Using random level generation");
            } else if (args[i].equals("--file") && i + 1 < args.length) {
                result.useRandomGeneration = false;
                result.levelFilePath = args[i + 1];
                System.out.println("Loading level from: " + result.levelFilePath);
            } else if (args[i].equals("--help") || args[i].equals("-h")) {
                printHelp();
                System.exit(0);
            }
        }
        
        return result;
    }
    
    private static void printHelp() {
        System.out.println("Tank Game - Command Line Options");
        System.out.println("  --random              Generate random level (default)");
        System.out.println("  --file <path>         Load level from file");
        System.out.println("  --help, -h            Show this help message");
    }
}
