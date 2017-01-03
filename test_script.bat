:: Must use "/" for directories. This works on both windows and linux.

:: importLoc:      the direcotry where you want the script to "watch". It will create a few files for tracking in this folder.
:: movieScheme:    the scheme/location you want the movies to be in. Dynamic keywords consist of: [Quality]
:: tvShowScheme:   the scheme/location you want the tvshows to be in. Dynamic keywords consist of: [Quality], [TV Show Name], [Season #], [Episode #]
:: subtitleType:   the type of subtiles you want: None, HI, ForeignLangOnly, Normal

@echo off
java -jar ./out/artifacts/moviemover_jar/moviemover.jar importLoc="./playground/import/" movieScheme="./playground/Movies/[Quality]/" tvShowScheme="./playground/TV Shows/[Quality]/TV Shows/[TV Show Name]/Season [Season #]/" subtitleType="HI"
