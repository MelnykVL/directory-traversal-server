package dev.testtask.telnet.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchServiceTest {

  private static final String RELATIVE_PATH_TO_RESOURCES = "src/test/resources";

  private final SearchService searchService = new SearchService(new ReentrantLock());

  @Test
  @SneakyThrows
  void search_givenNonExistentPath_shouldReturnEmptyList() {
    String absolutePathToResources = Path.of(RELATIVE_PATH_TO_RESOURCES)
        .toFile()
        .getAbsolutePath();
    List<String> fileList = this.searchService.search("non-existent", 0, "no-matter")
        .get();
    assertTrue(fileList.isEmpty());
  }

  @Test
  @SneakyThrows
  void search_GivenExistentPathWithDepthTwoAndMaskDir_ShouldListWithTwoDirs() {
    String absolutePathToResources = Path.of(RELATIVE_PATH_TO_RESOURCES)
        .toFile()
        .getAbsolutePath();
    List<String> fileList = this.searchService.search(absolutePathToResources, 1, "dir")
        .get();
    assertEquals(2, fileList.size());
  }

  @Test
  @SneakyThrows
  void search_GivenExistentPathWithDepthTwoAndMaskTest_ShouldListWithTwoFiles() {
    String absolutePathToResources = Path.of(RELATIVE_PATH_TO_RESOURCES)
        .toFile()
        .getAbsolutePath();
    List<String> fileList = this.searchService.search(absolutePathToResources, 2, "test")
        .get();
    assertEquals(2, fileList.size());
  }
}