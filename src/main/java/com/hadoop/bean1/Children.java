
package com.hadoop.bean1;


public class Children {

    private BoundingVolume boundingVolume;
    private Content content;
    private double geometricError;
    public void setBoundingVolume(BoundingVolume boundingVolume) {
         this.boundingVolume = boundingVolume;
     }
     public BoundingVolume getBoundingVolume() {
         return boundingVolume;
     }

    public void setContent(Content content) {
         this.content = content;
     }
     public Content getContent() {
         return content;
     }

    public void setGeometricError(double geometricError) {
         this.geometricError = geometricError;
     }
     public double getGeometricError() {
         return geometricError;
     }

}