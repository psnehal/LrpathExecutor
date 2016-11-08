package org.ncibi.task.executor.lrpath;

import java.util.List;

import org.ncibi.lrpath.LRPath;
import org.ncibi.lrpath.LRPathArguments;
import org.ncibi.lrpath.LRPathRServer;
import org.ncibi.lrpath.LRPathResult;
import org.ncibi.lrpath.config.RServerConfiguration;
import org.ncibi.mqueue.task.TaskArgRetriever;
import org.ncibi.task.TaskWriter;
import org.ncibi.task.executor.AbstractTaskExecutor;
import org.rosuda.REngine.REXPMismatchException;

public class LRPathTaskExecutor extends AbstractTaskExecutor<LRPathArguments, List<LRPathResult>>
{

    private final String rServeAddress = RServerConfiguration.rserverAddress();
    private final int rServePort = RServerConfiguration.rserverPort();

    public LRPathTaskExecutor(TaskArgRetriever<LRPathArguments> argRetriever,
            TaskWriter<List<LRPathResult>, LRPathArguments> resultWriter)
    {
        super(argRetriever, resultWriter);
    }

    @Override
    protected List<LRPathResult> executeWithArgs(LRPathArguments args)
    {
        final LRPathRServer rserver = new LRPathRServer(rServeAddress, rServePort);
        final LRPath lrpath = new LRPath(rserver);
        List<LRPathResult> results = lrpath.runAnalysis(args);
        rserver.close();
        return results;
    }
}
