package com.hadoop.squenceFiletest;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;


public class writeLocalTOHDFS {
    public static void writeLocalTOHDFSSequenceFile() throws URISyntaxException, IOException {
        Configuration conf = new Configuration();
        String input = "/root/test";
        Path localPath = (Path) Paths.get(input);
        Path outPath = new Path("test/output");
        //构造writer, 并使用try获取资源, 最后自动关闭资源
        try (SequenceFile.Writer writer = SequenceFile.createWriter(
                conf,
                SequenceFile.Writer.file(outPath),//设置文件名
                SequenceFile.Writer.keyClass(Text.class),//设置keyclass
                SequenceFile.Writer.valueClass(Text.class),
                SequenceFile.Writer.appendIfExists(false),
                SequenceFile.Writer.compression(SequenceFile.CompressionType.BLOCK, new GzipCodec())
        )) {
            Text key = new Text();
            Text value = new Text();
            //Collection<File> fileList = FileUtils.listFiles(new File(inputDir),null,false);
            try (Stream<java.nio.file.Path> stream = Files.list((java.nio.file.Path) localPath)) {
                //遍历所有以.txt结尾的小文件写入到SequenceFile
                stream.filter(x -> x.toString().endsWith(".txt")).forEach(localDir -> {
                    try {
                        System.out.println(localDir.toString());
                        byte[] bytes = Files.readAllBytes(localDir);
                        key.set(localDir.toString());
                        value.set(bytes);
                        writer.append(key, value);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("read" + localDir.toString() + "error");
                    }
                });
            }
        }
    }
}
