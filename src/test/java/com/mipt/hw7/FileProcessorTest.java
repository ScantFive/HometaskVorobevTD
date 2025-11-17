package com.mipt.hw7;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

  @Test
  void testSplitAndMergeFile() throws IOException {
    FileProcessor processor = new FileProcessor();

    // Создать тестовый файл
    Path testFile = Files.createTempFile("test", ".dat");
    byte[] testData = new byte[1500]; // 1.5KB данных
    new Random().nextBytes(testData);
    Files.write(testFile, testData);

    // Разбить на части по 500 байт
    String outputDir = Files.createTempDirectory("parts").toString();
    List<Path> parts = processor.splitFile(testFile.toString(), outputDir, 500);

    // Должно быть 3 части
    assertEquals(3, parts.size(), "Должно быть создано 3 части");

    // Проверить существование каждой части
    for (Path part : parts) {
      assertTrue(Files.exists(part), "Часть должна существовать: " + part);
    }

    // Проверить размеры частей
    assertEquals(500, Files.size(parts.get(0)), "Размер первой части должен быть 500 байт");
    assertEquals(500, Files.size(parts.get(1)), "Размер второй части должен быть 500 байт");
    assertEquals(500, Files.size(parts.get(2)), "Размер третьей части должен быть 500 байт");

    // Объединить обратно
    Path mergedFile = Files.createTempFile("merged", ".dat");
    processor.mergeFiles(parts, mergedFile.toString());

    // Проверить что исходный и объединенный файлы идентичны
    assertArrayEquals(Files.readAllBytes(testFile), Files.readAllBytes(mergedFile));
  }
}