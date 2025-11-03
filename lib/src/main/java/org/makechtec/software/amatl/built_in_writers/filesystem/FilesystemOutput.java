package org.makechtec.software.amatl.built_in_writers.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;
import java.util.logging.Logger;

public class FilesystemOutput {

    private static final Logger LOG = Logger.getLogger(FilesystemOutput.class.getName());

    private final FileGenerationSettings settings;
    private Calendar lastCalendarSnapshot;
    private String filename;

    public FilesystemOutput(FileGenerationSettings settings) {
        this.settings = settings;
        this.lastCalendarSnapshot = Calendar.getInstance();
        updateFilename();
    }

    public void saveMessage(final String message) {

        if (hasToChangeOutputFile()) {
            updateFilename();
        }

        var filePath = Paths.get(filename);
        var lastParentDirectoryPath = filePath.getParent();

        try {

            LOG.info("creating log file with next filename: " + filename);


            if(Files.notExists(lastParentDirectoryPath)){
                Files.createDirectories(lastParentDirectoryPath);
            }

            if(Files.notExists(filePath)){
                Files.createFile(filePath);
            }
            

            Files.write(filePath, message.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOG.warning("FileSystemOutput has had a problem resolving the filesystem, so the logs are not being saved");
        }

    }

    private boolean hasToChangeOutputFile() {
        var currentCalendar = Calendar.getInstance();
        var temporaryCalendar = this.createComparableCalendar();
        var comparisonResult = currentCalendar.compareTo(temporaryCalendar);

        if (comparisonResult >= 0) {
            lastCalendarSnapshot = currentCalendar;
            return true;
        }

        return false;
    }

    private Calendar createComparableCalendar() {
        var temporaryCalendar = Calendar.getInstance();

        temporaryCalendar.setTimeInMillis(lastCalendarSnapshot.getTimeInMillis());

        switch (settings.frequencyUnit()) {
            case HOUR -> temporaryCalendar.add(Calendar.HOUR, settings.frequencyQuantity());
            case DAY -> temporaryCalendar.add(Calendar.DAY_OF_MONTH, settings.frequencyQuantity());
            case WEEK -> temporaryCalendar.add(Calendar.DAY_OF_MONTH, settings.frequencyQuantity() * 7);
            case MONTH -> temporaryCalendar.add(Calendar.MONTH, settings.frequencyQuantity());
            case YEAR -> temporaryCalendar.add(Calendar.YEAR, settings.frequencyQuantity());
        }

        return temporaryCalendar;
    }

    private void updateFilename() {
        filename = settings.nameGenerationStrategy().generateFilename(settings.nameSettings());
    }

}
