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

public class Tank extends Entity {
  private GridPoint2 destination;
  private float rotation;
  private float movementProgress;
  private final TileMovement mover;
  private static final float SPEED = 0.4f;
  private final Field field;

  public Tank(TextureRegion graphics, GridPoint2 startCoords, Field field) {
    super(graphics, startCoords);
    this.destination = new GridPoint2(startCoords);
    this.movementProgress = 1f;
    this.rotation = 0f;
    this.field = field;
    this.mover = new TileMovement(field.getLayer(), Interpolation.smooth);
  }

  public void update(float deltaTime) {
    // Обработка ввода
    if (movementProgress >= 1f) {
      for (Direction dir : Direction.values()) {
        if (Gdx.input.isKeyPressed(dir == Direction.UP
                    ? UP
                    : dir == Direction.DOWN ? DOWN : dir == Direction.LEFT ? LEFT : RIGHT)
            || Gdx.input.isKeyPressed(dir == Direction.UP
                    ? W
                    : dir == Direction.DOWN ? S : dir == Direction.LEFT ? A : D)) {
          attemptMove(dir);
          break;
        }
      }
    }

    // Анимация движения
    mover.moveRectangleBetweenTileCenters(bounds, coordinates, destination, movementProgress);
    movementProgress = GdxGameUtils.continueProgress(movementProgress, deltaTime, SPEED);
    if (movementProgress >= 1f) {
      coordinates.set(destination);
    }
  }

  private void attemptMove(Direction dir) {
    GridPoint2 target = new GridPoint2(coordinates.x + dir.dx, coordinates.y + dir.dy);
    if (field.isWalkable(target)) {
      destination.set(target);
      movementProgress = 0f;
    }
    rotation = dir.rotation;
  }

  @Override
  public void render(Batch batch) {
    GdxGameUtils.drawTextureRegionUnscaled(batch, graphics, bounds, rotation);
  }
}
