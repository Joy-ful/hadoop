package com.hadoop.bean1;

import com.google.gson.*;

import java.io.*;
import java.util.List;

public class test {
    public static void main(String[] args) throws FileNotFoundException {
        JsonParser parse = new JsonParser();
        JsonElement parse1 = parse.parse(new FileReader("D:\\\\临时目录\\\\osgbCity_fire\\\\tileset.json"));
        JsonObject asJsonObject = parse1.getAsJsonObject();
        //asJsonObject
        FileInputStream fis = new FileInputStream("D:\\临时目录\\osgbCity_fire\\tileset.json");
        try (Reader reader = new InputStreamReader(fis, "UTF-8")) {
            Gson gson = new GsonBuilder().create();
            beann p = gson.fromJson(reader, beann.class);

            //asset
            Asset asset = p.getAsset();
            String generatetool = asset.getGeneratetool();
            String gltfUpAxis = asset.getGltfUpAxis();
            String version = asset.getVersion();


            //geometricError
            //Double jsonRootBean = p.getGeometricError();

            //root

            //box
            List<Double> box = p.getRoot().getBoundingVolume().getBox();
            //
            List<Children> children = p.getRoot().getChildren();

            Content content = p.getJsonRootBean().getRoot().getContent();
            String uri = content.getUri();
            System.out.println(uri);

            /*List<Children> content = p.getRoot().getChildren();
            for (Children children : content) {
                System.out.println(children.getBoundingVolume().getBox());
            }*/
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}