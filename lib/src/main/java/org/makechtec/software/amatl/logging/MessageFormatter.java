package org.makechtec.software.amatl.logging;

import java.util.Arrays;
import java.util.regex.Pattern;

public class MessageFormatter {

    private static final String PLACEHOLDER_SIGN = "{}";

    public String cleanMessage(CharSequence template, Object... values){

        var buffer = template.toString();

        for(var value: values){
            buffer = buffer.replace("{}", value.toString());
        }



        return buffer;
    }

    private int findPlaceholdersCount(CharSequence template){
        var pattern = Pattern.compile("\\{\\}");
        var matcher = pattern.matcher(template);

        var count = 0;
        while(matcher.find()){
            count++;
        }

        return count;
    }

}
