package org.ncibi.task.executor.thinkback;

import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.Task;
import org.ncibi.task.executor.AbstractTaskArgRetriever2;
import org.ncibi.ws.BeanXMLDecoder;
import org.ncibi.ws.thinkback.GseaThinkArgs;

public class GseaThinkArgRetriever extends AbstractTaskArgRetriever2<GseaThinkArgs>
{
    public GseaThinkArgRetriever(PersistenceSession persistence)
    {
        super(persistence);
    }

    @Override
    protected String hqlQueryToRetrieveArgsForTask(Task task)
    {
        return "from ws.ServiceArguments where uuid = '" + task.getUuid() + "'";
    }

	@Override
	protected GseaThinkArgs createObjectFromXml(String xml) {

        BeanXMLDecoder decoder = new BeanXMLDecoder(xml);
        return decoder.fromXml();
	}

}
