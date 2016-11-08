package org.ncibi.task.executor;

import org.ncibi.db.ws.Task;
import org.ncibi.log.Logger;
import org.ncibi.mqueue.task.TaskArgRetriever;
import org.ncibi.task.TaskExecutor;
import org.ncibi.task.TaskStatus;
import org.ncibi.task.TaskWriter;

public abstract class AbstractTaskExecutor<A, R> implements TaskExecutor
{
    protected abstract R executeWithArgs(A args);

    private final TaskArgRetriever<A> argRetriever;
    private final TaskWriter<R, A> resultWriter;

    public AbstractTaskExecutor(TaskArgRetriever<A> argRetriever,
                TaskWriter<R, A> resultWriter)
    {
        this.argRetriever = argRetriever;
        this.resultWriter = resultWriter;
    }

    @Override
    public void run(Task task)
    {
        A args = argRetriever.retrieveArgsForTask(task);
        Logger.log.logMessage(task, "Retrieved args: " + truncatedArgs(args));
        R results = executeWithArgs(args);
        task.setStatus(TaskStatus.DONE);
        resultWriter.save(results, args, task);
        argRetriever.deleteArgsForTask(task);
    }
    
    private String truncatedArgs(A args)
    {
        String argsStr = args.toString();
        int length = argsStr.length();
        int lengthToTruncate = length < 500 ? length : 500;
        return argsStr.substring(0, lengthToTruncate);
    }
}
