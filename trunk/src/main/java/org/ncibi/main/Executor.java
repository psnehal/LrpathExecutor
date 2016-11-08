package org.ncibi.main;

import org.ncibi.commons.ipc.ThreadUtil;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.PersistenceUnit;
import org.ncibi.db.EntityManagers;
import org.ncibi.db.ws.Task;
import org.ncibi.log.Logger;
import org.ncibi.mqueue.MessageQueue;
import org.ncibi.mqueue.MessageQueueConfiguration;
import org.ncibi.mqueue.QueueOperationNotAllowedException;
import org.ncibi.mqueue.poll.NSecondsPoller;
import org.ncibi.mqueue.poll.Poller;
import org.ncibi.mqueue.task.PersistentTaskDequeuer;
import org.ncibi.task.TaskStatus;
import org.ncibi.task.executor.TaskRunner;
import org.ncibi.task.factory.TaskRunnerFactory;

final class Executor
{
    private final TaskRunner taskRunner;
    private final PersistentTaskDequeuer taskDequeuer;
    
    public Executor(PersistenceSession persistence, MessageQueue queue)
    {
        taskRunner = TaskRunnerFactory.newTaskRunner(persistence);
        taskDequeuer = createTaskDequeuer(persistence, queue);
    }
    
    private PersistentTaskDequeuer createTaskDequeuer(PersistenceSession persistence, MessageQueue queue)
    {
        final Poller poller = new NSecondsPoller(queue, MessageQueueConfiguration.pollingWait());
        final PersistentTaskDequeuer taskDequeuer = new PersistentTaskDequeuer(poller, persistence);
        return taskDequeuer;
    }
    
    public void retrieveAndExecuteTasks()
    {
        Logger.log.logMessage("Processing task requests...");
        while (true)
        {
        	try
        	{
        		retrieveAndRunNextTask();
        	}
        	catch (Exception e)
        	{
        		e.printStackTrace();
        	}
        }
    }
     
    private void retrieveAndRunNextTask()
    {
        Task task = retrieveNextTask();
        TaskStatus taskStatus = runTaskHandlingExceptions(task);
        taskDequeuer.markTaskAs(task, taskStatus);
    }
      
    private Task retrieveNextTask()
    {
        Task task = null;
        while (task == null)
        {
            task = retrieveNextTaskHandlingQueueExceptions();
        }
        
        return task;
    }

    private Task retrieveNextTaskHandlingQueueExceptions()
    {
        try
        {
            return taskDequeuer.dequeue();
        }
        catch (QueueOperationNotAllowedException e)
        {
            Logger.log.logMessage("Queue not currently processing requests.");
            ThreadUtil.waitSeconds(60);
            return null;
        }
    }
    
    private TaskStatus runTaskHandlingExceptions(Task task)
    {
    	TaskStatus status = TaskStatus.ERRORED;
    	
    	try
    	{
    		status = taskRunner.runTask(task);
    		
    		if( status.equals(TaskStatus.DONE)){
       		 PersistenceSession persistence = new PersistenceUnit(EntityManagers
       	                .newEntityManagerFromProject("task"));
       	        ChipEnrichDbConnection db = new ChipEnrichDbConnection();
       	        String uuid = task.getUuid();
       	        String email = db.getEmail(uuid);
       			JavaMailer mail = new JavaMailer();
       			String output = db.getOutname(uuid).split(";")[0];
       			String rnaenrich = db.getOutname(uuid).split(";")[2];
       			String directionField = db.getOutname(uuid).split(";")[1];
       			String status2= "";
       			if (rnaenrich.equals("yes"))
       			{
       				status2 = "RNA Enrich";
       				
       			}
       			else
       			{
       				status2 = "LR Path";
       			}
       			
       			String sender = "lrpath@umich.edu";
       			// String recipient = "snehalbpatil87@gmail.com";
       			String subject = status2+" Analysis Results";
       			String emailMessage = "Your "+status2+" "+ output  +" analysis is done. Please check your results here:   http://lrpath.ncibi.org/result.jsp?uid="+uuid+"&df="+directionField;
       			mail.sendMail(sender, email, subject, emailMessage);
       			Logger.log.logMessage("From task done id loop and name of the utr");}
    		
    		
    		
    	}
    	catch (Exception e)
    	{
    		Logger.log.logMessage("Task " + task.getUuid() + " failed with exception: " + e);
    	}
    	
    	return status;
    }
}
