package org.ncibi.task.factory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Vector;

import org.ncibi.db.PersistenceSession;
import org.ncibi.db.ws.SplitterArgs;
import org.ncibi.db.ws.Task;
import org.ncibi.db.ws.TaskType;
import org.ncibi.lrpath.LRPathArguments;
import org.ncibi.lrpath.LRPathResult;
import org.ncibi.task.CTask;
import org.ncibi.task.FileTaskWriter;
import org.ncibi.task.TaskExecutor;
import org.ncibi.task.TaskWriter;
import org.ncibi.task.executor.TaskRunner;
import org.ncibi.task.executor.lrpath.LRPathArgRetriever;
import org.ncibi.task.executor.lrpath.LRPathTaskExecutor;
import org.ncibi.task.executor.nlp.NlpTaskExecutor;
import org.ncibi.task.executor.nlp.SegmenterArgRetriever;
import org.ncibi.task.executor.nlp.SegmenterTaskExecutor;
import org.ncibi.task.executor.stop.StopExecutor;
import org.ncibi.task.executor.thinkback.GseaThinkArgRetriever;
import org.ncibi.task.executor.thinkback.GseaThinkTaskExecutor;
import org.ncibi.task.executor.thinkback.LrpathThinkArgRetriever;
import org.ncibi.task.executor.thinkback.LrpathThinkTaskExecutor;
import org.ncibi.ws.Response;
import org.ncibi.ws.ResponseStatus;
import org.ncibi.ws.encoder.XmlEncoder;
import org.ncibi.ws.encoder.status.LRPathRequestStatusResponseEncoder;
import org.ncibi.ws.model.encoder.xstream.NcibiXStreamNlpEncoder;
import org.ncibi.ws.request.RequestStatus;
import org.ncibi.ws.thinkback.GseaThinkArgs;
import org.ncibi.ws.thinkback.LRThinkArgs;

import com.thoughtworks.xstream.XStream;

public final class TaskRunnerFactory
{
    private static final XStream xstream = new XStream();

    public static TaskRunner newTaskRunner(PersistenceSession persistence)
    {
        final EnumMap<TaskType, TaskExecutor> taskExecutors = new EnumMap<TaskType, TaskExecutor>(
                TaskType.class);
        taskExecutors.put(TaskType.LRPATH, new LRPathTaskExecutor(new LRPathArgRetriever(persistence),
                newXmlTaskWriter()));
        taskExecutors.put(TaskType.LRPATH_JAVAX, new LRPathTaskExecutor(
                new LRPathArgRetriever(persistence), newJavaBeanXMLTaskWriter()));
        taskExecutors.put(TaskType.LRPATH_CONCEPTGEN, new LRPathTaskExecutor(new LRPathArgRetriever(
                persistence), newConceptgenTaskWriter()));
        taskExecutors.put(TaskType.SEGMENT_SENTENCES, new SegmenterTaskExecutor(new SegmenterArgRetriever(
                persistence), newSegmenterTaskWriter()));
        taskExecutors.put(TaskType.STOP, new StopExecutor());
        taskExecutors.put(TaskType.MXTERMINATOR, new NlpTaskExecutor("mxpost.sh",
                new SegmenterArgRetriever(persistence), newNlpTaskWriter()));
        taskExecutors.put(TaskType.STANFORD_PARSER, new NlpTaskExecutor("stanford_parser.sh",
                new SegmenterArgRetriever(persistence), newNlpSingleLineTaskWriter()));
        taskExecutors.put(TaskType.GSEA_THINK, new GseaThinkTaskExecutor(new GseaThinkArgRetriever(
                persistence), newGseaThinkTaskWriter()));
        taskExecutors.put(TaskType.LRPATH_THINK, new LrpathThinkTaskExecutor(new LrpathThinkArgRetriever(
                persistence), newLrpathThinkTaskWriter()));
        return new TaskRunner(taskExecutors);
    }

    private static TaskWriter<List<LRPathResult>, LRPathArguments> newXmlTaskWriter()
    {
        TaskWriter<List<LRPathResult>, LRPathArguments> taskWriter = new FileTaskWriter<List<LRPathResult>, LRPathArguments>()
        {
            @Override
            protected void writeResults(BufferedWriter out, Task task, List<LRPathResult> results,
                    LRPathArguments args) throws IOException
            {
                out.write("<result>\n");
                for (LRPathResult result : results)
                {
                    write(out, result.getConceptId(), "conceptId");
                    write(out, result.getConceptName(), "conceptName");
                    write(out, result.getConceptType(), "conceptType");
                    write(out, Double.toString(result.getCoeff()), "coeff");
                    write(out, Double.toString(result.getFdr()), "fdr");
                    write(out, Integer.toString(result.getNumUniqueGenes()), "uniqueGenesCount");
                    write(out, Double.toString(result.getOddsRatio()), "oddsRatio");
                    write(out, Double.toString(result.getPValue()), "pvalue");
                    write(out, result.getSigGenes(), "gene");
                }
                out.write("</result>\n");
            }

            private void write(BufferedWriter out, String value, String tag) throws IOException
            {
                out.write("<" + tag + ">" + value + "</" + tag + ">\n");
            }

            private void write(BufferedWriter out, Vector<String> values, String tag) throws IOException
            {
                out.write("<vector>\n");
                for (String value : values)
                {
                    write(out, value, tag);
                }
                out.write("</vector>");
            }
        };

        return taskWriter;
    }

    private static TaskWriter<List<LRPathResult>, LRPathArguments> newJavaBeanXMLTaskWriter()
    {
        TaskWriter<List<LRPathResult>, LRPathArguments> taskWriter = new FileTaskWriter<List<LRPathResult>, LRPathArguments>()
        {
            @Override
            protected void writeResults(BufferedWriter out, Task task, List<LRPathResult> results,
                    LRPathArguments args) throws IOException
            {
                ResponseStatus status = new ResponseStatus(null, true, "Success");
                CTask ctask = new CTask(task.getUuid(), task.getStatus());
                RequestStatus<List<LRPathResult>> rs = new RequestStatus<List<LRPathResult>>(ctask, results);
                Response<RequestStatus<List<LRPathResult>>> response = new Response<RequestStatus<List<LRPathResult>>>(
                        status, rs);
                String xml = new LRPathRequestStatusResponseEncoder(response).toXmlString();
                out.write(xml);
            }
        };

        return taskWriter;
    }

    private static TaskWriter<List<LRPathResult>, LRPathArguments> newConceptgenTaskWriter()
    {
        TaskWriter<List<LRPathResult>, LRPathArguments> taskWriter = new FileTaskWriter<List<LRPathResult>, LRPathArguments>()
        {
            @Override
            protected void writeResults(BufferedWriter out, Task task, List<LRPathResult> results,
                    LRPathArguments args) throws IOException
            {
                ResponseStatus status = new ResponseStatus(null, true, "Success");
                CTask ctask = new CTask(task.getUuid(), task.getStatus());
                RequestStatus<List<LRPathResult>> rs = new RequestStatus<List<LRPathResult>>(ctask, results);
                Response<RequestStatus<List<LRPathResult>>> response = new Response<RequestStatus<List<LRPathResult>>>(
                        status, rs);
                String xml = new LRPathRequestStatusResponseEncoder(response).toXmlString();
                out.write(xml);

            }
        };

        return taskWriter;
    }

    private static TaskWriter<List<String>, SplitterArgs> newSegmenterTaskWriter()
    {
        TaskWriter<List<String>, SplitterArgs> taskWriter = new FileTaskWriter<List<String>, SplitterArgs>()
        {
            @Override
            protected void writeResults(BufferedWriter out, Task task, List<String> sentences,
                    SplitterArgs args) throws IOException
            {
                for (String sentence : sentences)
                {
                    out.write(sentence);
                    out.newLine();
                }
            }
        };
        return taskWriter;
    }

    private static TaskWriter<List<String>, SplitterArgs> newNlpTaskWriter()
    {
        TaskWriter<List<String>, SplitterArgs> taskWriter = new FileTaskWriter<List<String>, SplitterArgs>()
        {
            @Override
            protected void writeResults(BufferedWriter out, Task task, List<String> what, SplitterArgs args)
                    throws IOException
            {
                XmlEncoder<Response<List<String>>> encoder = new NcibiXStreamNlpEncoder<List<String>>(xstream);
                ResponseStatus status = new ResponseStatus(null, true, "Success");
                Response<List<String>> response = new Response<List<String>>(status, what);
                encoder.setResult(response);
                String xml = encoder.toXml();
                out.write(xml);
            }
        };

        return taskWriter;
    }

    private static TaskWriter<List<String>, SplitterArgs> newNlpSingleLineTaskWriter()
    {
        TaskWriter<List<String>, SplitterArgs> taskWriter = new FileTaskWriter<List<String>, SplitterArgs>()
        {
            @Override
            protected void writeResults(BufferedWriter out, Task task, List<String> what, SplitterArgs args)
                    throws IOException
            {
                XmlEncoder<Response<String>> encoder = new NcibiXStreamNlpEncoder<String>(xstream);
                ResponseStatus status = new ResponseStatus(null, true, "Success");
                StringBuilder sb = new StringBuilder(10000);
                for (String entry : what)
                {
                    sb.append(entry.trim());
                }
                Response<String> response = new Response<String>(status, sb.toString());
                encoder.setResult(response);
                String xml = encoder.toXml();
                out.write(xml);
            }
        };

        return taskWriter;
    }
    
    private static TaskWriter<List<String>, GseaThinkArgs> newGseaThinkTaskWriter()
    {
        TaskWriter<List<String>, GseaThinkArgs> taskWriter = new FileTaskWriter<List<String>, GseaThinkArgs>()
        {
            @Override
            protected void writeResults(BufferedWriter out, Task task, List<String> what, GseaThinkArgs args)
                    throws IOException
            {
                StringBuilder sb = new StringBuilder(10000);
                for (String entry : what)
                {
                    sb.append(entry.trim());
                }
                out.write(sb.toString());
            }            
        };
        
        return taskWriter;
    }
    
    private static TaskWriter<List<String>, LRThinkArgs> newLrpathThinkTaskWriter() {
    	
    	TaskWriter<List<String>, LRThinkArgs> taskWriter = new FileTaskWriter<List<String>, LRThinkArgs>() {
            @Override
            protected void writeResults(BufferedWriter out, Task task, List<String> what, LRThinkArgs args)
                    throws IOException
            {
                StringBuilder sb = new StringBuilder(10000);
                for (String entry : what)
                {
                    sb.append(entry.trim());
                }
                out.write(sb.toString());
            }            
    	};
    	return taskWriter;
    }
}
