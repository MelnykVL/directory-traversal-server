package dev.testtask.telnet.service;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchService {

    public Future<List<String>> search(String rootPath, int depth, String mask) {
        Queue<File> queue = new ArrayDeque<>();
        List<String> fileList = new ArrayList<>();
        queue.add(new File(rootPath));

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
                    for (File file : files) {
                        queue.add(file);
                    }
                }
            }
        }
        return Executors.newCachedThreadPool()
                .submit(() -> fileList);
    }

    private int getCurrentDepth(File current, String rootPath) {
        int depth = 0;
        File parent = current;
        while (!parent.getAbsolutePath()
                .equals(rootPath)) {
            parent = parent.getParentFile();
            depth++;
        }
        return depth;
    }
}
