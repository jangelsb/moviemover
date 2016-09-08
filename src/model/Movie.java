package model;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;


public class Movie extends VideoNew {

	private String year;


	public Movie(File movie) {
		this(movie, true);
	}

	public Movie(File movie, boolean copy) {
		super(movie, copy);
	}

	private String getTitle() {

		// TODO
		// Convert stripNameMovie
		// Set year in here
		return "";
	}


}
