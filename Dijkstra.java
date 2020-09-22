// Cooper Urich
// COP 3503C
// Matthew Gerber
// Submitted July 13, 2020

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintWriter;
import java.nio.file.Paths;

class Dijkstra{  
    static class DijkstraGraph{ 
        int numVertices;
        ArrayList<ArrayList<Edge>> list;
        int[] distance;
        boolean[] visited;
        int[] parent;
        // Call this method to build the Graph using ArrayLists (Adjacency List)
        DijkstraGraph(int numVertices){
            this.numVertices = numVertices;
            list = new ArrayList<>(numVertices);
            // making enough room in the list for the amout of elements in the graph
            // size is 1 greater to avoid subtracting one from the values to access a certain index
            for (int i = 0; i < (numVertices + 1); i++){
                // println("Adding Element : " + i + " into the graph");
                list.add(new ArrayList<>());
            }
        } 
        // Creates a new edge between two nodes with a weight between the two
        public void addNewEdge(int a, int b, int cost){
            // Hook the edge in on both nodes
            Edge aEdge = new Edge(a, b, cost);
            Edge bEdge = new Edge(b, a, cost);
            // Adds each end to the oppposite list
            list.get(a).add(aEdge);
            list.get(b).add(bEdge);
        }
        
        // receives the sourceVertex to start the algorithm
        public void dijkstrasAlgorithm(int sourceVertex){
            int i = 0, size = 0, visitedCount = 0, min = 0, next = -1, count =0;
            int length, index, num = 0;
            ArrayList<Integer> notMarked = new ArrayList<>();
            distance = new int[numVertices + 1];
            visited = new boolean[numVertices + 1];
            parent = new int[numVertices + 1];
            
            // Set all Vertices to unvisited, set 0 to true to avoid funky errors in logic
            visited[0] = true;
            for (i = 1; i < (visited.length); i++){
                visited[i] = false;
            }
            // Set all distances to infinity (or in this case -1)
            distance[0] = 0;
            for (i = 1; i < (distance.length); i++){
                distance[i] = -1;
            }
            // Parents are all set to undefined (or -1 in this case)
            parent[0] = 0;
            for (i = 1; i < (parent.length); i++){
                parent[i] = -1;
            }
    
          // Do this for the amount of vertices there are in the graph
            while (visitedCount <= numVertices){
                // the amount of neighbor nodes this vertex has
                size = list.get(sourceVertex).size();
                // cycle through each neighbor (in numeric order)
                for (i = 0; i < size; i++){
                    index = list.get(sourceVertex).get(i).getB();
                    length = list.get(sourceVertex).get(i).getCost();
                    // if it is the first SourceNode of the entire graph
                    if (distance[sourceVertex] == -1){
                        // only if this node has not been visited or been compared to
                        if (distance[index] == -1 && visited[index] == false){
                            distance[index] = length;
                            parent[index] = sourceVertex;
                        }
                    }
                    // for every other vertex besides the source vertex
                    if (distance[sourceVertex] != -1 && visited[index] == false){
                        // Make the cost of that vertex the cost from that node to the source node
                        if (distance[index] == -1 && visited[index] == false){
                            distance[index] = length + distance[sourceVertex];
                            parent[index] = sourceVertex;
                        }
                        if (distance[index] != -1 && visited[index] == false){
                            // if the distance from the current source to the original source is 
                            // longer than going through the neighbor... 
                            if (distance[sourceVertex] > (distance[index] + length)){
                                //  make the new distance go through the neighbor node
                                distance[sourceVertex] = (distance[index] + length);
                                // make that node its parent
                                parent[sourceVertex] = index;
                            }
                            // if the distance from the current index to the original source is 
                            // longer than the neighbors current distance plus the distance to the neighbor...
                            if (distance[index] > (distance[sourceVertex] + length)){
                                // the node will not go throguh the sourceVertex
                                distance[index] = (distance[sourceVertex] + length);
                                // make that node its parent
                                parent[index] = sourceVertex;
                            }
                        }
                    }
                    // if the node has already been visited, but going through the source node is shorter
                    if (visited[index] == true){
                        if (distance[sourceVertex] + length < distance[index]){
                            // make the sourceVertex the parent of the index
                            distance[index] = distance[sourceVertex] + length;
                            parent[index] = sourceVertex;
                        }
                    }
                    // for if/ else statement, find the shortest path to next neighborNode
                    if (min == 0 && visited[index] == false){
                        min = length;
                        next = index;
                    }
                    else{
                        if (length < min && visited[index] == false){
                            min = length;
                            next = index;
                        }
                    }
                }
                // Marking Source as visited
                visited[sourceVertex] = true;
                visitedCount++;
                // Moving SourceVertex to shortest neighbor
                sourceVertex = next;
                min = 0;
            }
        }
        // Prints the graph in adjacency list format
        public void printGraph(int numVertices){
            for (int i = 1; i < numVertices + 1; i++){
                print(i + ": ");
                for (int j = 0; j < list.get(i).size(); j++){
                    print(list.get(i).get(j).getB() + " ");
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
        File text = new File("cop3503-asn2-input.txt");
        Scanner scnr = new Scanner(text);
        DijkstraGraph graph;
        PrintWriter pw;

        // Scan in the number of edges, source Vertex, and Number of edges
        numVertices = scnr.nextInt();
        buffer = scnr.nextLine();
        sourceVertex = scnr.nextInt();
        buffer = scnr.nextLine();
        numEdges = scnr.nextInt();
        buffer = scnr.nextLine();

        graph = new DijkstraGraph(numVertices);

        // Entering in all of the edges with weights/cost
        for (int i = 0; i < numEdges; i++){
            a = scnr.nextInt();
            b = scnr.nextInt();
            weight = scnr.nextInt();
            
            // Just for reading in comments, works even if there isnt comments
            if (i < numEdges - 1)
                buffer = scnr.nextLine();

            graph.addNewEdge(a, b, weight);
            // println("" + a + " " + b + " " + weight);
        }
        // Call Dijkstra's Algorithm        
        graph.dijkstrasAlgorithm(sourceVertex);

        try{
            File asn = new File("cop3503-asn2-out-urich-cooper.txt");
            pw = new PrintWriter(asn);

            // Printing results to the file
            pw.printf("%d\n", numVertices);
            for (int i = 1; i < graph.visited.length; i++){
                pw.printf("%d %d %d\n", i , graph.distance[i], graph.parent[i]);
            }
            pw.close();

        } catch (Exception e){
            e.printStackTrace();
            return;
        }
    }
    public static void println(String s){
        System.out.println(s);
    }
    public static void print(String s){
        System.out.print(s);
    }

}
