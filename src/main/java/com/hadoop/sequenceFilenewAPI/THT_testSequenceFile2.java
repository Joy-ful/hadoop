package com.hadoop.sequenceFilenewAPI;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.util.ReflectionUtils;

public class THT_testSequenceFile2 {
    private static Configuration conf = new Configuration();

    private static final String[] DATA = {"One, two, buckle my shoe",
            "Three, four, shut the door", "Five, six, pick up sticks",
            "Seven, eight, lay them straight", "Nine, ten, a big fat hen"};

    public static void main(String[] args) throws IOException {
        ReadSequences();
        //writeSequences();
    }

    public static void writeSequences() {
        // String uri = args[0];
        // Configuration conf = new Configuration();
        String uri = "/test/test_new1";
        Path path = new Path(uri);

        IntWritable key = new IntWritable();
        Text value = new Text();
        SequenceFile.Writer writer = null;
        SequenceFile.Writer.Option option1 = Writer.file(path);
        SequenceFile.Writer.Option option2 = Writer.keyClass(key.getClass());
        SequenceFile.Writer.Option option3 = Writer.valueClass(value.getClass());

        try {

            writer = SequenceFile.createWriter(conf, option1, option2, option3, Writer.compression(SequenceFile.CompressionType.RECORD));

            for (int i = 0; i < 10; i++) {
                key.set(1 + i);
                value.set(DATA[i % DATA.length]);
                System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key,
                        value);
                writer.append(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(writer);
        }
    }

    public static void ReadSequences() {
        //String uri = args[0];
        String uri = "/test/test_new1";
        Configuration conf = new Configuration();
        Path path = new Path(uri);
        SequenceFile.Reader.Option option1 = SequenceFile.Reader.file(path);
        SequenceFile.Reader.Option option2 = SequenceFile.Reader.length(130);//这个参数表示读取的长度
        //SequenceFile.Metadata
        SequenceFile.Metadata metadata = new SequenceFile.Metadata();
        TreeMap<Text, Text> metadata1 = metadata.getMetadata();
        System.out.println(metadata1);
        SequenceFile.Reader reader = null;
        try {
            reader = new SequenceFile.Reader(conf, option1, option2);
            Writable key = (Writable) ReflectionUtils.newInstance(
                    reader.getKeyClass(), conf);
            Writable value = (Writable) ReflectionUtils.newInstance(
                    reader.getValueClass(), conf);
            long position = reader.getPosition();
            //long l = 160;
            //reader.sync(l);
            //long seeks = reader.seek();
            while (reader.next(key, value)) {
                String syncSeen = reader.syncSeen() ? "*" : "";
                System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, key,
                        value);
                position = reader.getPosition(); // beginning of next record


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(reader);
        }

    }

}
