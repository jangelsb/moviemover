package model;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;


public class TVShow extends VideoNew {

	private String tvShowName;
	private String season;
	private String episode;

	public TVShow(File tvShow, String tvShowName, String season, String episode, boolean copy) {
		super(tvShow, copy);
		this.tvShowName = tvShowName;
		this.season = season;
		this.episode = episode;
		this.destLoc = this.tvD + this.quality + "/" + this.tvShowName + "/Season " + this.season + "/";

		super.setUpDest();
	}
	
}
