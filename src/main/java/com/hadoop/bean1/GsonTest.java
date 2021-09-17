package com.hadoop.bean1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.List;

public class GsonTest {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("D:\\临时目录\\osgbCity_fire\\top\\0_0_0_0.json");
        try (Reader reader = new InputStreamReader(fis, "UTF-8")) {
            Gson gson = new GsonBuilder().create();
            JsonRootBean json = gson.fromJson(reader, JsonRootBean.class);

            //Children children1 = json.getRoot().getChildren().get(3);
            //List<Double> box1 = children1.getBoundingVolume().getBox();
            //System.out.println("ceshi"+box1);
            //Asset

            String generatetool = json.getAsset().getGeneratetool();
            //json.getAsset().setGeneratetool(generatetool);
            System.out.println("generatetool:"+generatetool);

            String gltfUpAxis = json.getAsset().getGltfUpAxis();
            //json.getAsset().setGltfUpAxis(gltfUpAxis);
            System.out.println("gltfUpAxis:"+gltfUpAxis);

            String version = json.getAsset().getVersion();
            //json.getAsset().setVersion(version);
            System.out.println("version:"+version);

            //GeometricError

            double geometricError = json.getGeometricError();
            //json.setGeometricError(geometricError);
            System.out.println("geometricError:"+geometricError);

            //Root

            List<Double> box = json.getRoot().getBoundingVolume().getBox();
            //json.getRoot().getBoundingVolume().setBox(box);
            System.out.println("boundingVolume:"+box);

            List<Children> children = json.getRoot().getChildren();
            for (Children child : children) {

                List<Double> boundingVolume = child.getBoundingVolume().getBox();
                //child.getBoundingVolume().setBox(boundingVolume);

                String uri = child.getContent().getUri();
                child.getContent().setUri("www.baidu.com");

                double geometricError1 = child.getGeometricError();
                //child.setGeometricError(geometricError1);

                System.out.println("boundingVolume:"+boundingVolume);
                System.out.println("uri:"+uri);
                System.out.println("geometricError1:"+geometricError1);
            }

            double geometricError2 = json.getRoot().getGeometricError();
            //json.getRoot().setGeometricError(geometricError2);
            System.out.println("geometricError1:"+geometricError2);

            Content content = json.getRoot().getContent();
            //json.getRoot().setContent(content);
            System.out.println(content);

            //Transform
            List<Double> transform = json.getRoot().getTransform();
            System.out.println("content:"+transform);

            String json1 = gson.toJson(json);
            System.out.println(json1);
            //System.out.println(geometricError);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
