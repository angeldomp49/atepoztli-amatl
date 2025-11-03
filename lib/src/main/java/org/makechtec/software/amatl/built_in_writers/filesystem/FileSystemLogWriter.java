package org.makechtec.software.amatl.built_in_writers.filesystem;

import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.format.MessageFormatter;
import org.makechtec.software.amatl.logging.writing.LogWriter;

import java.util.Arrays;

public class FileSystemLogWriter implements LogWriter {

    private final MessageFormatter formatter;
    private final MetadataGenericBuilder metadataBuilder;
    private final FilesystemOutput filesystemOutput;

    public FileSystemLogWriter(MessageFormatter formatter, MetadataGenericBuilder metadataBuilder, FilesystemOutput filesystemOutput) {
        this.formatter = formatter;
        this.metadataBuilder = metadataBuilder;
        this.filesystemOutput = filesystemOutput;
    }

    @Override
    public void info(String message, Object... args) {
        var fullMessage = metadataBuilder.build("[INFO]") + " " + formatter.formatMessageFromTemplate(message, args);

        filesystemOutput.saveMessage(fullMessage);
    }

    @Override
    public void debug(String message, Object... args) {
        var fullMessage = metadataBuilder.build("[DEBUG]") + " " + formatter.formatMessageFromTemplate(message, args);

        filesystemOutput.saveMessage(fullMessage);
    }

    @Override
    public void warning(String message, Object... args) {
        var fullMessage = metadataBuilder.build("[WARNING]") + " " + formatter.formatMessageFromTemplate(message, args);

        filesystemOutput.saveMessage(fullMessage);
    }

    @Override
    public void severe(String message, Object... args) {

        var fullMessage = metadataBuilder.build("[SEVERE]") + " " + formatter.formatMessageFromTemplate(message, args);

        filesystemOutput.saveMessage(fullMessage);
    }

    @Override
    public void error(String message, Object... args) {
        var fullMessage = metadataBuilder.build("[ERROR]") + " " + formatter.formatMessageFromTemplate(message, args);

        filesystemOutput.saveMessage(fullMessage);
    }

    @Override
    public void traceStack(Throwable throwable) {
        Arrays.stream(throwable.getStackTrace())
                .forEachOrdered(stackTraceElement -> {
                    var fullMessage = metadataBuilder.build("[STACK TRACE") + " " + formatter.formatMessageFromTemplate(stackTraceElement.toString());

                    filesystemOutput.saveMessage(fullMessage);
                });
    }


}
