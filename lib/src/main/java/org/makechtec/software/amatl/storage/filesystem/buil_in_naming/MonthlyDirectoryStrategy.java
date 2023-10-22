package org.makechtec.software.amatl.storage.filesystem.buil_in_naming;

import org.makechtec.software.amatl.storage.filesystem.naming.NameGenerationStrategy;
import org.makechtec.software.amatl.storage.filesystem.naming.NameSettings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MonthlyDirectoryStrategy implements NameGenerationStrategy {
    @Override
    public String generateFilename(NameSettings settings) {

        var monthFormatter = new SimpleDateFormat("MM-yyyy");
        var monthString = monthFormatter.format(Calendar.getInstance().getTime());

        var dayFormatter = new SimpleDateFormat("-dd__hh-mm-ss");
        var dayString = dayFormatter.format(Calendar.getInstance().getTime());

        return
                settings.rootDirectory() +
                 File.pathSeparator +
                 monthString +
                 File.pathSeparator +
                 settings.prefix() +
                 dayString +
                 settings.suffix();

    }
}
