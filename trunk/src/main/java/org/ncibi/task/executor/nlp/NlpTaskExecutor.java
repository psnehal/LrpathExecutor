package org.ncibi.task.executor.nlp;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.ncibi.commons.io.FileOutputStreamProcessor;
import org.ncibi.commons.process.ShellScriptExternalProcess;
import org.ncibi.db.ws.SplitterArgs;
import org.ncibi.mqueue.task.TaskArgRetriever;
import org.ncibi.task.TaskWriter;
import org.ncibi.task.executor.AbstractTaskExecutor;
import org.ncibi.uuid.UuidUtil;

import com.google.common.collect.ImmutableList;

public class NlpTaskExecutor extends AbstractTaskExecutor<SplitterArgs, List<String>>
{
    private final String nlpScript;
    
    public NlpTaskExecutor(String nlpScript, TaskArgRetriever<SplitterArgs> argRetriever, TaskWriter<List<String>, SplitterArgs> taskWriter)
    {
        super(argRetriever, taskWriter);
        this.nlpScript = nlpScript;
    }
    
    @Override
    protected List<String> executeWithArgs(SplitterArgs args)
    {
        String filepath = serializeArgsToTemporaryFile(args);
        List<String> scriptArgs = ImmutableList.of(nlpScript, filepath);
        ShellScriptExternalProcess script = new ShellScriptExternalProcess("run_nlp_script.sh", scriptArgs);
        return script.run(0);
    }
    
    private String serializeArgsToTemporaryFile(final SplitterArgs args)
    {
        FileOutputStreamProcessor processor = new FileOutputStreamProcessor()
        {
            @Override
            public void processStream(FileOutputStream stream)
            {
                OutputStreamWriter o = new OutputStreamWriter(stream);
                BufferedWriter writer = new BufferedWriter(o);
                try
                {
                    writer.append(args.getSentences());
                    writer.append("\n");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
                        writer.close();
                    }
                    catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
        };
        
        String tempfile = UuidUtil.tempUuidFile();
        processor.process(tempfile);
        return tempfile;
    }
}
