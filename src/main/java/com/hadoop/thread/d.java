package com.hadoop.thread;

public class d {
}

class MusicImplements implements Runnable {
    private String idNum;

    public  void setId(String idNum) {
        this.idNum = idNum;

    }

    @Override
    public void run() {
        System.out.println(idNum);
    }
}