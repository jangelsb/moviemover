package model;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public abstract class Video {

    protected String quality;
    protected String ext;
    protected String fileName;
    protected File video;

    protected String destLoc;
    protected File destination = null;
    protected String parentName;

    private boolean copy; // true = copy false = just move

    // A number I have come up with from using this program for years, not too small, not too large
    public static final int SIZE_THRESHOLD = 78643200; // 78.64 MBs
    public static final String[] EXTS = {".avi", ".mkv", ".mp4"};

    enum Type {
        MOVIE, TVSHOW
    }

    protected Video(File video, boolean copy) {
        this.copy = copy;
        init(video);
    }

    private void init(File video) {
        this.video = video;
        this.fileName = video.getName();
        this.parentName = video.getParentFile().getName();
        this.quality = getVideoQuality();
        this.ext = getExtension();
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

    public static Video createVideoType(File video) {
        return createVideoType(video, true);
    }

    public static Video createVideoType(String fileName, boolean copy) {
        return createVideoType(new File(fileName), copy);
    }

    public static Video createVideoType(String fileName) {
        return createVideoType(new File(fileName));
    }

    public static Type getVideoType(String fileName) {

        Pattern pattern = Pattern.compile("s\\d+e\\d+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);

        if(matcher.find())
            return Type.TVSHOW;

        return Type.MOVIE;
    }

    public static String cleanUp(String text) {
        return text.replace('.', ' ').trim();
    }

    private String getVideoQuality() {

        String fileOrFolder = this.fileName + this.parentName;

        Pattern pattern = Pattern.compile(".*(720p|1080p).*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileOrFolder);

        if(matcher.find())
            return matcher.group(1).toLowerCase();

        return "Non HD";
    }

    private String getExtension() {
        return Video.getExtension(this.fileName);
    }

    public static String getExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return index > -1 ? fileName.substring(index) : "unknown";
    }

    protected boolean setUpDest() {
        this.destination = new File(destLoc);
        return destination.mkdirs();
    }

    //TODO skip if exists
    public boolean move() {

        try {
            if (copy) {
                FileUtils.copyFileToDirectory(this.video, this.destination);
            } else {
                FileUtils.moveFileToDirectory(this.video, this.destination, true);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public File getVideo() {
        return video;
    }

    public String getInfo() {
        return this.fileName + " was " + (copy? "COPIED" : "MOVED") + " to " + this.destination.getPath();
    }
}
