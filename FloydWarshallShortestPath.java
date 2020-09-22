import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintWriter;
import java.nio.file.Paths;


class FloydWarshallShortestPath{  
    static class FloydWarshall{ 
        int numVertices;
        ArrayList<ArrayList<Edge>> edges;
        int[] distance;
        boolean[] visited;
        int[] parent;
        // Call this method to build the Graph using ArrayLists (Adjacency List)
        FloydWarshall(int numVertices){
            this.numVertices = numVertices;
            edges = new ArrayList<>(numVertices);
            // making enough room in the list for the amout of elements in the graph
            // size is 1 greater to avoid subtracting one from the values to access a certain index
            for (int i = 0; i < (numVertices + 1); i++){
                // println("Adding Element : " + i + " into the graph");
                edges.add(new ArrayList<>());
            }
        } 
        // Creates a new edge between two nodes with a weight between the two
        public void addNewEdge(int a, int b, int cost){
            // Hook the edge in on both nodes
            Edge aEdge = new Edge(a, b, cost);
            Edge bEdge = new Edge(b, a, cost);
            // Adds each end to the oppposite list
            edges.get(a).add(aEdge);
           edges.get(b).add(bEdge);
        }
        
        // receives the sourceVertex to start the algorithm
        public void FloydWarshallAlgorithm(int sourceVertex){
            int i = 0, j = 0, k = 0, size = 0, visitedCount = 1;
            int original = sourceVertex;
            int weight, index, num = 0;
            PrintWriter pw;
            int[][] arr= new int[numVertices + 1][numVertices + 1];
            
            // Set all distances to infinity 
            for (i = 0; i < numVertices + 1; i++){
                for (j = 0; j < numVertices + 1; j++){
                    arr[i][j] = Integer.MAX_VALUE / 2;
                }
            }

            // set all vertices distances to themself as 0
            for (i = 1; i < numVertices + 1; i++){
                arr[i][i] = 0;
            }

            
            // set all of the initial lengths
            for (i = 1; i < numVertices + 1; i++){
                size = edges.get(i).size();
                for (j = 0; j < size; j++){
                    index = edges.get(i).get(j).getB();
                    weight = edges.get(i).get(j).getCost();

                    arr[i][index] = weight;
                }
            }

            // do the floyd warshall algorithm
            for (k = 1; k < numVertices + 1; k++){
                for (i = 1; i < numVertices + 1; i++){
                    for (j = 1; j < numVertices + 1; j++){
                        if (arr[i][k] + arr[k][j] < arr[i][j]){
                            arr[i][j] = arr[i][k] + arr[k][j];
                        }
                    } 
                }
            }
        

            try{
                File asn = new File("cop3503-asn2-out-urich-cooper-fw.txt");
                pw = new PrintWriter(asn);

                // Printing results to the file
                
               pw.printf("%d\n", numVertices);
                for (i = 1; i < numVertices + 1; i++){
                    for (j = 1; j < numVertices + 1; j++){
                        pw.printf("%d ", arr[i][j]);
                    }
                    pw.printf("\n");
                }
            
                pw.close();

            } catch (Exception e){
                e.printStackTrace();
                return;
            }

            println("\nFloyd-Warshall");
            println("" + numVertices);
            for (i = 1; i < numVertices + 1; i++){
                for (j = 1; j < numVertices + 1; j++){
                    print(arr[i][j] + " ");
                }
                println("");
            }
            println("");

        }
    
        // Prints the graph in adjacency list format
        public void printGraph(int numVertices){
            for (int i = 1; i < numVertices + 1; i++){
                print(i + ": ");
                for (int j = 0; j < edges.get(i).size(); j++){
                    print(edges.get(i).get(j).getB() + " ");
                }
                println("");
            }
        }
    }
    // Edge Constructor
    static class Edge{
        int a, b, cost;

        public Edge(int a, int b, int cost){
            this.a = a;
            this.b = b;
            this.cost = cost;
        }

        public int getA(){
            return this.a;
        }

        public int getB(){
            return this.b;
        }

        public int getCost(){
            return this.cost;
        }
    }     
    
    public static void main(String args[]) throws FileNotFoundException {
        int numVertices, sourceVertex, numEdges;
        int a, b, weight;
        String buffer;
        String filename;
        File text;
        Scanner s = new Scanner(System.in);
        Scanner scnr;
        DijkstraGraph graph;
        BellmanFord graph1;
        PrintWriter pw;

        System.out.println("Enter file name\n");
        filename = s.nextLine();

        text = new File(filename);
        scnr = new Scanner(text);

        // Scan in the number of edges, source Vertex, and Number of edges
        numVertices = scnr.nextInt();
        buffer = scnr.nextLine();
        sourceVertex = scnr.nextInt();
        buffer = scnr.nextLine();
        numEdges = scnr.nextInt();
        buffer = scnr.nextLine();

        FloydWarshall = new FloydWarshall(numVertices);

        // Entering in all of the edges with weights/cost
        for (int i = 0; i < numEdges; i++){
            a = scnr.nextInt();
            b = scnr.nextInt();
            weight = scnr.nextInt();
            
            // Just for reading in comments, works even if there isnt comments
            if (i < numEdges - 1)
                buffer = scnr.nextLine();


            FloydWarshall.addNewEdge(a, b, weight);
            FloydWarshall.addNewEdge(a, b, weight);

            
        }
        // Call the Algorithm
        FloydWarshall.FloydWarshallAlgorithm(sourceVertex);

       
    }
    public static void println(String s){
        System.out.println(s);
    }
    public static void print(String s){
        System.out.print(s);
    }

}
