package org.ncibi.main;

import org.ncibi.db.EntityManagers;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.PersistenceUnit;
import org.ncibi.mqueue.MessageQueue;
import org.ncibi.mqueue.MessageQueueConfiguration;
import org.ncibi.mqueue.persistent.AnyMessagePersistentMessageQueue;
//import org.ncibi.rabbitmq.RabbitMQ;

public final class TaskExecutorMain
{
    private static final PersistenceSession persistence = new PersistenceUnit(EntityManagers
            .newEntityManagerFromProject("task"));;
    private static final MessageQueue queue = new AnyMessagePersistentMessageQueue(persistence,
            MessageQueueConfiguration.queueName());
    //private static final MessageQueue rabbitQueue = RabbitMQ.createQueueAsReceiver(
    //        MessageQueueConfiguration.queueName(), MessageQueueConfiguration.queueHost());

    public static void main(String[] args)
    {
        if (args.length == 1 && "config".equals(args[0]))
        {
            Configuration c = new Configuration(false);
            c.logConfiguration();
        }
        else if (args.length == 1 && "resubmit".equals(args[0]))
        {
            Submitter s = new Submitter(persistence, queue);
            s.resubmitNonCompletedTasks();
        }
        else
        {
            startupAndRetrieveTasks();
        }
    }

    private static void startupAndRetrieveTasks()
    {
        logConfigurationForPid();
        foreverRetrieveAndExecuteTasks();
    }

    private static void logConfigurationForPid()
    {
        Configuration c = new Configuration(true);
        c.logConfiguration();
    }

    private static void foreverRetrieveAndExecuteTasks()
    {
        Executor e = new Executor(persistence, queue);
        e.retrieveAndExecuteTasks();
    }
}
