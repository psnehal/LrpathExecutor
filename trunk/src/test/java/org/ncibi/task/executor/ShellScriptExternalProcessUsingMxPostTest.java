package org.ncibi.task.executor;

import java.util.List;

import org.junit.Test;
import org.ncibi.commons.process.ShellScriptExternalProcess;

import com.google.common.collect.ImmutableList;

public class ShellScriptExternalProcessUsingMxPostTest
{
    @Test
    public void testMxPostCall()
    {
        // Command paths are hard coded. This needs to be changed.
        List<String> args = ImmutableList.of("/Users/gtarcea/Work/nlp/test.txt");
        String command = "/Users/gtarcea/Work/nlp/runmxpost.sh";
        ShellScriptExternalProcess script = new ShellScriptExternalProcess(command, args);
        List<String> lines = script.run(0);
        System.out.println(lines);
    }
}
