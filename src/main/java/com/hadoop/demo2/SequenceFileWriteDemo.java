package com.hadoop.demo2;//cc SequenceFileWriteDemo Writing a SequenceFile
import java.io.IOException;
import java.net.URI;
 
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
 
//vv SequenceFileWriteDemo
public class SequenceFileWriteDemo {
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
	private static final String[] DATA = { "One, two, buckle my shoe",
			"Three, four, shut the door", "Five, six, pick up sticks",
			"Seven, eight, lay them straight", "Nine, ten, a big fat hen" };
 
	public static void main(String[] args) throws IOException {
		//           /test/test_out  /test/test1
		Path path = new Path("/test/test2");
		IntWritable key = new IntWritable();
		Text value = new Text();
		SequenceFile.Writer writer = null;
		try {
			writer = SequenceFile.createWriter(fs, conf, path, key.getClass(),
					value.getClass());
 
			for (int i = 0; i < 100; i++) {
				key.set(100 - i);
				value.set(DATA[i % DATA.length]);
				System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key,
						value);
				writer.append(key, value);
			}
		} finally {
			IOUtils.closeStream(writer);
		}
	}
}
