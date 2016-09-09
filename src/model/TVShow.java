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
	
	public TVShow(File tvShow) {
		this(tvShow, true);
	}

	public TVShow(File tvShow, boolean copy) {
		super(tvShow, copy);
	}

	public TVShow(File tvShow, String tvShowName, String season, String episode, boolean copy) {
		super(tvShow, copy);
		this.tvShowName = tvShowName;
		this.season = season;
		this.episode = episode;
	}

	private String getTitle() {

		// TODO
		// Convert stripNameTV
		// Set season and episode in here
		return "";
	}
	
}
