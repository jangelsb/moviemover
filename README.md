# README #
This is a program to automate your video downloads. It can even unrar those pesky rar files. 


### How does it work? ###
It "watches" a folder for new movies or tv shows. The default directories it watches: `./playground/import`

It copies or moves videos to `./playground/tvshows` or `./playground/movies` based on it's type. 

e.g.,
```
Sun Sep 11 19:38:13 PDT 2016: Searching for videos...
 + The.Big.Bang.Theory.S08E23.720p.HDTV.X264-DIMENSION.mkv
Done.

Sun Sep 11 19:38:13 PDT 2016: Moving videos to correct directories...
 - The.Big.Bang.Theory.S08E23.720p.HDTV.X264-DIMENSION.mkv was COPIED to playground/tvshows/720p/The Big Bang Theory/Season 08
Done.

Time Elapsed: 3.999 secs
```

If it unrars a file it will move it to the destination, if it doesn't have to, it copies the video.


### Prerequisites ###
* [Intellij Community Version](https://www.jetbrains.com/idea/download/)
* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)


### Setup ###

* Clone repository
* Open Intelij
* Open MovieMover project