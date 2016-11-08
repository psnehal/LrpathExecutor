package org.ncibi.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SentenceSegmenter {
    public static List<String> segment(String sentences)
    {
        String[] ss = sentences.split("[.!?]");   
        return new ArrayList<String>(Arrays.asList(ss));
    }
}

