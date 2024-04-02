package dev.testtask.telnet.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class SearchService {

  private final ReentrantLock lock;

  public SearchService(ReentrantLock lock) { this.lock = lock; }

  public Future<List<String>> search(String rootPath, int depth, String mask) {
    lock.lock();
    try {
      Queue<File> queue = new ArrayDeque<>();
      List<String> fileList = new ArrayList<>();
      Path path = Path.of(rootPath);
      if (Files.exists(path)) {
        queue.add(new File(rootPath));
      }

      while (!queue.isEmpty()) {
        File current = queue.poll();
        int currentDepth = this.getCurrentDepth(current, rootPath);

        if (currentDepth == depth && current.getName()
            .contains(mask)) {
          fileList.add(current.getAbsolutePath());
        }

        if (currentDepth < depth && current.isDirectory()) {
          File[] files = current.listFiles();
          if (files != null) {
            queue.addAll(Arrays.asList(files));
          }
        }
      }
      return Executors.newCachedThreadPool()
          .submit(() -> fileList);
    } finally {
      lock.unlock();
    }
  }

  private int getCurrentDepth(File current, String rootPath) {
    int depth = -1;
    File parent = current;
    while (!parent.getAbsolutePath()
        .equals(rootPath)) {
      parent = parent.getParentFile();
      depth++;
    }
    return depth;
  }
}
