package Logging;

import java.io.FileWriter;

public class Logger {

    private String fileName;
    private StringBuilder contents = new StringBuilder();

    public Logger(String nameOfFile) {
        fileName = nameOfFile;
    }

    public void appendln(String str) {
        if (str == null) { return; }
        contents.append(str + "\n");
    }

    public void append(String str) {
        if (str == null) { return; }
        contents.append(str);
    }

    public void flush() {
        FileWriter fw = null;

        try {
            fw = new FileWriter(fileName, false);
            fw.write(contents.toString());
            fw.close();
        } catch (Exception ex) {
            contents.append(ex.getMessage());
        }
    }
}
