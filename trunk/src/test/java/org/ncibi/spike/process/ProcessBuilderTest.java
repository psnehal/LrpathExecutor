package org.ncibi.spike.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Test;

public class ProcessBuilderTest
{
    @Test
    public void testReadingProcessStream() throws IOException
    {
        ProcessBuilder pb = new ProcessBuilder( "/Users/gtarcea/echo.sh", "hello");
        System.out.println(System.getProperty("os.name"));
        List<String> c = pb.command();
        System.out.println(c);
        Process p = pb.start();
        InputStream str = p.getInputStream();
        InputStreamReader sr = new InputStreamReader(str);
        BufferedReader in = new BufferedReader(sr);
        while (true)
        {
            String output = in.readLine();
            if (output == null)
            {
                break;
            }
            System.out.println(output);
        }
    }
}
