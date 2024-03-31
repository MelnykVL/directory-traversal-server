package dev.testtask.telnet.server;

import dev.testtask.telnet.exception.TelnetServerStartupException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;

@Slf4j
public class SimpleTelnetServer {

    private final int port;
    private ServerSocket serverSocket;

    public SimpleTelnetServer(int port) {
        this.port = port;
    }

    public void setup() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new TelnetServerStartupException("Unable startup server.", e);
        }
    }

    public void start() {

    }
}
