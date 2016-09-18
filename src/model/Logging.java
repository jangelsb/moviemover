package model;

import java.io.*;
import java.util.Date;
import java.util.HashSet;

public class Logging {

    public static File whiteListLog = null;
    public static File infoLog = null;

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

    public static void myLog(String message, int dest) {
        switch (dest){
            case 0:
                System.out.println(message);
                break;
            case 1:
                writeToInfoLog(message);
                break;
        }
    }


    public static String getCurrentTime() {
        return new Date().toString();
    }
}
