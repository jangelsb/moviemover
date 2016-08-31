import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.extract.ExtractArchive;
import com.github.junrar.rarfile.FileHeader;



public class main {
	
	static ArrayList<File> ALLFILES = new ArrayList<>();
	static ArrayList<Video> videos = new ArrayList<>();
	static ArrayList<String> exts = new ArrayList<>();
	
	
	//static String  importL = "/Users/joshangelsberg/Documents/movie testttt/download/";
	static String  importL = "A:\\My Libraries\\BitTorrent\\Complete\\Files\\";
	
	static String movielogloc = importL+".moviemover";
	static String infologloc = importL+".mmlog";

	
	public static boolean nonewmovies = true;

	public static void main(String[] args) {

		ALLFILES.clear();
		videos.clear();
		
		//String importloc = "A:\\My Libraries\\BitTorrent\\Complete\\Files\\"; //TODO location of files/movies to move
		String importloc = importL;
		
		
		
		File mlog = new File(movielogloc);
		File ilog = new File(infologloc);

 
        if(!mlog.exists()) 
        	fillMovieLog();
        
        if(!ilog.exists())
			try {
				ilog.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        
        Date d = new Date();
        
		System.out.println(d.toString()+"\nSearching for new videos / setting up...");
		writeToInfoLog(d.toString()+"\nSearching for new videos / setting up...");
		
		

		nonewmovies=true;
		importVideos(importloc);
		
		if(nonewmovies)
		{
			System.out.println("No new videos found.");
			writeToInfoLog("No new videos found.\n");

		}
		else
		{
			System.out.println("Done.");
			writeToInfoLog("Done.");

			
			printVideos();
			
			
			writeFoundMovie();
			
			System.out.println("Moving videos to correct directories...");
			writeToInfoLog("Moving videos to correct directories...");

			moveVideos();
			
			printVideosInfo();
			writeMovieInfo();
			System.out.println("Done.");
			writeToInfoLog("Done.\n");

		}

		//printVideos();

		
		
        //writeToMovieLog("Test");
		//readFile();
		
	}
	
	
	private static void writeFoundMovie() {
		
			writeToInfoLog("Found movies:");

			for(Video v : videos)
			{
				writeToInfoLog(v.getVideo().getName());
			}
		
		
	}
	
	private static void writeMovieInfo() {


		for(Video v : videos)
		{
			writeToInfoLog(v.getInfo());
		}
	
	
}




	public static void fillMovieLog()
	{
		File importloc = new File(importL);
		
		for(File f : importloc.listFiles())
		{
			writeToMovieLog(f.getAbsolutePath());
		}

	}
	
	public static void writeToMovieLog(String file)
	{
	        BufferedWriter writer = null;
	        try {
	        	
	        	
	            File movielog = new File(movielogloc);

	            // This will output the full path where the file will be written to...
	           // System.out.println(logFile.getCanonicalPath());

	            writer = new BufferedWriter(new FileWriter(movielog,true));
	            writer.write(file+"\n");
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {
	            }
	        }
	    
	}
	
	public static void writeToInfoLog(String info)
	{
	        BufferedWriter writer = null;
	        try {
	        	
	        	
	            File infolog = new File(infologloc);

	            // This will output the full path where the file will be written to...
	           // System.out.println(logFile.getCanonicalPath());

	            writer = new BufferedWriter(new FileWriter(infolog,true));
	            writer.write(info+"\n");
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {
	            }
	        }
	    
	}
	
	public static boolean isFileNew(String fileloc)
	{
		    String line = null;

		    try {
			        // FileReader reads text files in the default encoding.
			        FileReader fileReader = 
			            new FileReader(movielogloc);
	
			        // Always wrap FileReader in BufferedReader.
			        BufferedReader bufferedReader = 
			            new BufferedReader(fileReader);
			//it works great
			        while((line = bufferedReader.readLine()) != null)
			        {
			        	if(line.equalsIgnoreCase(fileloc)) 
			                return false;
			        }	
	
			        // Always close files.
			        bufferedReader.close();			
		    	}
			    catch(Exception e) {			
			    }
			   
		return true;
	}
	
	
	public static void readFile()
	{
    // The name of the file to open.
   // String fileName = ".moviemover";

    // This will reference one line at a time
    String line = null;

    try {
        // FileReader reads text files in the default encoding.
        FileReader fileReader = 
            new FileReader(movielogloc);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = 
            new BufferedReader(fileReader);
//it works great
        while((line = bufferedReader.readLine()) != null) {
        	
        	if(line.equalsIgnoreCase("Hello world!"))  //
                System.out.println("trrrrue");

            System.out.println(line);
        }	

        // Always close files.
        bufferedReader.close();			
    }
    catch(FileNotFoundException ex) {
        System.out.println(
            "Unable to open file '" + 
            		movielogloc + "'");				
    }
    catch(IOException ex) {
        System.out.println(
            "Error reading file '" 
            + movielogloc + "'");
    }
}
	
	private static void moveVideos() {
		
		for(Video v : videos)
		{
			v.move();
		}
	}

	private static void printVideos() {
		
		for(Video v : videos)
		{
			System.out.println(v.getVideo().getAbsolutePath());
		}
	}
	
	private static void printVideosInfo() {
		
		
		for(int i=0; i<videos.size(); i++)
		{
			System.out.println(videos.get(i).getInfo());
		}
		
	}

	public static void importVideos(String directoryName)
	{
		
	    File directory = new File(directoryName);
	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    for (File file : fList) 
	    {
	    	if(isFileNew(file.getAbsolutePath()))
	    	{
		        if (file.isFile() && (file.getAbsolutePath().endsWith(".mkv") || file.getAbsolutePath().endsWith(".avi") || file.getAbsolutePath().endsWith(".mp4")) && file.length()>78643200) // 75mbs  1000000000b = 1GB
		        {
		        	videos.add(new Video(file));
		        	//writeToMovieLog(findParentFile(file));
		        	
		        	
		        	if(file.getParentFile().equals(new File(importL)))
		        		writeToMovieLog(file.getAbsolutePath());
		        	else
		        		writeToMovieLog(file.getParentFile().getAbsolutePath());
		        	nonewmovies=false;
		        } 
		        else if(file.isFile() && (file.getPath().endsWith(".rar") && isAVideo(file))) //&& (rarn.contains("720p") || rarn.contains("1080p"))))
		        {
		        	
		        	
		        	File videoFromRar = getVideoFromRar(file);
		        	videos.add(new Video(videoFromRar,false));
		        	
		        	if(file.getParentFile().equals(new File(importL)))
		        		writeToMovieLog(file.getAbsolutePath());
		        	else
		        		writeToMovieLog(file.getParentFile().getAbsolutePath());

		        	nonewmovies=false;
		        }
		        else if (file.isDirectory()) 
		        {
		        	importVideos(file.getAbsolutePath());
		        }
	    	}
	    }
	}
	
	private static String findParentFile(File f) {
		
		
		String pname = f.getName();
		File basef = f;
		// TODO Auto-generated method stub
		return null;
	}


	private static boolean isAVideo(File f) {
		
		Archive rar = null;
		try {
			rar = new Archive(f);
		} catch (RarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		List<FileHeader> files = rar.getFileHeaders();		
			
		for (FileHeader hd : files)
		{
			
			String filen = hd.getFileNameString();
			long filesize = hd.getFullUnpackSize();
			if(filen.endsWith(".mkv") || filen.endsWith(".avi") || filen.endsWith(".mp4") && filesize>400000000) // 1000000000b = 1GB  //250000000 = 250 MBs
			{
				return true;
			}

		}
		
		return false;
	}

	private static File getVideoFromRar(File f) {

			
			final File rar = f; 

			final File destFolder = new File(rar.getParentFile().getAbsolutePath());
			
			ExtractArchive extractArchive = new ExtractArchive();  
			extractArchive.extractArchive(rar, destFolder);
			
			
			File nmovie = null;
			
			File[] fList = destFolder.listFiles();
		    for (File file : fList) 
		    {
		        if (file.isFile() && (file.getPath().endsWith(".mkv") || file.getPath().endsWith(".avi") || file.getPath().endsWith(".mp4")) && file.length()>400000000) // 1000000000b = 1GB
		        {
		        	nmovie = file;
		        	break;
		        }
		    }
		    
		return nmovie;
	}

	

	
	
	public static void printFiles()
	{
		for(int i=1; i<ALLFILES.size(); i++) //stars at 1 because at location 0 its the base folder
		{
			File f = ALLFILES.get(i);

			if(f.isFile())
				System.out.println("FILE - "+ f.getAbsolutePath());
			else
				System.out.println("FOLDER - "+f.getAbsolutePath());	
		}	
	}
	
	public static void findExts()
	{
		
		for(File f : ALLFILES)
		{	
			String fn = f.getName();
			String fe = fn.substring(fn.lastIndexOf("."));
			boolean checkE = true;
			
			for(String check : exts)
			{
				if(check.equalsIgnoreCase(fe))
				{
					checkE=false;
					break;
				}
			}
			
			if(checkE)
				exts.add(fe);
		}

	}
	
	public static void printExts()
	{
		for(String s : exts)
		{
			System.out.println(s);	
		}
	}
	
	private static void sortFilesBySizeDes()
	{
		for(int i = 0; i<ALLFILES.size(); i++)
		{
			for(int j = 0; j<ALLFILES.size()-1;j++)
			{
				if (ALLFILES.get(j).length() < ALLFILES.get(j+1).length())
				{
					File tmp = ALLFILES.get(j+1);
					ALLFILES.set(j+1, ALLFILES.get(j));
					ALLFILES.set(j, tmp);
				}
			}
		}
	}	

}
