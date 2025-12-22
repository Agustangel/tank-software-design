package ru.mipt.bit.platformer.levelloaders;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;

import ru.mipt.bit.platformer.controller.LevelController;
import ru.mipt.bit.platformer.controller.LevelControllerFactory;
import ru.mipt.bit.platformer.model.Level;

/**
 * Интерфейс для генерирования уровней
 * SRP: различные реализации отвечают за свой способ генерирования
 */
public interface LevelGenerator {

    LevelController generateLevel(TiledMap tiledMap, Batch batch,
                                  LevelControllerFactory factory);
    
    String getName();
}
