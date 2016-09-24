package model.video;

import utils.Globals;

import java.io.File;

public class TVShow extends Video {

	private String tvShowName;
	private String season;
	private String episode;

	public String rootLoc = Globals.tvshowRootLoc;

	public TVShow(File tvShow, String tvShowName, String season, String episode, boolean copy) {
		super(tvShow, copy);

		this.tvShowName = tvShowName;
		this.season = season;
		this.episode = episode;
	}

	@Override
	protected String getDestLoc() {
		return String.format("%s/%s/%s/Season %s/", this.rootLoc, this.quality, this.tvShowName, this.season);
	}
}
