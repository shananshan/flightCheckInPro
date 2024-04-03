import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Call Logger.getInstance().log("Your log message") wherever you need to log.
public class Logger {
    private static Logger instance;
    private static final String LOG_FILE_PATH = "log.txt";

    // Private constructor to prevent instantiation
    private Logger() {
    }

    // Get Logger instance
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
            // Clear the content of the existing log file
            clearLogFile();
        }
        return instance;
    }

    // Log the message
    public synchronized void log(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) { // Change this true to false to rewrite instead of append
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(formattedDateTime + " - " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clear the content of the log file
    private static void clearLogFile() {
        try {
            new FileWriter(LOG_FILE_PATH, false).close(); // The false parameter will clear the file content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

