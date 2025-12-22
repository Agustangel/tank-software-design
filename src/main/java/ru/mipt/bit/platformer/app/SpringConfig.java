package ru.mipt.bit.platformer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import ru.mipt.bit.platformer.controller.GameObjectControllerFactory;
import ru.mipt.bit.platformer.controller.LevelControllerFactory;
import ru.mipt.bit.platformer.levelloaders.LevelGenerator;
import ru.mipt.bit.platformer.levelloaders.FileLevelGenerator;
import ru.mipt.bit.platformer.levelloaders.RandomLevelGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Spring конфигурация
 * DIP: все зависимости создаются здесь
 */
@Configuration
@ComponentScan("ru.mipt.bit.platformer")
@PropertySource("classpath:application.properties")
public class SpringConfig {
    
    @Bean
    public ShapeRenderer shapeRenderer() {
        return new ShapeRenderer();
    }
    
    @Bean
    public GameObjectControllerFactory GameObjectControllerFactory() {
        return new GameObjectControllerFactory(32f, 32f, shapeRenderer());
    }
    
    @Bean
    public LevelControllerFactory levelControllerFactory() {
        return new LevelControllerFactory();
    }
    
    @Bean
    public LevelGenerator randomLevelGenerator() {
        return new RandomLevelGenerator();
    }
    
    @Bean
    public LevelGenerator fileLevelGenerator() {
        return new FileLevelGenerator("levels/level1.lvl");
    }
}
