package model;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;


public class Video {

	
//	private String movieD = "/Users/joshangelsberg/Documents/movie testttt/destfolder/Movies/";
//	private String tvD = "/Users/joshangelsberg/Documents/movie testttt/destfolder/TV Shows/";
//	private String ossep = "/";
	
//	private String movieD = "A:\\My Libraries\\Videos\\Movies\\";
//	private String tvD = "A:\\My Libraries\\Videos\\TV Shows\\";
//	private String ossep = "\\";
//	private String movieD = "/Users/joshangelsberg/Documents/programming/moviemover/playground/movies/";
//	private String tvD = "/Users/joshangelsberg/Documents/programming/moviemover/playground/tvshows/";
//	private String ossep = "/";

	private String movieD = "playground/movies/";
	private String tvD = "playground/tvshows/";
	private String ossep = "/";
	
	

	
	private File movieFile;
	private File DestLoc = null;

	private String filename; //full name of file with ext  not the location
	private String parentName;  

	private boolean type; // true = movie false = TV show
		private String season;  //eg Season 1 (blank for movies)
		private String tvname;  //Name of Tv Show or Movie

	private String quality; // true = 720p false = 1080p
	private String filetype; //.mkv, .mp4, .avi, .rar
	
	private boolean copy=true; // true = copy false = just move	
	
	public Video(File f) {
		
		
		this.copy=true;;
		fillVideo(f);
	}




	public Video(File f, boolean b) {
		
		this.copy=b;
		fillVideo(f);
	}




	private void fillVideo(File f) {

		this.movieFile = f;
		this.filename = f.getName();
		this.parentName = f.getParentFile().getName();
		
		this.quality = getVideoQuality();
		
		this.type = videoType(); // if a show season is filled and the tvname is filled		

		this.filetype = getFileType();
		
		
		setupDest();
		
		
	}



//Determines if its a movie or a tv show
	private boolean videoType() {
		String fname = this.filename.toLowerCase();
		String pname = this.parentName.toLowerCase();
 
		//System.out.println(fname);

		Pattern pattern = Pattern.compile("s+\\d\\d+e+\\d\\d"); // vs   s+\\d\\d+e+\\d
		Matcher matcherF = pattern.matcher(fname);
		
		int start=0;  //start of the data
		String data = ""; // eg S02E05
		
		boolean found = false;
		
		  while (matcherF.find()) {
		        start= matcherF.start();
		        data=matcherF.group();
		        found = true;
		    }
		  
		if(found)
		{
			//System.out.print(data + "  loc : " + start + " |||  ");
			int sni = Integer.parseInt(data.substring(1, 3));
			this.tvname = stripNameTV(start);
			this.season = "Season " + sni;
			return false;
		}
		
		else
		{
			stripNameMovie();
			return true;
		}
	}




	private String getVideoQuality() {		
		
		if(this.filename.contains("720p") || this.parentName.contains("720p"))
			return "720p";
		else if(this.filename.contains("1080p") || this.parentName.contains("1080p"))
			return "1080p";
		else 
			return "Non HD";
	}
	
	private String stripNameTV(int start) {
		
		
		String sn = this.filename.substring(0, start);
		
		sn=sn.replace('.', ' ');
		
		int loc720 = sn.indexOf("720p");
		int loc1080 = sn.indexOf("1080p");

		if(loc720!=-1)
		{
			sn=sn.substring(0, loc720);
		}
		else if(loc1080!=-1)
		{
			sn=sn.substring(0, loc1080);
		}
		
		while(sn.endsWith(" "))
		{
			sn=sn.substring(0,sn.length()-1);
		}

		//add functionality to capitalize first letter of each word TODO
	
		return sn;
	}
	
	private String stripNameMovie() {
		
		
		String s = this.filename;
		
		s=s.replace('.', ' ');
		s=s.replace('.', ' ');
		s=s.replace("-", "");
		s=s.replace("_", "");
		s=s.replace("BlueRay", "");
		s=s.replace("Extended", "");
		s=s.replace("EXTENDED", "");
		s=s.replace("extended", "");
		s=s.replace("CUT", "");
		s=s.replace("BR", "");
		s=s.replace("BDRip", "");
		
		int loc720 = s.indexOf("720p");
		int loc1080 = s.indexOf("1080p");

		if(loc720!=-1)
		{
			s=s.substring(0, loc720);
		}
		else if(loc1080!=-1)
		{
			s=s.substring(0, loc1080);
		}
		
		while(s.endsWith(" "))
		{
			s=s.substring(0,s.length()-1);
		}

		//add functionality to capitalize first letter of each word TODO
	
		return s;
	}
	
	private String getFileType()
	{
		return this.filename.substring(this.filename.lastIndexOf("."));
	}
	
	private boolean setupDest() // TODO windows vs mac
	{	
		if(type)
		{
			//DestLoc = new File("A:\\My Libraries\\Videos\\Movies\\"+this.quality+"\\"); //TODO location for movies
			DestLoc = new File(this.movieD+this.quality+this.ossep);
		}
		else
		{//+this.quality+"/TV Shows/"+this.tvname+"/"+this.season+"/");
			
			//DestLoc = new File("A:\\My Libraries\\Videos\\TV Shows\\"+this.quality+"\\TV Shows\\"+this.tvname+"\\"+this.season+"\\"); //TODO location for tv
			DestLoc = new File(this.tvD+this.quality+this.ossep+"TV Shows"+this.ossep+this.tvname+this.ossep+this.season+this.ossep);
		}	//A:\My Libraries\Videos\TV Shows\   720p\     TV Shows\    Agents of Shield\    Season 1\
		
		DestLoc.mkdirs(); // instead of mkdir b/c theres ton of new folders
		return true;
	}


	public boolean move()
	{
		if(copy)
		{
			try {
				FileUtils.copyFileToDirectory(this.movieFile, this.DestLoc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		else
		{
			try{
				
				FileUtils.moveFileToDirectory(this.movieFile,this.DestLoc, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return true;
	}
	
	/*{
		
			if(rared)
			{
				extract;
			}
	}*/




	public File getVideo() {
		// TODO Auto-generated method stub
		return this.movieFile;
	}
	
	public String getInfo() {
		// TODO Auto-generated method stub
		String info = "";
		if(copy)
			info+=this.filename+" was copied to " + this.DestLoc.getAbsolutePath();
		else
			info+=this.filename+" was moved to " + this.DestLoc.getAbsolutePath();
		
		return info;
	}
}
