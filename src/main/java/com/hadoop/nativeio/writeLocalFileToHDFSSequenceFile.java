package com.hadoop.nativeio;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

//注意：使用MapFile或SequenceFile虽然可以解决HDFS中小文件的存储问题，但也有一定局限性，如：
// 1. 文件不支持复写操作，不能向已存在的SequenceFile(MapFile)追加存储记录。
// 2. 当write流不关闭的时候，没有办法构造read流。也就是在执行文件写操作的时候，该文件是不可读取的。
public class writeLocalFileToHDFSSequenceFile {
    static Configuration conf = new Configuration();
    static String HDFS_PATH = "hdfs:node1:9870";
    static FileSystem fs;

    static {
        try {
            fs = FileSystem.get(URI.create(HDFS_PATH), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        //writeHDFS();
        readHDFS();
        //search();
    }

    public static void readHDFS() throws Exception {
        Configuration conf = new Configuration();
        Path path = new Path("/test/test1");

        //writeHDFS();
        try (
                SequenceFile.Reader reader = new SequenceFile.Reader(conf,
                        SequenceFile.Reader.file(path),
                        SequenceFile.Reader.bufferSize(1024 * 8)
                )) {
            Text key = new Text();
            Text value = new Text();
            long position = reader.getPosition();

            while (reader.next(key, value)) {
                System.out.println(key);
                System.out.println(value);
                System.out.println("***************************************");
                System.out.println(position);

            }
            //reader.close();
        }
        //reader.close();
    }

    //上传SequenceFile文件
    public static void writeHDFS() throws Exception {
        Configuration conf = new Configuration();
        String inputDir = "D:\\临时目录\\input";
        java.nio.file.Path localDir = Paths.get(inputDir);
        Path outFile = new Path("/test/test1");
        //构造writer, 并使用try获取资源, 最后自动关闭资源
        try (SequenceFile.Writer writer = SequenceFile.createWriter(conf,
                SequenceFile.Writer.file(outFile),//设置文件名
                SequenceFile.Writer.keyClass(Text.class),//设置keyclass
                SequenceFile.Writer.valueClass(Text.class),//设置valueclass
                SequenceFile.Writer.appendIfExists(false),
                SequenceFile.Writer.compression(SequenceFile.CompressionType.BLOCK, new GzipCodec()) //设置block+gzip的压缩方式
        )) {
            Text key = new Text();
            Text value = new Text();



            // Collection<File> fileList = FileUtils.listFiles(new File(inputDir),null,false);
            try (Stream<java.nio.file.Path> stream = Files.list(localDir)) {
                //遍历所有以.txt结尾的小文件写入到SequenceFile
                stream.filter(x -> x.toString().endsWith(".txt")).forEach(localPath -> {
                    try {
                        //System.out.println(localPath.toString());
                        byte[] bytes = Files.readAllBytes(localPath);
                        key.set(localPath.toString().substring(14, 19));
                        value.set(bytes);
                        writer.append(key, value);
                        System.out.println(key);
                        System.out.println(value);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("read " + localPath.toString() + " error");
                    }
                });
                writer.close();
            }
        }
    }

    public static void search() throws Exception {
        Configuration conf = new Configuration();
        Path inpath = new Path("/test/test1");

        String outDir = "/test/test_out";
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入文件名：");
        String name = sc.nextLine();
        SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(inpath));
        Writable key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
        Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
        boolean flag = false;
        DataOutputStream out = null;
        Map<Writable, Writable> kvMap = new MapWritable();
        byte[] bys = new byte[1024];
        while (reader.next(key, value)) {
            //System.out.println(key);
            if (key.toString().equals(name)) {
                if (!flag) {
                    System.out.println(key);
                    System.out.println(value);
                    kvMap.put(key, value);


                            //System.out.println(kvMap);
                    /*boolean mkdirs = fs.mkdirs(new Path(outDir));
                    System.out.println(mkdirs);
                    out = new DataOutputStream(new FileOutputStream(outDir));
                    out.writeBytes(key.getClass().getName()+"\t"+value+"\n");
                    System.out.println(out);*/
                    /*if (!file.exists()) {
                        boolean mkdirs = file.mkdirs();
                        System.out.println(mkdirs);
                    }
                    //new File(outDir).mkdirs();
                    System.out.println(out);*/
                    flag = true;
                }
                //System.out.println(key.getClass().getName() + "\t" + value);
                //out.writeBytes(key.getClass().getName()+"\t"+value+"\n");
            }
        }
        //out.close();
        /*if (flag == true) {
            System.out.println(name + "保存成功");
        } else {
            System.out.println(name + "保存失败");
        }*/
        reader.close();
    }
}