package ru.mipt.bit.platformer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import ru.mipt.bit.platformer.Level;
import ru.mipt.bit.platformer.ObservableLevel;
import ru.mipt.bit.platformer.controller.AIController;
import ru.mipt.bit.platformer.controller.InputController;
import ru.mipt.bit.platformer.levelloaders.LevelGenerator;
import ru.mipt.bit.platformer.levelloaders.RandomLevelGenerator;

/**
 * Конфигурация Spring для неграфических компонентов
 * которые можно создать до инициализации LibGDX
 */
@Configuration
@PropertySource("classpath:application.properties")
public class SpringConfig {

    @Autowired
    private Environment env;

    /**
     * Необходим для работы с properties файлами
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Генератор уровней - настраивается через properties
     */
    @Bean
    public LevelGenerator levelGenerator() {
        int width = env.getProperty("level.width", Integer.class, 10);
        int height = env.getProperty("level.height", Integer.class, 8);
        float density = env.getProperty("level.obstacle.density", Float.class, 0.25f);
        
        return new RandomLevelGenerator(width, height, density);
    }

    /**
     * Данные уровня, сгенерированные выбранным генератором
     */
    @Bean
    public Level level(LevelGenerator levelGenerator) {
        return levelGenerator.generateLevel();
    }

    /**
     * Наблюдаемый уровень для системы событий
     */
    @Bean
    public ObservableLevel observableLevel() {
        return new ObservableLevel();
    }

    /**
     * Контроллер ввода игрока
     */
    @Bean
    public InputController inputController() {
        return new InputController();
    }

    /**
     * Контроллер ИИ для управления AI-танками
     * Зависит от настроек уровня и системы событий
     */
    @Bean
    public AIController aiController(ObservableLevel observableLevel) {
        int levelWidth = env.getProperty("level.width", Integer.class, 10);
        int levelHeight = env.getProperty("level.height", Integer.class, 8);
        
        return new AIController(new java.util.ArrayList<>(), 
                              new java.util.ArrayList<>(), 
                              levelWidth, levelHeight);
    }
}
