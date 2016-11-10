import subprocess
import os
import time;
from datetime import datetime



global_user = "mendozagabe"
global_input_dir = "input_title_url"
global_append = "_TEST"

def getLocalTime_Formatted():
    d = datetime.now()
    formatted = d.strftime('%Y%m%d@%H-%M')
    return formatted
# user = "mendozagabe"
def execute(command) :
    subprocess.call(command, shell=True)

def init_HdfsDirectories(user, input_dir, append ):
    print "[Hadoop] Creating directories on HDFS"

    mkdir = "hadoop fs -mkdir -p /user/%s/%s" % (user, input_dir)
    copy_dir = "hadoop fs -put ./downloads  /user/%s/%s" % (user, input_dir)

    if append:
        copy_dir+="-"
        copy_dir+=append
        mkdir +="-"
        mkdir+=append

    mkdir_copydir= "%s && %s" %(mkdir, copy_dir)
    execute(mkdir_copydir)
    time.sleep(10) # to give the hdf a chance to make the dir

def start_mapreduce_process(user,  input_dir, append):
    print "[Hadoop] Starting HdpMR"
    if ( user != None) and ( input_dir != None):
        init_HdfsDirectories(None, None,append)
    else :
        init_HdfsDirectories(global_user, global_input_dir, append)

    print "[Hadoop] Executing jar...\n"
    # copyfile("/target/hmr-1.jar", "./hmr-1.jar")
    formatted_dt = getLocalTime_Formatted()

    copy_cmnd = "cp target/opennlp-hmr-gmendo8.jar ."
    yarn_cmd = "yarn jar opennlp-hmr-gmendo8.jar "
    arg_input_dir = " /user/%s/%s " %(user, input_dir)
    arg_output_dir = "/user/%s/output_%s" % (user, formatted_dt)

    cmd_str = yarn_cmd + arg_input_dir + arg_output_dir
    commands = copy_cmnd + " && " + cmd_str
    execute(commands)

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
