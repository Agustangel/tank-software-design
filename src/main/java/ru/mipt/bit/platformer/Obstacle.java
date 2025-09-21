package ru.mipt.bit.platformer;

import static com.badlogic.gdx.Input.Keys.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.Direction;
import ru.mipt.bit.platformer.Field;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

public class Obstacle extends Entity {
  public Obstacle(TextureRegion graphics, GridPoint2 coords, TiledMapTileLayer layer) {
    super(graphics, coords);
    GdxGameUtils.moveRectangleAtTileCenter(layer, bounds, coords);
  }

  @Override
  public void render(Batch batch) {
    GdxGameUtils.drawTextureRegionUnscaled(batch, graphics, bounds, 0f);
  }
}
