package com.hadoop.nativeio;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.net.URI;

public class mergeFile {
    public static void main(String[] args) throws Exception {
        //获取分布式文件系统
        FileSystem fileSystem = FileSystem.get(new URI("hdfs:node1:9870"), new Configuration(),"root");
        FSDataOutputStream outputStream = fileSystem.create(new Path("/test/test1"));

        //获取本地文件系统
        LocalFileSystem local = FileSystem.getLocal(new Configuration());

        //通过本地文件系统获取文件列表，为一个集合
        FileStatus[] fileStatuses = local.listStatus(new Path("file:///D:\\临时目录\\input"));
        for (FileStatus fileStatus : fileStatuses) {
            FSDataInputStream inputStream = local.open(fileStatus.getPath());
            IOUtils.copy(inputStream,outputStream);
            IOUtils.closeQuietly(inputStream);
        }
        IOUtils.closeQuietly(outputStream);
        local.close();
        fileSystem.close();
    }
}
