package org.makechtec.software.amatl.writing;

public interface LogWriter {
    
    void info(CharSequence message);
    void debug(CharSequence message);
    void warning(CharSequence message);
    void severe(CharSequence message);
    void error(CharSequence message);
    
}
