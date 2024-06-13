package it.unipi.cloud.combiner;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;

public class LetterTotalCount {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text("total");

    public static class CounterMapper extends Mapper<Object, Text, NullWritable, LongWritable> 
    {
        @Override
        public void map(Object key, Text value, Mapper<Object, Text, NullWritable, LongWritable>.Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split("\\s+");
            for (String str : words) {
                if (!str.isEmpty()) {
                    context.write(word, one);
                }
            }
        }
    }

    public static class CounterReducer extends Reducer<NullWritable, LongWritable, NullWritable, LongWritable> 
    {
        @Override
        public void reduce(NullWritable key, Iterable<LongWritable> values, Reducer<NullWritable, LongWritable, NullWritable, LongWritable>.Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable val : values) {
                sum += val.get();
            }
            context.write(key, new LongWritable(sum));
        }
    }   
}
