package org.makechtec.software.amatl.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageFormatterTest {

    @Test
    void shouldFormatMessageWithSingleArgument() {
        MessageFormatter formatter = new MessageFormatter();
        
        String result = formatter.formatMessageFromTemplate("User {} logged in", "admin");
        
        assertEquals("User admin logged in", result);
    }

    @Test
    void shouldFormatMessageWithMultipleArguments() {
        MessageFormatter formatter = new MessageFormatter();
        
        String result = formatter.formatMessageFromTemplate("User {} logged in at port {}", "admin", "8080");
        
        assertEquals("User admin logged in at port 8080", result);
    }
}

