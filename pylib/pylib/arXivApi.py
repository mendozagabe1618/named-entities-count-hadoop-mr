"""
    Author: Gabriel Mendoza
    Borrowed from example on arxiv.org
    https://arxiv.org/help/api/examples/python_arXiv_parsing_example.txt

    *** with each day it looks less and less like the original ***

	<ORIGINAL COMMENT HEADER>
	python_arXiv_parsing_example.py

	This sample script illustrates a basic arXiv api call
	followed by parsing of the results using the
	feedparser python module.

	Please see the documentation at
	http://export.arxiv.org/api_help/docs/user-manual.html
	for more information, or email the arXiv api
	mailing list at arxiv-api@googlegroups.com.

	urllib is included in the standard python library.
	feedparser can be downloaded from http://feedparser.org/ .

	Author: Julius B. Lucks

	This is free software.  Feel free to do what you want
	with it, but please play nice with the arXiv API!
"""

import subprocess
import os
import urllib, urllib2
import feedparser
import string
import argparse
from os.path import expanduser
import threading
from subprocess import Popen, PIPE, STDOUT
import pip
import imp
import libscriptdefs

# Query's database and downloads the results
def query_and_download(search_query, max_results):
    num = 0;
    if search_query:
        search_query = 'all:' + search_query
    else:
        search_query = 'all:electron' # default search
        num += 1

    # print "arxiv max results: %s" % max_results
    if max_results > -1:
        max_results = max_results
    else:
        max_results = 3 # default
        num += 1

    if num == 2:
        print "[arXiv] Running default values: 'electron' and '3 results max'"

    # default search parameters
    # search_query = 'all:' + 'computer science' # search for electron in all fields
    start = 0                     # retreive the first 5 results
    # max_results = 3

    # Base api query url
    base_url = 'http://export.arxiv.org/api/query?';
    # build query from command line arguements or default values
    query = 'search_query=%s&start=%i&max_results=%i' % (search_query,
                                                         start,
                                                         max_results)
    # Opensearch metadata such as totalResults, startIndex,
    # and itemsPerPage live in the opensearch namespase.
    # Some entry metadata lives in the arXiv namespace.
    # This is a hack to expose both of these namespaces in
    # feedparser v4.1
    feedparser._FeedParserMixin.namespaces['http://a9.com/-/spec/opensearch/1.1/'] = 'opensearch'
    feedparser._FeedParserMixin.namespaces['http://arxiv.org/schemas/atom'] = 'arxiv'

    # perform a GET request using the base_url and query
    print "[arXiv] Sending HTTP Request "

    # get the http response
    print "[arXiv] Waiting for HTTP Response"
    response = urllib.urlopen(base_url+query).read()
    print "[arXiv] HTTP Response Received"
    # parse the response using feedparser
    feed = feedparser.parse(response)

    num = 0
    for entr in feed.entries:
        num += 1

    print "[arXiv] Search returned %d results" % num
    print "[arXiv] Starting downloads..."
    print "        Can sometimes be slow.\n        Be patient."
    # Run through each entry, and print out information

    directory = "downloads"
    if not os.path.exists(directory):
        print "[arXiv] Created \'downloads\' directory."
        os.makedirs(directory)
    num = 1
    file_name = "hello"
    for entry in feed.entries:
        # get the links to the abs page and pdf for this e-print
        for link in entry.links:
            # print the metadata URL
            if link.rel == 'alternate':
                var = 3

            # print the PDF URL
            elif link.title == 'pdf':
                # print '%d. PDF URL: %s' %(num, link.href)

                # Huge Thank you to
                # http://stackoverflow.com/questions/22676/how-do-i-download-a-file-over-http-using-python
                # for the following lines of code.
                # very few modifications to fit the scheme of what I need,
                # besides that, if it isnt broken, why fix it
                url = link.href
                title = entry.title.replace(" ", "")
                title = title.replace(":","" )
                title = title.replace("?","")
                title = title.replace("'","")
                
                # file_name = url.split('/')[-1] + '.pdf'
                file_name = "downloads/%s.txt" % title
                f = open( file_name , 'wb')
                f.write(url)
                f.close()
                print "        %d. %s" % (num, entry.title)
                print "        filename: %s" % file_name
                print "        URL: %s" % url
                num += 1
                # print "[arXiv] Requesting %s" % file_name
                #
                # u = urllib2.urlopen(url)
                # meta = u.info()
                # file_size = int(meta.getheaders("Content-Length")[0])
                # print "        Downloading \"%s\"\t\tSize: %s Kb" % (file_name, file_size/1024)
                #
                # f = open( 'downloads/' + file_name, 'wb')
                #
                # # print "\tDownloading: %s Bytes: %s" % (file_name, file_size)
                # file_size_dl = 0
                # block_sz = 8192
                #
                # while True:
                #     buffer = u.read(block_sz)
                #     # print " buffer = %s" %buffer
                #     if not buffer:
                #         break
                #     file_size_dl += len(buffer)
                #     f.write(buffer)
                #     # status = r"%10d  [%3.2f%%]" % (file_size_dl, file_size_dl * 100. / file_size)
                #     # status = status + chr(8) * (len(status) + 1)
                #     # print status,
                #
                # f.close()

        # print "        Finished downloading %s" % file_name
        # print "        %d downloaded out of possible %d" % (num, max_results)


    print "[arXiv] Finished extracting %d titles and Urls" % num
