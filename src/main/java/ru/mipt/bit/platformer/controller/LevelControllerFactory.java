package ru.mipt.bit.platformer.controller;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.graphics.LevelGraphics;

/**
 * Factory для создания LevelController
 */
public class LevelControllerFactory {
    
    public LevelController createLevelController(TiledMap tiledMap,
                                                Level levelModel,
                                                Batch batch) {
        LevelGraphics levelGraphics = new LevelGraphics(
            GdxGameUtils.createSingleLayerMapRenderer(tiledMap, batch));
        return new LevelController(levelModel, levelGraphics);
    }
}
