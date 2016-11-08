package org.ncibi.task.executor;

import java.util.UUID;

import org.hibernate.Session;
import org.junit.Ignore;
import org.junit.Test;
import org.ncibi.db.EntityManagers;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.PersistenceUnit;
import org.ncibi.db.ws.TaskType;
import org.ncibi.db.ws.Task;
import org.ncibi.hibernate.SessionProcedure;
import org.ncibi.hibernate.Sessions;
import org.ncibi.mqueue.task.PersistentTaskDequeuer;
import org.ncibi.task.TaskStatus;

@Ignore
public class TaskDequeuerTest
{
    private static final PersistenceSession persistence = new PersistenceUnit(EntityManagers
                .newEntityManagerFromProject("task"));
    
    @Test
    public void testAllAspects()
    {
        System.out.println("Creating task...");
        Task task = createNewTask();
        System.out.println(task);
        PersistentTaskDequeuer td = new PersistentTaskDequeuer(null, persistence);
        System.out.println("Marking as done...");
        td.markTaskAs(task, TaskStatus.DONE);
        System.out.println(task);
        td.remove(task);
        System.out.println("Deleted task.");
    }
    
    private Task createNewTask()
    {
        final Task task = new Task();
        UUID uuid = UUID.randomUUID();
        task.setUuid(uuid.toString());
        task.setTaskType(TaskType.LRPATH);
        task.setStatus(TaskStatus.QUEUED);
        Sessions.withSession(persistence.session(), new SessionProcedure()
        {
            @Override
            public void apply(Session session)
            {
                session.saveOrUpdate(task);
            }      
        });
        
        return task;
    }
}
