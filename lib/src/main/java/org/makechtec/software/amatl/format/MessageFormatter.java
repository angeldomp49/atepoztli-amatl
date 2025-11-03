package org.makechtec.software.amatl.format;

public class MessageFormatter {


    public String formatMessageFromTemplate(String template, Object... values) {

        var buffer = template;

        for (var value : values) {
            buffer = buffer.replaceFirst("\\{\\}", value.toString());
        }


        return buffer;
    }

}
