package dev.testtask.telnet.server;

import dev.testtask.telnet.exception.TelnetServerSocketSetupException;
import dev.testtask.telnet.handler.ClientRequestHandler;
import dev.testtask.telnet.model.ClientRequest;
import dev.testtask.telnet.searcher.FileSearcher;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class SimpleTelnetServer {

  private final int port;
  private final ExecutorService executorService;

  public SimpleTelnetServer(int port) {
    this.port = port;
    this.executorService = Executors.newCachedThreadPool();
  }

  public void start() {
    try (ServerSocket serverSocket = new ServerSocket(this.port);) {
      log.info("Server listening on port {}.", this.port);

      BlockingQueue<ClientRequest> queue = new LinkedBlockingQueue<>();
      executorService.submit(new FileSearcher(queue));
      while (true) {
        Socket clientSocket = serverSocket.accept();
        log.info("Client connected: {}", clientSocket);
        ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientSocket, queue);
        executorService.submit(clientRequestHandler);
      }
    } catch (IOException ex) {
      throw new TelnetServerSocketSetupException(ex);
    } finally {
      executorService.shutdown();
    }
  }
}
