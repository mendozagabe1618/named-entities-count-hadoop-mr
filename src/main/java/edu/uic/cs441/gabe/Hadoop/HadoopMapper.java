package edu.uic.cs441.gabe.Hadoop;

import edu.uic.cs441.gabe.TikaAndOpenNLP.OpenNLP;
import opennlp.tools.util.Span;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.*;


public class HadoopMapper extends Mapper<Text, IntWritable, Text, IntWritable> {
//    private final static IntWritable one = new IntWritable(1);

    public static final Log logger = LogFactory.getLog(HadoopMapper.class);


    private final static IntWritable one = new IntWritable(1);


    @Override
    public void map(Text key, IntWritable value, Context output) throws IOException,
            InterruptedException {



        output.write(new Text(), one);
    }
}
