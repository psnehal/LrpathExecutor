package org.ncibi.task.executor.thinkback;

import java.beans.XMLEncoder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.ncibi.commons.process.ShellScriptExternalProcess;
import org.ncibi.lrpath.LRPathArguments;
import org.ncibi.mqueue.task.TaskArgRetriever;
import org.ncibi.task.TaskWriter;
import org.ncibi.task.executor.AbstractTaskExecutor;
import org.ncibi.uuid.UuidUtil;
import org.ncibi.ws.AbstractBeanXMLEncoder;
import org.ncibi.ws.thinkback.LRThinkArgs;

import com.google.common.collect.ImmutableList;

public class LrpathThinkTaskExecutor extends AbstractTaskExecutor<LRThinkArgs, List<String>>
{
    private static class LRThinkArgsEncoder extends AbstractBeanXMLEncoder<LRPathArguments>
    {

        @Override
        protected void setupPersistenceDelegatesForType(XMLEncoder encoder)
        {

        }

    }

    public LrpathThinkTaskExecutor(TaskArgRetriever<LRThinkArgs> argRetriever,
            TaskWriter<List<String>, LRThinkArgs> taskWriter)
    {
        super(argRetriever, taskWriter);
    }

    /*
     * 
     * 0 - ThinkbackEnrichmentMethod 1 - ThinkbackAdjustmentMethod
     * 
     * LRPATH 2 - fileLRpathArguments 3 - filePathways
     */

    @Override
    protected List<String> executeWithArgs(LRThinkArgs args)
    {
        String fileLRpathArgs = UuidUtil.tempUuidFile("lrpathargs");

        try
        {
            FileWriter fstream = new FileWriter(fileLRpathArgs);
            BufferedWriter out = new BufferedWriter(fstream);
            LRThinkArgsEncoder encoder = new LRThinkArgsEncoder();
            encoder.setObjectToEncode(args.getLrpathArgs());
            out.write(encoder.toXmlString());
            // Close the output stream
            out.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        /**
         * CALL THINK-Back with List<LRPathResult>
         */
        List<String> scriptArgs = ImmutableList.of("LRPATH", args.getAdjustmentMethod().toString(),
                fileLRpathArgs, "/tmp/does-not-exist");
        System.out.println("run_lrpath_think.sh " + scriptArgs);
        ShellScriptExternalProcess script = new ShellScriptExternalProcess("run_gsea_think.sh", scriptArgs);
        return script.run(0);
    }
}
