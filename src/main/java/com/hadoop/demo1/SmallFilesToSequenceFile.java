package com.hadoop.demo1;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class SmallFilesToSequenceFile {

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new SmallFilesToSequenceFileConverter(), args);
        System.out.println(run);
    }

    //自定义inputFormat
    public class WholeFileInput extends FileInputFormat<NullWritable, BytesWritable> {
        //返回文件不可切割，保证一个文件一个完整行
        @Override
        public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            WholeFileRecord reader = new WholeFileRecord();
            reader.initialize(inputSplit, taskAttemptContext);
            return reader;
        }
        //自定义RecordReader

        /**
         * RecordReader的核心工作逻辑：
         * 通过nextKeyValue()方法去读取数据构造将返回的key   value
         * 通过getCurrentKey 和 getCurrentValue来返回上面构造好的key和value
         *
         * @author
         */
        public class WholeFileRecord extends RecordReader<NullWritable, BytesWritable> {
            private FileSplit fileSplit1;
            private Configuration conf1;
            private BytesWritable value1 = new BytesWritable();
            private boolean processed1 = false;

            @Override
            public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
                this.fileSplit1 = (FileSplit) split;
                this.conf1 = context.getConfiguration();
            }

            @Override
            public boolean nextKeyValue() throws IOException {
                if (!processed1) {
                    byte[] contents = new byte[(int) fileSplit1.getLength()];
                    Path file = fileSplit1.getPath();
                    FileSystem fs = file.getFileSystem(conf1);
                    FSDataInputStream in = null;
                    try {
                        in = fs.open(file);
                        IOUtils.readFully(in, contents, 0, contents.length);
                        value1.set(contents, 0, contents.length);
                    } finally {
                        IOUtils.closeStream(in);
                    }
                    processed1 = true;
                    return true;
                }
                return false;
            }

            @Override
            public NullWritable getCurrentKey() throws IOException,
                    InterruptedException {
                return NullWritable.get();
            }

            @Override
            public BytesWritable getCurrentValue() throws IOException,
                    InterruptedException {
                return value1;
            }

            @Override
            public float getProgress() throws IOException {
                return processed1 ? 1.0f : 0.0f;
            }

            @Override
            public void close() throws IOException {
            }
        }
    }

    //定义mapreduce处理流程
    public static class SmallFilesToSequenceFileConverter extends Configured implements Tool {
        class SequenceFileMapper extends Mapper<NullWritable, BytesWritable, Text, BytesWritable> {

            private Text filenameKey;

            @Override
            protected void setup(Context context){
                InputSplit split = context.getInputSplit();
                Path path = ((FileSplit) split).getPath();
                filenameKey = new Text(path.toString());
            }

            @Override
            protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
                context.write(filenameKey, value);
            }
        }

        @Override
        public int run(String[] args) throws Exception {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "combine small files to sequencefile");
            job.setJarByClass(SmallFilesToSequenceFileConverter.class);
            job.setInputFormatClass(WholeFileInput.class);
            Demo1.WholeFileInputFormat.addInputPath(job, new Path("file:///F:\\自定义inputformat_小文件合并\\input"));
            job.setOutputFormatClass(SequenceFileOutputFormat.class);
            SequenceFileOutputFormat.setOutputPath(job, new Path("file:///F:\\自定义inputformat_小文件合并\\output"));
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(BytesWritable.class);
            job.setMapperClass(SequenceFileMapper.class);
            return job.waitForCompletion(true) ? 0 : 1;
        }
    }
}

