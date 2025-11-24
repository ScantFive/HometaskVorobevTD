package com.mipt.hw7;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
  public List<Path> splitFile(String sourcePath, String outputDir, int partSize) throws IOException {
    if (partSize <= 0) {
      throw new IllegalArgumentException("partSize can`t be less than 1");
    }

    Path source = Paths.get(sourcePath);
    if (!Files.exists(source)) {
      throw new IOException("File not found: " + source);
    }
    Path outputDirectory = Paths.get(outputDir);
    Files.createDirectories(outputDirectory);

    String fileName = source.getFileName().toString();
    String baseName = fileName.substring(0, fileName.lastIndexOf('.'));

    List<Path> partPaths = new ArrayList<>();

    try (FileChannel sourceChannel = FileChannel.open(source)) {
      long fileSize = sourceChannel.size();
      long position = 0;
      int partNumber = 1;

      ByteBuffer byteBuffer = ByteBuffer.allocate(partSize);

      while (position < fileSize) {
        int bytesToRead = (int) Math.min(partSize, fileSize - position);

        String partFileName = baseName + ".part" + partNumber;
        Path partPath = outputDirectory.resolve(partFileName);
        partPaths.add(partPath);

        try (FileChannel partChannel = FileChannel.open(
          partPath,
          StandardOpenOption.CREATE,
          StandardOpenOption.WRITE,
          StandardOpenOption.TRUNCATE_EXISTING)) {

          sourceChannel.position(position);
          int bytesRead = sourceChannel.read(byteBuffer);
          if (bytesRead == -1) break;

          byteBuffer.flip();

          partChannel.write(byteBuffer);

          byteBuffer.clear();
        }

        position += bytesToRead;
        partNumber++;
      }
    }
    return partPaths;
  }

  public void mergeFiles(List<Path> partPaths, String outputPath) throws IOException {
    for (Path part : partPaths) {
      if (!Files.exists(part)) {
        throw new IOException("Part does not exist: " + part);
      }
    }


    Path output = Paths.get(outputPath);
    Files.createDirectories(output.getParent());

    try (FileChannel outputChannel = FileChannel.open(
      output,
      StandardOpenOption.CREATE,
      StandardOpenOption.WRITE,
      StandardOpenOption.TRUNCATE_EXISTING)) {

      for (Path partPath : partPaths) {
        try (FileChannel partChannel = FileChannel.open(partPath, StandardOpenOption.READ)) {
          partChannel.transferTo(0, partChannel.size(), outputChannel);
        }
      }
    }
  }
}
