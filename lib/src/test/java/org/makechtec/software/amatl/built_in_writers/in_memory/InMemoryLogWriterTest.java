package org.makechtec.software.amatl.built_in_writers.in_memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.built_in_writers.commons.TimeInformationFormatter;
import org.makechtec.software.amatl.format.MessageFormatter;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryLogWriterTest {

    @Mock
    private MessageFormatter messageFormatter;

    @Mock
    private TimeInformationFormatter timeInformationFormatter;

    @Mock
    private MetadataGenericBuilder metadataGenericBuilder;

    private InMemoryLogWriter logWriter;

    @BeforeEach
    void setUp() {
        lenient().when(timeInformationFormatter.getCurrentFormattedTime()).thenReturn("12:00:00");
        lenient().when(metadataGenericBuilder.build(anyString())).thenAnswer(invocation -> {
            String level = invocation.getArgument(0);
            return level + " 12:00:00 -- ";
        });
        lenient().when(messageFormatter.formatMessageFromTemplate(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        logWriter = new InMemoryLogWriter(messageFormatter, metadataGenericBuilder);
    }

    @Test
    void shouldStoreInfoMessage() {
        String testMessage = "Test info message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.info(testMessage);

        var messages = logWriter.getMessages();
        assertEquals(1, messages.size());
        assertEquals(testMessage, messages.get(0).message());
        assertEquals("[INFO] 12:00:00 -- ", messages.get(0).metadata());
    }

    @Test
    void shouldStoreInfoMessageWithArguments() {
        String template = "User {} logged in";
        String formattedMessage = "User John logged in";
        when(messageFormatter.formatMessageFromTemplate(template, "John")).thenReturn(formattedMessage);

        logWriter.info(template, "John");

        var messages = logWriter.getMessages();
        assertEquals(1, messages.size());
        assertEquals(formattedMessage, messages.get(0).message());
    }

    @Test
    void shouldStoreDebugMessage() {
        String testMessage = "Test debug message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.debug(testMessage);

        var messages = logWriter.getMessages();
        assertEquals(1, messages.size());
        assertEquals(testMessage, messages.get(0).message());
        assertEquals("[DEBUG] 12:00:00 -- ", messages.get(0).metadata());
    }

    @Test
    void shouldStoreWarningMessage() {
        String testMessage = "Test warning message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.warning(testMessage);

        var messages = logWriter.getMessages();
        assertEquals(1, messages.size());
        assertEquals(testMessage, messages.get(0).message());
        assertEquals("[WARNING] 12:00:00 -- ", messages.get(0).metadata());
    }

    @Test
    void shouldStoreSevereMessage() {
        String testMessage = "Test severe message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.severe(testMessage);

        var messages = logWriter.getMessages();
        assertEquals(1, messages.size());
        assertEquals(testMessage, messages.get(0).message());
        assertEquals("[SEVERE] 12:00:00 -- ", messages.get(0).metadata());
    }

    @Test
    void shouldStoreErrorMessage() {
        String testMessage = "Test error message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.error(testMessage);

        var messages = logWriter.getMessages();
        assertEquals(1, messages.size());
        assertEquals(testMessage, messages.get(0).message());
        assertEquals("[ERROR] 12:00:00 -- ", messages.get(0).metadata());
    }

    @Test
    void shouldStoreMultipleMessages() {
        when(messageFormatter.formatMessageFromTemplate("Message 1")).thenReturn("Message 1");
        when(messageFormatter.formatMessageFromTemplate("Message 2")).thenReturn("Message 2");
        when(messageFormatter.formatMessageFromTemplate("Message 3")).thenReturn("Message 3");

        logWriter.info("Message 1");
        logWriter.debug("Message 2");
        logWriter.error("Message 3");

        var messages = logWriter.getMessages();
        assertEquals(3, messages.size());
    }

    @Test
    void shouldTraceStackFromThrowable() {
        RuntimeException exception = new RuntimeException("Test exception");

        when(messageFormatter.formatMessageFromTemplate(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        logWriter.traceStack(exception);

        var messages = logWriter.getMessages();
        assertFalse(messages.isEmpty());

        messages.forEach(message -> assertEquals("[STACK_TRACE] 12:00:00 -- ", message.metadata()));
    }

    @Test
    void shouldReturnUnmodifiableList() {
        logWriter.info("Test message");

        var messages = logWriter.getMessages();

        assertEquals(1, messages.size());
    }
}

