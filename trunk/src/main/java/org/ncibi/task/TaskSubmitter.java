package org.ncibi.task;

import org.ncibi.db.ws.Task;

public interface TaskSubmitter
{
    public void resubmitTask(Task task);
}
