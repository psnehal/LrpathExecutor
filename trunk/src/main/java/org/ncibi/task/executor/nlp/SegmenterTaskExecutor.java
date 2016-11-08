package org.ncibi.task.executor.nlp;

import java.util.List;

import org.ncibi.db.ws.SplitterArgs;
import org.ncibi.mqueue.task.TaskArgRetriever;
import org.ncibi.nlp.SentenceSegmenter;
import org.ncibi.task.TaskWriter;
import org.ncibi.task.executor.AbstractTaskExecutor;

public class SegmenterTaskExecutor extends AbstractTaskExecutor<SplitterArgs, List<String>>
{
    public SegmenterTaskExecutor(TaskArgRetriever<SplitterArgs> argRetriever,
                TaskWriter<List<String>, SplitterArgs> taskWriter)
    {
        super(argRetriever, taskWriter);
    }

    @Override
    protected List<String> executeWithArgs(SplitterArgs args)
    {
        return SentenceSegmenter.segment(args.getSentences());
    }
}
