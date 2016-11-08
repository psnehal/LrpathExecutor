#!/bin/bash

function makedir()
{
    if [ ! -d "$1" ]; then
        echo "  Creating directory $1"
        mkdir "$1"
        chmod a+rwx "$1"
    fi
}

function stopExisting()
{
    if [ -d "$NCIBI_HOME" ]; then
        bash ./S80ncibiexecutor stop
    fi
}

function checkIfMainServer()
{
    /bin/echo -n "Is this a main server?[y]"
    read
    if [ -z "$REPLY" -o "$REPLY" = "y" ]; then
	    touch "$NCIBI_HOME"/main_server
    else
        rm -f "$NCIBI_HOME"/main_server
    fi
}

function startExecutor()
{
    /bin/echo -n "Start Executor?[y]"
    read
    if [ -z "$REPLY" -o "$REPLY" = "y" ]; then
        bash /etc/rc4.d/S80ncibiexecutor start
    fi
}

if [ ! `whoami` = "root" ]; then
    echo "You must be root to run this script."
    exit 1
fi

if [ -f /etc/ncibi-executor.conf ]; then
    . /etc/ncibi-executor.conf
fi

if [ -z "$NCIBI_HOME" ]; then
    export NCIBI_HOME="/usr/share/ncibi"
fi

stopExisting

echo " "
echo "Setting up executor..."
echo "  NCIBI_HOME: $NCIBI_HOME"

makedir "$NCIBI_HOME"

makedir "$NCIBI_HOME"/logs
makedir "$NCIBI_HOME"/status
makedir "$NCIBI_HOME"/bin
makedir "$NCIBI_HOME"/nlp
makedir "$NCIBI_HOME"/lib
makedir "$NCIBI_HOME"/thinkback

echo "  Copying jar and run_executor.sh to $NCIBI_HOME/bin..."
cp ws-executor.jar "$NCIBI_HOME"/bin
cp run_executor.sh "$NCIBI_HOME"/bin
chmod a+x "$NCIBI_HOME"/bin/run_executor.sh

echo "  Setting up $NCIBI_HOME/lib..."
cp dependency/*.jar "$NCIBI_HOME"/lib

echo "  Copying nlp executables..."
cp nlp/run_nlp_script.sh "$NCIBI_HOME"/bin
chmod a+x "$NCIBI_HOME"/bin/run_nlp_script.sh

# mxpost files
cp -r nlp/biomed_eos.project "$NCIBI_HOME"/nlp
cp nlp/mxpost.jar "$NCIBI_HOME"/nlp
cp nlp/mxpost.sh "$NCIBI_HOME"/nlp
chmod a+x "$NCIBI_HOME"/nlp/mxpost.sh

# stanford parser files
cp nlp/englishPCFG.ser.gz "$NCIBI_HOME"/nlp
cp nlp/stanford-parser.jar "$NCIBI_HOME"/nlp
cp nlp/stanford_parser.sh "$NCIBI_HOME"/nlp
chmod a+x "$NCIBI_HOME"/nlp/stanford_parser.sh

echo "  Setting up Thinkback services..."
rm -rf "$NCIBI_HOME"/thinkback/lib
cp -r thinkback/lib "$NCIBI_HOME"/thinkback/lib
cp thinkback/think-back.jar "$NCIBI_HOME"/thinkback
cp thinkback/run_gsea_think.sh  "$NCIBI_HOME"/bin
cp thinkback/run_lrpath_think.sh "$NCIBI_HOME"/bin
chmod a+x "$NCIBI_HOME"/bin/run_gsea_think.sh
chmod a+x "$NCIBI_HOME"/bin/run_lrpath_think.sh

SYS=`uname`

if [ "$SYS" = "Linux" ]; then
    echo "  Copying S80ncibiexecutor to /etc/rc4.d startup scripts"
    cp S80ncibiexecutor /etc/rc4.d
    chmod a+x /etc/rc4.d/S80ncibiexecutor
fi

checkIfMainServer

startExecutor

echo "Done."
