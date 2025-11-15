package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.GridPoint2;

/**
 * Декоратор для отображения полоски здоровья над танком
 * Реализует шаблон "Декоратор", добавляя новую функциональность (отображение здоровья)
 * к существующему объекту Tank без изменения его структуры
 */
public class HealthBarDecorator implements GameObject {
    private final Tank tank;
    private final ShapeRenderer shapeRenderer;
    private boolean visible;

    public HealthBarDecorator(Tank tank) {
        this.tank = tank;
        this.shapeRenderer = new ShapeRenderer();
        this.visible = false;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void update(float deltaTime) {
        tank.update(deltaTime);
    }

    @Override
    public void render(Batch batch) {
        // Сначала отрисовываем танк
        tank.render(batch);

        // Затем отрисовываем полоску здоровья
        if (visible && tank.isAlive()) {
            renderHealthBar(batch);
        }
    }

    private void renderHealthBar(Batch batch) {
        // Времено завершаем batch для использования ShapeRenderer
        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Rectangle bounds = tank.getBounds();
        float x = bounds.x;
        float y = bounds.y + bounds.height + 5;
        float width = bounds.width;
        float height = 4;

        // Фон полоски (красный)
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);

        // Здоровье (зеленый)
        float healthPercentage = (float) tank.getHealth() / tank.getMaxHealth();
        float healthWidth = width * healthPercentage;
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y, healthWidth, height);

        shapeRenderer.end();

        // Контур (черный)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();

        // Возобновляем batch
        batch.begin();
    }

    @Override
    public GridPoint2 getPosition() {
        return tank.getPosition();
    }

    public Tank getTank() {
        return tank;
    }
}
