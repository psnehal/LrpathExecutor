package org.ncibi.task.executor.lrpath;

import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.Task;
import org.ncibi.lrpath.LRPathArguments;
import org.ncibi.task.executor.AbstractTaskArgRetriever2;
import org.ncibi.ws.BeanXMLDecoder;

public class LRPathArgRetriever extends AbstractTaskArgRetriever2<LRPathArguments>
{
    public LRPathArgRetriever(PersistenceSession persistence)
    {
        super(persistence);
    }
    
    protected String hqlQueryToRetrieveArgsForTask(Task task)
    {
        return "from ws.ServiceArguments where uuid = '" + task.getUuid() + "'";
    }

    @Override
    protected LRPathArguments createObjectFromXml(String xml)
    {
        BeanXMLDecoder decoder = new BeanXMLDecoder(xml);
        return decoder.fromXml();
    }
}

