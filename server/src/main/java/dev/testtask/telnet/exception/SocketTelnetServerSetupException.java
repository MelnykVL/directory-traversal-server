package dev.testtask.telnet.exception;

public class SocketTelnetServerSetupException extends RuntimeException {

    public SocketTelnetServerSetupException(Throwable ex) {
        super("Unable setup server socket.", ex);
    }
}
