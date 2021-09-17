package com.hadoop.nativeio;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class WordCount {
    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        Configuration configuration = new Configuration();
        Tool tool  =  new JobMain();
        int run = ToolRunner.run(configuration, tool, args);
        System.exit(run);
    }

    public static class WordCountMapper extends Mapper<LongWritable,Text,Text,LongWritable> {
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] split = line.split(",");
            for (String word : split) {
                context.write(new Text(word),new LongWritable(1));
            }

        }
    }

    public static class WordCountReducer extends Reducer<Text,LongWritable,Text,LongWritable> {
        /**
         * 自定义我们的reduce逻辑
         * 所有的key都是我们的单词，所有的values都是我们单词出现的次数
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long count = 0;
            for (LongWritable value : values) {
                count += value.get();
            }
            context.write(key,new LongWritable(count));
        }
    }

    public static class JobMain extends Configured implements Tool {
        @Override
        public int run(String[] args) throws Exception {
            Job job = Job.getInstance(super.getConf(), JobMain.class.getSimpleName());
            //打包到集群上面运行时候，必须要添加以下配置，指定程序的main函数
            job.setJarByClass(JobMain.class);
            //第一步：读取输入文件解析成key，value对
            job.setInputFormatClass(TextInputFormat.class);
            TextInputFormat.addInputPath(job,new Path("/test/test1"));

            //第二步：设置我们的mapper类
            job.setMapperClass(WordCountMapper.class);
            //设置我们map阶段完成之后的输出类型
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(LongWritable.class);
            //第三步，第四步，第五步，第六步，省略
            //第七步：设置我们的reduce类
            job.setReducerClass(WordCountReducer.class);
            //设置我们reduce阶段完成之后的输出类型
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(LongWritable.class);
            //第八步：设置输出类以及输出路径
            job.setOutputFormatClass(TextOutputFormat.class);
            TextOutputFormat.setOutputPath(job,new Path("/test/wordcount_out1"));
            boolean b = job.waitForCompletion(true);
            return b?0:1;
        }}
}
