package model;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;


public class TVShow extends VideoNew {

	private String season;
	private String episode;


	public TVShow(File tvShow) {
		this(tvShow, true);
	}

	public TVShow(File tvShow, boolean copy) {
		super(tvShow, copy);
	}


	private String getTitle() {

		// TODO
		// Convert stripNameTV
		// Set season and episode in here
		return "";
	}
	
}
