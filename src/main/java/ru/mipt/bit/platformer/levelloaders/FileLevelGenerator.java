package ru.mipt.bit.platformer.levelloaders;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.GridPoint2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.mipt.bit.platformer.controller.LevelController;
import ru.mipt.bit.platformer.controller.LevelControllerFactory;
import ru.mipt.bit.platformer.model.Level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Генератор уровней из файла
 */
@Component
public class FileLevelGenerator implements LevelGenerator {
    
    private final String filePath;
    
    public FileLevelGenerator(@Value("${game.level.file:levels/level1.lvl}") 
                             String filePath) {
        this.filePath = filePath;
    }
    
    @Override
    public LevelController generateLevel(TiledMap tiledMap, Batch batch,
                                        LevelControllerFactory factory) {
        try {
            List<String> lines = readLevelFile();
            Level levelModel = parseLevelData(lines);
            return factory.createLevelController(tiledMap, levelModel, batch);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load level from file: " 
                + filePath, e);
        }
    }
    
    private List<String> readLevelFile() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
    
    private Level parseLevelData(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Level file is empty");
        }
        
        String[] dimensions = lines.get(0).split(",");
        int width = Integer.parseInt(dimensions[0].trim());
        int height = Integer.parseInt(dimensions[1].trim());
        
        Level levelModel = new Level(width, height, 
                                              new GridPoint2(1, 1));
        
        for (int i = 1; i < lines.size(); i++) {
            String[] coords = lines.get(i).split(",");
            int x = Integer.parseInt(coords[0].trim());
            int y = Integer.parseInt(coords[1].trim());
            levelModel.addObstacle(new GridPoint2(x, y));
        }
        
        return levelModel;
    }
    
    @Override
    public String getName() {
        return "FileLevelGenerator";
    }
}
