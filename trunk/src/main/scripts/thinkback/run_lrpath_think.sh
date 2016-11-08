#!/bin/bash
if [ -z "$NCIBI_HOME" ]; then
    NCIBI_HOME=/usr/share/ncibi
fi

LIB="."

for jar in $(ls "$NCIBI_HOME"/thinkback/lib)
do
    LIB="$LIB:$jar"
done

LIB="$NCIBI_HOME/thinkback/think-back.jar:$LIB"

java -classpath "$LIB" edu.umich.think.ThinkBackWsDriver $*
rm -f $*
