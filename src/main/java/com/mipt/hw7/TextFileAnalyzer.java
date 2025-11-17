package com.mipt.hw7;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TextFileAnalyzer {

  public static class AnalysisResult {
    private final long lineCount;
    private final long wordCount;
    private final long charCount;
    private final Map<Character, Long> characterFrequency;

    // Конструктор с частотой символов
    public AnalysisResult(long lineCount, long wordCount, long charCount, Map<Character, Long> characterFrequency) {
      this.lineCount = lineCount;
      this.wordCount = wordCount; // ✅ Исправлено
      this.charCount = charCount; // ✅ Исправлено
      this.characterFrequency = characterFrequency != null ? new HashMap<>(characterFrequency) : new HashMap<>();
    }

    // Конструктор без частоты (для тестов)
    public AnalysisResult(long lineCount, long wordCount, long charCount) {
      this(lineCount, wordCount, charCount, new HashMap<>());
    }

    public long getLineCount() { return lineCount; }
    public long getWordCount() { return wordCount; }
    public long getCharCount() { return charCount; }
    public Map<Character, Long> getCharacterFrequency() { return new HashMap<>(characterFrequency); }

    @Override
    public String toString() {
      return "AnalysisResult{" +
        "lineCount=" + lineCount +
        ", wordCount=" + wordCount +
        ", charCount=" + charCount +
        ", characterFrequency=" + characterFrequency +
        '}';
    }
  }

  // Метод анализа — ВНЕ класса AnalysisResult!
  public AnalysisResult analyzeFile(String path) throws IOException {
    long lineCount = 0;
    long wordCount = 0;
    long charCount = 0;
    Map<Character, Long> charFrequency = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lineCount++;
        charCount += line.length();
        String[] words = line.split("\\s+");
        for (String word : words) {
          if (!word.isEmpty()) {
            wordCount++;
          }
        }
        for (char c : line.toCharArray()) {
          charFrequency.merge(c, 1L, Long::sum);
        }
      }
    }
    return new AnalysisResult(lineCount, wordCount, charCount, charFrequency);
  }

  // Метод сохранения — ВНЕ класса AnalysisResult!
  public void saveAnalysisResult(AnalysisResult result, String path) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      writer.write(result.toString());
    }
  }
}