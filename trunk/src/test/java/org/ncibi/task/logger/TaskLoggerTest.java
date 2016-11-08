package org.ncibi.task.logger;

import java.util.Date;

import junit.framework.Assert;

import org.apache.commons.lang.ObjectUtils.Null;
import org.hibernate.Session;
import org.junit.Ignore;
import org.junit.Test;
import org.ncibi.db.EntityManagers;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.PersistenceUnit;
import org.ncibi.db.ws.TaskLog;
import org.ncibi.hibernate.SessionFunction;
import org.ncibi.hibernate.Sessions;

@Ignore
public class TaskLoggerTest
{
    private static final PersistenceSession persistence = new PersistenceUnit(EntityManagers
                .newEntityManagerFromProject("task"));

    @Test
    public void testLogEntry()
    {
        final TaskLog taskLog = new TaskLog();
        taskLog.setEntryTimestamp(new Date());
        taskLog.setLogEntry("Hello World");
        taskLog.setTaskUuid("ABC12345");
        Sessions.withSession(persistence.session(), new SessionFunction<Null>()
        {
            @Override
            public Null apply(Session session)
            {
                session.saveOrUpdate(taskLog);
                return null;
            }
        });
        
        final TaskLog tl = persistence.hqlQuery("from ws.TaskLog where taskUuid = 'ABC12345'").single();
        Assert.assertTrue(tl.getLogEntry().equals(taskLog.getLogEntry()));
        Assert.assertTrue(tl.getEntryTimestamp().equals(taskLog.getEntryTimestamp()));
        System.out.println(tl);
    }
}
