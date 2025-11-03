# Amatl - Java Logging Library

A lightweight and flexible logging library for Java applications with support for multiple logging strategies.

## Requirements

- Java 17 or greater

## Quick Start

### Maven (XML)

```xml
<dependency>
    <groupId>org.makechtec.software</groupId>
    <artifactId>amatl</artifactId>
    <version>1.0.13</version>
</dependency>
```

### Gradle (Groovy)

```groovy
implementation 'org.makechtec.software:amatl:1.0.13'
```

### Gradle (Kotlin DSL)

```kotlin
implementation("org.makechtec.software:amatl:1.0.13")
```

## Documentation

For complete documentation with detailed examples and usage guides, please refer to:

- **[English Documentation](docs/README.md)** - Full documentation in English
- **[Documentación en Español](docs/README_ES.md)** - Documentación completa en español
- **[Documentation Française](docs/README_FR.md)** - Documentation complète en français

## Features

- **Multiple Log Writers**: In-memory, file system, and stream-based logging
- **Five Log Levels**: DEBUG, INFO, WARNING, SEVERE, ERROR
- **Message Formatting**: Parameterized messages with placeholders
- **Stack Trace Logging**: Built-in exception stack trace logging
- **Flexible File Organization**: Pre-built strategies for organizing log files
- **Thread-Safe**: Safe for concurrent use

## Simple Usage Example
### File System Logging

```java
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
FileSystemLogWriter logger = new FileSystemLogWriter(
    new MessageFormatter(),
    new MetadataGenericBuilder(new TimeInformationFormatter()),
    output
);

logger.info("Application initialized");
logger.debug("Processing request {}", "REQ-001");
```

### Stream Logging

```java
StreamLogWriter logger = new StreamLogWriter(
    System.out,
    new MessageFormatter(),
    new MetadataGenericBuilder(new TimeInformationFormatter())
);

logger.info("Server started on port {}", 8080);
logger.debug("Received request from {}", "192.168.1.100");
```

## Testing
logger.debug("User {} logged in", "JohnDoe");
The library includes comprehensive test coverage:

- **77 tests** with 100% success rate
- Unit tests using JUnit 5 and Mockito
- Integration tests demonstrating real-world usage scenarios
            new InMemoryOutput(messages)
Run tests with:
```bash
./gradlew test
```

## License

Distributed under the terms specified by MakechTec Software.
## Streaming logging ##
## Support
            new StreamOutput(System.out),
For issues, questions, or contributions, please contact MakechTec Software.

            new StreamOutput(System.out),
            new StreamOutput(System.out)
    ));

    log.info("hello streaming with {}", "angel");

## Debug ##

    ...
    log.info("hello streaming with {}", "angel");

    if(log.hasException()){
        var originalException = log.getOriginalException();

        e.printStackTrace();
    }
