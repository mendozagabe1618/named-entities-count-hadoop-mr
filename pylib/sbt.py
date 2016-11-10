import subprocess
from subprocess import Popen, PIPE, STDOUT

def compile_and_package_jar():

    # cleaning =  "echo \"\n[SBT]  Cleaning...\""
    # clean =     "sbt clean"
    #
    # compiling = "echo \"\n[SBT]  Compiling...\""
    # comp =   "sbt compile"

    pack =      "sbt package"
    cp   =      "cp target/opennlp-hmr-gmendo8.jar ."

    # cmd = "%s && %s && %s && %s && %s && %s && %s" % (cleaning, clean, compiling, comp, packaging, pack, cp)
    cmd_pack_only = "%s && %s" % (pack, cp)
    subprocess.call(cmd_pack_only, shell=True)
