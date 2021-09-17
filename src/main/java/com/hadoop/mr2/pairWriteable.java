package com.hadoop.mr2;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class pairWriteable implements WritableComparable<pairWriteable> {

    private String first;
    private int second;

    public pairWriteable() {

    }

    public pairWriteable(String first, int second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int compareTo(pairWriteable o) {
        int comp = this.first.compareTo(o.first);
        if (comp != 0) {
            return comp;
        } else {
            return Integer.valueOf(this.second).compareTo(Integer.valueOf(o.getSecond()));
        }
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(first);
        dataOutput.writeInt(second);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.first = dataInput.readUTF();
        this.second = dataInput.readInt();
    }
}