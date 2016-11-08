package org.ncibi.task.submitter;

import org.ncibi.db.ws.Task;
import org.ncibi.mqueue.task.TaskArgRetriever;
import org.ncibi.mqueue.task.TaskQueuer;
import org.ncibi.task.TaskStatus;
import org.ncibi.task.TaskSubmitter;
import org.ncibi.db.ws.TaskType;

public class AbstractTaskSubmitter<A> implements TaskSubmitter
{
    private final TaskArgRetriever<A> argRetriever;
    private final TaskQueuer<A> taskQueuer;
    private final TaskType expectedCommand;

    public AbstractTaskSubmitter(TaskArgRetriever<A> argRetriever, TaskQueuer<A> taskQueuer,
            TaskType expectedCommand)
    {
        this.argRetriever = argRetriever;
        this.taskQueuer = taskQueuer;
        this.expectedCommand = expectedCommand;
    }

    @Override
    public void resubmitTask(Task task)
    {
        verifyCommandTypeForTask(task);

        System.out.printf("Resubmitting unfinished task %s of type: %s%n", task.getUuid(), task
                .getTaskType());
        A args = argRetriever.retrieveArgsForTask(task);
        task.setStatus(TaskStatus.QUEUED);
        taskQueuer.queue(task, args);
    }

    protected void verifyCommandTypeForTask(Task task)
    {
        if (task.getTaskType() != expectedCommand)
        {
            throw new IllegalArgumentException("Task has CommandType: " + task.getTaskType()
                    + ", Expected type is: " + expectedCommand);
        }
    }

}
