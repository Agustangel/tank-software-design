package ru.mipt.bit.platformer.api;

import java.util.Optional;
import ru.mipt.bit.platformer.model.BulletModel;

/**
 * Интерфейс для объектов, которые могут стрелять
 */
public interface Shootable {
    Optional<BulletModel> shoot();
}
