package com.hadoop.squenceFiletest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class writeHDFSFileTOHDFS {

    private static Object IOException;

    //将HDFS某个目录下所有的小文件合并成一个SequenceFile文件（写）
    //以小文件的文件路径作为key，小文件的内容作为value。然后将其写入到SequenceFile文件。
    public static void main(String[] args) throws URISyntaxException, IOException {




            Configuration conf = new Configuration();
            FileSystem fileSystem = FileSystem.get(new URI("hdfs:10.10.13.109:9870"), conf);
            String inpueDir = "/test/input";
            Path path = new Path(inpueDir);
            //最后生成的sequenceFile文件
            Path output = new Path("/test/output");
            //生名一个byte[]用于存放小文件内容
            byte[] buffer;
            //获取input目录下所有文件
            FileStatus[] fileStatuses = fileSystem.listStatus(path);
            //构造write并使用try获取资源，关闭资源
            try (SequenceFile.Writer writer = SequenceFile.createWriter(
                    conf,
                    SequenceFile.Writer.keyClass(Text.class),
                    SequenceFile.Writer.valueClass(Text.class),
                    SequenceFile.Writer.file(output),
                    SequenceFile.Writer.appendIfExists(false),
                    SequenceFile.Writer.compression(SequenceFile.CompressionType.BLOCK, new GzipCodec())
            )) {
                //循环定义key和value，避免重复定义，
                Text key = new Text();
                Text value = new Text();

                for (FileStatus fileStatus : fileStatuses) {
                    System.out.println("this file name is ," + fileStatus.getPath());

                    //利用File System打开文件
                    FSDataInputStream fsDataInputStream = fileSystem.open(fileStatus.getPath());
                    //根据文件大小定义byte长度
                    buffer = new byte[(int) fileStatus.getLen()];
                    //将文件读到buffer这个byete[]中
                    fsDataInputStream.read(buffer);
                    key.set(fileStatus.getPath().toString());
                    value.set(buffer);
                    //通过append方法写到sequenFile
                    writer.append(key, value);
                }
            }
        }
    }
