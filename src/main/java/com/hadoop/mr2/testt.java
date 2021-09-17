package com.hadoop.mr2;

import org.elasticsearch.action.bulk.BulkItemResponse;

public class testt {
    public static void main(String[] args) {
        //System.out.println(reverses(1223));
        int[] a = {8, 9, 5, 6, 3, 8, 4, 7, 2};//7 1 5 1
        System.out.println(singleNumber(a));
        //System.out.println(maxProfit(a));
    }

    //数字反转
    public static int reverses(int x) { //1223
        int res = 0;
        while (x != 0) {
            //每次取末尾数字
            int tmp = x % 10; //3 2 2 1
            //判断是否 大于 最大32位整数
            if (res > 214748364 || (res == 214748364 && tmp > 7)) {
                return 0;
            }
            //判断是否 小于 最小32位整数
            if (res < -214748364 || (res == -214748364 && tmp < -8)) {
                return 0;
            }
            res = res * 10 + tmp;//(30+2)*10+2
            x /= 10;//122 12 1
        }
        return res;
    }

    //贪心算法
    public static int maxProfit(int[] prices) {
        if (prices.length == 0) {
            return 0;
        }

        int max = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            if (prices[i] < prices[i + 1]) {
                max += prices[i + 1] - prices[i];
            }else {

            }
        }
        return max;
    }

    public static int singleNumber(int[] nums) {
        int reduce = 0;
        for (int num : nums) {
            reduce = reduce ^ num;
        }
        return reduce;
    }


}
