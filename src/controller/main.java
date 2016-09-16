package controller;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.lang.System.exit;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.extract.ExtractArchive;
import com.github.junrar.rarfile.FileHeader;

import static model.Logging.*;
import static model.QuasiConsts.importLoc;
import static model.QuasiConsts.infoLogLoc;
import static model.QuasiConsts.whiteListLogLoc;

import model.Video;

public class main {

    static ArrayList<Video> videos = null;
    static HashSet<String> whiteList = null;

    static File importDir = null;

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
            myLog(getCurrentTime() + ": Created new whitelist log.");
            finish();
        }
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
        return exts.contains(Video.getExtension(fileName)) && fileSize > Video.SIZE_THRESHOLD;
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
