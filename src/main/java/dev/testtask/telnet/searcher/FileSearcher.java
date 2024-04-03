package dev.testtask.telnet.searcher;

import dev.testtask.telnet.model.ClientRequest;
import dev.testtask.telnet.model.SearchSetting;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class FileSearcher implements Runnable {

  private final BlockingQueue<ClientRequest> blockingQueue;

  public FileSearcher(BlockingQueue<ClientRequest> blockingQueue) {
    this.blockingQueue = blockingQueue;
  }

  @SneakyThrows
  @Override
  public void run() {
    while (true) {
      log.info(Thread.currentThread()
          .getName() + ": ----- FileSearcher");
      ClientRequest clientRequest = blockingQueue.take();
      Queue<File> queue = new ArrayDeque<>();
      SearchSetting searchSetting = clientRequest.searchSetting();
      PrintWriter writer = clientRequest.writer();
      Path path = Path.of(searchSetting.rootPath());

      if (Files.exists(path)) {
        queue.add(new File(searchSetting.rootPath()));
      }

      while (!queue.isEmpty()) {
        File current = queue.poll();
        int currentDepth = this.getCurrentDepth(current, searchSetting.rootPath());

        if (currentDepth == searchSetting.depth() && current.getName()
            .contains(searchSetting.mask())) {
          writer.println("\r");
          writer.println(current.getAbsolutePath());
        }

        if (currentDepth < searchSetting.depth() && current.isDirectory()) {
          File[] files = current.listFiles();
          if (files != null) {
            queue.addAll(Arrays.asList(files));
          }
        }
      }
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
