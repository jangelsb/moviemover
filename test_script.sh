# importLoc:      the direcotry where you want the script to "watch" it will create a few files for tracking in this folder
# tvShowRootLoc:  the root diretory for tvshows to go in
# movieRootLoc:   the root diretory for movies to go in
# tvShowScheme:   the scheme you want the tvshows to be in. Must start with [ROOT]. Keywords consist of: [Root], [Quality], [TV Show Name], [Season #]
# movieScheme:    the scheme you want the movies to be in. Must start with [ROOT]. Keywords consist of: [Root], [Quality]
# subtitleType:   the type of subtiles you want: None, HI, ForLangOnly, Normal

java -jar ./out/artifacts/moviemover_jar/moviemover.jar importLoc="./playground/import/" tvShowRootLoc="./playground/TV Shows" movieRootLoc="./playground/Movies" movieScheme="[Root]/[Quality]/" tvShowScheme="[Root]/[Quality]/TV Shows/[TV Show Name]/Season [Season #]/" subtitleType="HI"