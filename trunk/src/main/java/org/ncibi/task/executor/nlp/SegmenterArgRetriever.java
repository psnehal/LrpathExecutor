package org.ncibi.task.executor.nlp;

import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.SplitterArgs;
import org.ncibi.db.ws.Task;
import org.ncibi.task.executor.AbstractTaskArgRetriever;

public class SegmenterArgRetriever extends AbstractTaskArgRetriever<SplitterArgs>
{
    public SegmenterArgRetriever(PersistenceSession persistence)
    {
        super(persistence);
    }

    protected String hqlQueryToRetrieveArgsForTask(Task task)
    {
        return "from ws.Splitter where uuid = '" + task.getUuid() + "'";
    }
}
