package model.video;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import subscene.api.model.Subtitle;

import static utils.Globals.subtitleType;

public abstract class Video {

    private boolean copy;
    protected String quality;
    protected String parentName;
    protected String fileName;
    protected File video = null;
    protected File destination = null;
    protected File subtitle = null;

    /**
     * Return the destination of where this video should go.
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
        this.subtitle = getSubtitles();
    }

    private String getVideoQuality() {

        String fileOrFolder = this.fileName + this.parentName;

        Pattern pattern = Pattern.compile(".*(720p|1080p).*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileOrFolder);

        if (matcher.find())
            return matcher.group(1).toLowerCase();

        return "Non HD";
    }

    private File getSubtitles() {
        return subtitleType != null ? new Subtitle(video, subtitleType).setVerbose(false).download() : null;
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

            if (subtitle != null) {
                FileUtils.moveFileToDirectory(this.subtitle, this.destination, true);
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
        return String.format("%s%s was _%s_ to %s", this.fileName, (subtitle != null ? " (with subs)" : ""), (copy ? "COPIED" : "MOVED"), getDestLoc());
    }

    public String getPreliminaryInfo() {
        return String.format("%s %s%s to %s... ", (copy ? "COPYING" : "MOVING"), this.fileName, (subtitle != null ? " (with subs)" : ""), getDestLoc());
    }
}
