# README #
This is a program to automate your video downloads. It can even unrar those pesky rar files *and* download subtiles from subscene using this [java api](https://github.com/jangelsb/subscene_api)


### How does it work? ###
It "watches" a folder for new movies or tv shows. By default, the directory it watches is `./playground/import`

It copies or moves videos to `./playground/tvshows` or `./playground/movies` based on it's type. 

However, if you run the jar file from the terminal, you can customize these locations. This is demonstarted and explained in the `./test_script.sh`

e.g., executing:
```
java -jar ./out/artifacts/moviemover_jar/moviemover.jar importLoc="./playground/import/" movieScheme="./playground/Movies/[Quality]/" tvShowScheme="./playground/TV Shows/[Quality]/TV Shows/[TV Show Name]/Season [Season #]/" subtitleType="None"
```
yields:
```
Fri Dec 30 15:22:02 PST 2016: Searching for videos...
 + The.Big.Bang.Theory.S08E22.720p.HDTV.X264-DIMENSION.mkv
Done.

Fri Dec 30 15:22:02 PST 2016: Moving videos to correct directories...
 - The.Big.Bang.Theory.S08E22.720p.HDTV.X264-DIMENSION.mkv was _COPIED_ to ./playground/TV Shows/720p/TV Shows/The Big Bang Theory/Season 08
Done.

Time Elapsed: 0.628 secs
------------------------------------------------------------
```

Or you can just execute the script:
```
./test_script.sh 
```

If it unrars a file it will move it to the destination, if it doesn't have to, it copies the video.


### Prerequisites ###
* [Intellij Community Version](https://www.jetbrains.com/idea/download/)
* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)


### Setup ###

* Clone repository
* Open Intelij
* Open MovieMover project
