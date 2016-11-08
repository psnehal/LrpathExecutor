package org.ncibi.task;

import org.ncibi.db.ws.Task;

public interface TaskExecutor
{
    public void run(Task task);
}
