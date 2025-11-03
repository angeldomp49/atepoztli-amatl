package org.makechtec.software.amatl.logging.writing;

public interface LogWriter {
    
    void info(String message, Object... args);
    void debug(String message, Object... args);
    void warning(String message, Object... args);
    void severe(String message, Object... args);
    void error(String message, Object... args);
    void traceStack(Throwable throwable);
}
