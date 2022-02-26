package Logger;

import java.io.IOException;
import java.util.logging.*;

public class MyLogger{
    static Logger myLogger;
    public Handler fileHandler;
    SimpleFormatter simpleFormatter;

    private MyLogger() throws IOException {
        myLogger = Logger.getLogger(MyLogger.class.getName());
        fileHandler = new FileHandler("ServerLog.txt", true);
        simpleFormatter = new SimpleFormatter();
        fileHandler.setFormatter(simpleFormatter);
        myLogger.addHandler(fileHandler);
    }

    private static Logger getLogger(){
        if(myLogger == null){
            try {
                new MyLogger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myLogger;
    }
    public static void log(Level level, String msg){
        getLogger().log(level, msg);
        System.out.println(msg);
    }
}