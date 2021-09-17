package com.hadoop.nativeio;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//下载文件到本地
public class HadoopTest2 {
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        //创建hdfs集群连接
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        FSDataInputStream fis = null;
        FileOutputStream fos = null;
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs:node1:9870"), conf);
        //递归查找hdfs的根目录下的所有文件
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/test/test1"), true);
        //迭代器遍历下载文件到本地文件夹  C:\Users\admin\Desktop\测试下载
        while (listFiles.hasNext()){
            LocatedFileStatus fileStatus = listFiles.next();
            //获取文件的路径
            Path path = fileStatus.getPath().getParent();
            //获取文件的名字
            String name = fileStatus.getPath().getName();
            //通过IO流将文件下载到本地
            //创建hdfs的oper流
            fis = fs.open(new Path(path, name));
            //创建本地的output流
            fos = new FileOutputStream(new File("D:\\临时目录\\output", name));
            IOUtils.copyBytes(fis,fos,conf);
        }
        IOUtils.closeStream(fis);
        IOUtils.closeStream(fos);
        IOUtils.closeStream(fs);
        System.out.println("复制文件到指定文件夹成功！！！");

    }
}