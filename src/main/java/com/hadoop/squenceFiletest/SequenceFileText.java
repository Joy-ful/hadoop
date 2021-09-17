package com.hadoop.squenceFiletest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.net.URI;
import java.util.TreeMap;

public class SequenceFileText {
    private static Configuration configuration = new Configuration();
    private static String HDFS_PATH = "hdfs:node1:9870";
    private static String[] date = {"a,b,c,d,e,f,g", "e,f,g,h,j,k", "l,m,n,o,p,q,r,s", "t,y,v,w,x,y,z"};

    public static void main(String[] args) throws IOException {

        //System.setProperty("HADOOP_USER_NAME", "hadoop");
        //writer
        FileSystem fileSystem = FileSystem.get(URI.create(HDFS_PATH), configuration);
        Path output = new Path("/test/MySequenceFile.txt");
        IntWritable key = new IntWritable();
        Text value = new Text();
        SequenceFile.Writer writer = SequenceFile.createWriter(fileSystem, configuration, output, IntWritable.class, Text.class, SequenceFile.CompressionType.RECORD, new BZip2Codec());
        for (int i = 0; i < 10; i++) {
            key.set(10 - i);
            value.set(date[i % date.length]);
            writer.append(key, value);
        }
        IOUtils.closeStream(writer);

        //reader
        Path path = new Path("/test/MySequenceFile.txt");
        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem, path, configuration);
        Writable keys = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), configuration);
        Writable values = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), configuration);
        long pos = reader.getPosition();
        System.out.println(pos);
        //reader.seek(616);
        //System.out.println("文件指针：" + pos);

        while (reader.next(keys, values)) {
            long pos1 = reader.getPosition();
            System.out.println(pos1);
            // reader.sync(reader.getPosition());
            System.out.println("文件指针：" + reader.getPosition());
            System.out.println("key的值" + key.toString() + "===>" + "value的值" + value.toString());

            //System.out.println("key"+keys)
            //System.out.println("values"+values);
            //reader.getCurrentValue(values);
            // reader.seek(248);
            //System.out.println("position"+reader.getPosition());
        }

        //IOUtils.closeStream(reader);


    }
}
