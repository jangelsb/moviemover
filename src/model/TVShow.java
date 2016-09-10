package model;

import java.io.File;

public class TVShow extends Video {


	private String tvShowName;
	private String season;
	private String episode;

	public TVShow(File tvShow, String tvShowName, String season, String episode, boolean copy) {
		super(tvShow, copy);

		this.tvShowName = tvShowName;
		this.season = season;
		this.episode = episode;
		this.destLoc = this.tvLoc + this.quality + "/" + this.tvShowName + "/Season " + this.season + "/";
		super.setUpDest();
	}
}
