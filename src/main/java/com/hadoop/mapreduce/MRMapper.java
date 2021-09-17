package com.hadoop.mapreduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class MRMapper extends Mapper<Object, Text, Text, IntWritable> {

    IntWritable one = new IntWritable(1);

    Text word = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        String[] wordTosplit = value.toString().split(",");

        for (String words : wordTosplit) {
            word.set(words);
            //将word存到容器中，记一个数
            context.write(word, one);
        }

    }
}
