package model.video;

import utils.Globals;

import java.io.File;

public class Movie extends Video {

	private String year;

	public static String rootLoc = Globals.movieRootLoc;

	public Movie(File movie) {
		this(movie, true);
	}

	public Movie(File movie, boolean copy) {
		super(movie, copy);
	}

	@Override
	protected String getDestLoc() {
		return String.format("%s/%s/", this.rootLoc, this.quality);
	}
}