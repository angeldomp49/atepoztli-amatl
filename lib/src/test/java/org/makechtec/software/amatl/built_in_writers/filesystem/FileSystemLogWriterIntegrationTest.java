package org.makechtec.software.amatl.built_in_writers.filesystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.built_in_writers.commons.TimeInformationFormatter;
import org.makechtec.software.amatl.built_in_writers.filesystem.naming.NameGenerationStrategy;
import org.makechtec.software.amatl.built_in_writers.filesystem.naming.NameSettings;
import org.makechtec.software.amatl.format.MessageFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemLogWriterIntegrationTest {

    @TempDir
    Path tempDir = Paths.get("~");

    private FileSystemLogWriter logWriter;
    private Path logFilePath;

    @BeforeEach
    void setUp() {
        logFilePath = tempDir.resolve("test-logs").resolve("application.log");
        
        NameGenerationStrategy nameStrategy = nameSettings -> logFilePath.toString();
        
        NameSettings nameSettings = new NameSettings(
            logFilePath.getParent().toString(),
            "application",
            ".log"
        );
        
        FileGenerationSettings fileSettings = new FileGenerationSettings(
            1,
            FrequencyUnit.DAY,
            nameStrategy,
            nameSettings
        );
        
        FilesystemOutput filesystemOutput = new FilesystemOutput(fileSettings);
        MessageFormatter messageFormatter = new MessageFormatter();
        TimeInformationFormatter timeInformationFormatter = new TimeInformationFormatter();
        MetadataGenericBuilder metadataGenericBuilder = new MetadataGenericBuilder(timeInformationFormatter);
        
        logWriter = new FileSystemLogWriter(messageFormatter, metadataGenericBuilder, filesystemOutput);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(logFilePath)) {
            Files.delete(logFilePath);
        }
    }

    @Test
    void shouldCreateLogFileAndWriteMessage() throws IOException {
        logWriter.info("Application started");

        assertTrue(Files.exists(logFilePath));
        
        List<String> lines = Files.readAllLines(logFilePath);
        assertFalse(lines.isEmpty());
        
        String firstLine = String.join("", lines);
        assertTrue(firstLine.contains("[INFO]"));
        assertTrue(firstLine.contains("Application started"));
    }

    @Test
    void shouldWriteMessageWithArguments() throws IOException {
        String username = "admin";
        int port = 8080;
        
        logWriter.info("Server started by {} on port {}", username, port);

        List<String> lines = Files.readAllLines(logFilePath);
        String content = String.join("", lines);
        
        assertTrue(content.contains("Server started by admin on port 8080"));
    }

    @Test
    void shouldWriteMultipleLevelMessages() throws IOException {
        logWriter.debug("Debug information");
        logWriter.info("Information message");
        logWriter.warning("Warning message");
        logWriter.severe("Severe issue");
        logWriter.error("Error message");

        List<String> lines = Files.readAllLines(logFilePath);
        String content = String.join("", lines);
        
        assertTrue(content.contains("[DEBUG]"));
        assertTrue(content.contains("[INFO]"));
        assertTrue(content.contains("[WARNING]"));
        assertTrue(content.contains("[SEVERE]"));
        assertTrue(content.contains("[ERROR]"));
    }

    @Test
    void shouldWriteApplicationWorkflowToFile() throws IOException {
        logWriter.info("Initializing application");
        logWriter.debug("Loading configuration from {}", "config.yml");
        logWriter.info("Connecting to database at {}", "localhost:5432");
        logWriter.info("Database connection established");
        logWriter.info("Application ready to serve requests");

        List<String> lines = Files.readAllLines(logFilePath);
        String content = String.join("", lines);
        
        assertTrue(content.contains("Initializing application"));
        assertTrue(content.contains("config.yml"));
        assertTrue(content.contains("localhost:5432"));
        assertTrue(content.contains("Application ready to serve requests"));
    }

    @Test
    void shouldWriteStackTraceToFile() throws IOException {
        RuntimeException exception = new RuntimeException("Database connection failed");
        
        logWriter.error("Critical error occurred");
        logWriter.traceStack(exception);

        List<String> lines = Files.readAllLines(logFilePath);
        String content = String.join("", lines);
        
        assertTrue(content.contains("Critical error occurred"));
        assertTrue(content.contains("[STACK TRACE"));
    }

    @Test
    void shouldAppendMultipleMessagesToSameFile() throws IOException {
        logWriter.info("First message");
        logWriter.info("Second message");
        logWriter.info("Third message");

        List<String> lines = Files.readAllLines(logFilePath);
        String content = String.join("", lines);
        
        assertTrue(content.contains("First message"));
        assertTrue(content.contains("Second message"));
        assertTrue(content.contains("Third message"));
    }

    @Test
    void shouldLogCompleteRequestCycle() throws IOException {
        String requestId = "req-12345";
        String clientIp = "192.168.1.100";
        String endpoint = "/api/users";
        int processingTime = 156;
        
        logWriter.info("Received request {} from {}", requestId, clientIp);
        logWriter.debug("Processing {} request to {}", "GET", endpoint);
        logWriter.debug("Query execution took {}ms", processingTime);
        logWriter.info("Request {} completed successfully", requestId);

        List<String> lines = Files.readAllLines(logFilePath);
        String content = String.join("", lines);
        
        assertTrue(content.contains("req-12345"));
        assertTrue(content.contains("192.168.1.100"));
        assertTrue(content.contains("/api/users"));
        assertTrue(content.contains("156ms"));
    }

    @Test
    void shouldLogErrorScenarioWithRecovery() throws IOException {
        String serviceName = "EmailService";
        String recipient = "user@example.com";
        
        logWriter.info("Attempting to send email to {}", recipient);
        logWriter.warning("Service {} is slow to respond", serviceName);
        logWriter.error("Failed to send email via {}", serviceName);
        logWriter.info("Retrying email send operation");
        logWriter.info("Email sent successfully to {}", recipient);

        List<String> lines = Files.readAllLines(logFilePath);
        String content = String.join("", lines);
        
        assertTrue(content.contains("user@example.com"));
        assertTrue(content.contains("EmailService"));
        assertTrue(content.contains("Retrying"));
        assertTrue(content.contains("sent successfully"));
    }

    @Test
    void shouldLogUserAuthenticationFlow() throws IOException {
        String username = "john.doe";
        String ipAddress = "10.0.0.25";
        
        logWriter.info("User {} attempting login from {}", username, ipAddress);
        logWriter.debug("Validating credentials for user {}", username);
        logWriter.debug("Checking user permissions");
        logWriter.info("User {} logged in successfully", username);
        logWriter.info("Session created for user {}", username);

        List<String> lines = Files.readAllLines(logFilePath);
        String content = String.join("", lines);
        
        assertTrue(content.contains("john.doe"));
        assertTrue(content.contains("10.0.0.25"));
        assertTrue(content.contains("logged in successfully"));
        assertTrue(content.contains("Session created"));
    }

    @Test
    void shouldLogDatabaseOperations() throws IOException {
        String table = "users";
        int recordsAffected = 5;
        long queryTime = 234L;
        
        logWriter.debug("Executing query on table {}", table);
        logWriter.debug("Query completed in {}ms", queryTime);
        logWriter.info("Updated {} records in table {}", recordsAffected, table);

        List<String> lines = Files.readAllLines(logFilePath);
        String content = String.join("", lines);
        
        assertTrue(content.contains("table users"));
        assertTrue(content.contains("234ms"));
        assertTrue(content.contains("Updated 5 records"));
    }

    @Test
    void shouldCreateDirectoryStructureIfNotExists() {
        Path deepLogPath = tempDir.resolve("logs").resolve("2024").resolve("november").resolve("app.log");
        
        NameGenerationStrategy deepStrategy = nameSettings -> deepLogPath.toString();
        
        NameSettings deepSettings = new NameSettings(
            deepLogPath.getParent().toString(),
            "app",
            ".log"
        );
        
        FileGenerationSettings deepFileSettings = new FileGenerationSettings(
            1,
            FrequencyUnit.DAY,
            deepStrategy,
            deepSettings
        );
        
        FilesystemOutput deepFilesystemOutput = new FilesystemOutput(deepFileSettings);
        FileSystemLogWriter deepLogWriter = new FileSystemLogWriter(
            new MessageFormatter(),
            new MetadataGenericBuilder(new TimeInformationFormatter()),
            deepFilesystemOutput
        );
        
        deepLogWriter.info("Testing deep directory creation");

        assertTrue(Files.exists(deepLogPath));
        assertTrue(Files.isDirectory(deepLogPath.getParent()));
    }
}

