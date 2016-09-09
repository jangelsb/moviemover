package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.extract.ExtractArchive;
import com.github.junrar.rarfile.FileHeader;

import model.VideoNew;


public class mainNew {

    static final int VIDEO_SIZE_THRESHHOLD = 78643200;

    static ArrayList<VideoNew> videos = new ArrayList<>();
    static HashSet<String> whiteList = new HashSet<>();

    static String  importL = "playground/import/";
    static String movielogloc = importL+".moviemover";
    static String infologloc = importL+".mmlog";


    public static boolean noNewMovies = true;

    public static void mainNew(String[] args) {


        setUp();

        String importloc = importL;

        File mlog = new File(movielogloc);
        File ilog = new File(infologloc);


        if (!ilog.exists()) {
            try {
                ilog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!mlog.exists()) {
            fillMovieLog();
            return;
        }




//        load mlog into a hashset used to compare with newfiles

        loadWhiteList(mlog);

        if(importVideos(importloc)) {
            myLog("Done.\n");
//            writeFoundMovie();
            myLog("Moving videos to correct directories...");
            moveVideos();
//            writeMovieInfo();
            myLog("Done.");
        }
        else {
            myLog("No new videos found.");
        }


        myLog("------------------------------");

        //printVideos();



        //writeToMovieLog("Test");
        //readFile();

    }

    public static void setUp() {
        videos.clear();
        whiteList.clear();

        // add other setup stuff
    }

    public static void loadWhiteList(File mlog) {

        Date d = new Date();
        myLog(d.toString()+"\nSearching for new videos / setting up...");

        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(mlog);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            //it works great
            while((line = bufferedReader.readLine()) != null)
                whiteList.add(line);

            // Always close files.
            bufferedReader.close();
        }
        catch(Exception e) {
        }
    }


//    private static void writeFoundMovie() {
//
//        myLog("Found movies:");
//
//        for(VideoNew v : videos)
//        {
//            myLog(v.getVideo().getName());
//        }
//
//        myLog(""); // adds a new line
//
//
//    }
//
//    private static void writeMovieInfo() {
//
//
//        for(VideoNew v : videos)
//        {
//            myLog(v.getInfo());
//        }
//
//
//    }




    public static void fillMovieLog() {
        File importloc = new File(importL);

        for(File f : importloc.listFiles()) {
            writeToMovieLog(f.getAbsolutePath());
        }

    }

    public static void writeToMovieLog(String file) {
        BufferedWriter writer = null;
        try {


            File movielog = new File(movielogloc);

            // This will output the full path where the file will be written to...
            // System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(movielog,true));
            writer.write(file+"\n");
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

    public static void writeToInfoLog(String info) {
        BufferedWriter writer = null;
        try {


            File infolog = new File(infologloc);

            // This will output the full path where the file will be written to...
            // System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(infolog,true));
            writer.write(info+"\n");
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

    public static boolean isFileNew(String fileloc) {
        return !whiteList.contains(fileloc);
    }


    public static void readFile() {
        // The name of the file to open.
        // String fileName = ".moviemover";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(movielogloc);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
//it works great
            while((line = bufferedReader.readLine()) != null) {

                if(line.equalsIgnoreCase("Hello world!"))  //
                    System.out.println("trrrrue");

                System.out.println(line);
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            movielogloc + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + movielogloc + "'");
        }
    }

    private static void moveVideos() {
        for(VideoNew v : videos) {
            v.move();
        }
    }

    private static void printVideos() {
        for(VideoNew v : videos) {
            System.out.println(v.getVideo().getAbsolutePath());
        }
    }

    private static void printVideosInfo() {
        for(int i=0; i<videos.size(); i++) {
            System.out.println(videos.get(i).getInfo());
        }
    }

    public static boolean importVideos(String directoryName) {

        boolean foundMovies = false;

        File directory = new File(directoryName);
        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if(isFileNew(file.getAbsolutePath())) {

                if(isAVideo(file)) {
//                if (file.isFile() && (file.getAbsolutePath().endsWith(".mkv") || file.getAbsolutePath().endsWith(".avi") || file.getAbsolutePath().endsWith(".mp4")) && file.length()>78643200) { // 75mbs  1000000000b = 1GB
                    videos.add(VideoNew.createVideoType(file));
                    //writeToMovieLog(findParentFile(file));


                    if(file.getParentFile().equals(new File(importL)))
                        writeToMovieLog(file.getAbsolutePath());
                    else
                        writeToMovieLog(file.getParentFile().getAbsolutePath());
                    foundMovies = true;
                }
                else if(isARarVideo(file)) {
//                else if(file.isFile() && (file.getPath().endsWith(".rar") && isARarVideo(file))) { //&& (rarn.contains("720p") || rarn.contains("1080p"))))

                    // move extraction code to a new function (it's currently in isARarVideo
                    
                    File videoFromRar = getVideoFromRar(file);
                    videos.add(VideoNew.createVideoType(videoFromRar, false));

                    if(file.getParentFile().equals(new File(importL)))
                        writeToMovieLog(file.getAbsolutePath());
                    else
                        writeToMovieLog(file.getParentFile().getAbsolutePath());

                    foundMovies = true;
                }
                else if (file.isDirectory()) {
                    importVideos(file.getAbsolutePath());
                }
            }
        }

        return foundMovies;
    }

    public static boolean isAVideo (File file) {
        return file.isFile() && (file.getAbsolutePath().endsWith(".mkv") || file.getAbsolutePath().endsWith(".avi") || file.getAbsolutePath().endsWith(".mp4")) && file.length() > VIDEO_SIZE_THRESHHOLD;
    }
    private static String findParentFile(File f) {

        String pname = f.getName();
        File basef = f;
        // TODO Auto-generated method stub
        return null;
    }


    private static boolean isARarVideo(File f) {

        if (!f.isFile() || !f.getPath().endsWith(".rar"))
            return false;

        Archive rar = null;
        try {
            rar = new Archive(f);
        } catch (RarException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<FileHeader> files = rar.getFileHeaders();

        for (FileHeader hd : files) {

            String filen = hd.getFileNameString();
            long filesize = hd.getFullUnpackSize();
            if(filen.endsWith(".mkv") || filen.endsWith(".avi") || filen.endsWith(".mp4") && filesize>400000000) { // 1000000000b = 1GB  //250000000 = 250 MBs
                return true;
            }

        }

        return false;
    }

    private static File getVideoFromRar(File f) {


        final File rar = f;

        final File destFolder = new File(rar.getParentFile().getAbsolutePath());

        ExtractArchive extractArchive = new ExtractArchive();
        extractArchive.extractArchive(rar, destFolder);


        File nmovie = null;

        File[] fList = destFolder.listFiles();
        for (File file : fList) {
            if (file.isFile() && (file.getPath().endsWith(".mkv") || file.getPath().endsWith(".avi") || file.getPath().endsWith(".mp4")) && file.length()>400000000) { // 1000000000b = 1GB
                nmovie = file;
                break;
            }
        }

        return nmovie;
    }

    private static void myLog(String message) {
        System.out.println(message);
        writeToInfoLog(message);
    }

//    public static void printFiles() {
//        for(int i=1; i<ALLFILES.size(); i++) { //stars at 1 because at location 0 its the base folder
//            File f = ALLFILES.get(i);
//
//            if(f.isFile())
//                System.out.println("FILE - "+ f.getAbsolutePath());
//            else
//                System.out.println("FOLDER - "+f.getAbsolutePath());
//        }
//    }

//    public static void findExts() {
//
//        for(File f : ALLFILES) {
//            String fn = f.getName();
//            String fe = fn.substring(fn.lastIndexOf("."));
//            boolean checkE = true;
//
//            for(String check : exts) {
//                if(check.equalsIgnoreCase(fe)) {
//                    checkE=false;
//                    break;
//                }
//            }
//
//            if(checkE)
//                exts.add(fe);
//        }
//
//    }

//    public static void printExts() {
//        for(String s : exts) {
//            System.out.println(s);
//        }
//    }

//    bubble sort...bad
//    private static void sortFilesBySizeDes() {
//        for(int i = 0; i<ALLFILES.size(); i++) {
//            for(int j = 0; j<ALLFILES.size()-1;j++) {
//                if (ALLFILES.get(j).length() < ALLFILES.get(j+1).length()) {
//                    File tmp = ALLFILES.get(j+1);
//                    ALLFILES.set(j+1, ALLFILES.get(j));
//                    ALLFILES.set(j, tmp);
//                }
//            }
//        }
//    }

}
