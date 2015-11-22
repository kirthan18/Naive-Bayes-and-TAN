package com.kirthanaa.nb.Entities;

/**
 * Created by kirthanaaraghuraman on 11/10/15.
 */
public class NBAttributeClass {

    private String mAttributeValue = "";

    private int[] mAttributeClassCount;

    public NBAttributeClass(String attributeValue, int[] attributeClassCount) {
        this.mAttributeValue = attributeValue;
        this.mAttributeClassCount = new int[2];
        this.mAttributeClassCount = attributeClassCount;
    }

    public int[] getAttributeClassCount() {
        return mAttributeClassCount;
    }

    public String getAttributeValue() {
        return mAttributeValue;
    }

    public void setAttributeClassCount(int index, int count) {
        this.mAttributeClassCount[index] = count;
    }
}
