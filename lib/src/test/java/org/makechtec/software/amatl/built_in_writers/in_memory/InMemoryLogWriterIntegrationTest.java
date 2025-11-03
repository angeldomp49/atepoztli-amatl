package org.makechtec.software.amatl.built_in_writers.in_memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.built_in_writers.commons.TimeInformationFormatter;
import org.makechtec.software.amatl.format.MessageFormatter;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryLogWriterIntegrationTest {

    private InMemoryLogWriter logWriter;

    @BeforeEach
    void setUp() {
        MessageFormatter messageFormatter = new MessageFormatter();
        TimeInformationFormatter timeInformationFormatter = new TimeInformationFormatter();
        MetadataGenericBuilder metadataGenericBuilder = new MetadataGenericBuilder(timeInformationFormatter);

        logWriter = new InMemoryLogWriter(messageFormatter, metadataGenericBuilder);
    }

    @Test
    void shouldLogCompleteInfoMessage() {
        logWriter.info("Application started successfully");

        var messages = logWriter.getMessages();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).message().contains("Application started successfully"));
        assertTrue(messages.get(0).metadata().contains("[INFO]"));
    }

    @Test
    void shouldLogMessageWithSingleArgument() {
        String username = "JohnDoe";

        logWriter.info("User {} logged in", username);

        var messages = logWriter.getMessages();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).message().contains("User JohnDoe logged in"));
    }

    @Test
    void shouldLogMessageWithMultipleArguments() {
        String username = "JohnDoe";
        String action = "updated";
        String resource = "profile";

        logWriter.info("User {} {} {}", username, action, resource);

        var messages = logWriter.getMessages();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).message().contains("JohnDoe"));
        assertTrue(messages.get(0).message().contains("updated"));
        assertTrue(messages.get(0).message().contains("profile"));
    }

    @Test
    void shouldLogDifferentLevelsOfMessages() {
        logWriter.debug("Debug information");
        logWriter.info("Information message");
        logWriter.warning("Warning message");
        logWriter.severe("Severe issue detected");
        logWriter.error("Error occurred");

        var messages = logWriter.getMessages();

        assertEquals(5, messages.size());
        assertTrue(messages.get(0).metadata().contains("[DEBUG]"));
        assertTrue(messages.get(1).metadata().contains("[INFO]"));
        assertTrue(messages.get(2).metadata().contains("[WARNING]"));
        assertTrue(messages.get(3).metadata().contains("[SEVERE]"));
        assertTrue(messages.get(4).metadata().contains("[ERROR]"));
    }

    @Test
    void shouldLogApplicationWorkflow() {
        logWriter.info("Starting application initialization");
        logWriter.debug("Loading configuration files");
        logWriter.debug("Connecting to database");
        logWriter.info("Application initialized successfully");
        logWriter.info("Processing user request {}", "123");
        logWriter.warning("Request processing took longer than expected");
        logWriter.info("Request completed successfully");

        var messages = logWriter.getMessages();

        assertEquals(7, messages.size());
        assertTrue(messages.get(4).message().contains("123"));
    }

    @Test
    void shouldLogExceptionStackTrace() {
        RuntimeException exception = new RuntimeException("Database connection failed");

        logWriter.error("An error occurred while processing request");
        logWriter.traceStack(exception);

        var messages = logWriter.getMessages();

        assertTrue(messages.size() > 1);
        assertTrue(messages.get(0).message().contains("An error occurred"));

        long stackTraceMessages = messages.stream()
                .filter(msg -> msg.metadata().contains("[STACK_TRACE]"))
                .count();

        assertTrue(stackTraceMessages > 0);
    }

    @Test
    void shouldLogNumericAndObjectArguments() {
        int requestId = 42;
        double processingTime = 125.5;
        boolean success = true;

        logWriter.info("Request {} completed in {}ms with status: {}", requestId, processingTime, success);

        var messages = logWriter.getMessages();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).message().contains("42"));
        assertTrue(messages.get(0).message().contains("125.5"));
        assertTrue(messages.get(0).message().contains("true"));
    }

    @Test
    void shouldMaintainMessagesInOrder() {
        for (int i = 1; i <= 10; i++) {
            logWriter.info("Message number {}", i);
        }

        var messages = logWriter.getMessages();

        assertEquals(10, messages.size());
        for (int i = 0; i < 10; i++) {
            assertTrue(messages.get(i).message().contains(String.valueOf(i + 1)));
        }
    }

    @Test
    void shouldLogErrorScenarioWithContext() {
        String userId = "user123";
        String operation = "updateProfile";

        logWriter.info("User {} attempting operation: {}", userId, operation);
        logWriter.warning("Validation failed for user {}", userId);
        logWriter.error("Operation {} failed for user {}", operation, userId);

        var messages = logWriter.getMessages();

        assertEquals(3, messages.size());
        assertTrue(messages.get(2).message().contains("updateProfile"));
        assertTrue(messages.get(2).message().contains("user123"));
    }

    @Test
    void shouldReturnUnmodifiableMessagesList() {
        logWriter.info("Test message");

        var messages = logWriter.getMessages();

        assertThrows(UnsupportedOperationException.class, messages::clear);
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

        var messages = logWriter.getMessages();

        assertEquals(5, messages.size());
        assertTrue(messages.get(0).message().contains("sess-abc123"));
        assertTrue(messages.get(2).message().contains("/api/data"));
        assertTrue(messages.get(3).message().contains("unauthorized"));
    }

    @Test
    void shouldLogDatabaseOperations() {
        String tableName = "users";
        int recordsProcessed = 150;
        long queryTimeMs = 234L;

        logWriter.debug("Executing query on table {}", tableName);
        logWriter.debug("Query completed in {}ms", queryTimeMs);
        logWriter.info("Processed {} records from table {}", recordsProcessed, tableName);

        var messages = logWriter.getMessages();

        assertEquals(3, messages.size());
        assertTrue(messages.get(0).message().contains("users"));
        assertTrue(messages.get(1).message().contains("234"));
        assertTrue(messages.get(2).message().contains("150"));
    }

    @Test
    void shouldLogApplicationLifecycle() {
        logWriter.info("Application starting");
        logWriter.debug("Initializing components");
        logWriter.info("Server listening on port {}", 8080);
        logWriter.info("Application ready");

        logWriter.info("Shutting down gracefully");
        logWriter.debug("Closing database connections");
        logWriter.info("Application stopped");

        var messages = logWriter.getMessages();

        assertEquals(7, messages.size());
        assertTrue(messages.get(0).message().contains("starting"));
        assertTrue(messages.get(2).message().contains("8080"));
        assertTrue(messages.get(6).message().contains("stopped"));
    }

    @Test
    void shouldLogServiceHealthCheck() {
        String serviceName = "PaymentService";
        String status = "healthy";
        int responseTime = 45;

        logWriter.info("Health check for service: {}", serviceName);
        logWriter.debug("Service {} responded in {}ms", serviceName, responseTime);
        logWriter.info("Service {} status: {}", serviceName, status);

        var messages = logWriter.getMessages();

        assertEquals(3, messages.size());
        assertTrue(messages.get(0).message().contains("PaymentService"));
        assertTrue(messages.get(1).message().contains("45"));
        assertTrue(messages.get(2).message().contains("healthy"));
    }

    @Test
    void shouldHandleEmptyMessages() {
        logWriter.info("");

        var messages = logWriter.getMessages();

        assertEquals(1, messages.size());
        assertTrue(messages.get(0).metadata().contains("[INFO]"));
    }

    @Test
    void shouldLogSecurityEvents() {
        String ipAddress = "192.168.1.100";
        String username = "admin";
        String action = "login_attempt";

        logWriter.warning("Security event: {} from IP {}", action, ipAddress);
        logWriter.info("User {} authenticated from {}", username, ipAddress);

        var messages = logWriter.getMessages();

        assertEquals(2, messages.size());
        assertTrue(messages.get(0).metadata().contains("[WARNING]"));
        assertTrue(messages.get(0).message().contains("192.168.1.100"));
        assertTrue(messages.get(1).message().contains("admin"));
    }
}

