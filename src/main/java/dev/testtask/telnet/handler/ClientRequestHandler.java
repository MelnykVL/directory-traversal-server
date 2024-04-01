package dev.testtask.telnet.handler;

import dev.testtask.telnet.model.SearchSetting;
import dev.testtask.telnet.service.SearchService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

@Slf4j
public class ClientRequestHandler implements Runnable {

  private final Socket clientSocket;
  private final SearchService searchService;

  public ClientRequestHandler(Socket clientSocket, SearchService searchService) {
    this.clientSocket = clientSocket;
    this.searchService = searchService;
  }

  @SneakyThrows
  @Override
  public void run() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        clientSocket) {

      boolean proceed = true;
      while (proceed) {
        SearchSetting searchSetting = this.askSearchSetting(reader, writer);

        List<String> fileList = searchService.search(searchSetting.rootPath(), searchSetting.depth(),
                searchSetting.mask())
            .get();

        fileList.forEach((f) -> {
          writer.println(f);
          writer.println("\r");
        });

        writer.println("To exit, type exit or press enter to proceed.\r");
        String command = reader.readLine();
        if (command.equals("exit")) {
          proceed = false;
          log.info("Client disconnected: {}", clientSocket);
        }
      }
    } catch (IOException e) {
      log.error("Unexpected IO exception.", e);
    }
  }

  private SearchSetting askSearchSetting(BufferedReader reader, PrintWriter writer) throws IOException {
    writer.println("Type root path:\r");
    String rootPath = reader.readLine();
    writer.println("Type depth:\r");
    int depth = Integer.parseInt(reader.readLine());
    writer.println("Type mask:\r");
    String mask = reader.readLine();
    return new SearchSetting(rootPath, depth, mask);
  }
}
