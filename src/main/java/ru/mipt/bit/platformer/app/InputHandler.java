package ru.mipt.bit.platformer.app;

import com.badlogic.gdx.Gdx;
import ru.mipt.bit.platformer.command.*;
import ru.mipt.bit.platformer.controller.GameObjectController;
import ru.mipt.bit.platformer.controller.TankController;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * InputHandler - обработка ввода
 * SRP: ТОЛЬКО обнаружение ввода и создание Commands
 * НЕ делает: логику, обновление, отрисовку
 */
@Component
public class InputHandler {
    
    /**
     * Обработать ввод
     * @return очередь команд для выполнения
     */
    public Queue<Command> handleInput(TankController tankController,
                                     Set<GameObjectController> botControllers) {
        Queue<Command> commands = new ArrayDeque<>();
        
        handleLevelInput(commands, tankController, botControllers);
        handleTankInput(commands, tankController);
        
        // AI бот вводит команды случайно
        botControllers.forEach(botController ->
            handleBotInput(commands, botController));
        
        return commands;
    }
    
    /**
     * Обработать общий ввод уровня (L - показать/скрыть здоровье)
     */
    private void handleLevelInput(Queue<Command> commands,
                                 TankController tankController,
                                 Set<GameObjectController> botControllers) {
        if (isJustPressed(L)) {
            if (tankController != null) {
                commands.add(new ToggleHealthBarCommand(tankController));
            }
            botControllers.forEach(botController ->
                commands.add(new ToggleHealthBarCommand(botController)));
        }
    }
    
    /**
     * Обработать ввод танка (WASD + Space)
     */
    private void handleTankInput(Queue<Command> commands,
                                  TankController tankController) {
        if (tankController == null) return;
        
        if (isJustPressed(W, UP)) {
            commands.add(new MoveUpCommand(tankController));
        } else if (isJustPressed(S, DOWN)) {
            commands.add(new MoveDownCommand(tankController));
        } else if (isJustPressed(A, LEFT)) {
            commands.add(new MoveLeftCommand(tankController));
        } else if (isJustPressed(D, RIGHT)) {
            commands.add(new MoveRightCommand(tankController));
        }
        
        if (isJustPressed(SPACE)) {
            commands.add(new ShootCommand(tankController));
        }
    }
    
    /**
     * Обработать ввод AI бота (случайные команды)
     */
    private void handleBotInput(Queue<Command> commands,
                               GameObjectController botController) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        double randomValue = random.nextDouble();
        
        // 50% вероятность ничего не делать
        if (randomValue < 0.5) return;
        
        // 25% вероятность двигаться в случайном направлении
        if (randomValue < 0.75) {
            int moveType = random.nextInt(4);
            switch (moveType) {
                case 0 -> commands.add(new MoveUpCommand(botController));
                case 1 -> commands.add(new MoveDownCommand(botController));
                case 2 -> commands.add(new MoveLeftCommand(botController));
                case 3 -> commands.add(new MoveRightCommand(botController));
            }
        } else {
            // 25% вероятность стрелять
            commands.add(new ShootCommand(botController));
        }
    }
    
    /**
     * Проверить, были ли нажаты клавиши
     */
    private boolean isJustPressed(int... keys) {
        for (int key : keys) {
            if (Gdx.input.isKeyJustPressed(key)) {
                return true;
            }
        }
        return false;
    }
}
