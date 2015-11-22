package com.kirthanaa.nb.Entities;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kirthanaaraghuraman on 11/13/15.
 */
public class NBGraph {

    /**
     * Number of edges in the graph
     */
    private int mEdges;

    /**
     * Number of vertices in the graph
     */
    private int mVertices;

    /**
     * Adjacency Matrix for attribute graph
     */
    private double[][] mAdjacencyMatrix;

    /**
     * Adjacency matrix for the maximum spanning tree
     */
    private double[][] mMSTAdjacencyMatrix;

    /**
     * Initializes a graph with the number of vertices specified
     *
     * @param numVertex Number of vertices
     */
    public NBGraph(int numVertex) {
        if (numVertex < 0) {
            throw new IllegalArgumentException("Number of vertices cannot be less than 0.");
        }
        this.mVertices = numVertex;
        //System.out.println("Number of vertices : " + mVertices);
        this.mEdges = (numVertex * (numVertex - 1)) / 2;
        addEdges();
    }

    /**
     * Adds edge weights for the graph
     */
    private void addEdges() {
        mAdjacencyMatrix = new double[mVertices][mVertices];
        for (int vertex = 0; vertex < mVertices; vertex++) {
            Arrays.fill(mAdjacencyMatrix[vertex], -1);
        }
    }

    /**
     * Returns the number of edges
     *
     * @return Number of edges
     */
    public int getEdges() {
        return mEdges;
    }

    /**
     * Returns the number of vertices
     *
     * @return Number of vertices
     */
    public int getVertices() {
        return mVertices;
    }

    /**
     * Checks if a vertex is valid
     *
     * @param v Vertex number
     */
    private void validateVertex(int v) {
        if (v < 0 || v > mVertices) {
            throw new IndexOutOfBoundsException("Vertex " + v + " must be between 0 and " + (mVertices - 1));
        }
    }

    /**
     * Adds an edge weight to the graph
     *
     * @param weight     Weight of the edge
     * @param srcVertex  Source vertex
     * @param destVertex Destination vertex
     */
    public void addEdgeWeight(double weight, int srcVertex, int destVertex) {
        validateVertex(srcVertex);
        validateVertex(destVertex);
        mAdjacencyMatrix[srcVertex][destVertex] = weight;
    }

    /**
     * Returns adjancency matrix of attribute graph
     *
     * @return Adjancency matrix of attribute graph
     */
    public double[][] getAdjacencyMatrix() {
        return this.mAdjacencyMatrix;
    }

    /**
     * Returns the adjacency matrix of the Maximum spanning tree
     *
     * @return Adjacency matrix of the Maximum spanning tree
     */
    public double[][] getMSTAdjacencyMatrix() {
        return this.mMSTAdjacencyMatrix;
    }

    /**
     * Initializes Adjacency Matrix of Maximum Spanning tree
     */
    public void initializeMSTAdjacencyMatrix() {
        mMSTAdjacencyMatrix = new double[mVertices + 1][mVertices + 1];
        for (int vertex = 0; vertex < mVertices; vertex++) {
            Arrays.fill(mMSTAdjacencyMatrix[vertex], -1);
        }
    }

    /**
     * Constructs maximal spanning tree from the attribute graph
     */
    public void getMaximalSpanningTree() {

        initializeMSTAdjacencyMatrix();

        ArrayList<Integer> used = new ArrayList<Integer>();
        used.add(0);
        for (int edgeCount = 0; edgeCount < mVertices - 1; edgeCount++) {
            double max = -1;
            int bestfrom = -1;
            int bestTo = -1;
            //finding the best edge as per Prim's Algorithm.
            for (int i = 0; i < used.size(); i++) {
                //i refers to used features
                for (int j = 0; j < mVertices; j++) {
                    // j refers to unused features, hence the following condition
                    if (used.contains(j))
                        continue;
                    if (Double.compare(mAdjacencyMatrix[used.get(i)][j], max) == 1) {
                        max = mAdjacencyMatrix[used.get(i)][j];
                        bestfrom = used.get(i);
                        bestTo = j;
                    }
                }
            }
            used.add(bestTo);
            //adding the links in the graph of the TAN.
            //featureNodeList.get(bestTo).addParent(bestfrom);
            //System.out.println(bestfrom + "-->" + bestTo + "  :  " + mAdjacencyMatrix[bestfrom][bestTo]);
            mMSTAdjacencyMatrix[bestfrom][bestTo] = mAdjacencyMatrix[bestfrom][bestTo];
        }
    }


    /**
     * Adds an extra node to the graph and connects it to all nodes in MST
     */
    public void addNodeFromClass() {
        for (int i = 0; i < mVertices; i++) {
            mMSTAdjacencyMatrix[mVertices][i] = 1.0;
        }
    }

    /**
     * Gets parent attributes of given attribute
     *
     * @param j Attribute for which parent attribute is to be found
     * @return List of parent attributes of given attribute
     */
    public ArrayList<Integer> getParentAttribute(int j) {
        ArrayList<Integer> parentList = new ArrayList<>();

        for (int i = 0; i < mVertices + 1; i++) {
            if (mMSTAdjacencyMatrix[i][j] != -1) {
                parentList.add(i);
            }
        }
        return parentList;
    }
}
