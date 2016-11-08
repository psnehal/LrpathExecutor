package org.ncibi.task.factory;

import org.ncibi.db.PersistenceSession;
import org.ncibi.mqueue.MessageQueue;
import org.ncibi.mqueue.task.TaskQueuer;
import org.ncibi.mqueue.task.PersistentTaskQueuer;

public final class TaskQueuerFactory
{
    private TaskQueuerFactory()
    {
    }

    public static <A> TaskQueuer<A> newPersistentTaskQueuer(MessageQueue queue, PersistenceSession persistence)
    {
        return new PersistentTaskQueuer<A>(queue, persistence);
    }
}
