package utils;

import java.io.*;
import java.util.Date;

import static utils.Globals.*;

public class Logging {

    private static File whiteListLog = null;
    private static File infoLog = null;

    public static void initLogs() {
        whiteListLog = new File(whiteListLogLoc);
        infoLog = new File(infoLogLoc);
        if (!infoLog.exists()) {
            try {
                infoLog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToLog(File log, String text){

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(log,true));
            writer.write(text + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    public static void writeToWhiteListLog(String file) {
        writeToLog(whiteListLog, file);
    }

    public static void writeToInfoLog(String info) {
        writeToLog(infoLog, info);
    }

    public static void myLog(String message) {
        System.out.println(message);
        writeToInfoLog(message);
    }

    public static void myLogWithTimestamp(String message) {
        myLog(getCurrentTime() + ": "  + message);
    }

    public static String getCurrentTime() {
        return new Date().toString();
    }

    public static File getWhiteListLog() {
        return whiteListLog;
    }
}
