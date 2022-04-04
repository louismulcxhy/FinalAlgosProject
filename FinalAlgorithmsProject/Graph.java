import java.util.ArrayList;



public class Graph {
	//Graph Class - implements weighted and directed graph using arraylists and adjacency list
	
	//asjacency list
	 ArrayList<ArrayList<Edge>> adjList = new ArrayList<ArrayList<Edge>>(); //list of lists of edges

     Graph(int edges) { // constructor
         for (int i = 0; i < edges; i++) {
             adjList.add(new ArrayList<Edge>());
         }
     }

     ArrayList<Edge> getNode(int node) { //returns specified node or vertex
         return adjList.get(node);
     }

     void addEdge(int from, int to, int weight) {
         adjList.get(from).add(new Edge(from, to, weight));
     }
}
