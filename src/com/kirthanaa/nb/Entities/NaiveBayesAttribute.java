package com.kirthanaa.nb.Entities;

/**
 * Created by kirthanaaraghuraman on 11/3/15.
 */
public class NaiveBayesAttribute {


    /**
     * Order of the attribute in the ARFF File
     */
    private int mAttributeOrdinal = -1;

    /**
     * Name of the attribute
     */
    private String mAttributeName;

    /**
     * List of values the attribute can take
     */
    private String[] mAttributeValues;

    /**
     * Constructor for initializing NaiveBayesAttribute instance
     *
     * @param attributeOrdinal Order in which attribute appears in the ARFF File
     * @param attributeName    Name of the attribute
     */
    public NaiveBayesAttribute(int attributeOrdinal, String attributeName, String[] attributeValues) {
        this.mAttributeOrdinal = attributeOrdinal;
        this.mAttributeName = attributeName;
        this.mAttributeValues = attributeValues;
    }

    /**
     * Function to get the ordinal of the attribute
     *
     * @return Integer representing the attribute ordinal
     */
    public int getAttributeOrdinal() {
        return mAttributeOrdinal;
    }

    /**
     * Function to get the attribute name
     *
     * @return String containing attribute name
     */
    public String getAttributeName() {
        return mAttributeName;
    }

    /**
     * Function to get all possible values the nominal attribute could take
     *
     * @return String array containing list of values
     */
    public String[] getAttributeValues() {
        return mAttributeValues;
    }
}
