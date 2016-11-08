package org.ncibi.task.executor;

import org.hibernate.Session;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.ServiceArguments;
import org.ncibi.db.ws.Task;
import org.ncibi.hibernate.SessionProcedure;
import org.ncibi.hibernate.Sessions;
import org.ncibi.mqueue.task.TaskArgRetriever;

public abstract class AbstractTaskArgRetriever2<T> implements TaskArgRetriever<T>
{
    protected abstract String hqlQueryToRetrieveArgsForTask(Task task);

    protected abstract T createObjectFromXml(String xml);

    private final PersistenceSession persistence;

    public AbstractTaskArgRetriever2(PersistenceSession persistence)
    {
        this.persistence = persistence;
    }

    @Override
    public T retrieveArgsForTask(Task task)
    {
        Session session = persistence.session();
        final ServiceArguments sa = (ServiceArguments) persistence.hqlQuery(session,
                hqlQueryToRetrieveArgsForTask(task)).single();
        return createObjectFromXml(sa.getArgsXml());
    }

    @Override
    public void deleteArgsForTask(final Task task)
    {
        Sessions.withSession(persistence.session(), new SessionProcedure()
        {
            @Override
            public void apply(Session session)
            {
                ServiceArguments args = (ServiceArguments) session.createQuery(
                        hqlQueryToRetrieveArgsForTask(task)).uniqueResult();
                session.delete(args);
            }
        });
    }
}
