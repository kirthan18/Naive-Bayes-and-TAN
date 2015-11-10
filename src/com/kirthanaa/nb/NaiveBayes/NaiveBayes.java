package com.kirthanaa.nb.NaiveBayes;

import com.kirthanaa.nb.ARFFReader.ARFFReader;
import com.kirthanaa.nb.Entities.NBAttributeClass;

import java.util.ArrayList;

/**
 * Created by kirthanaaraghuraman on 11/3/15.
 */
public class NaiveBayes {

    private static ARFFReader mTrainFile = null;

    private static ARFFReader mTestFile = null;


    private static void predictClasses(){
        for(int i = 0; i < mTestFile.getNumberOfDataInstances(); i++){
            double attrProbability = 1;
            double initialClassProbability = (double)mTrainFile.mClassDistribution.get(mTrainFile.mClassLabels[0])/
                    (double)mTrainFile.getNumberOfDataInstances();
            double classProbability = 1;
            double attrClassProb = 1;
            double denominator = 0.0;
            double numerator = 0.0;

            /*for(int j = 0; j < mTestFile.getNumberOfAttributes(); j++){
                String attrName = mTestFile.getAttributeList().get(j).getAttributeName();
                String attrValue = mTestFile.getDataInstanceList().get(j).get(attrName);
                int index = -1;
                ArrayList<NBAttributeClass> nbAttributeClasses = mTrainFile.mAttributeDistributionList.get(attrName);
                for(int k = 0; k < nbAttributeClasses.size(); k++){
                    if(nbAttributeClasses.get(k).getAttributeValue().equalsIgnoreCase(attrValue)){
                        index = k;
                        break;
                    }
                }
                attrProbability = attrProbability * mTrainFile.mAttributeDistributionList.get(attrName)
                        .get(index).getAttributeClassCount()[0];
            }

            numerator = initialClassProbability * attrProbability;*/

            for(int j = 0; j < mTrainFile.mClassLabels.length; j++){
                classProbability = (double)mTrainFile.mClassDistribution.get(mTrainFile.mClassLabels[j])/
                        (double)mTrainFile.getNumberOfDataInstances();
                for(int k = 0; k < mTrainFile.getNumberOfAttributes(); k++){
                    String attrName = mTestFile.getAttributeList().get(j).getAttributeName();
                    String attrValue = mTestFile.getDataInstanceList().get(j).get(attrName);
                    int index = -1;
                    ArrayList<NBAttributeClass> nbAttributeClasses = mTrainFile.mAttributeDistributionList.get(attrName);
                    for(int l = 0; l < nbAttributeClasses.size(); l++){
                        if(nbAttributeClasses.get(l).getAttributeValue().equalsIgnoreCase(attrValue)){
                            index = l;
                            break;
                        }
                    }
                    attrClassProb = attrClassProb * ((double)mTrainFile.mAttributeDistributionList.get(attrName)
                            .get(index).getAttributeClassCount()[j]/(double)
                            (mTrainFile.mAttributeDistributionList.get(attrName).get(index).getAttributeClassCount()[0] +
                                    mTrainFile.mAttributeDistributionList.get(attrName).get(index).getAttributeClassCount()[1]));

                    if(j == 0){
                        attrProbability = attrClassProb;
                        numerator = initialClassProbability * attrProbability;
                    }
                }
                denominator = denominator + (classProbability * attrClassProb);
            }

            if(numerator/denominator > 0.5){
                System.out.println(mTestFile.mClassLabelList.get(i) + " " + mTrainFile.mClassLabels[0] + " " +
                        (numerator/denominator));
            }else{
                System.out.println(mTestFile.mClassLabelList.get(i) + " " + mTrainFile.mClassLabels[1] + " " +
                        (numerator/denominator));
            }
        }
    }


    public static void main(String[] args){
        String trainFile = "/Users/kirthanaaraghuraman/Documents/CS760/HW#3/src/com/kirthanaa/nb/Files/lymph_train.arff";
        String testFile = "/Users/kirthanaaraghuraman/Documents/CS760/HW#3/src/com/kirthanaa/nb/Files/lymph_test.arff";

        mTrainFile = ARFFReader.getInstance(trainFile);
        mTrainFile.parseARFFFile();

        mTestFile = ARFFReader.getInstance(testFile);
        mTestFile.parseARFFFile();

        predictClasses();
    }
}
