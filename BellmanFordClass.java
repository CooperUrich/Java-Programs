import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintWriter;
import java.nio.file.Paths;


class BellmanFordClass{  
    static class BellmanFord{ 
        int numVertices;
        ArrayList<ArrayList<Edge>> list;
        int[] distance;
        boolean[] visited;
        int[] parent;
        // Call this method to build the Graph using ArrayLists (Adjacency List)
        BellmanFordGraph(int numVertices){
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
        public void BellmanFordAlgorithm( int sourceVertex){
            int i = 0, size = 0, visitedCount = 0, min = 0, next = -1, count =0;
            int length, index, num = 0;
            ArrayList<Integer> notMarked = new ArrayList<>();
            distance = new int[numVertices + 1];
            visited = new boolean[numVertices + 1];
            parent = new int[numVertices + 1];
            
            
            
            
            // Set all distances to infinity (or in this case -1)
            distance[0] = 0;
            for (i = 1; i < (distance.length); i++){
                distance[i] = Integer.MAX_VALUE;
            }
            distance[sourceVertex] = 0;
            // Parents are all set to undefined (or -1 in this case)
            parent[0] = 0;
            for (i = 1; i < (parent.length); i++){
                parent[i] = -1;
            }
            visited[0] = true;
            for (i = 1; i < (visited.length); i++){
                visited[i] = false;
            }

            

            while (visitedCount <= numVertices){
                // the amount of neighbor nodes this vertex has
                size = list.get(sourceVertex).size();
                // cycle through each neighbor (in numeric order)
                for (i = 0; i < size; i++){
                    index = list.get(sourceVertex).get(i).getB();
                    length = list.get(sourceVertex).get(i).getCost();
                            
                            if (distance[index] > (distance[sourceVertex] + length)){
                                // the node will not go throguh the sourceVertex
                                distance[index] = (distance[sourceVertex] + length);
                                // make that node its parent
                                parent[index] = sourceVertex;
                            }
                    }
                    visited[sourceVertex] = true;
                    visitedCount++;
                    if (sourceVertex + 1 >= numVertices && visited[(sourceVertex + 1)] == false){
                        sourceVertex = 1;
                    }
                    if (sourceVertex + 1){
                    sourceVertex = next;
                    }
                }
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
        
        numVertices = scnr.nextInt();
        buffer = scnr.nextLine();
        sourceVertex = scnr.nextInt();
        buffer = scnr.nextLine();
        numEdges = scnr.nextInt();
        buffer = scnr.nextLine();

        graph = new DijkstraGraph(numVertices);
        graph1 = new BellmanFordGraph(numVertices);

        for (int i = 0; i < numEdges; i++){
            a = scnr.nextInt();
            b = scnr.nextInt();
            weight = scnr.nextInt();
            
            
            if (i < numEdges - 1)
                buffer = scnr.nextLine();

            graph1.addNewEdge(a, b, weight);
        }

        graph1.BellmanFordAlgorithm(sourceVertex);

            println(numVertices + "");
            for (int i = 1; i < graph1.parent.length; i++){
                println( i + " " + graph.distance[i] + " " + graph.parent[i]);
            }

    }
    public static void println(String s){
        System.out.println(s);
    }
    public static void print(String s){
        System.out.print(s);
    }

}
