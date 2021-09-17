package com.hadoop.sequenceFilenewAPI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;

public class SequenceFileNew {
    private static Configuration conf = new Configuration();

    public static void main(String[] args) throws IOException {
        NewWrite();
        //NewRead();
    }

    public static void NewWrite() throws IOException {
        SequenceFile.Writer.Option newfile = SequenceFile.Writer.file(new Path("/test/test_new"));
        SequenceFile.Writer.Option keyClass = SequenceFile.Writer.keyClass(IntWritable.class);
        SequenceFile.Writer.Option valueClass = SequenceFile.Writer.valueClass(IntWritable.class);
        SequenceFile.Writer writer = SequenceFile.createWriter(conf, newfile, keyClass, valueClass);
        for (int i = 0; i < 100; i++) {
            writer.append(new IntWritable(i), new IntWritable(i + 1));
            System.out.println(new IntWritable(i) + "," + new IntWritable(i + 1));
        }
        writer.close();
    }

    public static void NewRead() throws IOException {
        String url = "/test/test_new";
        Path path = new Path(url);
        SequenceFile.Reader.Option file = SequenceFile.Reader.file(path);
        SequenceFile.Reader.Option length = SequenceFile.Reader.length(50);
        SequenceFile.Reader reader = null;
        try {
            reader = new SequenceFile.Reader(conf, file, length);
            Writable key = (Writable) ReflectionUtils.newInstance(
                    reader.getKeyClass(), conf);
            Writable value = (Writable) ReflectionUtils.newInstance(
                    reader.getValueClass(), conf);
            long position = reader.getPosition();
            while (reader.next(key, value)) {
                String syncSeen = reader.syncSeen() ? "*" : "";
                System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, key,
                        value);
                position = reader.getPosition(); // beginning of next record
            }
        } finally {
            IOUtils.closeStream(reader);
        }
    }
}
