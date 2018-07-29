package Logging;

public class ErrorLogger extends Logger {

    private boolean flushed;

    public ErrorLogger(String fileName) {
        super(fileName);
    }

    public void logErrorln(String className, int lineNumber, String description) {
        super.appendln("[" + className + " at " + lineNumber + "]: " + description);
    }

    public void logError(String className, int lineNumber, String description) {
        super.append("[" + className + " at " + lineNumber + "]: " + description);
    }

    public void flush() {
        if (flushed) { return; }
        super.flush();
        flushed = true;
    }
}
