package dev.testtask.telnet;

import dev.testtask.telnet.server.SimpleTelnetServer;
import dev.testtask.telnet.service.SearchService;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

public class ServerApplication {

  public static void main(String[] args) {
    System.out.println("Type server port: ");
    Scanner scanner = new Scanner(System.in);
    int serverPort = scanner.nextInt();
    ReentrantLock lock = new ReentrantLock();
    SimpleTelnetServer simpleTelnetServer = new SimpleTelnetServer(serverPort, new SearchService(lock));
    simpleTelnetServer.start();
  }
}
