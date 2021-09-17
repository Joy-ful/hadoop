package com.hadoop.demo;


import java.io.*;
import java.util.List;

public class test1 {
    public static void main(String[] args) throws FileNotFoundException {
        /*JsonParser parse = new JsonParser();
        JsonElement parse1 = parse.parse(new FileReader("D:\\\\临时目录\\\\osgbCity_fire\\\\tileset.json"));
        JsonObject asJsonObject = ((JsonElement) parse1).getAsJsonObject();
        //asJsonObject
        FileInputStream fis = new FileInputStream("D:\\临时目录\\osgbCity_fire\\tileset.json");
        try (Reader reader = new InputStreamReader(fis, "UTF-8")) {
            Gson gson = new GsonBuilder().create();
            bean p = gson.fromJson(reader, bean.class);

            //asset
            Asset asset = p.getAsset();
            String generatetool = asset.getGeneratetool();
            String gltfUpAxis = asset.getGltfUpAxis();
            String version = asset.getVersion();
            System.out.println(generatetool);

            //geometricError
            //Double jsonRootBean = p.getGeometricError();

            //root

            //box
            List<Double> box = p.getRoot().getBoundingVolume().getBox();
            //
            List<Root2.NewsBean> children = p.getRoot().getChildren();


            *//*List<Children> content = p.getRoot().getChildren();
            for (Children children : content) {
                System.out.println(children.getBoundingVolume().getBox());
            }*//*
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}