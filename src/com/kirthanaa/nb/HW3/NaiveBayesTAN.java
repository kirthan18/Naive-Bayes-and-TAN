package com.kirthanaa.nb.HW3;

import com.kirthanaa.nb.ARFFReader.ARFFReader;
import com.kirthanaa.nb.Entities.NBAttributeClass;
import com.kirthanaa.nb.Entities.NaiveBayesAttribute;

import java.util.ArrayList;

/**
 * Created by kirthanaaraghuraman on 11/3/15.
 */
public class NaiveBayesTAN {

    /**
     * Training File instance
     */
    private static ARFFReader mTrainFile = null;

    /**
     * Test file instance
     */
    private static ARFFReader mTestFile = null;

    /**
     * Predics class labels using Naive Bayes method
     */
    private static void predictClasses() {

        int numCorrectlyClassified = 0;

        for (int i = 0; i < mTestFile.getNumberOfDataInstances(); i++) {
            double attrProbability = 1;
            double initialClassProbability = (double) (mTrainFile.mClassDistribution.get(mTrainFile.mClassLabels[0]) + 1) /
                    (double) (mTrainFile.getNumberOfDataInstances() + mTrainFile.mClassLabels.length);
            double classProbability = 1;
            double attrClassProb = 1;
            double denominator = 0.0;
            double numerator = 0.0;

            for (int j = 0; j < mTrainFile.mClassLabels.length; j++) {

               /* System.out.println("\n****************************************");
                System.out.println("Class label : " + mTrainFile.mClassLabels[j]);*/
                classProbability = (double) (mTrainFile.mClassDistribution.get(mTrainFile.mClassLabels[j]) + 1) /
                        (double) (mTrainFile.getNumberOfDataInstances() + mTrainFile.mClassLabels.length);
                //System.out.println("Class " + j + " probability : " + classProbability);

                attrClassProb = 1;

                for (int k = 0; k < mTrainFile.getNumberOfAttributes(); k++) {
                    String attrName = mTestFile.getAttributeList().get(k).getAttributeName();
                    String attrValue = mTestFile.getDataInstanceList().get(i).get(attrName);
                    int index = -1;
                    ArrayList<NBAttributeClass> nbAttributeClasses = mTrainFile.mAttributeDistributionList.get(attrName);
                    for (int l = 0; l < nbAttributeClasses.size(); l++) {
                        if (nbAttributeClasses.get(l).getAttributeValue().equalsIgnoreCase(attrValue)) {
                            index = l;
                            break;
                        }
                    }

                    //System.out.println("Attribute name : " + attrName + " Value : " + attrValue);

                    /*attrClassProb = attrClassProb * ((double)mTrainFile.mAttributeDistributionList.get(attrName)
                            .get(index).getAttributeClassCount()[j]/(double)
                            (mTrainFile.mAttributeDistributionList.get(attrName).get(index).getAttributeClassCount()[0] +
                                    mTrainFile.mAttributeDistributionList.get(attrName).get(index).getAttributeClassCount()[1]));*/
                    double numAttrGivenClass = ((double) mTrainFile.mAttributeDistributionList.get(attrName)
                            .get(index).getAttributeClassCount()[j]);
                    double laplaceNum = numAttrGivenClass + 1;
                    double laplaceDen = 0.0;
                    double attrProb = mTrainFile.mAttributeDistributionList.get(attrName).size();
                    for (int x = 0; x < mTrainFile.mAttributeDistributionList.get(attrName).size(); x++) {
                        attrProb = attrProb + ((double) mTrainFile.mAttributeDistributionList.get(attrName)
                                .get(x).getAttributeClassCount()[j]);
                    }
                    laplaceDen = attrProb;
                    double probAttrGivenClass = laplaceNum / laplaceDen;
                    attrClassProb = attrClassProb * probAttrGivenClass;

                    //double numClass = (double)mTrainFile.mClassDistribution.get(mTrainFile.mClassLabels[j]);
                    //double probAttrGivenClass = numAttrGivenClass/numClass;
                    /*System.out.println("NumAttrGivenClass = " + numAttrGivenClass);
                    System.out.println("Laplace num = " + laplaceNum);
                    System.out.println("Laplace den = " + laplaceDen);
                    System.out.println("Prob attr given class = " + probAttrGivenClass);*/
                    /*System.out.println("Attr class Prob = " + attrClassProb);
                    System.out.println("\n****************************************\n\n");*/

                }
                if (j == 0) {
                    attrProbability = attrClassProb;
                    numerator = initialClassProbability * attrProbability;
                }

                denominator = denominator + (classProbability * attrClassProb);
            }

            /*System.out.println("Numerator : " + numerator);
            System.out.println("Denominator : " + denominator);*/
            if (numerator / denominator > 0.5) {
                if (mTestFile.mClassLabels[0].equalsIgnoreCase(mTestFile.mClassLabelList.get(i))) {
                    numCorrectlyClassified++;
                }
                System.out.println(mTestFile.mClassLabels[0] + " " + mTestFile.mClassLabelList.get(i) + " " +
                        (numerator / denominator));
            } else {
                if (mTestFile.mClassLabels[1].equalsIgnoreCase(mTestFile.mClassLabelList.get(i))) {
                    numCorrectlyClassified++;
                }
                System.out.println(mTestFile.mClassLabels[1] + " " + mTestFile.mClassLabelList.get(i) + " " +
                        (1.0 - (numerator / denominator)));
            }
        }

        System.out.println("\n" + numCorrectlyClassified);
    }

    /**
     * Prints attributes according to specifications for Naive Bayes method
     */
    private static void printAttributes() {
        for (NaiveBayesAttribute attribute : mTrainFile.getAttributeList()) {
            System.out.println(attribute.getAttributeName() + " class");
        }
        System.out.println();
    }

    /**
     * Makes predictions using Naive Bayes classifier and prints output in
     * required format
     */
    private static void naiveBayesClassifier() {
        printAttributes();
        predictClasses();
    }

    /**
     * Constructs a graph of all attributes, computes edge weights using mutual information
     * Constructs a maximal spanning tree
     * Makes predictions using the constructed MST
     * Prints output in required format
     */
    private static void tanClassifier() {
        TAN tanClassifier = new TAN(mTrainFile, mTestFile);
        tanClassifier.computeEdgeWeights();
        tanClassifier.getMST();
        tanClassifier.printParentAtrributes();
        tanClassifier.classify();
    }


    public static void main(String[] args) {
        //String trainFile = "/Users/kirthanaaraghuraman/Documents/CS760/HW#3/src/com/kirthanaa/nb/Files/vote_train.arff";
        //String testFile = "/Users/kirthanaaraghuraman/Documents/CS760/HW#3/src/com/kirthanaa/nb/Files/vote_test.arff";

        String trainFile = args[0];
        String testFile = args[1];

        char algo = args[2].charAt(0);

        mTrainFile = ARFFReader.getInstance(trainFile);
        mTrainFile.parseARFFFile();

        mTestFile = ARFFReader.getInstance(testFile);
        mTestFile.parseARFFFile();

        switch (algo) {
            case 'b':
                naiveBayesClassifier();
                break;

            case 't':
                tanClassifier();
                break;
        }
    }
}
