import subprocess
import os
import time;
from datetime import datetime
import pwd



global_user = "mendozagabe"
global_input_dir = "input_title_url"
global_append = "_TEST"


def getLocalTime_Formatted():
    d = datetime.now()
    formatted = d.strftime('%Y%m%d@%H-%M')
    str = "%s" % formatted
    return formatted

# user = "mendozagabe"
def execute(command) :
    process = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE)
    process.wait()
    # print process.returncode

def buildInputDirString(user,input_dir, append):

    whoami = pwd.getpwuid(os.getuid()).pw_name # full /etc/passwd info

    right_now = datetime.now()
    now_strf_Date = right_now.strftime("%Y-%m-%d")
    now_strf_Time = right_now.strftime("%H-%M")
    TIME_STAMP = now_strf_Date + "@" + now_strf_Time

    #/user/hue/mendozagabe/title-url/title-url_TIMESTAMP/*
    indir = "/user/"+ whoami + "/" + user + "/"+ TIME_STAMP #+ input_dir

    if append:
        indir+="_"
        indir+= append

    return indir

def buildOutputDirString(user,append):
    whoami = pwd.getpwuid(os.getuid()).pw_name #

    right_now = datetime.now()
    now_strf_Date = right_now.strftime("%Y-%m-%d")
    now_strf_Time = right_now.strftime("%H-%M")
    TIME_STAMP = now_strf_Date + "@" + now_strf_Time

    # /user/hue/mendozagabe/output/output_TIME_STAMP/*
    outdir = "/user/"+ whoami + "/" + user + "/"+ TIME_STAMP # + "output"
    # outdir = "/user/" + whoami +"/"+ user +"/" + "output/output_"
    # outdir += TIME_STAMP

    if append:
        outdir += "_"
        outdir += append



    return outdir

def init_HdfsInputDirectory(indir):
    print "[Hadoop] Creating directories on HDFS"

    mkdir = "hadoop fs -mkdir -p %s" % indir
    print "[MKDIR] %s" % mkdir
    execute(mkdir)

    put = "hadoop fs -put inputs %s" % indir
    print "[Put]  $\%s" %put
    execute(put)

    return indir + "/inputs"
    # mv = "hadoop fs -mv %s/inputs %/.."
    # for filename in os.listdir("inputs"):
    #     put = "hadoop fs -put ./inputs/%s  %s " % (filename, indir)
    #     print "[PUT] %s" % put
    #     execute(put)

    # print "init_HdfsInputDirectory mkdir result : %d " % result
    # time.sleep(10) # to give the hdf a chance to make the dir

def start_mapreduce_process(user,  input_dir, append):
    print "\n[Hadoop] Starting Hadoop Map Reduce ..."

    indir = buildInputDirString(user,input_dir, append)
    outdir = buildOutputDirString(user,append)

    indir = init_HdfsInputDirectory(indir)

    print "[Hadoop] Executing jar...\n"
    # copyfile("/target/hmr-1.jar", "./hmr-1.jar")

    copy_cmnd = "cp target/opennlp-hmr-gmendo8.jar ."
    execute(copy_cmnd)

    outdir += "/output"
    print "INPUT FOLDER: %s" % indir
    print "OUTPUT FOLDER: %s" % outdir
    yarn_cmd = "yarn jar opennlp-hmr-gmendo8.jar %s %s" % (indir, outdir)
    print "[YARN] %s" % yarn_cmd
    execute(yarn_cmd)
    # print "start_mapreduce_process result: %d " % result

# get current path
# pwd
#
# START MAP REDUCE PROCESS
# yarn jar hmr-1.jar /user/hue/In /user/hue/Out
#
# COPY FROM HDFS TO LOCAL
# hadoop fs -get /user/hue/Out /usr/lib/hue/hw2
#
# REMOVE FILE / FOLDER
# hadoop fs -rm -r /user/hue/Out

if __name__ == "__main__":
    start_mapreduce_process("mendozagabe", "input_title_url", None )
