package com.mipt.hw7;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

class TextFileAnalyzerTest {

  @Test
  @DisplayName("Анализ файла с подсчетом строк, слов, символов и частоты")
  void testAnalyzeFile() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = Files.createTempFile("test", ".txt");
    Files.write(testFile, Arrays.asList("Java test", "Simple file", "Hello"));

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(3, result.getLineCount());
    assertEquals(5, result.getWordCount());
    assertEquals(25, result.getCharCount());

    Map<Character, Long> frequency = result.getCharacterFrequency();
    assertEquals(4, frequency.get('e').longValue());
    assertEquals(2, frequency.get(' ').longValue());
    assertEquals(4, frequency.get('l').longValue());
    assertEquals(1, frequency.get('J').longValue());
    assertEquals(2, frequency.get('a').longValue());
    assertEquals(1, frequency.get('H').longValue());
    assertEquals(2, frequency.get('i').longValue());

    Files.deleteIfExists(testFile);
  }

  @Test
  @DisplayName("Анализ пустого файла")
  void testAnalyzeEmptyFile() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = Files.createTempFile("empty", ".txt");
    Files.write(testFile, Arrays.asList(""));

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(1, result.getLineCount(), "Пустой файл должен иметь 1 строку");
    assertEquals(0, result.getWordCount(), "В пустом файле не должно быть слов");
    assertEquals(0, result.getCharCount(), "В пустой строке не должно быть символов");
    assertTrue(result.getCharacterFrequency().isEmpty(), "Частота символов должна быть пустой");

    Files.deleteIfExists(testFile);
  }

  @Test
  @DisplayName("Анализ файла с пробелами и специальными символами")
  void testAnalyzeFileWithSpecialCharacters() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = Files.createTempFile("special", ".txt");
    Files.write(testFile, Arrays.asList("  Multiple   spaces  ", "Tab\tseparated", "Punctuation!@#"));

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(3, result.getLineCount());
    assertEquals(5, result.getWordCount());
    assertTrue(result.getCharCount() > 0);

    Files.deleteIfExists(testFile);
  }

  @Test
  @DisplayName("Сохранение результата анализа в файл")
  void testSaveAnalysisResult() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(2, 5, 20);

    Path outputFile = Files.createTempFile("analysis", ".txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    assertTrue(Files.exists(outputFile), "Файл должен существовать");
    assertTrue(Files.size(outputFile) > 0, "Размер файла должен быть больше 0");

    String content = Files.readString(outputFile);
    assertTrue(content.contains("lineCount=2"), "Должно содержать lineCount=2");
    assertTrue(content.contains("wordCount=5"), "Должно содержать wordCount=5");
    assertTrue(content.contains("charCount=20"), "Должно содержать charCount=20");
    assertTrue(content.contains("AnalysisResult{"), "Должно содержать начало строки результата");

    Files.deleteIfExists(outputFile);
  }

  @Test
  @DisplayName("Сохранение результата с частотой символов")
  void testSaveAnalysisResultWithFrequency() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Map<Character, Long> frequency = Map.of('a', 3L, 'b', 2L, 'c', 1L);
    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(1, 3, 10, frequency);

    Path outputFile = Files.createTempFile("analysis_freq", ".txt");
    analyzer.saveAnalysisResult(result, outputFile.toString());

    String content = Files.readString(outputFile);
    assertTrue(content.contains("characterFrequency={"), "Должно содержать частоту символов");
    assertTrue(content.contains("a=3") || content.contains("'a'=3"), "Должно содержать a=3");

    Files.deleteIfExists(outputFile);
  }

  @Test
  @DisplayName("Анализ несуществующего файла - должно бросать исключение")
  void testAnalyzeNonExistentFile() {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    assertThrows(IOException.class, () -> {
      analyzer.analyzeFile("nonexistent_file_12345.txt");
    }, "Должно бросать IOException для несуществующего файла");
  }

  @Test
  @DisplayName("Сохранение в недоступную директорию - должно бросать исключение")
  void testSaveToInvalidPath() {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();
    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(1, 1, 1);

    assertThrows(IOException.class, () -> {
      analyzer.saveAnalysisResult(result, "/invalid/path/analysis.txt");
    }, "Должно бросать IOException для недоступного пути");
  }

  @Test
  @DisplayName("Проверка иммутабельности AnalysisResult")
  void testAnalysisResultImmutability() {
    Map<Character, Long> originalFrequency = new java.util.HashMap<>();
    originalFrequency.put('x', 5L);

    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(1, 2, 3, originalFrequency);

    originalFrequency.put('y', 10L);

    assertEquals(1, result.getCharacterFrequency().size(), "Частота символов не должна измениться");
    assertFalse(result.getCharacterFrequency().containsKey('y'), "Не должно содержать добавленный символ");

    Map<Character, Long> retrievedFrequency = result.getCharacterFrequency();
    retrievedFrequency.put('z', 15L);

    assertEquals(1, result.getCharacterFrequency().size(), "Оригинальная частота не должна измениться");
  }

  @Test
  @DisplayName("Анализ файла с разными регистрами символов")
  void testAnalyzeFileWithDifferentCases() throws IOException {
    TextFileAnalyzer analyzer = new TextFileAnalyzer();

    Path testFile = Files.createTempFile("cases", ".txt");
    Files.write(testFile, Arrays.asList("Hello HELLO hello", "Test TEST test"));

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(2, result.getLineCount());
    assertEquals(6, result.getWordCount());

    Map<Character, Long> frequency = result.getCharacterFrequency();
    assertTrue(frequency.get('H') != null, "Должна быть заглавная H");
    assertTrue(frequency.get('h') != null, "Должна быть строчная h");

    Files.deleteIfExists(testFile);
  }
}