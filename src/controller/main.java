package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

import static model.Logging.*;
import static model.QuasiConsts.importLoc;
import static model.QuasiConsts.infoLogLoc;
import static model.QuasiConsts.whiteListLogLoc;

import model.Video;
import static utils.VideoUtil.*;

public class main {

    static ArrayList<Video> videos = null;
    static HashSet<String> whiteList = null;

    static File importDir = null;

    private static long startTime = 0;

    public static void main(String[] args) {

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

        whiteList = loadWhiteList();
    }

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
            myLogWithTimestamp("Created new whitelist log.");
            finish();
        }
    }

    public static HashSet<String> loadWhiteList() {

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
        }

        return whiteList;
    }

    public static void fillWhiteListLog() {
        for(File file : importDir.listFiles()) {
            writeToWhiteListLog(file.getName());
        }
    }

    public static boolean isFileNew(String fileName) {
        return !whiteList.contains(fileName);
    }

    public static boolean isFileNew(File file) {
        return isFileNew(file.getName());
    }

    private static void moveVideos() {
        myLogWithTimestamp("Moving videos to correct directories...");

        for(Video video : videos) {
            video.move();
            myLog(" - " + video.getInfo());
        }

        myLog("Done.");
    }

    public static boolean findNewVideos(File importDir) {

        myLogWithTimestamp("Searching for videos...");

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
                        videos.add(createVideoType(file));
                        myLog(" + " + file.getName());
                        foundMovies = true;
                    }
                    else if(isARarVideo(file))
                    {
                        whiteListFile(importDir, file);
                        myLog("   > unraring: " + file.getName() + "...");
                        File videoFromRar = getVideoFromRar(file);
                        videos.add(createVideoType(videoFromRar, false));
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

        myLog(foundMovies ? "Done.\n" : "No new videos found.");

        return foundMovies;
    }

    public static void whiteListFile(File importDir, File file) {
        if(file.getParentFile().equals(importDir))
            writeToWhiteListLog(file.getName());
        else
            writeToWhiteListLog(file.getParentFile().getName());
    }

    private static void finish() {
        long stopTime = System.currentTimeMillis();
        myLog(String.format("\nTime Elapsed: %s secs", (stopTime - startTime)/1000.0));
        myLog("------------------------------");
        exit(0);
    }
}
