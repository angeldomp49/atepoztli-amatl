package org.makechtec.software.amatl.built_in_writers.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.makechtec.software.amatl.built_in_writers.commons.MetadataGenericBuilder;
import org.makechtec.software.amatl.format.MessageFormatter;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FileSystemLogWriterTest {

    @Mock
    private MessageFormatter messageFormatter;

    @Mock
    private MetadataGenericBuilder metadataGenericBuilder;

    @Mock
    private FilesystemOutput filesystemOutput;

    private FileSystemLogWriter logWriter;

    @BeforeEach
    void setUp() {
        lenient().when(metadataGenericBuilder.build(anyString())).thenAnswer(invocation -> {
            String level = invocation.getArgument(0);
            return level + " 12:00:00 -- ";
        });
        lenient().when(messageFormatter.formatMessageFromTemplate(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        
        logWriter = new FileSystemLogWriter(messageFormatter, metadataGenericBuilder, filesystemOutput);
    }

    @Test
    void shouldSaveInfoMessageToFilesystem() {
        String testMessage = "Test info message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.info(testMessage);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(filesystemOutput).saveMessage(messageCaptor.capture());
        
        String savedMessage = messageCaptor.getValue();
        assertEquals("[INFO] 12:00:00 --  " + testMessage, savedMessage);
    }

    @Test
    void shouldSaveInfoMessageWithArgumentsToFilesystem() {
        String template = "User {} logged in";
        String formattedMessage = "User John logged in";
        when(messageFormatter.formatMessageFromTemplate(template, "John")).thenReturn(formattedMessage);

        logWriter.info(template, "John");

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(filesystemOutput).saveMessage(messageCaptor.capture());
        
        String savedMessage = messageCaptor.getValue();
        assertEquals("[INFO] 12:00:00 --  " + formattedMessage, savedMessage);
    }

    @Test
    void shouldSaveDebugMessageToFilesystem() {
        String testMessage = "Test debug message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.debug(testMessage);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(filesystemOutput).saveMessage(messageCaptor.capture());
        
        String savedMessage = messageCaptor.getValue();
        assertEquals("[DEBUG] 12:00:00 --  " + testMessage, savedMessage);
    }

    @Test
    void shouldSaveWarningMessageToFilesystem() {
        String testMessage = "Test warning message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.warning(testMessage);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(filesystemOutput).saveMessage(messageCaptor.capture());
        
        String savedMessage = messageCaptor.getValue();
        assertEquals("[WARNING] 12:00:00 --  " + testMessage, savedMessage);
    }

    @Test
    void shouldSaveSevereMessageToFilesystem() {
        String testMessage = "Test severe message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.severe(testMessage);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(filesystemOutput).saveMessage(messageCaptor.capture());
        
        String savedMessage = messageCaptor.getValue();
        assertEquals("[SEVERE] 12:00:00 --  " + testMessage, savedMessage);
    }

    @Test
    void shouldSaveErrorMessageToFilesystem() {
        String testMessage = "Test error message";
        when(messageFormatter.formatMessageFromTemplate(testMessage)).thenReturn(testMessage);

        logWriter.error(testMessage);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(filesystemOutput).saveMessage(messageCaptor.capture());
        
        String savedMessage = messageCaptor.getValue();
        assertEquals("[ERROR] 12:00:00 --  " + testMessage, savedMessage);
    }

    @Test
    void shouldSaveMultipleMessagesToFilesystem() {
        when(messageFormatter.formatMessageFromTemplate("Message 1")).thenReturn("Message 1");
        when(messageFormatter.formatMessageFromTemplate("Message 2")).thenReturn("Message 2");
        when(messageFormatter.formatMessageFromTemplate("Message 3")).thenReturn("Message 3");

        logWriter.info("Message 1");
        logWriter.debug("Message 2");
        logWriter.error("Message 3");

        verify(filesystemOutput, times(3)).saveMessage(anyString());
    }

    @Test
    void shouldTraceStackFromThrowable() {
        RuntimeException exception = new RuntimeException("Test exception");
        
        when(messageFormatter.formatMessageFromTemplate(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

        logWriter.traceStack(exception);

        verify(filesystemOutput, times(exception.getStackTrace().length)).saveMessage(anyString());
    }

    @Test
    void shouldFormatStackTraceElementsCorrectly() {
        RuntimeException exception = new RuntimeException("Test exception");
        String stackElementString = exception.getStackTrace()[0].toString();
        
        when(messageFormatter.formatMessageFromTemplate(stackElementString)).thenReturn(stackElementString);

        logWriter.traceStack(exception);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(filesystemOutput, times(exception.getStackTrace().length)).saveMessage(messageCaptor.capture());
        
        messageCaptor.getAllValues().forEach(message -> {
            assertTrue(message.contains("[STACK TRACE"));
            assertTrue(message.contains("12:00:00"));
        });
    }

    @Test
    void shouldFormatMessagesWithMultipleArguments() {
        String template = "User {} with id {} performed action {}";
        String formattedMessage = "User John with id 123 performed action login";
        when(messageFormatter.formatMessageFromTemplate(template, "John", "123", "login"))
                .thenReturn(formattedMessage);

        logWriter.info(template, "John", "123", "login");

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(filesystemOutput).saveMessage(messageCaptor.capture());
        
        String savedMessage = messageCaptor.getValue();
        assertTrue(savedMessage.contains(formattedMessage));
    }
}

