package ru.mipt.bit.platformer;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.math.MathUtils.isEqual;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import java.util.List;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

public class GameDesktopLauncher implements ApplicationListener {
  private static final int WINDOW_WIDTH = 1280, WINDOW_HEIGHT = 1024;

  private SpriteBatch batch;
  private TiledMap map;
  private MapRenderer mapRenderer;
  private Field field;
  private Tank player;
  private Obstacle tree;

  @Override
  public void create() {
    batch = new SpriteBatch();

    map = new TmxMapLoader().load("level.tmx");
    TiledMapTileLayer groundLayer = GdxGameUtils.getSingleLayer(map);
    mapRenderer = GdxGameUtils.createSingleLayerMapRenderer(map, batch);

    // Инициализация поля с препятствиями
    List<GridPoint2> obstacles = List.of(new GridPoint2(1, 3));
    field = new Field(groundLayer, obstacles);

    // Создание сущностей
    TextureRegion tankTex = new TextureRegion(new Texture("images/tank_blue.png"));
    player = new Tank(tankTex, new GridPoint2(1, 1), field);

    TextureRegion treeTex = new TextureRegion(new Texture("images/greenTree.png"));
    tree = new Obstacle(treeTex, new GridPoint2(1, 3), groundLayer);
  }

  @Override
  public void render() {
    float dt = Gdx.graphics.getDeltaTime();
    Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
    Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

    player.update(dt);

    mapRenderer.render();
    batch.begin();
    player.render(batch);
    tree.render(batch);
    batch.end();
  }

  @Override
  public void resize(int w, int h) {}
  @Override
  public void pause() {}
  @Override
  public void resume() {}
  @Override
  public void dispose() {
    map.dispose();
    batch.dispose();
    // dispose textures inside entities if нужно
  }

  public static void main(String[] args) {
    Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
    cfg.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
    new Lwjgl3Application(new GameDesktopLauncher(), cfg);
  }
}
