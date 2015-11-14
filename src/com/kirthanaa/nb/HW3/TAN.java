package com.kirthanaa.nb.HW3;

import com.kirthanaa.nb.ARFFReader.ARFFReader;
import com.kirthanaa.nb.Entities.NBGraph;

/**
 * Created by kirthanaaraghuraman on 11/13/15.
 */
public class TAN {

    private NBGraph mGraph = null;

    private ARFFReader mTrainFile = null;

    private ARFFReader mTestFile = null;

    public TAN(ARFFReader trainFile, ARFFReader testFile){
        mGraph = new NBGraph(trainFile.getNumberOfAttributes());
        this.mTrainFile = trainFile;
        this.mTestFile = testFile;
    }


    private double calculateWeight(int srcAttribute, int destAttribute){
        //TODO - Calculate probabilities and update this
        return 0.0;
    }


    private void computeEdgeWeights(){
        for(int i = 0; i < mTrainFile.getNumberOfAttributes(); i++){
            for(int j = 0; j < mTrainFile.getNumberOfAttributes(); j++){
                if(i == j){
                    continue;
                }else{
                    mGraph.addEdgeWeight(calculateWeight(i, j),i, j);
                }
            }
        }
    }
}
