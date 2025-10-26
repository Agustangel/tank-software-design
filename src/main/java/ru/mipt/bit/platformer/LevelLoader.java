package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.GridPoint2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Загрузчик уровней из файлов
 */
public class LevelLoader {

  /**
   * Загружает уровень из текстового файла
   * Формат: T - дерево, X - игрок, _ - пустота
   */
  public static LevelData loadFromFile(String filePath) {
    try {
      FileHandle file = Gdx.files.internal(filePath);

      if (!file.exists()) {
        // Попробуем найти файл в других местах
        file = Gdx.files.classpath(filePath);
        if (!file.exists()) {
          throw new RuntimeException("File not found: " + filePath +
                                     " (tried as internal and classpath)");
        }
      }

      Gdx.app.log("LevelLoader", "Loading level from: " + file.path());
      String content = file.readString();
      return parseLevelContent(content);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load level from: " + filePath, e);
    }
  }

  private static LevelData parseLevelContent(String content) {
    String[] lines = content.split("\\r?\\n");
    List<GridPoint2> obstacles = new ArrayList<>();
    GridPoint2 playerStart = null;

    int height = lines.length;
    int width = 0;

    // Убедимся, что у нас есть хотя бы одна строка
    if (height == 0) {
      throw new RuntimeException("Level file is empty");
    }

    for (int y = height - 1; y >= 0; y--) {
      String line = lines[height - 1 - y].trim();
      if (line.isEmpty())
        continue; // Пропускаем пустые строки

      width = Math.max(width, line.length());

      for (int x = 0; x < line.length(); x++) {
        char cell = line.charAt(x);
        switch (cell) {
        case 'T':
          obstacles.add(new GridPoint2(x, y));
          break;
        case 'X':
          if (playerStart != null) {
            throw new RuntimeException(
                "Multiple player start positions (X) found");
          }
          playerStart = new GridPoint2(x, y);
          break;
        case '_':
          // Пустая клетка - ничего не делаем
          break;
        default:
          throw new RuntimeException("Unknown cell character: '" + cell +
                                     "' at position (" + x + "," + y + ")");
        }
      }
    }

    if (playerStart == null) {
      throw new RuntimeException(
          "No player start position (X) found in level file");
    }

    Gdx.app.log("LevelLoader", "Loaded level: " + width + "x" + height +
                                   ", player at " + playerStart +
                                   ", obstacles: " + obstacles.size());

    return new LevelData(playerStart, obstacles, width, height);
  }
}