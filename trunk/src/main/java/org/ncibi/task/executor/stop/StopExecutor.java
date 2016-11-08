package org.ncibi.task.executor.stop;

import org.ncibi.db.ws.Task;
import org.ncibi.task.TaskExecutor;

public class StopExecutor implements TaskExecutor
{
    @Override
    public void run(Task task)
    {
        System.exit(0);
    }
}
