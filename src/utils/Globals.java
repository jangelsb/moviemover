package utils;

import subscene.api.model.Subtitle;

public class Globals {

    // Logging file locations
    public static String importLoc = "playground/import/";
    public static String whiteListLogLoc = importLoc + ".mmlist";
    public static String infoLogLoc = importLoc + ".mmlog";

    // Video type locations
    public static String tvShowRootLoc = "playground/tvshows";
    public static String movieRootLoc = "playground/movies";

    // Video type schemes
    public static String tvShowScheme = "[Root]/[Quality]/TV Shows/[TV Show Name]/Season [Season #]/";
    public static String movieScheme = "[Root]/[Quality]/";

    // Subtitle type
    public static Subtitle.Type subtitleType = Subtitle.Type.HI;

    public static void initGlobals(String[] args) {

        for (String arg : args) {
            String varName = arg.substring(0, arg.indexOf("="));
            String value = arg.substring(arg.indexOf("=") + 1);

            switch (varName) {
                case "importLoc":
                    if (!value.endsWith("/")) value += "/";
                    importLoc = value;
                    whiteListLogLoc = importLoc + ".mmlist";
                    infoLogLoc = importLoc + ".mmlog";
                    break;
                case "tvShowRootLoc":
                    tvShowRootLoc = value;
                    break;
                case "movieRootLoc":
                    movieRootLoc = value;
                    break;
                case "tvShowScheme":
                    tvShowScheme = value;
                    break;
                case "movieScheme":
                    movieScheme = value;
                    break;
                case "subtitleType":
                    subtitleType = getSubtitleType(value);
                    break;
            }
        }
    }

    private static Subtitle.Type getSubtitleType(String value) {
        switch (value) {
            case "HI":
                return Subtitle.Type.HI;
            case "ForeignLangOnly":
                return Subtitle.Type.ForeignLangOnly;
            case "Normal":
                return Subtitle.Type.Normal;
        }
        return null;
    }

    public static void print() {
        System.out.println(String.format("importLoc=%s, tvShowRootLoc=%s, movieRootLoc=%s, tvShowScheme=%s, movieScheme=%s, subtitleType=%s", importLoc, tvShowRootLoc, movieRootLoc, tvShowScheme, movieScheme, subtitleType));
    }
}
