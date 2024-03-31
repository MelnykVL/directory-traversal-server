package dev.testtask.telnet.exception;

public class TelnetServerStartupException extends RuntimeException {

    public TelnetServerStartupException(Throwable ex) {
        super("Unable startup server.", ex);
    }
}
