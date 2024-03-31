package dev.testtask.telnet.exception;

public class TelnetServerSocketSetupException extends RuntimeException {

    public TelnetServerSocketSetupException(Throwable ex) {
        super("Unable setup server socket.", ex);
    }
}
