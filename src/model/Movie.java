package model;

import java.io.File;

public class Movie extends Video {

	private String year;

	public static String rootLoc = "playground/movies";

	public Movie(File movie) {
		this(movie, true);
	}

	public Movie(File movie, boolean copy) {
		super(movie, copy);

		// TODO this doesn't actually make sense for a user to implement,
		// figure out how to make a subclass set up this variable and call that super method
		// it doesn't have to be this way, but it needs to set up destLoc in Video
		this.destLoc = String.format("%s/%s/", this.rootLoc, this.quality);
		super.setUpDest();
	}
}
