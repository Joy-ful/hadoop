package com.hadoop.TipWindows;

import java.util.Scanner;//导入java.util包下的Scanner类
import javax.swing.JOptionPane;//导入java.swing包下的JOptionPane类
public class Test3{//类名
 public static void main(String[] args){//程序主函数
  while(true){//定义死循环
   System.out.print("Please input:");//提示输入
   Scanner s=new Scanner(System.in);//创建scanner，控制台会一直等待输入，直到敲回车结束
   String str=s.nextLine();//将用户的输入转换为字符串形式
   char[] a=str.toCharArray();//将字符串对象中的字符转换为一个字符数组
   if("ByeBye".equals(str)){//if语句的条件判断用户输入是否为ByeBye
    System.out.print("The process is over");//输出进程已结束
    System.exit(0);//关闭进程
   }else{
     for(int i=0;i<a.length;i++){//使用for循环遍历数组
      int b=Integer.valueOf(a[i]);//将数组中的元素转换为其对应的ASCII码
      //使用消息提示框输出信息，该信息包含输入的字符及其ASCII码
      JOptionPane.showMessageDialog(null, "You input is"+str+"\n"+"ASCII is"+b, str, JOptionPane.PLAIN_MESSAGE);
     }
   } 
  }
 }
}