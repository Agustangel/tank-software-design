package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.GdxGameUtils;

public abstract class Entity {
  protected TextureRegion graphics;
  protected GridPoint2 coordinates;
  protected Rectangle bounds;

  public Entity(TextureRegion graphics, GridPoint2 startCoords) {
    this.graphics = graphics;
    this.coordinates = new GridPoint2(startCoords);
    this.bounds = GdxGameUtils.createBoundingRectangle(graphics);
  }

  public Rectangle getBounds() {
    return bounds;
  }

  public GridPoint2 getCoordinates() {
    return coordinates;
  }

  public abstract void render(Batch batch);
}
