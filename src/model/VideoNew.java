package model;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

// I want this to actually be and abstract class
public abstract class VideoNew {

    protected String title;
    protected String quality;
    protected String ext;
    protected String fileName;
    protected File video;


    protected File DestLoc = null;
    // protected String fileName; //full name of file with ext  not the location
    protected String parentName;

    enum Type {
        MOVIE, TVSHOW
    }


//	private boolean type; // true = movie false = TV show
//	private String season;  //eg Season 1 (blank for movies)
//	private String tvname;  //Name of Tv Show or Movie

    //private String quality; // true = 720p false = 1080p
//	private String filetype; //.mkv, .mp4, .avi, .rar

    private boolean copy;// true = copy false = just move

    public VideoNew(File video, boolean copy) {
        this.copy = copy;
        init(video);
    }

    private void init(File video) {
        this.video = video;
        this.fileName = video.getName();
        this.parentName = video.getParentFile().getName();
        this.title = getTitle();
        this.quality = getVideoQuality();
        this.ext = getExtension();
    }

    public static VideoNew createVideoType(File video, boolean copy) {

        Pattern pattern = Pattern.compile("(.*)s(\\d+)e(\\d+).*");
        Matcher matcher = pattern.matcher(video.getName());

        if(matcher.find()) {
            String tvShowName = matcher.group(0);
            String season = matcher.group(1);
            String episode = matcher.group(2);
            return new TVShow(video, cleanUp(tvShowName), season, episode, copy);
        }

        return new Movie(video, copy);
    }

    public static VideoNew createVideoType(File video) {
        return createVideoType(video, true);
    }

    public static VideoNew createVideoType(String fileName, boolean copy) {
        return createVideoType(new File(fileName), copy);
    }

    public static VideoNew createVideoType(String fileName) {
        return createVideoType(new File(fileName));
    }

    public static Type getVideoType(String fileName) {

        Pattern pattern = Pattern.compile("s\\d+e\\d+");
        Matcher matcher = pattern.matcher(fileName);

        if(matcher.find())
            return Type.TVSHOW;

        return Type.MOVIE;
    }

    public static String cleanUp(String text) {
        return text.replace('.', ' ').trim();
    }

    private String getTitle() {

        // TODO
        // Possibly do a combination of stripNameTV and stripNameMovie
        // if it is different for Movies and TVShows, then make this abstract
        // if abstract, will this call the inherited classes?
        return "";
    }


    private String getVideoQuality() {

        String fileOrFolder = this.fileName + this.parentName;

        Pattern pattern = Pattern.compile(".*(720[p|P]|1080[p|P]).*");
        Matcher matcher = pattern.matcher(fileOrFolder);

        if(matcher.find())
            return matcher.group(0);

        return "Non HD";
    }

    private String stripNameMovie() {

        //TODO might not actually be needed
        return "";
    }

    private String getExtension() {
        return this.fileName.substring(this.fileName.lastIndexOf("."));
    }

    // private boolean setupDest() // TODO windows vs mac
    // {
    // 	if(type)
    // 	{
    // 		//DestLoc = new File("A:\\My Libraries\\Videos\\Movies\\"+this.quality+"\\"); //TODO location for movies
    // 		DestLoc = new File(this.movieD+this.quality+this.ossep);
    // 	}
    // 	else
    // 	{//+this.quality+"/TV Shows/"+this.tvname+"/"+this.season+"/");

    // 		//DestLoc = new File("A:\\My Libraries\\Videos\\TV Shows\\"+this.quality+"\\TV Shows\\"+this.tvname+"\\"+this.season+"\\"); //TODO location for tv
    // 		DestLoc = new File(this.tvD+this.quality+this.ossep+"TV Shows"+this.ossep+this.tvname+this.ossep+this.season+this.ossep);
    // 	}	//A:\My Libraries\Videos\TV Shows\   720p\     TV Shows\    Agents of Shield\    Season 1\

    // 	DestLoc.mkdirs(); // instead of mkdir b/c theres ton of new folders
    // 	return true;
    // }


    public boolean move() {

        try {
            if (copy) {
                FileUtils.copyFileToDirectory(this.video, this.DestLoc);
            } else {
                FileUtils.moveFileToDirectory(this.video, this.DestLoc, true);
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
        return this.video + " was " + (copy? "copied" : "moved") + " to " + this.DestLoc.getAbsolutePath();
    }
}
