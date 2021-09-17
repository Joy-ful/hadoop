package com.hadoop.demo;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;


public class Demo {
    public static void main(String[] args) throws Exception {
        //获取FileSystem
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs:node1:9870"), configuration);
        System.out.println(fileSystem);
        //递归遍历文件系统
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/"));
        for (FileStatus fileStatus : fileStatuses) {
            if (fileStatus.isDirectory()) {
                Path path = fileStatus.getPath();
                listAllFiles(fileSystem, path);
            } else {
                System.out.println("文件路径为" + fileStatus.getPath().toString());
            }
        }

        //下载到本地
        FSDataInputStream open = fileSystem.open(new Path("/test/test.txt"));
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\临时目录"));
        IOUtils.copy(open, fileOutputStream);
        IOUtils.closeQuietly(open);
        IOUtils.closeQuietly(fileOutputStream);
        fileSystem.close();

        //创建文件夹
        boolean mkdirs = fileSystem.mkdirs(new Path("/test/mydir"));
        System.out.println(mkdirs);
        fileSystem.close();

        //上传文件
        fileSystem.copyFromLocalFile(new Path("file:///D:\\临时目录\\test.txt"), new Path("/test/mydir"));
        fileSystem.close();

        //将多个本地文件上传hdfs，并合并成一个大文件
        //获取分布式文件系统
        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/test"));
        //获取本地文件系统
        LocalFileSystem local = FileSystem.getLocal(new Configuration());
        //通过本地文件系统获取文件列表，为一个集合
        FileStatus[] fileStatuses1 = local.listStatus(new Path("file:///D:\\临时目录\\test.txt"));
        for (FileStatus fileStatus : fileStatuses1) {
            FSDataInputStream inputStream = local.open(fileStatus.getPath());
            IOUtils.copy(inputStream, fsDataOutputStream);
            IOUtils.closeQuietly(inputStream);
        }
        IOUtils.closeQuietly(fileOutputStream);
        local.close();
        fileSystem.close();
    }

    public static void listAllFiles(FileSystem fileSystem, Path path) throws Exception {
        FileStatus[] fileStatuses = fileSystem.listStatus(path);
        for (FileStatus fileStatus : fileStatuses) {
            if (fileStatus.isDirectory()) {
                listAllFiles(fileSystem, fileStatus.getPath());
            } else {
                Path path1 = fileStatus.getPath();
                System.out.println("文件路径为" + path1);
            }
        }
    }

    public void listMyFile() throws Exception {
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://10.10.13.109:9870"), configuration);
        RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fileSystem.listFiles(new Path("/"), true);
        while (locatedFileStatusRemoteIterator.hasNext()) {
            LocatedFileStatus next = locatedFileStatusRemoteIterator.next();
            System.out.println(next.getPath().toString());
        }
        fileSystem.close();
    }
}
