# Named Entities Word Count

Distributed for counting the named entities in from a large amount of files and counting them using a MapReduce model.

### This bundle of code does a few things. ###
* A python script grabs sends a HTTP GET request through the arXiv API. Then we parse the Atom XML response to get the URLs to the PDFs for the returned results. arXiv API mostly only uses PDF. It supports other formats, but for sure will support PDF. However, the implementation allows for any input type. Simply limited by the API.

### Work Flow ###
1. A python script, run_me.py, takes 2 command line arguments
 * -k "keywords"      - quotes included
 * -m maxsize
2. The script then queries using the input keywords and downloads up to the maxsize of docuements.
3. While this is going on. The Script silently compiles and packages the jar file.
4. When ready, the script will scp
1. A python script, run_me.py, prompts SBT to compile and packages all the Java source code, required binaries, etc. into a jar as well as to launch the test suite


### How To Run ###
* I encountered a lot of promblems building my solution into a fat jar. It was SBT that was holding me down.could not figure out how to solve the many duplicate dependencies that I had .

```
./script.py -h
usage: script.py [-h] [-k KEYWORDS] [-m MAXRESULTS] [-s SKIPTO] [-c]

optional arguments:
  -h, --help            show this help message and exit
  -k KEYWORDS, --keywords KEYWORDS
                        keywords to searched for. in quotes if multiple
  -m MAXRESULTS, --maxresults MAXRESULTS
                        number of results to query = for.
  -s SKIPTO, --skipto SKIPTO
                        pack: skip downloading of files. must run atleast once
                        hadoop: skip downloading and packaging.
  -c                    Store a constant value
```

-c has priority, meaning that if set, the directory will be cleaning before anything else
-k and -m go hand in hand. search keywords and max results    
defaults are "computer science" and 3, respectively
-s is cool in that if you already downloaded documents with -m and -k you can, and/or packaged a jar through indirect calls to sbt package through the script, you can skip those parts so as to SKIP forward.
