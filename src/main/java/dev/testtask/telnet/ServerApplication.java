package dev.testtask.telnet;

import dev.testtask.telnet.server.SimpleTelnetServer;
import java.util.Scanner;

public class ServerApplication {

  public static void main(String[] args) {
    System.out.println("Type server port: ");
    Scanner scanner = new Scanner(System.in);
    int serverPort = scanner.nextInt();
    SimpleTelnetServer simpleTelnetServer = new SimpleTelnetServer(serverPort);
    simpleTelnetServer.start();
  }
}
