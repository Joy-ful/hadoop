/*
@author :yinzhengjie
Blog:http://www.cnblogs.com/yinzhengjie/tag/Hadoop%E8%BF%9B%E9%98%B6%E4%B9%8B%E8%B7%AF/
EMAIL:y1053419035@qq.com
*/
package com.hadoop.sequenceFilenewAPI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;


public class TestMapFile {

    public static void main(String[] args) throws Exception {
        //createMapfile();
        //createSeq();
        //convert();
        getVal();
    }
    //创建一个名称为data的SequenceFile文件
    public static void createSeq() throws IOException, URISyntaxException {
        //实例化一个Configuration，它会自动去加载本地的core-site.xml配置文件的fs.defaultFS属性。(该文件放在项目的resources目录即可。)
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs:node1:9870"), conf);
        //这个path是指是需要在文件系统中写入的数据,由于我修改了默认的配置，将数据会写在本地的windows操作系统，因此我这里给出了windows的路径。
        Path seqPath = new Path("/test/testData/SequenceFile/data");
        //我们创建四个序列化容器，对这四个写入器都指定不同的压缩方式，分别是不压缩，记录压缩，块压缩
        SequenceFile.Writer BlockWriter = SequenceFile.createWriter(fs, conf, seqPath, IntWritable.class,Text.class,SequenceFile.CompressionType.BLOCK);
        //往四个写入器写入相同的数据
        for (int i=0;i<10;i++){
            Random r = new Random();
            int j = r.nextInt(1000);
            IntWritable key = new IntWritable(j);
            Text value = new Text("@2018@" + j);
            BlockWriter.append(key,value);
            //System.out.println(BlockWriter.toString());
        }
        //释放资源
        BlockWriter.close();
    }

    //创建MapFile文件
    public static  void createMapfile() throws IOException, URISyntaxException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs:node1:9870"), conf);
        String DirPath = "/test/testData/MapFile";
       //第一个参数是读取我们的默认配置，第二个参数是进行初始化文件系统，第三个参数必须是一个目录的字符串路径，第四个参数是一个实现WritableComparable的key类型，第五个参数是指定value的数据类型
        MapFile.Writer writer = new MapFile.Writer(conf, fs, DirPath, IntWritable.class, Text.class);
        for (int i = 0; i < 1000; i++) {
            IntWritable key = new IntWritable(i);
            Text value = new Text("test" + i);
            //注意，这里写入的时候数据时候，key的类型必须为WritableComparable，换句话说，就是说这个key是可以进行比较的！至于value的就无所谓了。
            writer.append(key, value);
        }
        writer.close();
    }

    //将SequenceFile转换成MapFile文件，注意SequenceFile文件名必须改为data
    public static void convert() throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs:node1:9870"), conf);
        //注意,这个seqPath目录下的SequenceFil文件必须命名为data
        Path seqPath = new Path("/test/testData/SequenceFile");
        //最终会返回一个改变的条目数量，它是一个long数据类型。如果文件已经是MapFile类型的文件了，就会返回-1哟！
        long fix = MapFile.fix(fs, seqPath, IntWritable.class, Text.class, false, conf);

        System.out.println(fix);
    }

    //通过key获取value的值
    public static void getVal() throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs:node1:9870"), conf);
        String DirPath = "/test/testData/MapFile";
        MapFile.Reader reader = new MapFile.Reader(fs, DirPath, conf);
        IntWritable key = new IntWritable(10);
        Text val = new Text();
        Text w = (Text)reader.get(key,val);
        int length = w.getLength();
        System.out.println(length);
        System.out.println(w.toString());
    }

}
