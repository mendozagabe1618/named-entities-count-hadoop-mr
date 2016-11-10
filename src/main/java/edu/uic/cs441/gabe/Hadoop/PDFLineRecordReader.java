package edu.uic.cs441.gabe.Hadoop;

import edu.uic.cs441.gabe.TikaAndOpenNLP.OpenNLP;
import edu.uic.cs441.gabe.TikaAndOpenNLP.SplitDocument;
import edu.uic.cs441.gabe.TikaAndOpenNLP.TikaFile;
import opennlp.tools.util.Span;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.fs.Path;
import org.apache.tika.metadata.Metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PDFLineRecordReader extends RecordReader<Text, IntWritable> {

    private Text key ;
    private IntWritable value ;

    private SplitDocument splitDoc;
    private FSDataInputStream filein;
    private OpenNLP openNLP ;
    private TikaFile tikaFile = null;

    List<Text> entity_keys = new ArrayList<>();
    List<IntWritable> entity_values = new ArrayList<>();
    int currentLine = 0;

    public OpenNLP[] initializeOpenNlps() throws IOException{
        OpenNLP openNLPs [] = new OpenNLP[OpenNLP.NAMED_ENTITY_BINS.length];
        for(int i =0; i < OpenNLP.NAMED_ENTITY_BINS.length; i++){
            openNLPs[i] = new OpenNLP(OpenNLP.NAMED_ENTITY_BINS[i]);
        }
        return openNLPs;
    }


    @Override
    public void initialize(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException {

        FileSplit fileSplit = (FileSplit) split;
        final Path file = fileSplit.getPath();

        Configuration conf = context.getConfiguration();
        FileSystem fs = file.getFileSystem(conf);

        filein = fs.open(fileSplit.getPath());
        if (filein != null) {
            InputStream stream = filein.getWrappedStream();
            try {
                tikaFile = new TikaFile((stream));
                splitDoc = openNLP.splitDocument(tikaFile);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        int size = OpenNLP.NAMED_ENTITY_BINS.length;
        OpenNLP[] finders = initializeOpenNlps();


        String[] tokens;
        Span span[][] = new Span[size][];

        for(Text sentence : splitDoc.getSentences() ){
             tokens = finders[0].tokenize(sentence.toString());

            int i =0;
            for(OpenNLP nameFinder : finders ){
                span[i] = nameFinder.findEntitiesInTokens(tokens);

                String[] spanstrings = Span.spansToStrings(span[i], tokens);
                for( String entity : spanstrings) {

                    StringBuilder sb = new StringBuilder();
                    sb.append("[").append(entity).append(" , ");
                    sb.append(tikaFile.getMetadata().get(Metadata.TITLE));
                    sb.append(" , ");

                    switch(nameFinder.getNameFinderBin()) {
                        case OpenNLP.NAMED_ENTITY_DATE_BIN:
                            sb.append( "Date");
                            break;
                        case OpenNLP.NAMED_ENTITY_LOCATION_BIN:
                            sb.append("Money");
                            break;
                        case OpenNLP.NAMED_ENTITY_MONEY_BIN:
                            sb.append("Money");
                            break;
                        case OpenNLP.NAMED_ENTITY_ORGANIZATION_BIN:
                            sb.append("Oranization");
                            break;
                        case OpenNLP.NAMED_ENTITY_PERSON_BIN:
                            sb.append("Person");
                            break;
                        case OpenNLP.NAMED_ENTITY_TIME_BIN:
                            sb.append("Time");
                            break;
                        default:
                    }

                    sb.append("]");
                    entity_keys.add(new Text(sb.toString()) );
                    entity_values.add(new IntWritable(1));
                }

                i++;
            }



        }










    }

    // False ends the reading process
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {

        if (key == null) {
            key = new Text();
        }

        if (value == null) {
            value = new IntWritable();
        }

        if (currentLine < splitDoc.getSize()) {
            key = entity_keys.get(currentLine);
            value = entity_values.get(currentLine);

            currentLine++;

            return true;
        } else {

            // All lines are read? -> end
            key = null;
            value = null;
            return false;
        }
    }

    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return entity_keys.get(currentLine);
    }

    @Override
    public IntWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return (100.0f / splitDoc.getSize() * currentLine) / 100.0f;
    }

    @Override
    public void close() throws IOException {


    }

}