package com.hadoop.Sequences;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.io.SequenceFile.CompressionType;



/*
 * 1. 将文件夹中的路径添加到一个数组中
 * 2. 将所有小文件的内容放到SequenceFile中，以文件路径+key  为 key, 以value  为   value
 * 			首先创建Writer， 设置key, value
 * 			遍历每个小文件，在这层循环中创建Reader,key1, value1
 * 			对于每个小文件使用While()读取所有的key和value， 将此文件路径和key连接起来形成要给新的key;
 * 			将key1 和 value1 赋值给 key 和 value
 * 3. 查询操作
 * 		要求2和3其实都差不多
 * 		这里主要讲一下操作一的算法步骤
 * 		首先指定创建一个文件，然后去遍历SequenceFile,去找到相符文件
 * 		然后将这些相符文件名的整体内容使用输出流写入到指定的文件中去.
 * 4. 优化(还未实现)
 * 		可以通过文件的最后的数字进行一次排序，文件内部也可以根据key进行排序
 * 		接着每次查询我们可以将遍历操作改成二分操作
 * 		最终时间复杂度从O(N)优化成了O(NlogN)
 * */

public class MyMerge {
	private Configuration conf = new Configuration();
	private List<String> smallFilePaths = new ArrayList<String>();

	//随机生成随机内容文件
	public void Generate() {
		long start=System.currentTimeMillis();
		int numOfFiles = 110;
		int numOfRecorders = 10;
		//
		String uri = "D:\\临时目录\\hadoopdemo\\src\\main\\java\\com\\hadoop\\files";
		FileOutputStream fout = null;
		Random ra = new Random();
		try {
			for (int i = 1; i <= numOfFiles; i++) {
				System.out.println("writing file#"+i);
				fout = new FileOutputStream(new File(uri + "/file" + i));
				List<String> list = new ArrayList<String>();
				for (int j = 0; j < numOfRecorders; j++)
					list.add(ra.nextInt(numOfRecorders) + 1 + "\t" + "the recorder #" + j + " in file#" + i);
				PrintStream pStream = new PrintStream(new BufferedOutputStream(fout));
				for (String str : list) {
					pStream.println(str);
				}
				pStream.close();
				fout.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {

		}
		long end=System.currentTimeMillis();
		System.out.println("write "+numOfFiles+" files successfully in "+ (end-start)+"ms");

	}


	//定义方法用来添加小文件的路径
	public void addInputPath(String inputPath) throws Exception{
		File file = new File(inputPath);
		//给定路径是文件夹，则遍历文件夹，将文件夹中的文件都放入smallFilePaths
		File[] files = FileUtil.listFiles(file);
		for(File sFile:files) smallFilePaths.add(sFile.getPath());
	}

	//对小文件进行合并
	public void MergeSmallToBig() throws Exception {
		//指定合并后的路径
		String uri = "D:\\临时目录\\hadoopdemo\\src\\main\\java\\com\\hadoop\\files\\fi";

		//创造reader 和writer
		SequenceFile.Writer writer = null;             //Write

		//在window中运行，关闭读取本地gzip压缩库
		conf.setBoolean("io.native.lib.available",false);
		FileSystem fs = FileSystem.getLocal(conf);     //设置本地的文件系统
		Path path = new Path(uri);                     //指定写入的路径

		//设置写的key和value
		Text key = new Text();
		Text value = new Text();

//		for(String path1:smallFilePaths) {
//			System.out.println(path1);
//		}
		try {
			writer = SequenceFile.createWriter(fs, conf, path, key.getClass(), value.getClass(),CompressionType.NONE);
			for(String path1:smallFilePaths) {
				File file = new File(path1);
				BufferedReader reader = null;
				String tmp = new String();
				reader = new BufferedReader(new FileReader(file));
				while((tmp = reader.readLine()) != null) {
					String[] permt = tmp.split("\t");

					String pathstr = new String();
					for(int i = 0; i < path1.length(); i ++)
						if(path1.charAt(i) == '\\') pathstr = "";
						else pathstr += path1.charAt(i);

					Text permttmp = new Text(pathstr + "\t" + permt[0]);
					key.set(permttmp);
					String permttmp1 = new String();
					for(int i = 1; i < permt.length; i ++)
						permttmp1 += permt[i];
					value.set(permttmp1);
					System.out.printf("[%s]\t%s\t%s\n", writer.getLength(),key,value);     //当前写出的所在序列的位置/当前写出的字节数
					writer.append(key, value);     //然后用append方法，将key和value 写到文件中去.
				}
				IOUtils.closeStream(reader);
			}
		} finally {
			IOUtils.closeStream(writer);       //做完之后，就把流关掉.
		}

	}

	//处理查询操作
	public void Query() throws Exception {
		//创建文件系统
		String uri = "D:\\临时目录\\hadoopdemo\\src\\main\\java\\com\\hadoop\\files\\fi";
		FileSystem fs = FileSystem.getLocal(conf);
		Path SourcePath = new Path(uri);
		SequenceFile.Reader reader = null;

		//控制台输入
		System.out.println("请按照如下格式进行输入：filexxx xxx  如果只有一个参数请不要输入空格,最后请按下回车键");

		String input = new String();
		Scanner s = new Scanner(System.in);
		input = s.nextLine();

		Boolean f = false;
		for(int i = 0; i < input.length(); i ++)
			if(input.charAt(i) == ' ') {
				f = true;
				break;
			}
		System.out.println("333");

		if(f) {    //有两个参数即查询3
			System.out.println("333");
			String[] str1 = input.split(" ");
			try {
				//创建这个工具,给出文件系统，路径，配置.
				reader = new SequenceFile.Reader(fs, SourcePath, conf);
				//reader.getKeyClass(),getValueClass()得到key和value的类型，并通过ReflectionUtils实例化
				Text key = (Text) ReflectionUtils.newInstance(reader.getKeyClass(), conf);   //key值的内容用反射的方法来实例化.
				Text value = (Text) ReflectionUtils.newInstance(reader.getValueClass(), conf);
				while(reader.next(key, value)) {
					String keytmp = key.toString();
					String valuetmp = value.toString();

//					String[] str2 = new String[500];
					String[] str2 = keytmp.split("\t");
					System.out.println(str2[0]+"\t"+str2[1]);
					if(str1[0].equals(str2[0]) && str1[1].equals(str2[1])) System.out.println(valuetmp);

					for(int i = 0; i <= 1; i ++) str2[i] = "";
				}
			} finally {
				IOUtils.closeStream(reader);       //做完之后，就把流关掉.
			}
		} else if(input.charAt(0) == 'f') {     //只有文件名字即查询1
			//指定特定的路径
			System.out.print("请输入指定的路径名字：");
			String struri = new String();
			Scanner tmpuri = new Scanner(System.in);
			struri = tmpuri.nextLine();
			FileOutputStream fout = null;
			System.out.println("333");
			try {
				//创建reader,key,value
				reader = new SequenceFile.Reader(fs, SourcePath, conf);
				Text key = (Text) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
				Text value = (Text) ReflectionUtils.newInstance(reader.getValueClass(), conf);

				//创建一个新的文件
				fout = new FileOutputStream(new File(struri+"/"+input));
				//创建一个输出流
				PrintStream pStream = new PrintStream(new BufferedOutputStream(fout));
				while(reader.next(key, value)) {
					String keytmp = key.toString();
					String valuetmp = value.toString();

					String[] str1 = keytmp.split("\t");
					if(str1[0].equals(input)) {
						pStream.println(keytmp+"\t"+valuetmp);
						System.out.println(keytmp+"\t"+valuetmp);
					}
				}
				pStream.close();
				fout.close();
			} finally {
				IOUtils.closeStream(reader);       //做完之后，就把流关掉.
			}


		} else {     //只有key数字即查询2
			try {
				//创建一个reader，需要指定文件系统，想要读取的文件路径，以及配置;
				reader = new SequenceFile.Reader(fs, SourcePath, conf);
				//创建key和value,通过使用reader.getKeyClass(), reader.getValueClass(),并通过ReflectionUtils进行实例化
				Text key = (Text) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
				Text value = (Text) ReflectionUtils.newInstance(reader.getValueClass(), conf);
				System.out.println("value                              所属文件");
				while(reader.next(key, value)) {
					String keytmp = key.toString();
					String valuetmp = value.toString();

					String[] str2 = keytmp.split("\t");

					if(str2[1].equals(input)) {
						System.out.printf("%20s%15s\n", valuetmp, str2[0]);
					}
				}

			} finally {
				IOUtils.closeStream(reader);       //做完之后，就把流关掉.
			}
		}
	}

	public static void main(String[] args) throws Exception {
		MyMerge msf = new MyMerge();
		//msf.Generate();
		//msf.addInputPath("D:\\临时目录\\hadoopdemo\\src\\main\\java\\com\\hadoop\\files");
		//msf.MergeSmallToBig();
		msf.Query();
	}
}