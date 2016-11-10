#! /usr/bin/env python

import subprocess
import string
import argparse
import os
# from os.path import expanduser
import threading
import pip
import imp
import pylib.arXivApi
# import pylib.libscriptdefs
import pylib.hadoop
import pylib.sbt
import sys, traceback
import os.path

def install(package):
    pip.main(['install', package])

def checkIfDownloadsDirExists():
    # downloads_dir = "%s%s" % (os.getcwd(),"/downloads")
    if os.path.exists("inputs") == False:
        return False
    return True

def checkIfCompiledAndPackaged():
    # jar_location = "%s%s" % (os.getcwd(), "./opennlp-hmr-gmendo8.jar")
    if os.path.exists("opennlp-hmr-gmendo8.jar") == False:
        return False
    return True

def doClean():
    clean_cmd = "rm inputs/* && rm -rf target && rm -rf project/project && rm -rf project/target && rm *.jar"
    print "\n[Clean] Cleaning up ..."
    print "        rm-rf inputs"
    # subprocess.call( "rm inputs/*" , shell=True)
    p = subprocess.Popen(["rm", "-rf","inputs"], stdout=subprocess.PIPE)
    out, err = p.communicate()
    print "        rm -rf target"
    # subprocess.call( "rm -rf target" , shell=True)
    p = subprocess.Popen(["rm", "-rf", "target"], stdout=subprocess.PIPE)
    out, err = p.communicate()
    print "        rm -rf project/project"
    # subprocess.call( "rm -rf project/project" , shell=True)
    p= subprocess.Popen(["rm", "-rf", "project/project"], stdout=subprocess.PIPE)
    out, err = p.communicate()
    print "        rm -rf project/target"
    # subprocess.call( "rm -rf project/target", shell=True)
    p = subprocess.Popen(["rm", "-rf", "project/target"], stdout=subprocess.PIPE)
    out, err = p.communicate()
    print "        rm opennlp-hmr-gmendo8.jar"
    # subprocess.call( "*.jar", shell=True)
    p = subprocess.Popen(["rm", "opennlp-hmr-gmendo8.jar"], stdout=subprocess.PIPE)
    out, err = p.communicate()
    print "\n[Clean] Project directory back to initial state\n"

def printNoJarError():
    print "[Hadoop] Error: opennlp-hmr-gmendo8.jar not found"
    print "\tMust run ./script.sh with -k or -m flags at least to download input data"
    print "\tfor the executable jar to process. Must also be done when cleaning"
    print "\twith the -c flag since that reinitializes the directory\n"

def doSkipToPackaging(user, input_dir, append):
    print "[SBT]  Compiling sources and packing everything into opennlp-hmr-gmendo8.jar"
    pylib.sbt.compile_and_package_jar()

    print "\n[Hadoop] Starting MapReduce Task...\n"
    if checkIfDownloadsDirExists():
        pylib.hadoop.start_mapreduce_process(user, input_dir, append)
    else :
        print "[Error] No documents to process. Rerun script with flags -k and/or -m"
        print "        Do not use -c or -s skipto\n"

def doSkipToHadoop(user, input_dir, append):
    if checkIfCompiledAndPackaged():
        print "[Hadoop] Starting MapReduce Task..."
        if checkIfDownloadsDirExists():
            pylib.hadoop.start_mapreduce_process(user, input_dir, append)
        else:
            print "[Error] No documents to process. Rerun script without skipping."
    else :
        printNoJarError()


def checkSkipTo(skipto):
    if skipto:
        if skipto == "pack":
            return 1
        elif skipto == "hadoop":
            return 2
        else:
            print "\n***** Invalid skip arguement!\n\tOnly '--skipto package' or '--skipto hadoop' allowed"
            return 3
    else:
        return 0

def doAll(keywords, maxresults,user, input_dir, append):
    pylib.arXivApi.query_and_download(keywords, maxresults)

    print "\n[SBT]  Packaging..."
    pylib.sbt.compile_and_package_jar()
    # print "\n[Hadoop] Starting MapReduce Task.."
    if checkIfDownloadsDirExists():
        pylib.hadoop.start_mapreduce_process(user, input_dir, append)


def logic():
    # if feed parser is not installed, install it
    try:
        imp.find_module('feedparser')
        found = True
    except ImportError:
        found = False
        print "Python module feedparser is required but was not found."
        print "Now installing"
        install("feedparser")
        print " "
    import feedparser

    # The following lines heavily borrowed from the following link
    # http://www.tutorialspoint.com/python/python_command_line_arguments.htm
    # I also used this style/example in HW1

    skip_string = "pack: skip downloading of files. must run atleast once"
    skip_string += "\t\thadoop: skip downloading and packaging."
    parser = argparse.ArgumentParser()
    parser.add_argument("-k", "--keywords", help="keywords to searched for. in quotes if multiple")
    parser.add_argument("-m", "--maxresults", type=int, help="number of results to query = for.")
    parser.add_argument("-s", "--skipto", help=skip_string)
    # parser.add_argument("-c", "--clean", type=bool,help="Clears directory to initial state. Can be anything char(s)")
    parser.add_argument('-c', action='store_const', dest='clean',
                    const=True, help='Store a constant value')
    args = parser.parse_args()

    script = "./script "
    clean = False
    skipto = -1
    query = False

    if args.clean: # ./script.py -c
        # script += "-c "
        clean = True
        # clean()
        skipto = checkSkipTo(args.skipto)
        if skipto == 0:
            if args.keywords:
                query = True
                # script += "-k keywords "
            if args.maxresults:
                query = True
                # script += "-k maxresults"
        else:
            k = 1
            # script += "-s skipto "

    else : # ./script.py
        skipto = checkSkipTo(args.skipto)
        if skipto == 0:
            query = True
            # script += "-k keywords -m maxresults"
        else:
            k = 1
            # script += "-s skipto"

    # print script
    # print "clean: %s" % clean
    # print "skip %s" % skipto
    # print "-k or -m: %s" % query

    user = "mendozagabe"
    input_dir = "inputs"
    append = None

    print
    if clean:
        doClean()

    if skipto == 1:
        print "[Skip]  Skipped arXiv querying"
        print
        doSkipToPackaging(user, input_dir, append)
    elif skipto == 2:
        print "[Skip]  Skipped arXiv querying and SBT packaging"
        print
        doSkipToHadoop(user, input_dir, append)

    if query == True:
        print "[arXiv] Initializing query URL"
        doAll(args.keywords, args.maxresults,user, input_dir, append)

# end of logic()

def main():
    try:
        logic()
    except KeyboardInterrupt:
        print "\nShutdown requested...exiting"
    except Exception:
        traceback.print_exc(file=sys.stdout)
    sys.exit(0)

if __name__ == "__main__":
    main()
