package dev.testtask.telnet;

import dev.testtask.telnet.server.SimpleTelnetServer;
import dev.testtask.telnet.service.SearchService;

public class ServerApplication {

    public static void main(String[] args) {
        SimpleTelnetServer simpleTelnetServer = new SimpleTelnetServer(8081, new SearchService());
        simpleTelnetServer.start();
    }
}
