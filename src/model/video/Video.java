package model.video;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public abstract class Video {

    protected String quality;
    protected String ext;
    protected String fileName;
    protected File video;

    protected File destination = null;
    protected String parentName;

    private boolean copy; // true = copy false = just move

    /**
     * Return the destination for this video.
     */
    protected abstract String getDestLoc();

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

    private String getVideoQuality() {

        String fileOrFolder = this.fileName + this.parentName;

        Pattern pattern = Pattern.compile(".*(720p|1080p).*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileOrFolder);

        if(matcher.find())
            return matcher.group(1).toLowerCase();

        return "Non HD";
    }

    private String getExtension() {
        int index = fileName.lastIndexOf(".");
        return index > -1 ? fileName.substring(index) : "unknown";
    }

    private boolean setUpDest() {
        // add error checking here
        this.destination = new File(getDestLoc());
        return destination.mkdirs();
    }

    //TODO skip if exists
    public boolean move() {

        setUpDest();

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
        return this.fileName + " was _" + (copy? "COPIED" : "MOVED") + "_ to " + this.destination.getPath();
    }
}
