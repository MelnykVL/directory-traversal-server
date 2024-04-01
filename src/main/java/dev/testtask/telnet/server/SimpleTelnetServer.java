package dev.testtask.telnet.server;

import dev.testtask.telnet.exception.TelnetServerSocketSetupException;
import dev.testtask.telnet.handler.ClientRequestHandler;
import dev.testtask.telnet.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SimpleTelnetServer {

  private final int port;
  private final SearchService searchService;
  private final ExecutorService executorService;

  public SimpleTelnetServer(int port, SearchService searchService) {
    this.port = port;
    this.searchService = searchService;
    this.executorService = Executors.newCachedThreadPool();
  }

  public void start() {
    try (ServerSocket serverSocket = new ServerSocket(this.port);) {
      log.info("Server listening on port {}.", this.port);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        log.info("Client connected: {}", clientSocket);
        ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientSocket, searchService);
        executorService.submit(clientRequestHandler);
      }
    } catch (IOException ex) {
      throw new TelnetServerSocketSetupException(ex);
    } finally {
      executorService.shutdown();
    }
  }
}
