#!/bin/sh
java -classpath ./stanford-parser.jar edu.stanford.nlp.parser.lexparser.LexicalizedParser -outputFormat "penn" englishPCFG.ser.gz $1 2>/dev/null
