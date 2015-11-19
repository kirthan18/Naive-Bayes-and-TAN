package com.kirthanaa.nb.Entities;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kirthanaaraghuraman on 11/13/15.
 */
public class NBGraph {

    private int mEdges;

    private int mVertices;

    private double[][] mAdjacencyMatrix;

    private double[][] mMSTAdjacencyMatrix;

    public NBGraph(int numVertex){
        if(numVertex < 0){
            throw new IllegalArgumentException("Number of vertices cannot be less than 0.");
        }
        this.mVertices = numVertex;
        this.mEdges = (numVertex * (numVertex - 1))/2;
        addEdges();
    }

    private void addEdges(){
        mAdjacencyMatrix = new double[mVertices][mVertices];
        for(int vertex = 0; vertex < mVertices; vertex++){
            Arrays.fill(mAdjacencyMatrix[vertex], -1);
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

    public double[][] getAdjacencyMatrix(){
        return this.mAdjacencyMatrix;
    }

    public double[][] getMSTAdjacencyMatrix(){
        return this.mMSTAdjacencyMatrix;
    }

    public void initializeMSTAdjacencyMatrix(){
        mMSTAdjacencyMatrix = new double[mVertices+1][mVertices+1];
        for(int vertex = 0; vertex < mVertices; vertex++){
            Arrays.fill(mMSTAdjacencyMatrix[vertex], -1);
        }
    }

    public void getMaximalSpanningTree(){

        initializeMSTAdjacencyMatrix();

        /*boolean[] visited = new boolean[mVertices];
        Arrays.fill(visited, Boolean.FALSE);

        int initialVertex = 0;
        visited[initialVertex] = Boolean.TRUE;

        for(int i = 0; i < mVertices; i++){
            double max = -1;
            int bestFrom = -1;
            int bestTo = -1;

            for(int j = 0; j < visited.length; j++){
                for(int k = 0; k < visited.length; k++){
                    if(visited[j] == Boolean.TRUE && visited[k] == Boolean.FALSE){

                    }
                }
            }
        }*/

        ArrayList<Integer> used = new ArrayList<Integer>();
        used.add(0);
        for(int edgeCount = 0; edgeCount< mVertices - 1;edgeCount++){
            double max = -1;
            int bestfrom = -1;
            int bestTo = -1;
            //finding the best edge as per Prim's Algorithm.
            for(int i = 0; i<used.size(); i++){
                //i refers to used features
                for(int j=0;  j < mVertices; j++){
                    // j refers to unused features, hence the following condition
                    if(used.contains(j))
                        continue;
                    if(Double.compare(mAdjacencyMatrix[used.get(i)][j], max)==1){
                        max  = mAdjacencyMatrix[used.get(i)][j];
                        bestfrom = used.get(i);
                        bestTo=j;
                    }
                }
            }
            used.add(bestTo);
            //adding the links in the graph of the TAN.
            //featureNodeList.get(bestTo).addParent(bestfrom);
            System.out.println(bestfrom + "-->" + bestTo + "  :  " + mAdjacencyMatrix[bestfrom][bestTo]);
            mMSTAdjacencyMatrix[bestfrom][bestTo] = mAdjacencyMatrix[bestfrom][bestTo];
        }
    }


    public void addNodeFromClass() {
        for(int i = 0; i < mVertices; i++){
            mMSTAdjacencyMatrix[mVertices][i] = 1.0;
        }
    }
}
