package com.hadoop.demo2;//cc SequenceFileReadDemo Reading a SequenceFile
import java.io.IOException;
import java.net.URI;
 
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
 
//vv SequenceFileReadDemo
public class SequenceFileReadDemo {
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
 
	public static void main(String[] args) throws IOException {
		//           /test/test_out  /test/test1
		Path path = new Path("/test/test2");
 
		SequenceFile.Reader reader = null;
		try {
			reader = new SequenceFile.Reader(fs, path, conf);
			Writable key = (Writable) ReflectionUtils.newInstance(
					reader.getKeyClass(), conf);
			Writable value = (Writable) ReflectionUtils.newInstance(
					reader.getValueClass(), conf);
			long position = reader.getPosition();
			while (reader.next(key, value)) {
				String syncSeen = reader.syncSeen() ? "*" : "";
				System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, key,
						value);
				position = reader.getPosition(); // beginning of next record
			}
		} finally {
			IOUtils.closeStream(reader);
		}
	}
}
