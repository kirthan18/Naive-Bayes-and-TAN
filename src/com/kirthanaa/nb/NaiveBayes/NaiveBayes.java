package com.kirthanaa.nb.NaiveBayes;

import com.kirthanaa.nb.ARFFReader.ARFFReader;

import java.util.HashMap;

/**
 * Created by kirthanaaraghuraman on 11/3/15.
 */
public class NaiveBayes {

    private static ARFFReader mARFFReader = null;


    //private static

    private static void constructDictionary(){

    }
    public static void main(String[] args){
        String trainFile = "/Users/kirthanaaraghuraman/Documents/CS760/HW#3/src/com/kirthanaa/nb/Files/lymph_train.arff";

        mARFFReader = ARFFReader.getInstance(trainFile);
        mARFFReader.parseARFFFile();
    }
}
