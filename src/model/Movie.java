package model;

import java.io.File;


public class Movie extends Video {

	private String year;

	public Movie(File movie) {
		this(movie, true);
	}

	public Movie(File movie, boolean copy) {
		super(movie, copy);
		this.destLoc = this.movieD + this.quality + "/";
		super.setUpDest();
	}
}
