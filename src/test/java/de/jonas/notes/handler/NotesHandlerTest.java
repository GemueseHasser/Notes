package de.jonas.notes.handler;

import de.jonas.notes.Notes;
import de.jonas.notes.constant.FileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class NotesHandlerTest {

    @Test
    public void testDateFormat() {
        final String testDate = "23.04.2025T11-59-00";
        final LocalDateTime testTime = NotesHandler.getDateTime(testDate);
        Assertions.assertEquals(testDate, NotesHandler.getDateTimeText(testTime));
    }

    @Test
    public void testFileSystemAccess() throws IOException {
        final File testFile = new File(FileType.RAW.getFile() + File.separator + "test.txt");
        testFile.createNewFile();
        Assertions.assertTrue(testFile.canWrite());
        Assertions.assertTrue(testFile.canRead());
        testFile.delete();
    }

}
