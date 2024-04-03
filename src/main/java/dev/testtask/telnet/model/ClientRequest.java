package dev.testtask.telnet.model;

import java.io.PrintWriter;

public record ClientRequest(PrintWriter writer, SearchSetting searchSetting) { }
