package edu.uic.cs441.gabe.Hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;

public  class HadoopReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    private Logger logger = Logger.getLogger(HadoopReducer.class);

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context output)
            throws IOException, InterruptedException {
        int count = 0;
        for(IntWritable value: values){
            count += value.get();
        }
        output.write(key, new IntWritable(count));
    }
}
