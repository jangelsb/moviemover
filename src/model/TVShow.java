package model;

import java.io.File;

public class TVShow extends Video {

	private String tvShowName;
	private String season;
	private String episode;

	public static String rootLoc = "playground/tvshows";

	public TVShow(File tvShow, String tvShowName, String season, String episode, boolean copy) {
		super(tvShow, copy);

		this.tvShowName = tvShowName;
		this.season = season;
		this.episode = episode;

		// TODO this doesn't actually make sense for a user to implement,
		// figure out how to make a subclass set up this variable and call that super method
		// it doesn't have to be this way, but it needs to set up destLoc in Video
		this.destLoc = String.format("%s/%s/%s/Season %s/", this.rootLoc, this.quality, this.tvShowName, this.season);
		super.setUpDest();
	}
}
