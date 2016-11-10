package edu.uic.cs441.gabe.Hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * Created by gabe on 10/9/16.
 */
public class PDFInputFormat extends FileInputFormat<Text, IntWritable> {

    @Override
    public RecordReader<Text, IntWritable> createRecordReader(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException {
        return new PDFLineRecordReader();
    }

    // Do not allow to ever split PDF files, even if larger than HDFS block size
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }

}
