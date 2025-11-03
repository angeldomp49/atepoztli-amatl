# Amatl - Java Logging Library

Amatl is a lightweight and flexible logging library for Java applications that provides multiple logging strategies including in-memory, file system, and stream-based logging.

## Installation

### Maven (XML)

Add the following dependency to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.makechtec.software</groupId>
        <artifactId>amatl</artifactId>
        <version>1.0.13</version>
    </dependency>
</dependencies>
```

### Gradle (Groovy)

Add the following dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'org.makechtec.software:amatl:1.0.13'
}
```

### Gradle (Kotlin DSL)

Add the following dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("org.makechtec.software:amatl:1.0.13")
}
```

## Features

- **Multiple Log Writers**: In-memory, file system, and stream-based logging
- **Five Log Levels**: DEBUG, INFO, WARNING, SEVERE, ERROR
- **Message Formatting**: Support for parameterized messages with placeholders
- **Stack Trace Logging**: Built-in exception stack trace logging
- **Flexible File Organization**: Pre-built strategies for organizing log files by month
- **Thread-Safe**: Safe for concurrent use

## Usage Examples

### 1. In-Memory Logging

Perfect for testing, debugging, or temporary log storage.

```java
import org.makechtec.software.amatl.built_in_writers.in_memory.InMemoryLogWriter;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.built_in_writers.commons.TimeInformationFormatter;
import org.makechtec.software.amatl.format.MessageFormatter;

// Initialize the logger
MessageFormatter formatter = new MessageFormatter();
TimeInformationFormatter timeFormatter = new TimeInformationFormatter();
MetadataGenericBuilder metadataBuilder = new MetadataGenericBuilder(timeFormatter);

InMemoryLogWriter logger = new InMemoryLogWriter(formatter, timeFormatter, metadataBuilder);

// Log messages
logger.info("Application started successfully");
logger.debug("Loading configuration files");
logger.warning("Request processing took longer than expected");

// Log with parameters
String username = "JohnDoe";
logger.info("User {} logged in", username);

// Log with multiple parameters
int requestId = 42;
double processingTime = 125.5;
logger.info("Request {} completed in {}ms", requestId, processingTime);

// Retrieve all logged messages
var messages = logger.getMessages();
messages.forEach(msg -> {
    System.out.println(msg.metadata() + msg.message());
});
```

### 2. Stream-Based Logging

Write logs directly to any OutputStream (console, network stream, etc.).

```java
import org.makechtec.software.amatl.built_in_writers.streaming.StreamLogWriter;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.built_in_writers.commons.TimeInformationFormatter;
import org.makechtec.software.amatl.format.MessageFormatter;

import java.io.ByteArrayOutputStream;

// Initialize with System.out or any OutputStream
MessageFormatter formatter = new MessageFormatter();
TimeInformationFormatter timeFormatter = new TimeInformationFormatter();
MetadataGenericBuilder metadataBuilder = new MetadataGenericBuilder(timeFormatter);

StreamLogWriter logger = new StreamLogWriter(System.out, formatter, metadataBuilder);

// Log server startup
logger.info("Starting server on port {}", 8080);
logger.debug("Loading configuration from {}", "config.properties");
logger.info("Server ready to accept connections");

// Log request processing
logger.info("Received request from client {}", "192.168.1.100");
logger.debug("Processing request with id {}", "req-12345");
logger.info("Request processed successfully in {}ms", 45);

// Check if stream is open
if (logger.isStreamOpen()) {
    logger.info("Logger is active and ready");
}
```

### 3. File System Logging

Persist logs to the file system with custom naming strategies.

```java
import org.makechtec.software.amatl.built_in_writers.filesystem.FileSystemLogWriter;
import org.makechtec.software.amatl.built_in_writers.filesystem.FilesystemOutput;
import org.makechtec.software.amatl.built_in_writers.filesystem.FileGenerationSettings;
import org.makechtec.software.amatl.built_in_writers.filesystem.FrequencyUnit;
import org.makechtec.software.amatl.built_in_writers.filesystem.naming.NameSettings;
import org.makechtec.software.amatl.built_in_writers.filesystem.naming.NameGenerationStrategy;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.built_in_writers.commons.TimeInformationFormatter;
import org.makechtec.software.amatl.format.MessageFormatter;

import java.nio.file.Paths;

// Define naming strategy
NameGenerationStrategy nameStrategy = settings -> 
    Paths.get(settings.rootDirectory(), settings.prefix() + settings.suffix()).toString();

NameSettings nameSettings = new NameSettings(
    "/var/log/myapp",
    "application",
    ".log"
);

// Configure file generation
FileGenerationSettings fileSettings = new FileGenerationSettings(
    1,                      // Frequency quantity
    FrequencyUnit.DAY,      // Rotate daily
    nameStrategy,
    nameSettings
);

FilesystemOutput output = new FilesystemOutput(fileSettings);
MessageFormatter formatter = new MessageFormatter();
TimeInformationFormatter timeFormatter = new TimeInformationFormatter();
MetadataGenericBuilder metadataBuilder = new MetadataGenericBuilder(timeFormatter);

FileSystemLogWriter logger = new FileSystemLogWriter(formatter, metadataBuilder, output);

// Log application workflow
logger.info("Initializing application");
logger.debug("Connecting to database at {}", "localhost:5432");
logger.info("Database connection established");
logger.info("Application ready to serve requests");
```

### 4. Monthly Directory Strategy

Automatically organize logs by month using the built-in strategy.

```java
import org.makechtec.software.amatl.built_in_writers.filesystem.FileSystemLogWriter;
import org.makechtec.software.amatl.built_in_writers.filesystem.FilesystemOutput;
import org.makechtec.software.amatl.built_in_writers.filesystem.FileGenerationSettings;
import org.makechtec.software.amatl.built_in_writers.filesystem.FrequencyUnit;
import org.makechtec.software.amatl.built_in_writers.filesystem.buil_in_naming.MonthlyDirectoryStrategy;
import org.makechtec.software.amatl.built_in_writers.filesystem.naming.NameSettings;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.built_in_writers.commons.TimeInformationFormatter;
import org.makechtec.software.amatl.format.MessageFormatter;

// Use the pre-built monthly directory strategy
MonthlyDirectoryStrategy monthlyStrategy = new MonthlyDirectoryStrategy();

NameSettings nameSettings = new NameSettings(
    "/var/log/myapp",
    "application",
    ".log"
);

FileGenerationSettings fileSettings = new FileGenerationSettings(
    1,
    FrequencyUnit.DAY,
    monthlyStrategy,
    nameSettings
);

FilesystemOutput output = new FilesystemOutput(fileSettings);
MessageFormatter formatter = new MessageFormatter();
TimeInformationFormatter timeFormatter = new TimeInformationFormatter();
MetadataGenericBuilder metadataBuilder = new MetadataGenericBuilder(timeFormatter);

FileSystemLogWriter logger = new FileSystemLogWriter(formatter, metadataBuilder, output);

// Logs will be organized as: /var/log/myapp/11-2024/application-02.log
logger.info("Log entry for monthly organized directory");
logger.debug("Processing request {}", "REQ-001");
logger.warning("System resource usage at {}%", 85);
```

### 5. Exception and Stack Trace Logging

Log exceptions with full stack traces.

```java
logger.info("Processing payment transaction");

try {
    // Some operation that might fail
    processPayment();
} catch (Exception e) {
    logger.error("Failed to process payment");
    logger.traceStack(e);
}
```

### 6. Complete User Session Example

```java
String sessionId = "sess-abc123";
String username = "john.doe";

logger.info("Session {} created for user {}", sessionId, username);
logger.debug("User {} authenticated successfully", username);
logger.info("User {} accessed resource: {}", username, "/api/data");
logger.warning("User {} attempted unauthorized action", username);
logger.info("Session {} ended for user {}", sessionId, username);
```

### 7. Database Operations Logging

```java
String tableName = "users";
int recordsProcessed = 150;
long queryTimeMs = 234L;

logger.debug("Executing query on table {}", tableName);
logger.debug("Query completed in {}ms", queryTimeMs);
logger.info("Processed {} records from table {}", recordsProcessed, tableName);
```

### 8. Application Lifecycle Logging

```java
// Startup
logger.info("Application starting");
logger.debug("Initializing components");
logger.info("Server listening on port {}", 8080);
logger.info("Application ready");

// ... application runs ...

// Shutdown
logger.info("Shutting down gracefully");
logger.debug("Closing database connections");
logger.info("Application stopped");
```

### 9. Security Events Logging

```java
String ipAddress = "192.168.1.100";
String username = "admin";
String action = "login_attempt";

logger.warning("Security event: {} from IP {}", action, ipAddress);
logger.info("User {} authenticated from {}", username, ipAddress);
```

## Log Levels

Amatl supports five log levels:

- **DEBUG**: Detailed information for debugging purposes
- **INFO**: General informational messages
- **WARNING**: Warning messages for potentially problematic situations
- **SEVERE**: Severe issues that need attention
- **ERROR**: Error events that might still allow the application to continue

## Message Formatting

All log writers support parameterized messages using `{}` as placeholders:

```java
logger.info("User {} performed action {} at {}", username, action, timestamp);
```

The placeholders are replaced with the string representation of the provided arguments in order.

## File Rotation

The FileSystem logger supports automatic file rotation based on time intervals:

- **HOUR**: Rotate logs every N hours
- **DAY**: Rotate logs every N days
- **WEEK**: Rotate logs every N weeks
- **MONTH**: Rotate logs every N months
- **YEAR**: Rotate logs every N years

Example for daily rotation:
```java
FileGenerationSettings fileSettings = new FileGenerationSettings(
    1,                      // Every 1 day
    FrequencyUnit.DAY,
    nameStrategy,
    nameSettings
);
```

## Best Practices

1. **Choose the Right Writer**: Use InMemoryLogWriter for testing, StreamLogWriter for console output, and FileSystemLogWriter for production logging.

2. **Use Appropriate Log Levels**: Reserve ERROR and SEVERE for actual problems, use INFO for business events, and DEBUG for development.

3. **Parameterize Messages**: Always use placeholders instead of string concatenation for better performance.

4. **Log Structured Data**: Include context like user IDs, request IDs, and timestamps in your log messages.

5. **Handle Exceptions Properly**: Use `traceStack()` to log full exception details when needed.

## Thread Safety

All log writers in Amatl are designed to be thread-safe and can be used in concurrent environments.

## License

This library is distributed under the terms specified by MakechTec Software.

## Support

For issues, questions, or contributions, please contact MakechTec Software.

