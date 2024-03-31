package dev.testtask.telnet.server;

import dev.testtask.telnet.exception.TelnetServerSocketSetupException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;

@Slf4j
public class SimpleTelnetServer {

    private final int port;

    public SimpleTelnetServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {

        } catch (IOException ex) {
            throw new TelnetServerSocketSetupException(ex);
        }
    }
}
