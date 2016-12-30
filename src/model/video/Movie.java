package model.video;

import utils.Globals;

import java.io.File;

public class Movie extends Video {

	private String year;

	public Movie(File movie) {
		this(movie, true);
	}

	public Movie(File movie, boolean copy) {
		super(movie, copy);
	}

	@Override
	protected String getDestLoc() {
		return Globals.movieScheme.replace("[Quality]", this.quality);
	}
}
