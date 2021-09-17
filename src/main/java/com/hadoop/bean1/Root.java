
package com.hadoop.bean1;
import java.util.List;


public class Root {

    private BoundingVolume boundingVolume;
    private List<Children> children;
    private double geometricError;
    private List<Double> transform;
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public BoundingVolume getBoundingVolume() {
        return boundingVolume;
    }

    public void setBoundingVolume(BoundingVolume boundingVolume) {
        this.boundingVolume = boundingVolume;
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