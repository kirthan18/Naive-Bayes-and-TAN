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
            double initialClassProbability = (double)(mTrainFile.mClassDistribution.get(mTrainFile.mClassLabels[0]) + 1)/
                    (double)(mTrainFile.getNumberOfDataInstances() + mTrainFile.mClassLabels.length);
            double classProbability = 1;
            double attrClassProb = 1;
            double denominator = 0.0;
            double numerator = 0.0;

            for(int j = 0; j < mTrainFile.mClassLabels.length; j++){

               /* System.out.println("\n****************************************");
                System.out.println("Class label : " + mTrainFile.mClassLabels[j]);*/
                classProbability = (double)(mTrainFile.mClassDistribution.get(mTrainFile.mClassLabels[j]) + 1)/
                        (double)(mTrainFile.getNumberOfDataInstances() + mTrainFile.mClassLabels.length);
                //System.out.println("Class " + j + " probability : " + classProbability);

                attrClassProb = 1;

                for(int k = 0; k < mTrainFile.getNumberOfAttributes(); k++){
                    String attrName = mTestFile.getAttributeList().get(k).getAttributeName();
                    String attrValue = mTestFile.getDataInstanceList().get(i).get(attrName);
                    int index = -1;
                    ArrayList<NBAttributeClass> nbAttributeClasses = mTrainFile.mAttributeDistributionList.get(attrName);
                    for(int l = 0; l < nbAttributeClasses.size(); l++){
                        if(nbAttributeClasses.get(l).getAttributeValue().equalsIgnoreCase(attrValue)){
                            index = l;
                            break;
                        }
                    }

                    //System.out.println("Attribute name : " + attrName + " Value : " + attrValue);

                    /*attrClassProb = attrClassProb * ((double)mTrainFile.mAttributeDistributionList.get(attrName)
                            .get(index).getAttributeClassCount()[j]/(double)
                            (mTrainFile.mAttributeDistributionList.get(attrName).get(index).getAttributeClassCount()[0] +
                                    mTrainFile.mAttributeDistributionList.get(attrName).get(index).getAttributeClassCount()[1]));*/
                    double numAttrGivenClass = ((double)mTrainFile.mAttributeDistributionList.get(attrName)
                            .get(index).getAttributeClassCount()[j]);
                    double laplaceNum = numAttrGivenClass + 1;
                    double laplaceDen = 0.0;
                    double attrProb = mTrainFile.mAttributeDistributionList.get(attrName).size();
                    for(int x = 0; x < mTrainFile.mAttributeDistributionList.get(attrName).size(); x++){
                        attrProb = attrProb + ((double)mTrainFile.mAttributeDistributionList.get(attrName)
                                .get(x).getAttributeClassCount()[j]);
                    }
                    laplaceDen = attrProb;
                    double probAttrGivenClass = (double)laplaceNum/(double)laplaceDen;
                    //double numClass = (double)mTrainFile.mClassDistribution.get(mTrainFile.mClassLabels[j]);
                    //double probAttrGivenClass = numAttrGivenClass/numClass;

                    /*System.out.println("NumAttrGivenClass = " + numAttrGivenClass);
                    System.out.println("Laplace num = " + laplaceNum);
                    System.out.println("Laplace den = " + laplaceDen);
                    System.out.println("Prob attr given class = " + probAttrGivenClass);*/
                    attrClassProb = attrClassProb * probAttrGivenClass;
                    /*System.out.println("Attr class Prob = " + attrClassProb);
                    System.out.println("\n****************************************\n\n");*/

                }
                if(j == 0){
                    attrProbability = attrClassProb;
                    numerator = initialClassProbability * attrProbability;
                }

                //System.out.println("%%%%%% " + (classProbability * attrClassProb) + " %%%%%%");
                denominator = denominator + (classProbability * attrClassProb);
                //System.out.println("\n****************************************\n\n");
            }

            /*System.out.println("Numerator : " + numerator);
            System.out.println("Denominator : " + denominator);*/
            if(numerator/denominator > 0.5){
                System.out.println(mTestFile.mClassLabels[0] + " " + mTestFile.mClassLabelList.get(i) + " " +
                        (numerator/denominator));
            }else{
                System.out.println(mTestFile.mClassLabels[1] + " " + mTestFile.mClassLabelList.get(i) + " " +
                        (1.0 - (numerator/denominator)));
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
