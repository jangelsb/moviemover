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
		this.destLoc = String.format("%s/%s/%s/Season %s/", this.rootLoc, this.quality, this.tvShowName, this.season);
		super.setUpDest();
	}
}
