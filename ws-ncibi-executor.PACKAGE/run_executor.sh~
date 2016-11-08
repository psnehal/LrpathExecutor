if [ -z "$NCIBI_HOME" ]; then
    NCIBI_HOME=/usr/share/ncibi
fi

CPATH="$NCIBI_HOME"/bin/ws-executor.jar

for jar in $( ls "$NCIBI_HOME"/lib/* )
do
    CPATH=$CPATH:$jar
done

exec java -Dpid=$$ -DNCIBI_HOME="$NCIBI_HOME" -classpath $CPATH org.ncibi.main.TaskExecutorMain >> "$NCIBI_HOME"/logs/executor.out 2>&1
