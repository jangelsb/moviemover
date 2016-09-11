package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.extract.ExtractArchive;
import com.github.junrar.rarfile.FileHeader;

import model.Video;

public class main {

    static String importLoc = "playground/import/";
    static String whiteListLogLoc = importLoc + ".moviemover";
    static String infoLogLoc = importLoc + ".mmlog";

    static ArrayList<Video> videos = null;
    static HashSet<String> whiteList = null;

    static File importDir = null;
    static File whiteListLog = null;
    static File infoLog = null;

    private static long startTime = 0;

    public static void main(String[] args) {

//        TODO elapsed time for each job, total elapsed time
//        TODO custom parameters

        setUp();

        if(findNewVideos(importDir)) {
            moveVideos();
        }

        finish();
    }

    public static void setUp() {

        startTime = System.currentTimeMillis();

        initializeLogs();

        videos = new ArrayList<>();
        whiteList = new HashSet<>();

        loadWhiteList(whiteListLog);
    }

//  TODO: better name for this function, e.g., initializePIVFiles or something
    public static void initializeLogs() {

        whiteListLog = new File(whiteListLogLoc);
        infoLog = new File(infoLogLoc);
        importDir = new File(importLoc);

        if (!infoLog.exists()) {
            try {
                infoLog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!whiteListLog.exists()) {
            fillWhiteListLog();
            myLog(getCurrentTime() + ": Created new whitelist log.");
            finish();
        }
    }

    public static void loadWhiteList(File mlog) {

        String line = null;

        try {
            FileReader fileReader = new FileReader(mlog);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null)
                whiteList.add(line);

            bufferedReader.close();
        }
        catch(Exception e) {
        }
    }

    public static void fillWhiteListLog() {
        for(File file : importDir.listFiles()) {
            writeToWhiteListLog(file.getName());
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

    public static boolean isFileNew(String fileName) {
        return !whiteList.contains(fileName);
    }

    public static boolean isFileNew(File file) {
        return isFileNew(file.getName());
    }

    private static void moveVideos() {
        myLog(getCurrentTime() + ": Moving videos to correct directories...");

        for(Video video : videos) {
            video.move();
            myLog(" - " + video.getInfo());
        }

        myLog("Done.\n");
    }


    public static boolean findNewVideos(File importDir) {

        myLog(getCurrentTime() + ": Searching for videos...");

        boolean foundMovies = false;
        File currentDir = importDir;
        Queue<File> dirs = new ArrayDeque<>();

        dirs.add(currentDir);

        while(!dirs.isEmpty())
        {
            currentDir = dirs.remove();
            for (File file : currentDir.listFiles())
            {
                if(isFileNew(file))
                {
                    if(isAVideo(file))
                    {
                        whiteListFile(importDir, file);
                        videos.add(Video.createVideoType(file));
                        myLog(" + " + file.getName());
                        foundMovies = true;
                    }
                    else if(isARarVideo(file))
                    {
                        whiteListFile(importDir, file);
                        myLog("   > unraring: " + file.getName() + "...");
                        File videoFromRar = getVideoFromRar(file);
                        videos.add(Video.createVideoType(videoFromRar, false));
                        myLog("   > Done.");
                        myLog(" + " + videoFromRar.getName());
                        foundMovies = true;
                    }
                    else if (file.isDirectory())
                    {
                        dirs.add(file);
                    }
                }
            }
        }

        myLog(foundMovies ? "Done.\n" : "No new videos found.\n");

        return foundMovies;
    }


    public static boolean isAVideo(String fileName, long fileSize) {
        HashSet<String> exts = new HashSet<String>(Arrays.asList(Video.EXTS));
        return exts.contains(Video.getExtension(fileName)) && fileSize > Video.SIZE_THRESHHOLD;
    }

    public static boolean isAVideo (File file) {
        return file.isFile() && isAVideo(file.getAbsolutePath(), file.length());
    }

    public static void whiteListFile(File importDir, File file) {
        if(file.getParentFile().equals(importDir))
            writeToWhiteListLog(file.getName());
        else
            writeToWhiteListLog(file.getParentFile().getName());
    }

    public static void whiteListFile(File file) {
        writeToWhiteListLog(file.getParentFile().getName());
    }
    private static boolean isARarVideo(File file) {

        if (!file.isFile() || !file.getPath().endsWith(".rar"))
            return false;

        Archive rar = null;
        try {
            rar = new Archive(file);
        } catch (RarException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<FileHeader> filesInRar = rar.getFileHeaders();

        for (FileHeader fileHeader : filesInRar) {

            String fileName = fileHeader.getFileNameString();
            long fileSize = fileHeader.getFullUnpackSize();

            if(isAVideo(fileName, fileSize))
                return true;
        }

        return false;
    }

//    TODO there is a chance the rar file has more items than just the one file
//    should clean up all the files made except for the movie files...
    private static File getVideoFromRar(final File videoRar) {

        final File destFolder = new File(videoRar.getParentFile().getAbsolutePath());

        ExtractArchive extractArchive = new ExtractArchive();
        extractArchive.extractArchive(videoRar, destFolder);

        File[] fList = destFolder.listFiles();
        for (File file : fList)
            if (isAVideo(file))
                return file;

        return null;
    }

    private static void myLog(String message) {
        System.out.println(message);
        writeToInfoLog(message);
    }

    private static void myLog(String message, int dest) {
        switch (dest){
            case 0:
                System.out.println(message);
                break;
            case 1:
                writeToInfoLog(message);
                break;
        }
    }


    private static String getCurrentTime() {
        return new Date().toString();
    }

    private static void finish() {
        long stopTime = System.currentTimeMillis();
        myLog(String.format("Time Elapsed: %s secs", (stopTime - startTime)/1000.0));
        myLog("------------------------------");
        exit(0);
    }

}
