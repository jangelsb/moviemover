# Must use "/" for directories. This works on both windows and linux.

# importLoc:      the direcotry where you want the script to "watch". It will create a few files for tracking in this folder.
# tvShowRootLoc:  the root diretory for tvshows to go in. This folder will be created, if it doesn't exist.
# movieRootLoc:   the root diretory for movies to go in. This folder will be created, if it doesn't exist.
# tvShowScheme:   the scheme you want the tvshows to be in. Must start with [ROOT]. Keywords consist of: [Root], [Quality], [TV Show Name], [Season #], [Episode #]
# movieScheme:    the scheme you want the movies to be in. Must start with [ROOT]. Keywords consist of: [Root], [Quality]
# subtitleType:   the type of subtiles you want: None, HI, ForLangOnly, Normal

# example: java -jar ./out/artifacts/moviemover_jar/moviemover.jar importLoc="./playground/import/" tvShowRootLoc="./playground/TV Shows" movieRootLoc="./playground/Movies" movieScheme="[Root]/[Quality]/" tvShowScheme="[Root]/[Quality]/TV Shows/[TV Show Name]/Season [Season #]/" subtitleType="HI"

java -jar ./out/artifacts/moviemover_jar/moviemover.jar importLoc="./playground/import/" tvShowRootLoc="./playground/TV Shows" movieRootLoc="./playground/Movies" movieScheme="[Root]/[Quality]/" tvShowScheme="[Root]/[Quality]/TV Shows/[TV Show Name]/Season [Season #]/" subtitleType="HI"
