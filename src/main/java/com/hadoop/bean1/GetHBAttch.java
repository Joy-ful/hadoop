package com.hadoop.bean1;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.hadoop.hdfs.util.CombinedHostsFileReader.readFile;

public class GetHBAttch {
    public static void main(String[] args) throws Exception {
        write("D:\\临时目录\\osgbCity_fire", "/test/test_b3dm");
    }

    //hdfs的配置
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

    //文件合并成sequenceFile文件，写入到hdfs
    public static void write(String inputDri, String outputFile) throws Exception {
        SequenceFile.Writer.Option[] opts = new SequenceFile.Writer.Option[]{SequenceFile.Writer.file(new Path(String.valueOf(outputFile))), SequenceFile.Writer.keyClass(BytesWritable.class), SequenceFile.Writer.valueClass(BytesWritable.class)};
        //创建了一个writer实例
        SequenceFile.Writer writer = SequenceFile.createWriter(conf, opts);

        File file = new File(String.valueOf(inputDri));

        //File file1 = new File(inputDri);
        if (file.exists()) {//如果该目录存在
            if (!file.isDirectory()) {//如果是文件，输出目录路径
                //System.out.println("是文件");
                //System.out.println("path=" + file.getPath());
            } else if (file.isDirectory()) {//如果不是文件，而是文件夹，则循环
                //System.out.println("是文件夹！");
                String[] fileList = file.list();//返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录。
                if (fileList != null && fileList.length > 0) {
                    for (int i = 0; i < fileList.length; i++) {//循环文件，或者是文件夹
                        File readFile = new File(inputDri + "\\" + fileList[i]);//重新设置路径
                        if (!readFile.isDirectory()) {//如果是文件，输出目录路径
                            if (readFile.getName().endsWith(".b3dm")) {
                                String content = FileUtils.readFileToString(readFile, "UTF-8");
                                //获取文件名
                                String fileName = readFile.getName();
                                System.out.println(fileName);
                                BytesWritable key = new BytesWritable(fileName.getBytes());
                                BytesWritable value = new BytesWritable(content.getBytes());
                                //向SequenceFile中写入数据
                                writer.append(key, value);
                            }
                            //System.out.println("path=" + readFile.getPath());
                        } else if (readFile.isDirectory()) {//如果不是文件，而是文件夹的话，则返回去重新执行readFile方法(迭代)
                            readFile(inputDri + "\\" + fileList[i]);
                        }
                    }
                } else {
                    System.out.println("该目录下面为空！");
                }

            }
        } else if (!file.exists()) {//该目录不存在
            System.out.println("该目录或文件不存在");
        }
       /* File inputDirPath = new File(String.valueOf(inputDir));
        if (inputDirPath.isDirectory()) {
            //获取目录中的文件
            File[] files = inputDirPath.listFiles();
            //迭代文件
            for (File file1 : files) {
                //获取文件的全部内容
                String content = FileUtils.readFileToString(file1, "UTF-8");
                //获取文件名
                String fileName = file1.getName();
                BytesWritable key = new BytesWritable(fileName.getBytes());
                BytesWritable value = new BytesWritable(content.getBytes());
                //向SequenceFile中写入数据
                writer.append(key, value);
            }
        }*/
        writer.close();
    }
}
