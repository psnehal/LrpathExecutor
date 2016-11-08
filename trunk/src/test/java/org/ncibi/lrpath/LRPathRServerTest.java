package org.ncibi.lrpath;

import org.junit.Test;
import org.ncibi.lrpath.config.RServerConfiguration;

public class LRPathRServerTest
{
    @Test
    public void testRServer() throws Exception
    {
        LRPathRServer rserver = new LRPathRServer(RServerConfiguration.rserverAddress(),
                    RServerConfiguration.rserverPort());
        rserver.assignRVariable("stuff", "hello world");
        String r = rserver.evalRCommand("stuff").asString();
        System.out.println(r);
    }
}
