package com.hadoop.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PairWritable implements WritableComparable<PairWritable> {
    //MapReduce排序以及序列化
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        ToolRunner.run(configuration, new SecondarySort(), args);
    }


    // 组合key,第一部分是我们第一列，第二部分是我们第二列
    private String first;
    private int second;

    public PairWritable() {
    }

    public PairWritable(String first, int second) {
        this.set(first, second);
    }

    private void set(String first, int second) {
        this.first = first;
        this.second = second;
    }

    //重写比较器
    @Override
    public int compareTo(PairWritable o) {
        //每次比较都是调用该方法的对象与传递的参数进行比较，说白了就是第一行与第二行比较完了之后的结果与第三行比较，
        //得出来的结果再去与第四行比较，依次类推
        System.out.println(o.toString());
        System.out.println(this.toString());
        int comp = this.first.compareTo(o.first);
        if (comp != 0) {
            return comp;
        } else { // 若第一个字段相等，则比较第二个字段
            return Integer.valueOf(this.second).compareTo(Integer.valueOf(o.getSecond()));
        }
    }

    @Override
    public String toString() {
        return "PairWritable{" +
                "first='" + first + '\'' +
                ", second=" + second +
                '}';
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    //序列化
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(first);
        dataOutput.writeInt(second);
    }

    //反序列化
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.first = dataInput.readUTF();
        this.second = dataInput.readInt();
    }

    //自定义mapper
    public static class SortMapper extends Mapper<LongWritable, Text, PairWritable, IntWritable> {

        private PairWritable mapOutKey = new PairWritable();
        private IntWritable mapOutValue = new IntWritable();

        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String lineValue = value.toString();
            String[] strs = lineValue.split(",");

            //设置组合key和value ==> <(key,value),value>
            mapOutKey.set(strs[0], Integer.valueOf(strs[1]));
            mapOutValue.set(Integer.valueOf(strs[1]));
            context.write(mapOutKey, mapOutValue);
        }

        //自定义reducer
        public static class SortReducer extends Reducer<PairWritable, IntWritable, Text, IntWritable> {

            private Text outPutKey = new Text();

            @Override
            public void reduce(PairWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
                //迭代输出
                for (IntWritable value : values) {
                    outPutKey.set(key.getFirst());
                    context.write(outPutKey, value);
                }
            }
        }
    }

    public static class SecondarySort extends Configured implements Tool {

        @Override
        public int run(String[] strings) throws Exception {
            Configuration conf = super.getConf();
            conf.set("mapreduce.framwork.name", "local");
            Job job = Job.getInstance(conf, SecondarySort.class.getSimpleName());
            job.setJarByClass(SecondarySort.class);

            job.setInputFormatClass(TextInputFormat.class);
            TextInputFormat.addInputPath(job, new Path("/test/test.txt"));
            TextOutputFormat.setOutputPath(job, new Path("/test/output_Pair"));
            job.setMapperClass(SortMapper.class);
            job.setMapOutputKeyClass(PairWritable.class);
            job.setMapOutputValueClass(IntWritable.class);
            job.setReducerClass(SortMapper.SortReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
            boolean b = job.waitForCompletion(true);
            return b ? 0 : 1;
        }
    }
}