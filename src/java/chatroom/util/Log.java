package chatroom.util;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("UnusedReturnValue")
public abstract class Log {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final boolean          debug     = Boolean.parseBoolean(System.getProperty("debug"));
    private static final boolean          enaClient = Boolean.parseBoolean(System.getProperty("logger.client"));
    private static final boolean          enaServer = Boolean.parseBoolean(System.getProperty("logger.server"));
    private static final boolean          enaSocket = Boolean.parseBoolean(System.getProperty("logger.socket"));
    private static final boolean          enaStream = Boolean.parseBoolean(System.getProperty("logger.stream"));

    public static void client(String format, Object... arguments) {
        if (debug || enaClient)
            log("client", format, arguments);
    }

    public static void server(String format, Object... arguments) {
        if (debug || enaServer)
            log("server", format, arguments);
    }

    public static void socket(String format, Object... arguments) {
        if (debug || enaSocket)
            log("socket", format, arguments);
    }

    public static void stream(String format, Object... arguments) {
        if (debug || enaStream)
            log("stream", format, arguments);
    }

    public static void failed() {
        System.exit(-1);
    }

    public static <T> T client(String format, T arguments) {
        if (debug || enaClient)
            return log("client", format, arguments);
        else
            return arguments;
    }

    public static <T> T server(String format, T arguments) {
        if (debug || enaServer)
            return log("server", format, arguments);
        else
            return arguments;
    }

    public static <T> T socket(String format, T arguments) {
        if (debug || enaSocket)
            return log("socket", format, arguments);
        else
            return arguments;
    }

    public static <T> T stream(String format, T arguments) {
        if (debug || enaStream)
            return log("stream", format, arguments);
        else
            return arguments;
    }

    private static void log(String identifier, String format, Object... arguments) {
        System.out.printf(formatter.format(new Date()) + " [" + identifier + "]" + format + "%n", arguments);
    }

    private static <T> T log(String identifier, String format, T argument) {
        log(identifier, format, new Object[]{argument});
        return argument;
    }
}
