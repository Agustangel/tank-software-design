package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Graphics для пули
 */
public class BulletGraphics extends GameObjectGraphics {
    public BulletGraphics(TextureRegion texture, GridPoint2 position,
                     float tileWidth, float tileHeight) {
        super(texture, position, tileWidth, tileHeight);
    }
    
    @Override
    public void render(Batch batch) {
        super.render(batch);
    }
    
    @Override
    public void updateVisuals() {
        // Логика визуализации пули
    }
}
