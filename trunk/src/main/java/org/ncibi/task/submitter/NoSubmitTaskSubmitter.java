package org.ncibi.task.submitter;

import org.apache.commons.lang.ObjectUtils.Null;
import org.ncibi.db.ws.TaskType;
import org.ncibi.db.ws.Task;

public class NoSubmitTaskSubmitter extends AbstractTaskSubmitter<Null>
{
    public NoSubmitTaskSubmitter(TaskType expectedCommand)
    {
        super(null, null, expectedCommand);
    }

    @Override
    public void resubmitTask(Task task)
    {
        verifyCommandTypeForTask(task);
        return;
    }
}
