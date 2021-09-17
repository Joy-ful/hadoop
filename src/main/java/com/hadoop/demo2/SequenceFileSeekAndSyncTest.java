package com.hadoop.demo2;//== SequenceFileSeekAndSyncTest
//== SequenceFileSeekAndSyncTest-SeekNonRecordBoundary
//== SequenceFileSeekAndSyncTest-SyncNonRecordBoundary
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.*;

public class SequenceFileSeekAndSyncTest {
	private  static String HDFS_PATH = "hdfs:node1:9870";
	private  static Configuration conf = new Configuration();
	private  static SequenceFile.Reader reader;
	private  static Writable key;
	private static Writable value;
	private static FileSystem fs;

	static {
		try {
			fs = FileSystem.get(URI.create(HDFS_PATH), conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SequenceFileSeekAndSyncTest() throws IOException {
	}

	public static void main(String[] args) throws IOException {
		SequenceFileWriteDemo.main(new String[]{HDFS_PATH});

		Configuration conf = new Configuration();
		fs = FileSystem.get(URI.create(HDFS_PATH), conf);
		Path path = new Path("/test/test2");
		SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path));
		key = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
		value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);
		reader.seek(359);
		assertThat(reader.next(key, value), is(true));
		assertThat(((IntWritable) key).get(), is(95));
	}
//	@After
//	public void tearDown() throws IOException {
//		fs.delete(new Path(SF_URI), true);
//	}
 

	/*public static void seekToRecordBoundary() throws IOException {
		// vv SequenceFileSeekAndSyncTest
		reader.seek(359);
		assertThat(reader.next(key, value), is(true));
		assertThat(((IntWritable) key).get(), is(95));
		// ^^ SequenceFileSeekAndSyncTest
	}*/
 
/*	@Test(expected = IOException.class)
	public void seekToNonRecordBoundary() throws IOException {
		// vv SequenceFileSeekAndSyncTest-SeekNonRecordBoundary
		reader.seek(360);
		reader.next(key, value); // fails with IOException
		// ^^ SequenceFileSeekAndSyncTest-SeekNonRecordBoundary
	}*/
 
/*	@Test
	public void syncFromNonRecordBoundary() throws IOException {
		// vv SequenceFileSeekAndSyncTest-SyncNonRecordBoundary
		reader.sync(360);
		assertThat(reader.getPosition(), is(2021L));
		assertThat(reader.next(key, value), is(true));
		assertThat(((IntWritable) key).get(), is(59));
		// ^^ SequenceFileSeekAndSyncTest-SyncNonRecordBoundary
	}*/
 
/*	@Test
	public void syncAfterLastSyncPoint() throws IOException {
		reader.sync(4557);
		assertThat(reader.getPosition(), is(4788L));
		assertThat(reader.next(key, value), is(false));
	}*/

}