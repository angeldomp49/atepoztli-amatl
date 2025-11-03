package org.makechtec.software.amatl.built_in_writers.streaming;

import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.format.MessageFormatter;
import org.makechtec.software.amatl.logging.writing.LogWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Logger;

public class StreamLogWriter implements LogWriter {

    private static final Logger LOG = Logger.getLogger(StreamLogWriter.class.getName());
    public static final String GENERIC_ERROR_LOG_MESSAGE = "Application couldn't put logs in provided stream";

    private final OutputStream outputStream;
    private final MessageFormatter formatter;
    private final MetadataGenericBuilder metadataBuilder;
    

    public StreamLogWriter(OutputStream outputStream, MessageFormatter formatter, MetadataGenericBuilder metadataBuilder) {
        this.outputStream = outputStream;
        this.formatter = formatter;
        this.metadataBuilder = metadataBuilder;
    }
    
    public boolean isStreamOpen(){
        return outputStream != null;
    }

    @Override
    public void info(String message, Object... args) {
        
        var fullMessage = metadataBuilder.build("[INFO]") + " " + formatter.formatMessageFromTemplate(message, args);
        
        try {
            outputStream.write(fullMessage.getBytes());
        } catch(IOException e){
            LOG.warning(GENERIC_ERROR_LOG_MESSAGE);
        }
    }

    @Override
    public void debug(String message, Object... args) {
        
        var fullMessage = metadataBuilder.build("[DEBUG]") + " " + formatter.formatMessageFromTemplate(message, args);
        
        try {
            outputStream.write(fullMessage.getBytes());
        } catch(IOException e){
            LOG.warning(GENERIC_ERROR_LOG_MESSAGE);
        }
    }

    @Override
    public void warning(String message, Object... args) {
        
        var fullMessage = metadataBuilder.build("[WARNING]") + " " + formatter.formatMessageFromTemplate(message, args);
        
        try {
            outputStream.write(fullMessage.getBytes());
        } catch(IOException e){
            LOG.warning(GENERIC_ERROR_LOG_MESSAGE);
        }
    }

    @Override
    public void severe(String message, Object... args) {
        
        var fullMessage = metadataBuilder.build("[SEVERE]") + " " + formatter.formatMessageFromTemplate(message, args);
        
        try {
            outputStream.write(fullMessage.getBytes());
        } catch(IOException e){
            LOG.warning(GENERIC_ERROR_LOG_MESSAGE);
        }
    }

    @Override
    public void error(String message, Object... args) {

        var fullMessage = metadataBuilder.build("[ERROR]") + " " + formatter.formatMessageFromTemplate(message, args);
        
        try {
            outputStream.write(fullMessage.getBytes());
        } catch(IOException e){
            LOG.warning(GENERIC_ERROR_LOG_MESSAGE);
        }
    }

    @Override
    public void traceStack(Throwable throwable) {
        Arrays.stream(throwable.getStackTrace())
                .forEachOrdered(stackTraceElement -> {
                    try{

                        var fullMessage = metadataBuilder.build("[STACK TRACE") + " " + formatter.formatMessageFromTemplate(stackTraceElement.toString());

                        outputStream.write(fullMessage.getBytes());
                    } catch (IOException e) {
                        LOG.warning(GENERIC_ERROR_LOG_MESSAGE);
                    }
                });
    }
    
    
}
