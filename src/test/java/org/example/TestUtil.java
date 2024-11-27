package org.example;

import java.io.*;

public class TestUtil {

    public static BufferedReader getBufferedReader(final String input) {
        final InputStream in = new ByteArrayInputStream(input.getBytes());
        return new BufferedReader(new InputStreamReader(in));
    }

    public static ByteArrayOutputStream setOutToByteArray() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        return output;
    }

    public static void clearSetOutToByteArray(final ByteArrayOutputStream output) {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        try {
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTestFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }
}
