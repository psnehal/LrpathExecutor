#!/bin/sh

if [ -f /etc/ncibi-executor.conf ]; then
    . /etc/ncibi-executor.conf
fi

if [ -z "$NCIBI_HOME" ]; then
    export NCIBI_HOME=/usr/share/ncibi
fi

cd "$NCIBI_HOME"/nlp

sh ./$1 $2

rm -f $2
