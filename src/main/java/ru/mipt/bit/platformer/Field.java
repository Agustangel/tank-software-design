package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import java.util.List;

public class Field {
  private final TiledMapTileLayer groundLayer;
  private final List<GridPoint2> obstacles;

  public Field(TiledMapTileLayer groundLayer, List<GridPoint2> obstacleCoords) {
    this.groundLayer = groundLayer;
    this.obstacles = obstacleCoords;
  }

  public boolean isWalkable(GridPoint2 coords) {
    return !obstacles.contains(coords);
  }

  public TiledMapTileLayer getLayer() {
    return groundLayer;
  }
}
