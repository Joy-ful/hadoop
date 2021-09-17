package com.hadoop.demo1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GSonTest {
    public static void main(String[] args) {
        String json1 = "{\"name\":\"tom\",\"salary\":2999}";
        Gson gson = new Gson();
        MyEntry myEntry = gson.fromJson(json1, MyEntry.class);
        //System.out.println(myEntry.toString());
        String json2 = "[\"apple\", \"pear\", \"banana\"]";
        Gson gson2 = new Gson();
        String[] json = gson2.fromJson(json2, String[].class);
        String json3 = "[\"apple\", \"pear\", \"banana\"]";
        Gson gson3 = new Gson();
        List<String> fruitList = gson2.fromJson(json2, new TypeToken<List<String>>() {
        }.getType());
        String typeJson1 = "{\n" +
                "  \"code\":0,\n" +
                "  \"message\":\"success\",\n" +
                "  \"data\":{\n" +
                "    \"name\":\"tom\",\n" +
                "    \"age\":32,\n" +
                "    \"address\":\"street one\",\n" +
                "    \"salary\":4999\n" +
                "  }\n" +
                "}";
        Gson gson1 = new Gson();
        Type type = new TypeToken<Result<MyEntry>>() {
        }.getType();
        Result<MyEntry> json4 = gson1.fromJson(typeJson1, type);
        //System.out.println(json4);
        String typeJson2 = "{\n" +
                "  \"code\": 0,\n" +
                "  \"message\": \"success\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"name\": \"tom\",\n" +
                "      \"age\": 32,\n" +
                "      \"address\": \"street one\",\n" +
                "      \"salary\": 4999\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"lucy\",\n" +
                "      \"age\": 24,\n" +
                "      \"address\": \"street three\",\n" +
                "      \"salary\": 2333\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Gson typeGson2 = new Gson();
        // 再次动态生成java类型
        Type type2 = new TypeToken<Result<List<MyEntry>>>() {
        }.getType();
        // 再次动态生成java对象
        Result<List<MyEntry>> result2 = typeGson2.fromJson(typeJson2, type2);
        //System.out.println(result2);

        MyEntry m = new MyEntry();
        m.setName("tom");
        m.setAge(21);
        Gson gson4 = new Gson();
        String s = gson4.toJson(m);
        System.out.println(s);
    }
}
