package dev.testtask.telnet.handler;

import dev.testtask.telnet.model.ClientRequest;
import dev.testtask.telnet.model.SearchSetting;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class ClientRequestHandler extends Thread {

  private final Socket clientSocket;
  private final BlockingQueue<ClientRequest> blockingQueue;

  public ClientRequestHandler(Socket clientSocket, BlockingQueue<ClientRequest> blockingQueue) {
    this.clientSocket = clientSocket;
    this.blockingQueue = blockingQueue;
    this.setDaemon(true);
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
        ClientRequest clientRequest = new ClientRequest(writer, searchSetting);
        blockingQueue.add(clientRequest);

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
