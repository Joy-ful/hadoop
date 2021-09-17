package com.hadoop.squenceFiletest;

import java.util.*;

import static java.lang.reflect.Array.getChar;

public class Test {
    public static void main(String[] args) {
        /*String a="a",b="b";
        System.out.println(a.compareTo(b));*/
        System.out.println(getsame());

        Integer a = 200;
        String b = "a";
        System.out.println(Integer.valueOf(a));
        //System.out.println(Integer.parseInt(b));
        //System.out.println(a == b);
        //System.out.println(a.compareTo(Integer.valueOf(b)));

        int[] arr1 = {1, 2, 3, 4, 5};
        List<int[]> intList = Arrays.asList(arr1);
        // intList size: 1
        //System.out.println(String.format("intList size: %s", intList.size()));

        Integer[] arr2 = {1, 2, 3, 4, 5};
        List<Integer> integerList = Arrays.asList(arr2);
        // integerList size: 5
        //System.out.println(String.format("integerList size：%s", integerList.size()));

        List<String> arrayList = new ArrayList<String>();
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        // remove(arrayList);
        // remove2(arrayList);
        remove3(arrayList);
        //System.out.println(arrayList);
    }

    public static int comp(int[] value, int[] len) {
        int line1 = value.length;
        int line2 = len.length;
        int lim = Math.min(line1, line2);
        System.out.println(lim);
        System.out.println(line1);
        System.out.println(line2);
        System.out.println("--------------------------");
        for (int i = 0; i < lim; i++) {
            if (value[i] != len[i]) {
                return getChar(value, i) - getChar(len, i);
            }
        }
        return line1 - line2;
    }

    public static void remove3(List<String> list) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.equals("b")) {
                it.remove();
            }
        }
    }

    public static int getsame() {
        int a = 0;
        int b = 100;
        int result = 0;
        while (a < b) {
            result += a;
            a++;
        }
        return result;
    }

    @org.junit.Test
    public void re() {

        //截取倒数第二个 之后的  +1代表不要/       string.substring(string.lastIndexOf("\\", string.lastIndexOf("\\") - 1) + 1);
        String a = "../Tile_+002_+004/Tile_+002_+004_L15_000t1.json";
        String replace = a.replace("..", "").replace("/", "_");
        System.out.println("rejisji   " + replace);

        String str = "http://adsdaa/ccdasdcc/cccfffsfc/dasd/xiao.jpg";
        String substring = str.substring(str.lastIndexOf("/", str.lastIndexOf("/") - 1) + 1);
        String[] split = str.split("/");
        String s = split[split.length - 2];
        System.out.println("dddddddd        " + substring);
        // 第一种
        int idx = str.lastIndexOf("/");

        //str = str.substring(idx + 1, str.length());
        //System.out.println(str);

        // 第二种  最后一位
        //System.out.println(str.split("/")[str.split("/").length-1]);

        // 第三种
        //System.out.println(str.substring(str.lastIndexOf("/")+1));

        // 截取最后一个“/”前面的内容
        //System.out.println(str.substring(0,str.lastIndexOf("/")));

        //截取倒数第二个"/"之后的字符串
        /*String path="/home/henry/Desktop/1.txt";

        //获得"Desktop/1.txt",并且不需要前面的"/"
        String oo=path.substring(path.lastIndexOf("/",path.lastIndexOf("/")-1)+1);
        //"+1"代表在定位时往后取一位,即去掉"/"
        //"-1"代表以"/"字符定位的位置向前取一位
        //从path.lastIndexOf("/")-1位置开始向前寻找倒数第二个"/"的位置

        System.out.println(oo);*/

        //截取倒数第二个"/"之前的字符串
        String path = "/home/henry/Desktop/1.txt";

        //获得""/home/henry",并且不需要前面的"/"
        String oo = path.substring(0, path.lastIndexOf("/", path.lastIndexOf("/") - 1));
        //"-1"代表在定位时往前取一位,即去掉"/"
        //从path.lastIndexOf("/")-1位置开始向前寻找倒数第二个"/"的位置
        //最后用substring()方法来进行截取
        System.out.println(oo);
        test1 test1 = new test1();
        Date nextDays = test1.getNextDays(1);
        System.out.println(nextDays);


    }

}