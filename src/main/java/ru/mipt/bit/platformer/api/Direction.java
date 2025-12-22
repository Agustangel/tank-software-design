package ru.mipt.bit.platformer.api;

/**
 * Направление движения
 */
public enum Direction {
    UP(0, 1, 90),
    DOWN(0, -1, 270),
    LEFT(-1, 0, 180),
    RIGHT(1, 0, 0);
    
    public final int dx;
    public final int dy;
    public final int rotation;
    
    Direction(int dx, int dy, int rotation) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
    }
    
    public static Direction fromRotation(int rotation) {
        return switch (rotation) {
            case 90 -> UP;
            case 270 -> DOWN;
            case 180 -> LEFT;
            default -> RIGHT;
        };
    }
}
