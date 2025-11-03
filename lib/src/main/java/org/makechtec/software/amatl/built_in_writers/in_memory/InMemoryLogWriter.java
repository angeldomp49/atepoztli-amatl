package org.makechtec.software.amatl.built_in_writers.in_memory;

import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.built_in_writers.commons.TimeInformationFormatter;
import org.makechtec.software.amatl.format.MessageFormatter;
import org.makechtec.software.amatl.logging.writing.LogWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InMemoryLogWriter implements LogWriter {

    private final List<InMemoryMessageRecord> messages;
    private final MessageFormatter formatter;
    private final MetadataGenericBuilder metadataBuilder;

    public InMemoryLogWriter(MessageFormatter formatter, TimeInformationFormatter timeInformationFormatter, MetadataGenericBuilder metadataBuilder) {
        this.formatter = formatter;
        this.metadataBuilder = metadataBuilder;
        this.messages = new ArrayList<>();
    }

    @Override
    public void info(String message, Object... values) {

        registerLogRecord(message, "[INFO]", values);
    }

    @Override
    public void debug(String message, Object... args) {
        registerLogRecord(message, "[DEBUG]", args);
    }

    @Override
    public void warning(String message, Object... args) {
        registerLogRecord(message, "[WARNING]", args);
    }

    @Override
    public void severe(String message, Object... args) {
        registerLogRecord(message, "[SEVERE]", args);
    }

    @Override
    public void error(String message, Object... args) {
        registerLogRecord(message, "[ERROR]", args);
    }


    @Override
    public void traceStack(Throwable throwable) {
        Arrays.stream(throwable.getStackTrace())
                .forEachOrdered(stackTraceElement -> {
                    registerLogRecord(stackTraceElement.toString(), "[STACK_TRACE]");
                });
    }

    private void registerLogRecord(String message, String level, Object... values) {
        var metadata = metadataBuilder.build(level);

        var formattedMessage = formatter.formatMessageFromTemplate(message, values);

        messages.add(new InMemoryMessageRecord(formattedMessage, metadata));
    }

    public List<InMemoryMessageRecord> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public record InMemoryMessageRecord(
            String message,
            String metadata
    ) {
    }

}
