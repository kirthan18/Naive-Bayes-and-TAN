package com.kirthanaa.nb.Entities;

import java.util.Arrays;

/**
 * Created by kirthanaaraghuraman on 11/13/15.
 */
public class NBGraph {

    private int mEdges;

    private int mVertices;

    private double[][] mAdjacencyMatrix;

    public NBGraph(int numVertex){
        if(numVertex < 0){
            throw new IllegalArgumentException("Number of vertices cannot be less than 0.");
        }
        this.mVertices = 0;
        this.mEdges = (numVertex * (numVertex - 1))/2;
        addEdges();
    }

    private void addEdges(){
        mAdjacencyMatrix = new double[mVertices][mVertices];
        for(int vertex = 0; vertex < mVertices; vertex++){
            Arrays.fill(mAdjacencyMatrix[vertex], 1);
        }
    }

    public int getEdges(){
        return mEdges;
    }

    public int getVertices(){
        return mVertices;
    }

    private void validateVertex(int v){
        if(v < 0 || v > mVertices){
            throw new IndexOutOfBoundsException("Vertex " + v + " must be between 0 and " + (mVertices - 1));
        }
    }

    public void addEdgeWeight(double weight, int srcVertex, int destVertex){
        validateVertex(srcVertex);
        validateVertex(destVertex);
        mAdjacencyMatrix[srcVertex][destVertex] = weight;
    }



}
