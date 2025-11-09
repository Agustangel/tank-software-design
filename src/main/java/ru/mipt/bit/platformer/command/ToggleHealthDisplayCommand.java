package ru.mipt.bit.platformer;

import java.util.List;

/**
 * Конкретная команда для переключения отображения полосок здоровья
 */
public class ToggleHealthDisplayCommand implements Command {
    private final List<HealthBarDecorator> healthDecorators;

    public ToggleHealthDisplayCommand(List<HealthBarDecorator> healthDecorators) {
        this.healthDecorators = healthDecorators;
    }

    @Override
    public void execute() {
        // Переключаем видимость здоровья для всех декораторов
        for (HealthBarDecorator decorator : healthDecorators) {
            decorator.setVisible(!decorator.isVisible());
        }
    }
}