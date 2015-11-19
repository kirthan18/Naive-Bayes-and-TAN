package com.kirthanaa.nb.HW3;

import com.kirthanaa.nb.ARFFReader.ARFFReader;
import com.kirthanaa.nb.Entities.NBAttributeClass;
import com.kirthanaa.nb.Entities.NBGraph;
import com.kirthanaa.nb.Entities.NaiveBayesAttribute;

import java.util.ArrayList;

/**
 * Created by kirthanaaraghuraman on 11/13/15.
 */
public class TAN {

    private NBGraph mGraph = null;

    private ARFFReader mTrainFile = null;

    private ARFFReader mTestFile = null;

    public TAN(ARFFReader trainFile, ARFFReader testFile) {
        mGraph = new NBGraph(trainFile.getNumberOfAttributes());
        this.mTrainFile = trainFile;
        this.mTestFile = testFile;
    }


    /**
     * Calculates log base 2 of number
     *
     * @param n number whose log is to be calculates
     * @return log2 (n)
     */
    public static double log2(double n) {
        double num = Math.log(n);
        double den = Math.log(2.00);
        double ans = num / den;
        return ans;
    }

    private double findAttributeProbability(int attribute, String attrValue) {
        int count = 0;

        String attributeName = mTrainFile.getAttributeList().get(attribute).getAttributeName();

        ArrayList<NBAttributeClass> nbAttributeClasses = mTrainFile.mAttributeDistributionList.get(attributeName);

        for (int i = 0; i < nbAttributeClasses.size(); i++) {
            if (nbAttributeClasses.get(i).getAttributeValue().equalsIgnoreCase(attrValue)) {
                count = nbAttributeClasses.get(i).getAttributeClassCount()[0] +
                        nbAttributeClasses.get(i).getAttributeClassCount()[1];
                break;
            }
        }

        double laplaceNumerator = (double) (count + 1);
        double laplaceDenominator = (double) nbAttributeClasses.size() + (double) mTrainFile.getNumberOfDataInstances();
        double probAfterLaplaceEstimate = laplaceNumerator / laplaceDenominator;

        return probAfterLaplaceEstimate;
    }

    private double findClassProbability(String classLabel) {
        int classCount = mTrainFile.mClassDistribution.get(classLabel);

        double laplaceNumerator = (double) (classCount + 1);
        double laplaceDenominator = (double) (mTrainFile.getNumberOfDataInstances() + mTrainFile.mClassLabels.length);
        double probAfterLaplaceEstimation = laplaceNumerator / laplaceDenominator;

        return probAfterLaplaceEstimation;
    }

    private double findJointProbability(int srcAttribute, int destAttribute, String srcAttributeValue,
                                        String destAttributeValue, String classLabel) {
        double jointProbability = 0.0;
        int count = 0;

        String sourceAttribute = mTrainFile.getAttributeList().get(srcAttribute).getAttributeName();
        String destinationAttribute = mTrainFile.getAttributeList().get(destAttribute).getAttributeName();


        for (int i = 0; i < mTrainFile.getNumberOfDataInstances(); i++) {
            String instanceSourceAttrValue = mTrainFile.getDataInstanceList().get(i).get(sourceAttribute);
            String instanceDestAttrValue = mTrainFile.getDataInstanceList().get(i).get(destinationAttribute);
            String instanceClassLabel = mTrainFile.mClassLabelList.get(i);

            if (instanceSourceAttrValue.equalsIgnoreCase(srcAttributeValue)
                    && instanceDestAttrValue.equalsIgnoreCase(destAttributeValue)
                    && instanceClassLabel.equalsIgnoreCase(classLabel)) {
                count++;
            }
        }
        /*double pxi = findAttributeProbability(srcAttribute, srcAttributeValue);
        double pxj = findAttributeProbability(destAttribute, destAttributeValue);
        double py = findClassProbability(classLabel);

        jointProbability = pxi * pxj * py;*/

        double laplaceNumerator = (double) (count + 1);

        double laplaceDenominator = (double) (mTrainFile.getNumberOfDataInstances() +
                ((mTrainFile.getAttributeList().get(srcAttribute).getAttributeValues().length)
                * (mTrainFile.getAttributeList().get(destAttribute).getAttributeValues().length)
                        * (mTrainFile.mClassLabels.length)));

        jointProbability = laplaceNumerator / laplaceDenominator;

        return jointProbability;
    }

    private double getConditionalProbability(int srcAttr, String srcAttrValue,
                                             int destAttr, String destAttrValue,
                                             int classLabelIndex) {
        int count = 0;
        String classLabel = mTrainFile.mClassLabels[classLabelIndex];

        String sourceAttributeName = mTrainFile.getAttributeList().get(srcAttr).getAttributeName();
        String destAttributeName = mTrainFile.getAttributeList().get(destAttr).getAttributeName();

        for (int i = 0; i < mTrainFile.getNumberOfDataInstances(); i++) {
            String instanceSourceAttrValue = mTrainFile.getDataInstanceList().get(i).get(sourceAttributeName);
            String instanceDestAttrValue = mTrainFile.getDataInstanceList().get(i).get(destAttributeName);
            String instanceClassLabel = mTrainFile.mClassLabelList.get(i);

            if (instanceSourceAttrValue.equalsIgnoreCase(srcAttrValue)
                    && instanceDestAttrValue.equalsIgnoreCase(destAttrValue)
                    && instanceClassLabel.equalsIgnoreCase(classLabel)) {
                count++;
            }
        }

        double laplaceNumerator = (double) (count + 1);

        double laplaceDenominator = (double) (mTrainFile.mClassDistribution.get(classLabel) +
                ((mTrainFile.getAttributeList().get(srcAttr).getAttributeValues().length) *
                        (mTrainFile.getAttributeList().get(destAttr).getAttributeValues().length)));

        double conditionalProbability = laplaceNumerator / laplaceDenominator;

        return conditionalProbability;
    }

    private double getConditionalProbability(int attr, String attrValue, int classLabelIndex) {
        int count = 0;
        String classLabel = mTrainFile.mClassLabels[classLabelIndex];

        String attrName = mTrainFile.getAttributeList().get(attr).getAttributeName();
        ArrayList<NBAttributeClass> nbAttributeClasses = mTrainFile.mAttributeDistributionList.get(attrName);

        for (int i = 0; i < nbAttributeClasses.size(); i++) {
            if (nbAttributeClasses.get(i).getAttributeValue().equalsIgnoreCase(attrValue)) {
                count = nbAttributeClasses.get(i).getAttributeClassCount()[classLabelIndex];
                break;
            }
        }

        double laplaceNumerator = (double) (count + 1);
        double laplaceDenominator = (double) (mTrainFile.mClassDistribution.get(classLabel) + nbAttributeClasses.size());
        double probAfterLaplaceEstimation = laplaceNumerator / laplaceDenominator;

        return probAfterLaplaceEstimation;
    }


    private double calculateWeight(int srcAttribute, int destAttribute) {

        //Initialize sum = 0.0
        double sum = 0.0;

        NaiveBayesAttribute sourceAttr = mTrainFile.getAttributeList().get(srcAttribute);
        NaiveBayesAttribute destinationAttr = mTrainFile.getAttributeList().get(destAttribute);

        for (int i = 0; i < sourceAttr.getAttributeValues().length; i++) {
            for (int j = 0; j < destinationAttr.getAttributeValues().length; j++) {
                for (int k = 0; k < mTrainFile.mClassLabels.length; k++) {

                    //Find P(xi, xj, y)
                    double jointProbability = findJointProbability(srcAttribute, destAttribute,
                            sourceAttr.getAttributeValues()[i], destinationAttr.getAttributeValues()[j],
                            mTrainFile.mClassLabels[k]);
                    //System.out.println("Joint Probability = " + jointProbability);

                    //Find A = P((xi, xj)|y)
                    double pXiXjY = getConditionalProbability(srcAttribute, sourceAttr.getAttributeValues()[i],
                            destAttribute, destinationAttr.getAttributeValues()[j], k);

                    //Find B = P(xi | y)
                    double pXiY = getConditionalProbability(srcAttribute, sourceAttr.getAttributeValues()[i], k);

                    //Find C = P(xj | y)
                    double pXjY = getConditionalProbability(destAttribute, destinationAttr.getAttributeValues()[j], k);

                    //Let D = B * C
                    double D = pXiY * pXjY;

                    //Let E = A / D
                    double E = pXiXjY / D;

                    //Let F = log(E)
                    double F = log2(E);

                    //Let G = A * F
                    double G = jointProbability  * F;

                    //sum = sum + G
                    sum = sum + G;
                }
            }
        }
        return sum;
    }


    public void computeEdgeWeights() {
        for (int i = 0; i < mTrainFile.getNumberOfAttributes(); i++) {
            for (int j = 0; j < mTrainFile.getNumberOfAttributes(); j++) {
                if (i == j) {
                    continue;
                } else {
                    mGraph.addEdgeWeight(calculateWeight(i, j), i, j);
                }
            }
        }

        /*String attribute = mTrainFile.getAttributeList().get(0).getAttributeName();

        for(int i = 0 ; i < mTrainFile.getAttributeList().get(0).getAttributeValues().length; i++){
            System.out.println("Pr(" + attribute + " = " + mTrainFile.getAttributeList().get(0).getAttributeValues()[i]
             + " | " + " class label = " + mTrainFile.mClassLabels[0] + ") = " +
                    getConditionalProbability(0, mTrainFile.getAttributeList().get(0).getAttributeValues()[i], 0));

            System.out.println("Pr(" + attribute + " = " + mTrainFile.getAttributeList().get(0).getAttributeValues()[i]
                    + " | " + " class label = " + mTrainFile.mClassLabels[1] + ") = " +
                    getConditionalProbability(0, mTrainFile.getAttributeList().get(0).getAttributeValues()[i], 1));


        }*/

        printAdjacencyMatrix();
    }

    private void printAdjacencyMatrix(){
        for(int i = 0; i < mGraph.getVertices(); i++){
            for(int j = 0; j < mGraph.getVertices(); j++){
                System.out.print(mGraph.getAdjacencyMatrix()[i][j] + "\t");
            }
            System.out.println();
        }
    }

    private void printMSTAdjacenyMatrix(){
        for(int i = 0; i <= mGraph.getVertices(); i++){
            for(int j = 0; j <= mGraph.getVertices(); j++){
                System.out.print(mGraph.getMSTAdjacencyMatrix()[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public void getMST(){
        mGraph.getMaximalSpanningTree();
        //printMSTAdjacenyMatrix();
        mGraph.addNodeFromClass();
        printMSTAdjacenyMatrix();
    }
}
