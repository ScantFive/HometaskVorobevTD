package com.mipt.hw7;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class TextFileAnalyzerTest {

  @Test
  void testAnalyzeFile() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    // Создаем временный тестовый файл
    Path testFile = Files.createTempFile("test", ".txt");
    Files.write(testFile, Arrays.asList("Hello world!", "This is test."));

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    // Проверки на результат
    assertEquals(2, result.getLineCount(), "Неверное количество строк");
    assertEquals(5, result.getWordCount(), "Неверное количество слов");
    assertEquals(25, result.getCharCount(), "Неверное количество символов");
    assertNotNull(result.getCharacterFrequency(), "Частота символов не должна быть null");
    assertTrue(result.getCharacterFrequency().containsKey('H'), "Должен содержать символ 'H'");
    assertTrue(result.getCharacterFrequency().containsKey('!'), "Должен содержать символ '!'");
  }

  @Test
  void testSaveAnalysisResult() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    // Создаем тестовый результат
    Map<Character, Long> freq = new java.util.HashMap<>();
    freq.put('a', 2L);
    freq.put('b', 1L);
    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(2, 5, 25, freq);

    // Сохранить в файл
    Path outputFile = Files.createTempFile("analysis", ".txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    // Проверки на файл
    assertTrue(Files.size(outputFile) > 0, "Файл должен быть не пустым");

    // Прочитать файл и проверить содержимое
    String fileContent = Files.readString(outputFile);
    assertTrue(fileContent.contains("lineCount=2"), "Файл должен содержать lineCount=2");
    assertTrue(fileContent.contains("wordCount=5"), "Файл должен содержать wordCount=5");
    assertTrue(fileContent.contains("charCount=25"), "Файл должен содержать charCount=25");
    assertTrue(fileContent.contains("characterFrequency={"), "Файл должен содержать частоту символов");
  }

  @Test
  void testAnalyzeFileWithComplexText() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    // Создаём временный файл с многострочным текстом, включая пустые строки и спецсимволы
    Path testFile = Files.createTempFile("complex_test", ".txt");
    Files.write(testFile, Arrays.asList(
      "Java — это классный язык программирования!",
      "",
      "Он поддерживает: ООП, многопоточность, и многое другое.",
      "Символы: 123 @#$%^&*()",
      "   Пробелы в начале и конце строки   "
    ));

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    // Проверки
    assertEquals(5, result.getLineCount(), "Должно быть 5 строк (включая пустую)");
    assertEquals(22, result.getWordCount(), "Должно быть 22 слова");
    assertEquals(156, result.getCharCount(), "Неверное количество символов");

    // Проверка частоты конкретных символов
    Map<Character, Long> freq = result.getCharacterFrequency();
    assertTrue(freq.get(' ') >= 15, "Пробел должен встречаться много раз");
    assertTrue(freq.containsKey('—'), "Должен содержать тире");
    assertTrue(freq.containsKey('!'), "Должен содержать восклицательный знак");
    assertTrue(freq.containsKey('@'), "Должен содержать символ @");
    assertTrue(freq.containsKey('3'), "Должен содержать цифру 3");
    assertTrue(freq.containsKey('J'), "Должен содержать заглавную J");
  }
}