package model.video;

import utils.Globals;

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
	}

	@Override
	protected String getDestLoc() {
		String location = Globals.tvShowScheme.replace("[Quality]", this.quality);
		location = location.replace("[TV Show Name]", this.tvShowName);
		location = location.replace("[Season #]", this.season);
		location = location.replace("[Episode #]", this.episode);
		return location;
	}
}
