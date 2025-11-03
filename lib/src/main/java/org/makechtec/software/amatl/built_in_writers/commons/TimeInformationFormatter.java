package org.makechtec.software.amatl.built_in_writers.commons;

import java.text.SimpleDateFormat;

public class TimeInformationFormatter {

    public String getCurrentFormattedTime() {
        var simpleTimeFormatter = new SimpleDateFormat("HH:mm:ss");
        return simpleTimeFormatter.format(System.currentTimeMillis());
    }

}
