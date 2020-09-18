package org.company;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FlakyTest {
    String testMarkerFile = "testMarker.txt";

    @Test
    public void flaky() throws IOException {
        if (!new java.io.File(testMarkerFile).exists()) {
            System.out.println("I'm failing!");
            Path path = Paths.get(testMarkerFile);
            Files.write(path, "Hello".getBytes());
            Assert.assertTrue(false);
        } else {
            System.out.println("I'm passing!");
        }
    }
}
