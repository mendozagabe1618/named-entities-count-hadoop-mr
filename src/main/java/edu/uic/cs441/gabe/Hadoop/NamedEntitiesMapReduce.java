package edu.uic.cs441.gabe.Hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;



public class NamedEntitiesMapReduce extends Configured implements Tool{


    static String[] args;

    //     Define a static logger variable
    final static Logger logger = Logger.getLogger(NamedEntitiesMapReduce.class);
    static {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }

	public static void main(String[] args) throws Exception {
        // Set up a simple configuration that logs on the console.
		 BasicConfigurator.configure();

        System.out.println("NamedEntitiesMapReduce\nExit ...");
		int res = ToolRunner.run(new Configuration(), new NamedEntitiesMapReduce(), args);
		System.exit(res);
	}

    @Override
	public int run(String[] args) throws Exception {
        NamedEntitiesMapReduce.args = args;
		if (args.length != 2) {
			System.out.println("usage: [input] [output]");
			System.exit(-1);
		}

		Job job = Job.getInstance( new Configuration());
		job.setOutputKeyClass( Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass( HadoopMapper.class);
		job.setReducerClass( HadoopReducer.class);

		job.setInputFormatClass( PDFInputFormat.class);
		job.setOutputFormatClass( TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setJarByClass( NamedEntitiesMapReduce.class);

		job.submit();
		return 0;
	}
}
