package utils;

import subscene.api.model.Subtitle;

public class Globals {

    // Logging file locations
    public static String importLoc = "playground/import/";
    public static String whiteListLogLoc = importLoc + ".mmlist";
    public static String infoLogLoc = importLoc + ".mmlog";

    // Video type schemes / locations
    public static String tvShowScheme = "playground/tvshows/[Quality]/TV Shows/[TV Show Name]/Season [Season #]/";
    public static String movieScheme = "playground/movies/[Quality]/";

    // Subtitle type
    public static Subtitle.Type subtitleType = Subtitle.Type.HI;

    public static void initGlobals(String[] args) {

        for (String arg : args) {
            String varName = arg.substring(0, arg.indexOf("="));
            String value = arg.substring(arg.indexOf("=") + 1);

            switch (varName) {
                case "importLoc":
                    importLoc = getCorrectImportFormat(value);
                    whiteListLogLoc = importLoc + ".mmlist";
                    infoLogLoc = importLoc + ".mmlog";
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

    private static String getCorrectImportFormat(String value) {
        return !value.endsWith("/") ? value + "/" : value;
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

    public static void printCurrentParams() {
        System.out.println(String.format("importLoc=%s, tvShowScheme=%s, movieScheme=%s, subtitleType=%s", importLoc, tvShowScheme, movieScheme, subtitleType));
    }
}
