package org.ncibi.task.executor.thinkback;

import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.Task;
import org.ncibi.task.executor.AbstractTaskArgRetriever2;
import org.ncibi.ws.BeanXMLDecoder;
import org.ncibi.ws.thinkback.LRThinkArgs;

public class LrpathThinkArgRetriever extends AbstractTaskArgRetriever2<LRThinkArgs>
{
    public LrpathThinkArgRetriever(PersistenceSession persistence)
    {
        super(persistence);
    }

    @Override
    protected String hqlQueryToRetrieveArgsForTask(Task task)
    {
        return "from ws.ServiceArguments where uuid = '" + task.getUuid() + "'";
    }

	@Override
	protected LRThinkArgs createObjectFromXml(String xml) {

        BeanXMLDecoder decoder = new BeanXMLDecoder(xml);
        return decoder.fromXml();
	}

}
