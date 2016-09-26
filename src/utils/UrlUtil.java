package utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class UrlUtil {
    private static String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

//    String videoName = "Mr.Robot.S02E01.720p.HDTV.x264-KILLERS.mkv";
    public static File downloadSubtitle(File video) {

        try {

            System.out.println("Searching for subtitles for: " + video.getName());
            String videoNamePure = video.getName().substring(0, video.getName().lastIndexOf("."));

            Document doc = Jsoup.connect("https://subscene.com/subtitles/release?q=" + video.getName()).userAgent(userAgent).get();

            // assumes that Golden Beard exists and that his second link is HI, english
            //td.a5 a:containsOwn(GoldenBeard) => finds a td elements with class .a5 that have an 'a' element with text "GoldenBeard"
            String link = null;
            try {
                link = doc.select("td.a5 a:containsOwn(GoldenBeard)").get(1).parent().parent().select("td.a1 a").first().attr("abs:href");
//                System.out.println(link);
            } catch (Exception OutOfBounds) {
                // find the first english subtitle
                link = doc.select("td.a1 span.l.r.neutral-icon:containsOwn(English)").first().parent().parent().select("td.a1 a").first().attr("abs:href");
//                System.out.println(link);
            }

            Document doc2 = Jsoup.connect(link).userAgent(userAgent).get();
            String downloadLink = doc2.select("#downloadButton").first().attr("abs:href");
//        System.out.println(downloadLink);

            // download zip
            downloadFile(downloadLink, "subtitle.zip");
            FileUtil.unzip("subtitle.zip", video.getParent() + "/tmp");
            //delete zip
            new File("subtitle.zip").delete();

            // move and rename srt to the current directory
            File subtitle = (new File(video.getParent() + "/tmp")).listFiles()[0];

            File toReturn = new File(video.getParent() + "/" + videoNamePure + ".en.srt");
            subtitle.renameTo(toReturn);

            // delete tmp folder
            FileUtils.deleteDirectory(new File(video.getParent() + "/tmp"));

//            System.out.println("File: " + toReturn);

            return toReturn;
        } catch (Exception IOException) {
            return null;
        }
    }

    //    http://stackoverflow.com/questions/4797593/java-io-ioexception-server-returned-http-response-code-403-for-url
    public static void downloadFile(String url, String outputFileName) throws IOException {

        String fileName = outputFileName; //The file that will be saved on your computer
        URL link = new URL(url); //The file that you want to download
        URLConnection uc;
        uc = link.openConnection();
        uc.addRequestProperty("User-Agent", userAgent);
        uc.connect();
        uc.getInputStream();
        //Code to download
        InputStream in = new BufferedInputStream(uc.getInputStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();

        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(response);
        fos.close();
        //End download code
    }
}
