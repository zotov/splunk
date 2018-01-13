package com.soft.industry;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.util.*;
/**
 * 
 * @author Home
 *
 */
public class GraphMain {

	public static void main(String[] args) throws Throwable {
		
		 Graph g = new Graph(4);
		 //g.traversBFSGraph(g);
		 g.traversDFSGraph(g); 
	       		
	}	
}

class Graph {
	
    private int V;   // No. of vertices
    private LinkedList<Integer> adj[]; //Adjacency Lists
 
    // Constructor
    Graph(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i)
            adj[i] = new LinkedList();
    }
 
    public void addEdge( final int v1, final int v2){
    	   adj[v1].add(v2);   
    }
    
    public void BFS(int startV) {
    	System.out.println(startV + " ####");
    	boolean[] visited= new boolean[V];
    	Arrays.fill(visited, false);
    	
    	visited[startV]=true;
    	
    	Queue<Integer>queue=new LinkedTransferQueue();
    	queue.add(startV);
    	int parentNode;
    	while(queue.size() > 0) {
    		parentNode = queue.poll();
    		System.out.print(parentNode + " ");
    		
    		Iterator<Integer>iterator=adj[parentNode].iterator();
    		while(iterator.hasNext()) {
    			
    			int adjacentNode=iterator.next();
    			
    			if(!visited[adjacentNode]) {
    			   visited[adjacentNode]=true;
    			   queue.add(adjacentNode);
    			}
    		}
    	}
    	
    	System.out.println("\n*****\n");    	
    }
    
    
    public void DFS(int startV) {
    	System.out.println(startV + " ####");
    	boolean[] visited= new boolean[V];
    	Arrays.fill(visited, false);
    	    	
    	DFSdeep(startV, visited);
    	    	
    	System.out.println("\n*****\n");    	
    }
    
    
    public void DFSdeep(int parentNode, final boolean[] visited) {
    	System.out.print(parentNode + " ");
    	visited[parentNode]=true;
    	Iterator<Integer>iterator=adj[parentNode].iterator();
		while(iterator.hasNext()) {
			int childNode=iterator.next();			
			if(!visited[childNode]) {
				DFSdeep(childNode, visited);		   
			}
		}
    	
    }
    
    public void traversBFSGraph(final Graph g) {
    	
    	    g.addEdge(0, 1);
	        g.addEdge(0, 2);
	        g.addEdge(1, 2);
	        g.addEdge(2, 0);
	        g.addEdge(2, 3);
	        g.addEdge(3, 3);
	 
	        System.out.println("Following is Breadth First Traversal "+
	                           "(starting from vertex 2)");
	 
	        g.BFS(0);
	        g.BFS(1);
	        g.BFS(2);
	        g.BFS(3);
    }
    
    
    public void traversDFSGraph(final Graph g) {
    	
	    g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 3);
 
        System.out.println("Following is Depth First Traversal "+
                           "(starting from vertex 2)");
 
        g.DFS(0);
        g.DFS(1);
        g.DFS(2);
        g.DFS(3);
}

}

