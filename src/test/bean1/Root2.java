package com.hadoop.bean1;

import java.util.List;
import java.util.Map;

public class Root2 {
    //private List<Double> box;
    private boundingVolume boundingVolume;
    private List<NewsBean> children;
    private String content;
    private Double geometricError;

    public Root2.boundingVolume getBoundingVolume() {
        return boundingVolume;
    }

    public void setBoundingVolume(Root2.boundingVolume boundingVolume) {
        this.boundingVolume = boundingVolume;
    }

    public List<NewsBean> getChildren() {
        return children;
    }

    public void setChildren(List<NewsBean> children) {
        this.children = children;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getGeometricError() {
        return geometricError;
    }

    public void setGeometricError(Double geometricError) {
        this.geometricError = geometricError;
    }

    public class NewsBean{

        boundingVolume boundingVolume;
        String content;
        String geometricError;

        public Root2.boundingVolume getBoundingVolume() {
            return boundingVolume;
        }

        public void setBoundingVolume(Root2.boundingVolume boundingVolume) {
            this.boundingVolume = boundingVolume;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGeometricError() {
            return geometricError;
        }

        public void setGeometricError(String geometricError) {
            this.geometricError = geometricError;
        }
    }
    public class boundingVolume{
        List box;

        public List getBox() {
            return box;
        }

        public void setBox(List box) {
            this.box = box;
        }
    }
}

