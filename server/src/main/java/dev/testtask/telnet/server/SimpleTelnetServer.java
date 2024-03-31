package dev.testtask.telnet.server;

import dev.testtask.telnet.exception.SocketTelnetServerSetupException;
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
        } catch (IOException ex) {
            throw new SocketTelnetServerSetupException(ex);
        }
    }

    public void start() {
        this.setup();
    }
}
