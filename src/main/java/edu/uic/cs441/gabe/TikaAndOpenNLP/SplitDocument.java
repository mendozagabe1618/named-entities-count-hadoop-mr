package edu.uic.cs441.gabe.TikaAndOpenNLP;

import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabe on 10/6/16.
 */

public class SplitDocument {
    List<Text> document;
    int currentline;

    public SplitDocument(String[] sentences) {
        currentline = 0;
        document = new ArrayList<>();
        for (int i = 0; i < sentences.length; i++) {
            document.add(new Text(sentences[i]));
        }
    }

    public int getSize() {
        return document.size();
    }

    public void add(String sentence) {
        document.add(new Text(sentence));
    }

    public Text getSentence(int line) {
        return document.get(line);
    }

    public List<Text> getSentences() {
        return document;
    }

}