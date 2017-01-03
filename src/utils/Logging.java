package utils;

import java.io.*;
import java.util.Date;
import java.util.HashSet;

import static utils.Globals.*;

public class Logging {

    private static File whiteListLog = null;
    private static File infoLog = null;


    public static void setUpInfoLog() {
        infoLog = new File(infoLogLoc);

        if (!infoLog.exists()) {
            try {
                infoLog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean whiteListLogExists() {
        whiteListLog = new File(whiteListLogLoc);
        if(!whiteListLog.exists()) {
            return false;
        }
        return true;
    }

    public static void writeToLog(File log, String text, boolean withNewLine){

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(log,true));
            writer.write(text + (withNewLine ? "\n" : ""));
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
        writeToLog(whiteListLog, file, true);
    }

    public static void writeToInfoLog(String info) {
        writeToLog(infoLog, info, true);
    }

    public static void writeToInfoLogNoReturn(String info) {
        writeToLog(infoLog, info, false);
    }

    public static void myLog(String message) {
        System.out.println(message);
        writeToInfoLog(message);
    }

    public static void myLogNoReturn(String message) {
        System.out.print(message);
        writeToInfoLogNoReturn(message);
    }

    public static void myLogWithTimestamp(String message) {
        myLog(getCurrentTime() + ": "  + message);
    }

    public static String getCurrentTime() {
        return new Date().toString();
    }

    public static HashSet<String> loadWhiteListLog() {

        HashSet<String> whiteList = new HashSet<>();

        String line = null;

        try {
            FileReader fileReader = new FileReader(whiteListLog);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null)
                whiteList.add(line);

            bufferedReader.close();
        }
        catch(Exception e) {
            whiteList = new HashSet<>();
        }

        return whiteList;
    }
}
