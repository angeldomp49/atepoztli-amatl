package org.makechtec.software.amatl.built_in_writers.streaming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.format.MessageFormatter;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StreamLogWriterTest {

    @Mock
    private MessageFormatter messageFormatter;

    @Mock
    private MetadataGenericBuilder metadataGenericBuilder;

    @Mock
    private OutputStream outputStream;

    private StreamLogWriter logWriter;

    @BeforeEach
    void setUp() {
        lenient().when(metadataGenericBuilder.build(anyString())).thenAnswer(invocation -> {
            String level = invocation.getArgument(0);
            return level + " 12:00:00 -- ";
        });
        lenient().when(messageFormatter.formatMessageFromTemplate(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        
        logWriter = new StreamLogWriter(outputStream, messageFormatter, metadataGenericBuilder);
    }

    @Test
    void shouldWriteInfoMessageToStream() throws IOException {
        String testMessage = "Test info message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.info(testMessage);

        String expectedOutput = "[INFO] 12:00:00 --  " + testMessage;
        verify(outputStream).write(expectedOutput.getBytes());
    }

    @Test
    void shouldWriteInfoMessageWithArgumentsToStream() throws IOException {
        String template = "User {} logged in";
        String formattedMessage = "User John logged in";
        when(messageFormatter.formatMessageFromTemplate(template, "John")).thenReturn(formattedMessage);

        logWriter.info(template, "John");

        String expectedOutput = "[INFO] 12:00:00 --  " + formattedMessage;
        verify(outputStream).write(expectedOutput.getBytes());
    }

    @Test
    void shouldWriteDebugMessageToStream() throws IOException {
        String testMessage = "Test debug message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.debug(testMessage);

        String expectedOutput = "[DEBUG] 12:00:00 --  " + testMessage;
        verify(outputStream).write(expectedOutput.getBytes());
    }

    @Test
    void shouldWriteWarningMessageToStream() throws IOException {
        String testMessage = "Test warning message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.warning(testMessage);

        String expectedOutput = "[WARNING] 12:00:00 --  " + testMessage;
        verify(outputStream).write(expectedOutput.getBytes());
    }

    @Test
    void shouldWriteSevereMessageToStream() throws IOException {
        String testMessage = "Test severe message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.severe(testMessage);

        String expectedOutput = "[SEVERE] 12:00:00 --  " + testMessage;
        verify(outputStream).write(expectedOutput.getBytes());
    }

    @Test
    void shouldWriteErrorMessageToStream() throws IOException {
        String testMessage = "Test error message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.error(testMessage);

        String expectedOutput = "[ERROR] 12:00:00 --  " + testMessage;
        verify(outputStream).write(expectedOutput.getBytes());
    }

    @Test
    void shouldHandleIOExceptionWhenWritingInfo() throws IOException {
        doThrow(new IOException("Stream error")).when(outputStream).write(any(byte[].class));

        logWriter.info("Test message");

        verify(outputStream).write(any(byte[].class));
    }

    @Test
    void shouldHandleIOExceptionWhenWritingDebug() throws IOException {
        doThrow(new IOException("Stream error")).when(outputStream).write(any(byte[].class));

        logWriter.debug("Test message");

        verify(outputStream).write(any(byte[].class));
    }

    @Test
    void shouldHandleIOExceptionWhenWritingWarning() throws IOException {
        doThrow(new IOException("Stream error")).when(outputStream).write(any(byte[].class));

        logWriter.warning("Test message");

        verify(outputStream).write(any(byte[].class));
    }

    @Test
    void shouldHandleIOExceptionWhenWritingSevere() throws IOException {
        doThrow(new IOException("Stream error")).when(outputStream).write(any(byte[].class));

        logWriter.severe("Test message");

        verify(outputStream).write(any(byte[].class));
    }

    @Test
    void shouldHandleIOExceptionWhenWritingError() throws IOException {
        doThrow(new IOException("Stream error")).when(outputStream).write(any(byte[].class));

        logWriter.error("Test message");

        verify(outputStream).write(any(byte[].class));
    }

    @Test
    void shouldTraceStackFromThrowable() throws IOException {
        RuntimeException exception = new RuntimeException("Test exception");
        
        when(messageFormatter.formatMessageFromTemplate(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        logWriter.traceStack(exception);

        verify(outputStream, times(exception.getStackTrace().length)).write(any(byte[].class));
    }

    @Test
    void shouldHandleIOExceptionWhenTracingStack() throws IOException {
        doThrow(new IOException("Stream error")).when(outputStream).write(any(byte[].class));
        
        RuntimeException exception = new RuntimeException("Test exception");

        logWriter.traceStack(exception);

        verify(outputStream, times(exception.getStackTrace().length)).write(any(byte[].class));
    }

    @Test
    void shouldReturnTrueWhenStreamIsOpen() {
        assertTrue(logWriter.isStreamOpen());
    }

    @Test
    void shouldReturnFalseWhenStreamIsNull() {
        StreamLogWriter nullStreamWriter = new StreamLogWriter(null, messageFormatter, metadataGenericBuilder);
        
        assertFalse(nullStreamWriter.isStreamOpen());
    }

    @Test
    void shouldWriteToActualOutputStream() {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        StreamLogWriter actualWriter = new StreamLogWriter(byteOutputStream, messageFormatter, metadataGenericBuilder);
        
        String testMessage = "Test message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        actualWriter.info(testMessage);

        String output = byteOutputStream.toString();
        assertTrue(output.contains(testMessage));
        assertTrue(output.contains("[INFO]"));
    }
}

