package org.ncibi.task.executor.lrpath;

import junit.framework.Assert;

import org.hibernate.Session;
import org.junit.Test;
import org.ncibi.db.EntityManagers;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.PersistenceUnit;
import org.ncibi.db.ws.Task;
import org.ncibi.db.ws.TaskType;
import org.ncibi.hibernate.SessionProcedure;
import org.ncibi.hibernate.Sessions;
import org.ncibi.lrpath.LRPathArguments;
import org.ncibi.mqueue.MessageQueue;
import org.ncibi.mqueue.persistent.AnyMessagePersistentMessageQueue;
import org.ncibi.mqueue.task.PersistentTaskQueuer2;
import org.ncibi.mqueue.task.TaskUtil;

public class LRPathArgRetrieverTest
{

    private static final PersistenceSession persistence = new PersistenceUnit(
            EntityManagers.newEntityManagerFromProject("task"));
    private static final MessageQueue queue = new AnyMessagePersistentMessageQueue(persistence, "test");
    private static PersistentTaskQueuer2<LRPathArguments> taskQueuer = new PersistentTaskQueuer2<LRPathArguments>(
            queue, persistence);
    private static final LRPathArguments lrpathArgs = new LRPathArguments();

    @Test
    public void testRetrieveAndDeleteArgsFromDatabase()
    {
        Task task = TaskUtil.newQueuedTask(TaskType.LRPATH);
        queueLRPathArgsToRetrieve(task);
        LRPathArgRetriever argRetriever = new LRPathArgRetriever(persistence);
        LRPathArguments args = argRetriever.retrieveArgsForTask(task);
        Assert.assertEquals(args.getApplication(), "testme");
        argRetriever.deleteArgsForTask(task);
        deleteTask(task);
    }

    private void queueLRPathArgsToRetrieve(Task t)
    {
        lrpathArgs.setApplication("testme");
        taskQueuer.queue(t, lrpathArgs);
    }

    private void deleteTask(final Task task)
    {
        try
        {
            Sessions.withSession(persistence.session(), new SessionProcedure()
            {
                @Override
                public void apply(Session session)
                {
                    session.refresh(task);
                    session.delete(task);
                }
            });
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
    }
}
