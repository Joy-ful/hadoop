
package com.hadoop.bean1;


public class JsonRootBean {

    private Asset asset;
    private double geometricError;
    private Root root;
    public void setAsset(Asset asset) {
         this.asset = asset;
     }
     public Asset getAsset() {
         return asset;
     }

    public void setGeometricError(double geometricError) {
         this.geometricError = geometricError;
     }
     public double getGeometricError() {
         return geometricError;
     }

    public void setRoot(Root root) {
         this.root = root;
     }
     public Root getRoot() {
         return root;
     }

}