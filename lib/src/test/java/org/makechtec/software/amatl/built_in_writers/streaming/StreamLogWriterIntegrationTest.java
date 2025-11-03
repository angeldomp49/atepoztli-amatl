package org.makechtec.software.amatl.built_in_writers.streaming;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.built_in_writers.commons.TimeInformationFormatter;
import org.makechtec.software.amatl.format.MessageFormatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class StreamLogWriterIntegrationTest {

    private ByteArrayOutputStream outputStream;
    private StreamLogWriter logWriter;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        MessageFormatter messageFormatter = new MessageFormatter();
        TimeInformationFormatter timeInformationFormatter = new TimeInformationFormatter();
        MetadataGenericBuilder metadataGenericBuilder = new MetadataGenericBuilder(timeInformationFormatter);
        
        logWriter = new StreamLogWriter(outputStream, messageFormatter, metadataGenericBuilder);
    }

    @Test
    void shouldWriteInfoMessageToOutputStream() {
        logWriter.info("Application started");

        String output = outputStream.toString(StandardCharsets.UTF_8);
        
        assertTrue(output.contains("[INFO]"));
        assertTrue(output.contains("Application started"));
    }

    @Test
    void shouldWriteMessageWithArguments() {
        logWriter.info("User {} logged in at port {}", "admin", 8080);

        String output = outputStream.toString(StandardCharsets.UTF_8);
        
        assertTrue(output.contains("User admin logged in at port 8080"));
    }

    @Test
    void shouldWriteMultipleLevelMessages() {
        logWriter.debug("Debug trace");
        logWriter.info("Information");
        logWriter.warning("Warning detected");
        logWriter.severe("Severe problem");
        logWriter.error("Error occurred");

        String output = outputStream.toString(StandardCharsets.UTF_8);
        
        assertTrue(output.contains("[DEBUG]"));
        assertTrue(output.contains("[INFO]"));
        assertTrue(output.contains("[WARNING]"));
        assertTrue(output.contains("[SEVERE]"));
        assertTrue(output.contains("[ERROR]"));
    }

    @Test
    void shouldWriteApplicationFlowToStream() {
        logWriter.info("Starting server on port {}", 8080);
        logWriter.debug("Loading configuration from {}", "config.properties");
        logWriter.info("Server ready to accept connections");
        logWriter.info("Received request from client {}", "192.168.1.100");
        logWriter.debug("Processing request with id {}", "req-12345");
        logWriter.info("Request processed successfully in {}ms", 45);

        String output = outputStream.toString(StandardCharsets.UTF_8);
        
        assertTrue(output.contains("Starting server on port 8080"));
        assertTrue(output.contains("config.properties"));
        assertTrue(output.contains("192.168.1.100"));
        assertTrue(output.contains("req-12345"));
        assertTrue(output.contains("45ms"));
    }

    @Test
    void shouldWriteStackTraceToStream() {
        RuntimeException exception = new RuntimeException("Connection timeout");
        
        logWriter.error("Failed to connect to database");
        logWriter.traceStack(exception);

        String output = outputStream.toString(StandardCharsets.UTF_8);
        
        assertTrue(output.contains("Failed to connect to database"));
        assertTrue(output.contains("[STACK TRACE"));
    }

    @Test
    void shouldHandleComplexObjectArguments() {
        Object userId = 12345;
        Object timestamp = System.currentTimeMillis();
        Object status = true;
        
        logWriter.info("Transaction {} completed at {} with success: {}", userId, timestamp, status);

        String output = outputStream.toString(StandardCharsets.UTF_8);
        
        assertTrue(output.contains("Transaction 12345"));
        assertTrue(output.contains("true"));
    }

    @Test
    void shouldWriteErrorScenarioWithContext() {
        String service = "PaymentService";
        String errorCode = "ERR_500";
        
        logWriter.warning("Service {} is experiencing issues", service);
        logWriter.error("Service {} failed with error code: {}", service, errorCode);
        logWriter.severe("Critical failure in {}", service);

        String output = outputStream.toString(StandardCharsets.UTF_8);
        
        assertTrue(output.contains("PaymentService is experiencing issues"));
        assertTrue(output.contains("ERR_500"));
        assertTrue(output.contains("Critical failure in PaymentService"));
    }

    @Test
    void shouldReportStreamOpenStatus() {
        assertTrue(logWriter.isStreamOpen());
    }

    @Test
    void shouldReportStreamClosedStatusWhenNull() {
        StreamLogWriter nullStreamWriter = new StreamLogWriter(
            null, 
            new MessageFormatter(), 
            new MetadataGenericBuilder(new TimeInformationFormatter())
        );
        
        assertFalse(nullStreamWriter.isStreamOpen());
    }

    @Test
    void shouldContinueLoggingAfterIOException() throws IOException {
        OutputStream failingStream = new OutputStream() {
            private int callCount = 0;
            
            @Override
            public void write(int b) throws IOException {
                callCount++;
                if (callCount == 1) {
                    throw new IOException("Simulated IO error");
                }
            }
        };
        
        StreamLogWriter failingWriter = new StreamLogWriter(
            failingStream,
            new MessageFormatter(),
            new MetadataGenericBuilder(new TimeInformationFormatter())
        );
        
        assertDoesNotThrow(() -> failingWriter.info("First message"));
        assertDoesNotThrow(() -> failingWriter.info("Second message"));
    }

    @Test
    void shouldLogSequentialRequestsWithDifferentData() {
        for (int i = 1; i <= 5; i++) {
            logWriter.info("Processing request {} from user {}", i, "user" + i);
        }

        String output = outputStream.toString(StandardCharsets.UTF_8);
        
        assertTrue(output.contains("request 1 from user user1"));
        assertTrue(output.contains("request 5 from user user5"));
    }

    @Test
    void shouldLogCompleteUserSession() {
        String sessionId = "sess-abc123";
        String username = "john.doe";
        
        logWriter.info("Session {} created for user {}", sessionId, username);
        logWriter.debug("User {} authenticated successfully", username);
        logWriter.info("User {} accessed resource: {}", username, "/api/data");
        logWriter.warning("User {} attempted unauthorized action", username);
        logWriter.info("Session {} ended for user {}", sessionId, username);

        String output = outputStream.toString(StandardCharsets.UTF_8);
        
        assertTrue(output.contains("sess-abc123"));
        assertTrue(output.contains("john.doe"));
        assertTrue(output.contains("/api/data"));
        assertTrue(output.contains("unauthorized action"));
    }
}

