package org.ncibi.task.executor;

import org.hibernate.Session;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.Task;
import org.ncibi.hibernate.SessionProcedure;
import org.ncibi.hibernate.Sessions;
import org.ncibi.mqueue.task.TaskArgRetriever;

public abstract class AbstractTaskArgRetriever<T> implements TaskArgRetriever<T>
{
    protected abstract String hqlQueryToRetrieveArgsForTask(Task task);

    private final PersistenceSession persistence;

    public AbstractTaskArgRetriever(PersistenceSession persistence)
    {
        this.persistence = persistence;
    }

    @Override
    public T retrieveArgsForTask(Task task)
    {
        return retrieveArgsForTaskUsingSession(task, persistence.session());
    }
    
    private T retrieveArgsForTaskUsingSession(Task task, Session session)
    {
        /*
         * Without cast sun's compiler will not compile the code below while the eclipse compiler
         * will. I put the cast (T) in and then added the @SuppressWarnings("unchecked") to remove
         * warnings.
         */
        @SuppressWarnings("unchecked")
        final T entry = (T) persistence.hqlQuery(session, hqlQueryToRetrieveArgsForTask(task))
                    .single();
        return entry;
    }

    @Override
    public void deleteArgsForTask(final Task task)
    {
        Sessions.withSession(persistence.session(), new SessionProcedure()
        {
            @Override
            public void apply(Session session)
            {
                T args = retrieveArgsForTaskUsingSession(task, session);
                session.delete(args);
            }
        });
    }
}
