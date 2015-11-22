package com.kirthanaa.nb.HW3;

import com.kirthanaa.nb.ARFFReader.ARFFReader;
import com.kirthanaa.nb.Entities.NBAttributeClass;
import com.kirthanaa.nb.Entities.NBGraph;
import com.kirthanaa.nb.Entities.NaiveBayesAttribute;

import java.util.ArrayList;
import java.util.HashMap;

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

    private double findClassProbability(int classIndex) {
        int classCount = mTrainFile.mClassDistribution.get(mTestFile.mClassLabels[classIndex]);

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
        //System.out.println("Count = " + count);
        //System.out.println("Denominator = " + laplaceDenominator);
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
                    double G = jointProbability * F;

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

        //printAdjacencyMatrix();
    }

    private void printAdjacencyMatrix() {
        for (int i = 0; i < mGraph.getVertices(); i++) {
            for (int j = 0; j < mGraph.getVertices(); j++) {
                System.out.print(mGraph.getAdjacencyMatrix()[i][j] + "\t");
            }
            System.out.println();
        }
    }

    private void printMSTAdjacenyMatrix() {
        for (int i = 0; i <= mGraph.getVertices(); i++) {
            for (int j = 0; j <= mGraph.getVertices(); j++) {
                System.out.print(mGraph.getMSTAdjacencyMatrix()[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public void getMST() {
        mGraph.getMaximalSpanningTree();
        //printMSTAdjacenyMatrix();
        mGraph.addNodeFromClass();
        //printMSTAdjacenyMatrix();
    }

    public void printParentAtrributes() {
        for (int j = 0; j < mTrainFile.getNumberOfAttributes(); j++) {
            ArrayList<Integer> parentAttributeList = mGraph.getParentAttribute(j);
            ;
            if (parentAttributeList != null) {
                System.out.print(mTrainFile.getAttributeList().get(j).getAttributeName() + " ");
                for (int i = 0; i < parentAttributeList.size(); i++) {
                    if (parentAttributeList.get(i) == mTrainFile.getNumberOfAttributes()) {
                        System.out.print("class");
                    } else {
                        System.out.print(mTrainFile.getAttributeList().get(parentAttributeList.get(i)).getAttributeName() + " ");
                    }

                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public ArrayList<Integer> getParentAttribute(int j) {

        ArrayList<Integer> parentAttributeList;
        String attribute = mTrainFile.getAttributeList().get(j).getAttributeName();

        parentAttributeList = mGraph.getParentAttribute(j);

        /*if(parentAttributeList != null) {
            System.out.print(attribute + " ");
            for(int i = 0; i < parentAttributeList.size(); i++){
                if(parentAttributeList.get(i) == mTrainFile.getNumberOfAttributes()){
                    System.out.print("class");
                }else {
                    System.out.print(mTrainFile.getAttributeList().get(parentAttributeList.get(i)).getAttributeName() + "\t");
                }

            }
        }
        System.out.println();*/
        return parentAttributeList;
    }

    private double findAllAttributeJointProbability(int dataInstanceIndex) {
        HashMap<String, String> dataInstance = mTestFile.getDataInstanceList().get(dataInstanceIndex);
        double sum = 0;

        int count = 0;
        for (int i = 0; i < mTrainFile.getNumberOfDataInstances(); i++) {
            boolean isAllAttributeMatch = true;

            for (int j = 0; j < mTrainFile.getNumberOfAttributes(); j++) {
                String attributeName = mTestFile.getAttributeList().get(j).getAttributeName();
                String attributeValue = dataInstance.get(attributeName);

                if (attributeValue == mTrainFile.getDataInstanceList().get(i).get(attributeName)) {
                    continue;
                } else {
                    isAllAttributeMatch = false;
                    break;
                }
            }
            if (isAllAttributeMatch) {
                count++;
            }
        }
        double laplaceNumerator = (double) (count + 1);

        int laplaceCount = 1;

        for (int l = 0; l < mTrainFile.getNumberOfAttributes(); l++) {
            laplaceCount = laplaceCount * mTrainFile.getAttributeList().get(l).getAttributeValues().length;
        }

        double laplaceDenominator = (double) laplaceCount + (double) mTrainFile.getNumberOfDataInstances();

        double pX1XnY = laplaceNumerator / laplaceDenominator;
        return pX1XnY;
    }

    private double findJointProbability(int dataInstanceIndex, int attributeIndex, ArrayList<Integer> parentAttributeList,
                                        int classIndex) {
        HashMap<String, String> dataInstance = mTestFile.getDataInstanceList().get(dataInstanceIndex);

        NaiveBayesAttribute attribute = mTestFile.getAttributeList().get(attributeIndex);
        String attributeName = attribute.getAttributeName();
        String attributeValue = mTestFile.getDataInstanceList().get(dataInstanceIndex).get(attributeName);

        //Numerator calculation
        int count = 0;
        for (int i = 0; i < mTrainFile.getNumberOfDataInstances(); i++) {
            if (attributeValue.equalsIgnoreCase(mTrainFile.getDataInstanceList().get(i).get(attributeName))) {
                boolean isAllAttributeSame = true;
                for (int j = 0; j < parentAttributeList.size(); j++) {
                    if (parentAttributeList.get(j) == mTrainFile.getNumberOfAttributes()) {
                        if (mTrainFile.mClassLabelList.get(i).equalsIgnoreCase(mTrainFile.mClassLabels[classIndex])) {
                            continue;
                        } else {
                            isAllAttributeSame = false;
                            break;
                        }
                    } else {
                        String attrName = mTrainFile.getAttributeList().get(parentAttributeList.get(j)).getAttributeName();
                        if (dataInstance.get(attrName).equalsIgnoreCase(
                                mTrainFile.getDataInstanceList().get(i).get(attrName))) {
                            continue;
                        } else {
                            isAllAttributeSame = false;
                            break;
                        }
                    }
                }
                if (isAllAttributeSame) {
                    count++;
                }
            }
        }
        double laplaceNumerator = (double) (count + 1);

        int product = attribute.getAttributeValues().length;

        int dCount = 0;
        for (int i = 0; i < mTrainFile.getNumberOfDataInstances(); i++) {

            boolean isAllAttributeSame = true;
            for (int j = 0; j < parentAttributeList.size(); j++) {
                if (parentAttributeList.get(j) == mTrainFile.getNumberOfAttributes()) {
                    if (mTrainFile.mClassLabelList.get(i).equalsIgnoreCase(mTrainFile.mClassLabels[classIndex])) {
                        continue;
                    } else {
                        isAllAttributeSame = false;
                        break;
                    }
                } else {
                    String attrName = mTrainFile.getAttributeList().get(parentAttributeList.get(j)).getAttributeName();
                    if (dataInstance.get(attrName).equalsIgnoreCase(
                            mTrainFile.getDataInstanceList().get(i).get(attrName))) {
                        continue;
                    } else {
                        isAllAttributeSame = false;
                        break;
                    }
                }
            }
            if (isAllAttributeSame) {
                dCount++;
            }
        }

        double laplaceDenominator = (double) dCount + product;

        double pXiParentsXi = laplaceNumerator / laplaceDenominator;

       /* System.out.print("P(" + attributeName + " = " + attributeValue + " | ");
        for (int a = 0; a < parentAttributeList.size(); a++) {
            if (parentAttributeList.get(a) == mTestFile.getNumberOfAttributes()) {
                System.out.print("class = " + mTestFile.mClassLabels[0]);
            } else {
                System.out.print(mTestFile.getAttributeList().get(parentAttributeList.get(a)).getAttributeName()
                        + " = " + dataInstance.get(mTestFile.getAttributeList().get(parentAttributeList.get(a)).getAttributeName())
                        + " , ");
            }

        }
        System.out.println(") = " + pXiParentsXi);*/
        return pXiParentsXi;
    }


    public void classify() {
        int numCorrectlyClassified = 0;

        for (int i = 0; i < mTestFile.getNumberOfDataInstances(); i++) {
            double numerator = 0.0;
            double denominator = 0.0;

            for (int x = 0; x < mTestFile.mClassLabels.length; x++) {
                double attrProb = 1.0;
                double pClass = findClassProbability(x);

                for (int j = 0; j < mTestFile.getNumberOfAttributes(); j++) {
                    ArrayList<Integer> parentAttributes = getParentAttribute(j);
                    double pXiParentsXi = findJointProbability(i, j, parentAttributes, x);
                    attrProb = attrProb * pXiParentsXi;
                }

                if (x == 0) {
                    numerator = attrProb * pClass;
                }
                denominator = denominator + (attrProb * pClass);
            }

            double pY1X1Xn = numerator / denominator;
            double pY2X1Xn = 1 - pY1X1Xn;

            if (pY1X1Xn > pY2X1Xn) {
                if (mTestFile.mClassLabels[0].equalsIgnoreCase(mTestFile.mClassLabelList.get(i))) {
                    numCorrectlyClassified++;
                }
                System.out.println(mTestFile.mClassLabels[0] + " " + mTestFile.mClassLabelList.get(i) + " " + pY1X1Xn);
            } else {
                if (mTestFile.mClassLabels[1].equalsIgnoreCase(mTestFile.mClassLabelList.get(i))) {
                    numCorrectlyClassified++;
                }
                System.out.println(mTestFile.mClassLabels[1] + " " + mTestFile.mClassLabelList.get(i) + " " + pY2X1Xn);
            }
        }
        System.out.println("\n" + numCorrectlyClassified);
    }
}

