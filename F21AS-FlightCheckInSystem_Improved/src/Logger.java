import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//需要记录日志的地方调用 Logger.getInstance().log("Your log message");
public class Logger {
    private static Logger instance;
    private static final String LOG_FILE_PATH = "log.txt";

    // 私有构造函数，防止实例化
    private Logger() {
    }

    // 获取 Logger 实例
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // 记录日志信息
    public synchronized void log(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(formattedDateTime + " - " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
