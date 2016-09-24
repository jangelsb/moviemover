package utils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.extract.ExtractArchive;
import com.github.junrar.rarfile.FileHeader;
import model.video.Movie;
import model.video.TVShow;
import model.video.Video;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.VideoUtil.Type.*;

public class VideoUtil {

    // A number I have come up with from using this program for years, not too small, not too large
    public static final int SIZE_THRESHOLD = 78643200; // 78.64 MBs
    public static final String[] EXTS = {".avi", ".mkv", ".mp4"};

    enum Type {
        MOVIE, TVSHOW
    }

    public static String getExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return index > -1 ? fileName.substring(index) : "unknown";
    }

    public static String cleanUp(String text) {
        return text.replace('.', ' ').trim();
    }

    public static Video createVideoType(File video, boolean copy) {

        Pattern pattern = Pattern.compile("(.*)s(\\d+)e(\\d+).*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(video.getName());

        if(matcher.find()) {
            String tvShowName = matcher.group(1);
            String season = matcher.group(2);
            String episode = matcher.group(3);
            return new TVShow(video, cleanUp(tvShowName), season, episode, copy);
        }

        return new Movie(video, copy);
    }

    public static Video createVideoType(String fileName, boolean copy) {
        return createVideoType(new File(fileName), copy);
    }

    public static Video createVideoType(File video) {
        return createVideoType(video, true);
    }

    public static Video createVideoType(String fileName) {
        return createVideoType(new File(fileName), true);
    }

    public static Type getVideoType(String fileName) {

        Pattern pattern = Pattern.compile("s\\d+e\\d+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);

        if(matcher.find())
            return TVSHOW;

        return MOVIE;
    }

    public static boolean isAVideo(String fileName, long fileSize) {
        HashSet<String> exts = new HashSet<String>(Arrays.asList(VideoUtil.EXTS));
        return exts.contains(VideoUtil.getExtension(fileName)) && fileSize > VideoUtil.SIZE_THRESHOLD;
    }

    public static boolean isAVideo (File file) {
        return file.isFile() && isAVideo(file.getAbsolutePath(), file.length());
    }

    public static boolean isARarVideo(File file) {

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
    public static File getVideoFromRar(final File videoRar) {

        final File destFolder = new File(videoRar.getParentFile().getAbsolutePath());

        ExtractArchive extractArchive = new ExtractArchive();
        extractArchive.extractArchive(videoRar, destFolder);

        File[] fList = destFolder.listFiles();
        for (File file : fList)
            if (isAVideo(file))
                return file;

        return null;
    }



}
