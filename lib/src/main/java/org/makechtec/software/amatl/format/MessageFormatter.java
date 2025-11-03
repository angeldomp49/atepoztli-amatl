package org.makechtec.software.amatl.format;

import java.util.regex.Pattern;

public class MessageFormatter {

    private static final String PLACEHOLDER_SIGN = "{}";

    public String formatMessageFromTemplate(String template, Object... values) {

        var buffer = template;

        for (var value : values) {
            buffer = buffer.replaceFirst("\\{\\}", value.toString());
        }


        return buffer;
    }

    private int countPlaceholders(CharSequence template) {
        var pattern = Pattern.compile("\\{\\}");
        var matcher = pattern.matcher(template);

        var count = 0;
        while (matcher.find()) {
            count++;
        }

        return count;
    }

}
