package org.ncibi.task.executor.thinkback;

import java.util.List;

import org.ncibi.commons.process.ShellScriptExternalProcess;
import org.ncibi.mqueue.task.TaskArgRetriever;
import org.ncibi.task.TaskWriter;
import org.ncibi.task.executor.AbstractTaskExecutor;
import org.ncibi.ws.thinkback.GseaThinkArgs;

import com.google.common.collect.ImmutableList;

public class GseaThinkTaskExecutor extends AbstractTaskExecutor<GseaThinkArgs, List<String>>
{
    public GseaThinkTaskExecutor(TaskArgRetriever<GseaThinkArgs> argRetriever, TaskWriter<List<String>, GseaThinkArgs> taskWriter)
    {
        super(argRetriever, taskWriter);
    }
    
    @Override
    protected List<String> executeWithArgs(GseaThinkArgs args)
    {
        List<String> scriptArgs = ImmutableList.of(args.getDataset(), args.getClassfile(), args.getTemplate(), args.getChipfile());
        System.out.println("run_gsea_think.sh " + scriptArgs);
        ShellScriptExternalProcess script = new ShellScriptExternalProcess("run_gsea_think.sh", scriptArgs);
        return script.run(0);
    }
}
					