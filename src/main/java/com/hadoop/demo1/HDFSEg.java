package com.hadoop.demo1;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class HDFSEg {
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

    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        // listMyDir();
        //getFileToLocal();
        //mkdir();
        //putData();
        //deleteMkdir();
    }
    //打印目录下所有文件
    public static void listMyDir() throws IOException {
        RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fs.listFiles(new Path("/test"), true);
        while (locatedFileStatusRemoteIterator.hasNext()) {
            LocatedFileStatus next = locatedFileStatusRemoteIterator.next();
            System.out.println(next);
        }
        fs.close();
    }
    //下载目录下所有问价
    public static void getFileToLocal() throws Exception {
        FSDataInputStream open = fs.open(new Path("/test/test.txt"));
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\临时目录\\output"));
        IOUtils.copy(open,fileOutputStream );
        IOUtils.closeQuietly(open);
        IOUtils.closeQuietly(fileOutputStream);
        fs.close();
    }
    //hdfs上创建文件夹
    public static void mkdir() throws IOException {
        boolean mkdirs = fs.mkdirs(new Path("/test/test1"));
        fs.close();
    }
    //hdfs文件上传
    public static void putData() throws IOException {
        fs.copyFromLocalFile(new Path("file:///D:\\临时目录\\input\\test.txt"),new Path("/test/test1"));
        fs.close();
    }
    //hdfs删除文件
    public static void deleteMkdir() throws IOException {
        boolean delete = fs.delete(new Path("/test/test1"), true);
        if (delete){
            System.out.println("删除成功");
        }else {
            System.out.println("失败");
        }
        fs.close();
    }
}
