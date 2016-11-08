#!/bin/sh
java -classpath ./mxpost.jar eos.TestEOS biomed_eos.project < $1 2> /dev/null
