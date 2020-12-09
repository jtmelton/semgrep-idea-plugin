package com.github.jtmelton.semgrepideaplugin.utils;

import com.intellij.openapi.diagnostic.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileUtil {

    private static final Logger LOGGER = Logger.getInstance(FileUtil.class.getName());

    public static int getStartOffset(String filepath, int lineNumSearch, int colNumSearch) {
        int offset = 1;

        try (Scanner scanner = new Scanner(new File(filepath))) {
            scanner.useDelimiter("");

            // start at offset 1
            int lineNumber = 1;
            int charNumber = 1;
            int colNumber = 1;

            while(scanner.hasNext()) {
                char c = scanner.next().charAt(0);

                if(lineNumber == lineNumSearch && colNumber == colNumSearch) {
                    // FOUND!
                    LOGGER.debug("found item at ");
                    LOGGER.debug("\tlineNumber: " + lineNumber);
                    LOGGER.debug("\tcharNumber: " + charNumber);
                    LOGGER.debug("\tcolNumber: " + colNumber);
                    LOGGER.debug("\tlineNumSearch: " + lineNumSearch);
                    LOGGER.debug("\tcolNumSearch: " + colNumSearch);
                    offset = charNumber;
                    break;
                }

                charNumber++;
                colNumber++;

                if(c == System.getProperty("line.separator").charAt(0)) {
                    lineNumber++;
                    colNumber = 1;  // reset
                }

                // do something with char
            }

        } catch(FileNotFoundException fnfe) {
            LOGGER.error(fnfe);
        }

        return offset;
    }

}
