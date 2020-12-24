package chatroom.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Log {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");

    public static void client(String format, Object... arguments) {
        log("client", format, arguments);
    }

    public static <T> T client(String format, T arguments) {
        return log("client", format, arguments);
    }

    public static void server(String format, Object... arguments) {
        log("server", format, arguments);
    }

    public static <T> T server(String format, T arguments) {
        return log("server", format, arguments);
    }

    public static void socket(String format, Object... arguments) {
        log("socket", format, arguments);
    }

    public static <T> T socket(String format, T arguments) {
        return log("socket", format, arguments);
    }

    public static void stream(String format, Object... arguments) {
        log("stream", format, arguments);
    }

    public static <T> T stream(String format, T arguments) {
        return log("stream", format, arguments);
    }

    private static void log(String identifier, String format, Object... arguments) {
        System.out.printf(formatter.format(new Date()) + " [" + identifier + "]" + format + "%n", arguments);
    }

    private static <T> T log(String identifier, String format, T argument) {
        log(identifier, format, new Object[]{argument});
        return argument;
    }
}
