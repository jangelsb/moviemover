package controller;

import model.video.Video;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;

import static java.lang.System.exit;
import static utils.Globals.*;
import static utils.Logging.*;
import static utils.VideoUtil.*;

public class main {

    private static long startTime = 0;
    private static ArrayList<Video> videos = null;
    private static HashSet<String> whiteList = null;
    private static File importDir = null;

    public static void main(String[] args) {

        initGlobals(args);

        setUp();

        if (findNewVideos(importDir)) {
            moveVideos();
        }

        finish();
    }

    public static void setUp() {
        startTime = System.currentTimeMillis();
        importDir = new File(importLoc);

        setUpLogs();

        videos = new ArrayList<>();
        whiteList = loadWhiteListLog();
    }

    public static void setUpLogs() {
        setUpInfoLog();
        if (!whiteListLogExists()) {
            fillWhiteListLog(importDir);
            finish();
        }
    }

    public static void fillWhiteListLog(File importDir) {
        for (File file : importDir.listFiles()) {
            writeToWhiteListLog(file.getName());
        }
        myLogWithTimestamp("Created a whitelist log.");
    }

    public static boolean isFileNew(String fileName) {
        return !whiteList.contains(fileName);
    }

    public static boolean isFileNew(File file) {
        return isFileNew(file.getName());
    }

    // TODO this can if multiple files are in the same folder, this will whitelist the parentfolder multiple times, maybe check if already whitelisted?
    public static void whiteListFile(File importDir, File file) {
        if (file.getParentFile().equals(importDir))
            writeToWhiteListLog(file.getName());
        else
            writeToWhiteListLog(file.getParentFile().getName());
    }

    public static boolean findNewVideos(File importDir) {

        myLogWithTimestamp("Searching for videos...");

        boolean foundMovies = false;
        File currentDir = importDir;
        Queue<File> dirs = new ArrayDeque<>();

        dirs.add(currentDir);

        while (!dirs.isEmpty()) {
            currentDir = dirs.remove();
            for (File file : currentDir.listFiles()) {
                if (isFileNew(file)) {
                    if (isAVideo(file)) {
                        whiteListFile(importDir, file);
                        videos.add(createVideoType(file));
                        myLog(" + " + file.getName());
                        foundMovies = true;
                    } else if (isARarVideo(file)) {
                        whiteListFile(importDir, file);
                        myLog("   > unraring: " + file.getName() + "...");
                        File videoFromRar = getVideoFromRar(file);
                        videos.add(createVideoType(videoFromRar, false));
                        myLog("   > Done.");
                        myLog(" + " + videoFromRar.getName());
                        foundMovies = true;
                    } else if (file.isDirectory()) {
                        dirs.add(file);
                    }
                }
            }
        }

        myLog(foundMovies ? "Done.\n" : "No new videos found.");

        return foundMovies;
    }

    private static void moveVideos() {
        myLogWithTimestamp("Moving videos to correct directories...");

        for (Video video : videos) {
            video.move();
            myLog(" - " + video.getInfo());
        }

        myLog("Done.");
    }

    private static void finish() {
        long stopTime = System.currentTimeMillis();
        myLog(String.format("\nTime Elapsed: %s secs", (stopTime - startTime) / 1000.0));
        myLog("------------------------------------------------------------");
        exit(0);
    }
}
