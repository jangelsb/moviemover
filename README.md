# moviemover #
This is a program to automate your video downloads. It can even unrar those pesky rar files *and* download subtiles from subscene using this [java api](https://github.com/jangelsb/subscene_api).


### How do I use it? ###
All you need to do is download the [latest jar file](https://github.com/jangelsb/moviemover/tree/master/out/artifacts/moviemover_jar) and run it from the terminal or command prompt. 

You can see an example, with explanations, in [`./test_script.sh`](https://github.com/jangelsb/moviemover/blob/master/test_script.sh). By default, the script is self-contained in the project, so feel free to run it.

### How does it work? ###
It "watches" a folder, `importDir`, for new tv shows or movies. If found, it copies or moves the videos to its designated folder, defined by `tvShowScheme` and `movieScheme`. Before handling the video, it can also download subtitles for the video, specified by `subtitleType`.

<br>
**More in depth:**

On the initial run, it will create two files in the the import directory (`importDir`): `.mmlist` and `.mmlog`


> `.mmlist`: a list of every file that has already been processed (whitelisted)

>`.mmlog`: a log of what the script does with timestamps and durations

Since `.mmlist` didn't already exist, the program assumes everything in the folder has already been processed and whitelists every file.

<br>
The next time the program runs, it will search through the import directory (`importDir`) for any new files that aren't already whitelisted. If it finds a new file, it will determine if it is a video and if it is, it will handle it accordingly:

* **If it is a rar file**, it will unrar it and then *move* the newly created video file to its correct directory.

* **If it is an actual video** (.mkv, .mp4, .avi, etc) it will *copy* the video file to its correct directory. 

The reason it moves rar movie files and copies actual video files is because in both situations it leaves the original files intact. This way it doesn't actually move or modify the files it is looking at. 

<br>
The direcory it stores TV Shows and Movies is in a dynamic folder scheme, `tvShowScheme` and `movieShowScheme`, respectively. 

For example, `movieShowScheme` could look like this `movieShowScheme="~/playground/Movies/[Quality]/"`

The value(s) specified in `[]` are dynamic keywords that are updated with that video's specific values. 

Dynamic keywords consist of
> **Movie:** [Quality]

>  **TV Show:** [Quality], [TV Show Name], [Season #], and [Episode #]

<br>
If a subtitle type is specified when calling the jar file, the script will also try and find the correct subtitle for the video file from subscene. It uses this [java api](https://github.com/jangelsb/subscene_api).

For example, `subtitleType` could look like this `subtitleType="ForeignLangOnly"`

Subtitle values consist of: None, HI, ForeignLangOnly, Normal

<br>
**All together now...**

By running the jar file from the terminal, you can customize these values. 

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

This is demonstarted and explained in [`./test_script.sh`](https://github.com/jangelsb/moviemover/blob/master/test_script.sh). 

### Prerequisites ###
* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Want to contribute?  ###

* Clone repository
* Open [Intellij Community Version](https://www.jetbrains.com/idea/download/)
* Open MovieMover project
