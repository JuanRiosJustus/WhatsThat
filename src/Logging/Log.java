package Logging;

public class Log {

    protected Log() { }

    private static ErrorLogger logger = null;
    public static ErrorLogger getInstance() {
        if (logger == null) {
            logger = new ErrorLogger("Errors.txt");
        }
        return logger;
    }
}
