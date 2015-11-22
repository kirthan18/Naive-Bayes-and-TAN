package com.kirthanaa.nb.Entities;

/**
 * Created by kirthanaaraghuraman on 11/10/15.
 */
public class NBAttributeClass {

    /**
     * Value of attribute
     */
    private String mAttributeValue = "";

    /**
     * Attribute Value counts for each class labels
     */
    private int[] mAttributeClassCount;

    /**
     * Initializes NBAttributeClass variable
     *
     * @param attributeValue      Value of the attribute
     * @param attributeClassCount Count of attribute value for each class label
     */
    public NBAttributeClass(String attributeValue, int[] attributeClassCount) {
        this.mAttributeValue = attributeValue;
        this.mAttributeClassCount = new int[2];
        this.mAttributeClassCount = attributeClassCount;
    }

    /**
     * Returns the attribute value's class count
     *
     * @return Attribute value's class count
     */
    public int[] getAttributeClassCount() {
        return mAttributeClassCount;
    }

    /**
     * Returns the attribute value
     *
     * @return Attribute Value
     */
    public String getAttributeValue() {
        return mAttributeValue;
    }

    /**
     * Sets the attribute value's count for each class label
     *
     * @param index Index of the class variable
     * @param count Count of the class variable
     */
    public void setAttributeClassCount(int index, int count) {
        this.mAttributeClassCount[index] = count;
    }
}
