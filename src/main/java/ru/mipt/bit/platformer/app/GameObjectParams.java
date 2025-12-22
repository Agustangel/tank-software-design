package ru.mipt.bit.platformer.app;

/**
 * Параметры сущностей
 * Группирует все константы в одном месте
 */
public class GameObjectParams {
    
    // Игрок (танк)
    public static final float PLAYER_SPEED = 0.4f;
    public static final int PLAYER_HEALTH = 100;
    
    // Враги (танки)
    public static final float ENEMY_SPEED = 0.35f;
    public static final int ENEMY_HEALTH = 75;
    public static final int ENEMY_COUNT = 3;
    
    // Пули
    public static final float BULLET_SPEED = 1.5f;
    public static final int BULLET_DAMAGE = 25;
    
    // Графика
    public static final String PLAYER_TEXTURE = "images/tank_blue.png";
    public static final String ENEMY_TEXTURE = "images/tank_red.png";
    public static final String OBSTACLE_TEXTURE = "images/green_tree.png";
    public static final String BULLET_TEXTURE = "images/bullet.png";
    
    private GameObjectParams() {
        // Static utility class
    }
}
