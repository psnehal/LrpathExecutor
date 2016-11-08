package org.ncibi.main;

import java.util.EnumMap;
import java.util.List;

import org.hibernate.Session;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.SplitterArgs;
import org.ncibi.db.ws.Task;
import org.ncibi.db.ws.TaskType;
import org.ncibi.lrpath.LRPathArguments;
import org.ncibi.mqueue.MessageQueue;
import org.ncibi.mqueue.task.TaskQueuer;
import org.ncibi.task.TaskSubmitter;
import org.ncibi.task.executor.lrpath.LRPathArgRetriever;
import org.ncibi.task.executor.nlp.SegmenterArgRetriever;
import org.ncibi.task.executor.thinkback.GseaThinkArgRetriever;
import org.ncibi.task.factory.TaskQueuerFactory;
import org.ncibi.task.submitter.AbstractTaskSubmitter;
import org.ncibi.task.submitter.NoSubmitTaskSubmitter;
import org.ncibi.ws.thinkback.GseaThinkArgs;

final class Submitter
{
    private final PersistenceSession persistence;
    private final MessageQueue queue;

    private final EnumMap<TaskType, TaskSubmitter> taskSubmitters = new EnumMap<TaskType, TaskSubmitter>(
            TaskType.class);

    public Submitter(PersistenceSession persistence, MessageQueue queue)
    {
        this.persistence = persistence;
        this.queue = queue;
        initializeTaskSubmitters();
    }

    private void initializeTaskSubmitters()
    {
        taskSubmitters.put(TaskType.LRPATH, newLRPathTaskSubmitterForCommand(TaskType.LRPATH));      
        taskSubmitters.put(TaskType.LRPATH_CONCEPTGEN, newLRPathTaskSubmitterForCommand(TaskType.LRPATH_CONCEPTGEN));
        taskSubmitters.put(TaskType.LRPATH_JAVAX, newLRPathTaskSubmitterForCommand(TaskType.LRPATH_JAVAX));
        
        taskSubmitters.put(TaskType.LRPATH_THINK, newLRPathThinkTaskSubmitterForCommand(TaskType.LRPATH_THINK));
        taskSubmitters.put(TaskType.GSEA_THINK, newGseaThinkTaskSubmitterForCommand(TaskType.GSEA_THINK));

        taskSubmitters.put(TaskType.MXTERMINATOR, newNlpSegmenterSubmitterForCommand(TaskType.MXTERMINATOR));
        taskSubmitters.put(TaskType.SEGMENT_SENTENCES, newNlpSegmenterSubmitterForCommand(TaskType.SEGMENT_SENTENCES));
        
        taskSubmitters.put(TaskType.STOP, new NoSubmitTaskSubmitter(TaskType.STOP));
    }
    
    private TaskSubmitter newLRPathTaskSubmitterForCommand(TaskType command)
    {
        TaskQueuer<LRPathArguments> lrpathTaskQueuer = TaskQueuerFactory.newPersistentTaskQueuer(queue, persistence);
        return new AbstractTaskSubmitter<LRPathArguments>(new LRPathArgRetriever(
                persistence), lrpathTaskQueuer, command);
    }
    
    private TaskSubmitter newLRPathThinkTaskSubmitterForCommand(TaskType command)
    {
        TaskQueuer<LRPathArguments> lrpathTaskQueuer = TaskQueuerFactory.newPersistentTaskQueuer(queue, persistence);
        return new AbstractTaskSubmitter<LRPathArguments>(new LRPathArgRetriever(
                persistence), lrpathTaskQueuer, command);
    }

    private TaskSubmitter newGseaThinkTaskSubmitterForCommand(TaskType command) {
    	TaskQueuer<GseaThinkArgs> gseaTaskQueuer = TaskQueuerFactory.newPersistentTaskQueuer(queue, persistence);
    	return new AbstractTaskSubmitter<GseaThinkArgs>(new GseaThinkArgRetriever(
    			persistence), gseaTaskQueuer, command);
    }
    
    private TaskSubmitter newNlpSegmenterSubmitterForCommand(TaskType command)
    {
        TaskQueuer<SplitterArgs> nlpTaskQueuer = TaskQueuerFactory.newPersistentTaskQueuer(queue, persistence);
        return new AbstractTaskSubmitter<SplitterArgs>(new SegmenterArgRetriever(persistence), nlpTaskQueuer, command);
    }

    public void resubmitNonCompletedTasks()
    {
        System.out.println("Resubmitting unfinished tasks...");
        Session s = persistence.session();

        final List<Task> uncompletedTasks = persistence.hqlQuery(s, "from ws.Task where status = 'RUNNING'")
                .list();
        for (Task task : uncompletedTasks)
        {
            resubmitTaskByCommandType(task);
        }
        System.out.println("Finished resubmitting unfinished tasks.");
    }

    private void resubmitTaskByCommandType(Task task)
    {
        TaskSubmitter submitter = taskSubmitters.get(task.getTaskType());
        if (submitter == null)
        {
            System.out.println("ERROR: No submitter for command of type: " + task.getTaskType());
        }
        else
        {
            submitter.resubmitTask(task);
        }
    }
}
