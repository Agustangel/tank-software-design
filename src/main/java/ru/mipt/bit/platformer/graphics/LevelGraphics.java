package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Graphics для уровня
 * Отвечает за отрисовку карты
 */
public class LevelGraphics implements Graphical {
    private final MapRenderer mapRenderer;
    
    public LevelGraphics(MapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }
    
    @Override
    public void render(Batch batch) {
        mapRenderer.render();
    }
    
    @Override
    public GridPoint2 getPosition() {
        return new GridPoint2(0, 0);
    }
    
    @Override
    public void updateVisuals() {
        // Уровень не обновляется визуально
    }
}
