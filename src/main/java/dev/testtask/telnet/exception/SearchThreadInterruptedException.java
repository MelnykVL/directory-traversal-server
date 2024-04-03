package dev.testtask.telnet.exception;

public class SearchThreadInterruptedException extends RuntimeException{

  public SearchThreadInterruptedException(Throwable ex) {
    super("Search thread was interrupted.", ex);
  }
}
