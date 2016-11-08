package org.ncibi.task.executor.nlp;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.ncibi.db.ws.SplitterArgs;
import org.ncibi.ws.Response;
import org.ncibi.ws.ResponseStatus;
import org.ncibi.ws.encoder.XmlEncoder;
import org.ncibi.ws.model.encoder.xstream.NcibiXStreamNlpEncoder;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.xstream.XStream;

public class NlpTaskExecutorTest
{
    @Test
    public void testExecuteWithArgsForMxPost()
    {
        NlpTaskExecutor executor = new NlpTaskExecutor("mxpost.sh", null, null);
        SplitterArgs args = new SplitterArgs();
        args
                .setSentences("First sentence. Second sentence! Is this the third sentence? This is the fourth sentence.");
        List<String> sentences = executor.executeWithArgs(args);
        Assert.assertEquals(4, sentences.size());
        Assert.assertEquals("First sentence.", sentences.get(0));
        Assert.assertEquals("Second sentence!", sentences.get(1));
        Assert.assertEquals("Is this the third sentence?", sentences.get(2));
        Assert.assertEquals("This is the fourth sentence.", sentences.get(3));
    }

    @Test
    public void testEncoder()
    {
        List<String> sentences = ImmutableList.of("First sentence.", "Second sentence!",
                "Third sentence?");
        XStream xstream = new XStream();
        XmlEncoder<Response<List<String>>> encoder = new NcibiXStreamNlpEncoder<List<String>>(xstream);
        ResponseStatus status = new ResponseStatus(null, true, "Success");
        Response<List<String>> response = new Response<List<String>>(status, sentences);
        encoder.setResult(response);
        String xml = encoder.toXml();
        System.out.println(xml);
    }
}
