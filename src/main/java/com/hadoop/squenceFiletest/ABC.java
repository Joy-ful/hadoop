package com.hadoop.squenceFiletest;

import java.io.*;
import java.util.Scanner;

public class ABC {
    public static void main(String[] args) throws IOException {
/*Path outFile = new Path("/test/test1_int");
                    //构造writer, 并使用try获取资源, 最后自动关闭资源
                    SequenceFile.Writer writer = SequenceFile.createWriter(conf,
                            SequenceFile.Writer.file(outFile),//设置文件名
                            SequenceFile.Writer.keyClass(Text.class),//设置keyclass
                            SequenceFile.Writer.valueClass(Text.class),//设置valueclass
                            SequenceFile.Writer.appendIfExists(false),
                            SequenceFile.Writer.compression(SequenceFile.CompressionType.BLOCK, new GzipCodec()) //设置block+gzip的压缩方式
                    );
                    writer.append(key, value);*/
                    /*if (!file.exists()) {
                        boolean mkdirs = file.mkdirs();
                        System.out.println(mkdirs);
                    }
                    //new File(outDir).mkdirs();
                    out = new DataOutputStream(new FileOutputStream(outDir));
                    System.out.println(out);*/
/*        String dir = "D:\\临时目录\\output\\fi.txt\\TestIO";

        File file = new File(dir);

        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            System.out.println(mkdirs);
        }*/
        //out = new DataOutputStream(new FileOutputStream(outDir));
//        out.close();
/*        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();//2
        int m = sc.nextInt();//4

        int k = 0;

        for (int i = 0; i < n; i++) {
            int sign = m;
            for (k = i; k > 0 && sign > 0; k--, sign--)
                System.out.print((char) ('A' + k));
           // for (int j = k; j < m - i; j++) {
             //   System.out.print((char) ('A' + j));
           // }
            //asdo
            //dsdo
            System.out.println();
        }
    }*/

byte a = 127;
a+= 1;
        System.out.println(a);
    }
}