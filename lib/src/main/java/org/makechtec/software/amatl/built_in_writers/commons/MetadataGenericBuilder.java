package org.makechtec.software.amatl.built_in_writers.commons;

public class MetadataGenericBuilder {
    
    private final TimeInformationFormatter timeInformationFormatter;

    public MetadataGenericBuilder(TimeInformationFormatter timeInformationFormatter) {
        this.timeInformationFormatter = timeInformationFormatter;
    }


    public String build(String level){
        var metadataBuilder = new StringBuilder();

        metadataBuilder.append(level)
                .append(" ")
                .append(timeInformationFormatter.getCurrentFormattedTime())
                .append(" -- ");
        return metadataBuilder.toString();
    }
    
}
