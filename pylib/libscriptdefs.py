import subprocess
from subprocess import Popen, PIPE, STDOUT
# from shutil import copyfile





# Executes a command cmd, displays stdout output in realtime
# from http://blog.kagesenshi.org/2008/02/teeing-python-subprocesspopen-output.html
def execute_with_args(cmd, args):
    p = subprocess.Popen([cmd, args], shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout = []
    while True:
        line = p.stdout.readline()
        stdout.append(line)
        print line,
        if line == '' and p.poll() != None:
            break
    return ''.join(stdout)

# Executes a command cmd, displays stdout output in realtime
# from http://blog.kagesenshi.org/2008/02/teeing-python-subprocesspopen-output.html
def execute(cmd):
    p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout = []
    while True:
        line = p.stdout.readline()
        stdout.append(line)
        print line,
        if line == '' and p.poll() != None:
            break
    return ''.join(stdout)
