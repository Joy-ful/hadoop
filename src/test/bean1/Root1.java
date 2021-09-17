/**
  * Copyright 2021 bejson.com 
  */
package com.hadoop.bean1;
import java.util.List;

/**
 * Auto-generated: 2021-04-14 16:11:21
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Root1 {

    private BoundingVolume boundingVolume;
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    private List<Children> children;
    private double geometricError;
    private List<Double> transform;
    public void setBoundingVolume(BoundingVolume boundingVolume) {
         this.boundingVolume = boundingVolume;
     }
     public BoundingVolume getBoundingVolume() {
         return boundingVolume;
     }

    public void setChildren(List<Children> children) {
         this.children = children;
     }
     public List<Children> getChildren() {
         return children;
     }

    public void setGeometricError(double geometricError) {
         this.geometricError = geometricError;
     }
     public double getGeometricError() {
         return geometricError;
     }

    public void setTransform(List<Double> transform) {
         this.transform = transform;
     }
     public List<Double> getTransform() {
         return transform;
     }

}