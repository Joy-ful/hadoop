package com.hadoop.squenceFiletest;

import com.hadoop.hbase.HbaseUtil;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.net.URI;

public class SequenceFilePicture {
    private static Configuration conf = new Configuration();
    private static String HDFS_PATH = "hdfs:node1:9870";
    private static FileSystem fs;

    static {
        try {
            fs = FileSystem.get(URI.create(HDFS_PATH), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "/test/picture";
        Path output = new Path("/test/test_picture");
        //SnappyCodec snappyCodec = ReflectionUtils.newInstance(SnappyCodec.class, conf);
        GzipCodec snappyCodec = ReflectionUtils.newInstance(GzipCodec.class, conf);//压缩器
        SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, output, Text.class, BytesWritable.class, SequenceFile.CompressionType.NONE);
        FileStatus[] fileStatuses = fs.listStatus(new Path(path));

        //num.set(bytes1);
        for (FileStatus fileStatus : fileStatuses) {
            //byte fileText = new Byte(fileStatus.getPath().toString());
            String name = fileStatus.getPath().getName();
            byte[] bytes1 = IOUtils.toByteArray(name);
            //System.out.println("--------"+bytes1.length);
            Text b = new Text(bytes1);
            //System.out.println("文件地址："+fileText);

            FSDataInputStream in = fs.open(new Path(fileStatus.getPath().toString()));
            //System.out.println(in);

            byte[] bytes = IOUtils.toByteArray(in);
            in.read(bytes);
            BytesWritable value = new BytesWritable(bytes);

            /*System.out.println("key的值：" + b);
            System.out.println("key的长度：" + b.getLength());
            System.out.println("value的值：" + value);
            System.out.println("value的长度：" + value.getLength());
            //long length = writer.getLength();
            System.out.println("*******************************************");*/
            writer.append(b, value);
        }
        writer.close();
        System.out.println("-----------------------------------------------------------------------------------------------");
        //reader
        Path path1 = new Path("/test/test_picture");
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, path1, conf);
        Text keys = (Text) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
        BytesWritable values = (BytesWritable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
        SequenceFile.CompressionType type = reader.getCompressionType();
        boolean compressed = reader.isCompressed();
        //System.out.println("type:  " + type);
        //System.out.println("codec:  " + compressed);
        long pos = reader.getPosition();
        //System.out.println("头：" + pos);
        System.out.println("******************");
        //reader.seek(616);
        //System.out.println("文件指针：" + pos);
        while (reader.next(keys, values)) {
            long pos1 = reader.getPosition();
            System.out.println("新文件offset：" + pos1);
            // reader.sync(reader.getPosition());
            System.out.println("文件指针：" + reader.getPosition());
            System.out.println("key的值：" + keys);
            System.out.println("key的长度：" + keys.getLength());
            //System.out.println("value的值：" + values);
            System.out.println("value的长度：" + values.getLength());
            System.out.println("--------------------------------------------------");
            HbaseUtil.insert("test", keys.toString(), "myfc1", "key", String.valueOf(keys.getLength()));
           /* //文件指针
            long position = reader.getPosition();
            //System.out.println("文件指针：" + position);
            //key的长度
            long keysLengthlength = keys.getLength();
            //value的长度
            long valuesLengthlength = values.getLength();
            // offset
            long offset = position - valuesLengthlength;
            //http
            String http = "http://10.10.13.109:9870/webhdfs/v1" + output + "?op=OPEN&offset=";
            String length = "&length=";
            String value = http + offset + length + valuesLengthlength;
            System.out.println("value  "+value);*/
        }
        reader.close();
    }
}
