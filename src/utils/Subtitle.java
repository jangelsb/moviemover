package utils;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

import static utils.UrlUtil.downloadFile;
import static utils.UrlUtil.getDocument;

public class Subtitle {

    private String language  = "English";
    private String lanSuffix = "en";
    private File video = null;

    public Subtitle (File video) {
        this.video = video;
    }
    public Subtitle (File video, String language, String lanSuffix) {
        this.video = video;
        this.language  = language;
        this.lanSuffix = lanSuffix;
    }

    public File download() {

        try {

            System.out.println("Searching for subtitles for: " + video.getName());
            String videoNamePure = video.getName().substring(0, video.getName().lastIndexOf("."));

            Document doc = getDocument("https://subscene.com/subtitles/release?q=" + video.getName());

            String downloadLink = findFirstHISub(doc, videoNamePure);

            if (downloadLink != null) {
                return downloadSub(videoNamePure, downloadLink);
            }

        } catch (Exception IOException) {
        }

        return null;
    }

    // grabs the first non colored HI english sub
    private String findFirstHISub(Document doc, String videoNamePure) throws IOException
    {
        Elements allPossibleSubtitles = doc.select("td.a1 span.l.r.neutral-icon:containsOwn(" + language + ")");

        for (Element row : allPossibleSubtitles) {
            // the language info is three deep in the dom tree
            // if a41 exists, then it is an HI sub
            Elements candidate = row.parent().parent().parent().select("td.a41");
            if (!candidate.isEmpty()) {
                String link = candidate.first().parent().select("td.a1 a").first().attr("abs:href");
                Document doc2 = getDocument(link);
                boolean correctSub = !doc2.select("li.release:contains(" + videoNamePure + ")").isEmpty();
                boolean hasCHI = !doc2.select("li.release:contains(CHI)").isEmpty();
                if(correctSub && !hasCHI) {
                    System.out.println(link);
                    return doc2.select("#downloadButton").first().attr("abs:href");
                }
            }
        }
        return null;

        // if hi doesn't matter, with no check,
//        link = doc.select("td.a1 span.l.r.neutral-icon:containsOwn(English)").first().parent().parent().select("td.a1 a").first().attr("abs:href");

    }

    private File downloadSub(String videoNamePure, String downloadLink) throws IOException {
            // download zip
            downloadFile(downloadLink, "subtitle.zip");
            FileUtil.unzip("subtitle.zip", video.getParent() + "/tmp");
            //delete zip
            new File("subtitle.zip").delete();

            // move and rename srt to the current directory
            File subtitle = (new File(video.getParent() + "/tmp")).listFiles()[0];

            File toReturn = new File(video.getParent() + "/" + videoNamePure + "." + lanSuffix + ".srt");
            subtitle.renameTo(toReturn);

            // delete tmp folder
            FileUtils.deleteDirectory(new File(video.getParent() + "/tmp"));

            System.out.println("File: " + toReturn);

            return toReturn;
    }

}
