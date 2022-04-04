import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class ShortestPath{
	int numberOfEdges = 0;
    ArrayList<Stop> stopList;
    Graph systemGraph;
    
    double[] dists;

	public ShortestPath(File transfersFile, File stopTimesFile, File stopsFile) throws FileNotFoundException {
        
        stopList = new ArrayList<Stop>();
        
        
        Scanner stopsScanner = new Scanner(stopsFile);
        stopsScanner.nextLine();

        String stop;
        String[] stopInfo;
        int stopId;

        while (stopsScanner.hasNextLine()) 
        {
            stop = stopsScanner.nextLine();
            stopInfo = stop.split(",");
            stopId = Integer.parseInt(stopInfo[0]);
            stopList.add(new Stop(stopId, stopInfo[2]));
            numberOfEdges = Math.max(stopId, numberOfEdges);
        }
        numberOfEdges++;
        
        
        

        Scanner transferScanner = new Scanner(transfersFile);
        transferScanner.nextLine();

        systemGraph = new Graph(numberOfEdges);

        String transfer = "";
        String[] transferValues;

        while (transferScanner.hasNextLine()) {
            transfer = transferScanner.nextLine();
            transferValues = transfer.split(",");

            int from = Integer.parseInt(transferValues[0]);
            int to = Integer.parseInt(transferValues[1]);
            int type = Integer.parseInt(transferValues[2]);
            int cost;
            if (type == 0) {
            	cost = 2;
            }
            else {
            	cost = Integer.parseInt(transferValues[3]) / 100;
            }

            systemGraph.addEdge(from, to, cost);
        }
        

        Scanner stopTimeScanner = new Scanner(stopTimesFile);
        stopTimeScanner.nextLine();

        String trip = "";
        String[] tripValues;
        int from = 1;
        int to = 0;
        int stopSequenceValue = 0;
        boolean skip = false;

        while (stopTimeScanner.hasNextLine()) {
            trip = stopTimeScanner.nextLine();
            tripValues = trip.split(",");
            stopSequenceValue = Integer.parseInt(tripValues[4]);

            skip = false;

            if (stopSequenceValue != 1) {
                to = Integer.parseInt(tripValues[3]);
                for (Edge thisEdge : systemGraph.getNode(from)) {
                    if (thisEdge.to == to) {
                        skip = true;
                        break;
                    }
                }
                if (!skip) {
                    systemGraph.addEdge(from, to, 1);
                }
            }

            from = Integer.parseInt(tripValues[3]);
        }
        stopTimeScanner.close();
        stopsScanner.close();
        transferScanner.close();
    }
	public Edge[] runDijkstra(int startNode) {
        double[] distTo = new double[numberOfEdges];
        boolean[] relaxed = new boolean[numberOfEdges];
        Edge[] edgeTo = new Edge[numberOfEdges];

        for (Stop stop : stopList) {
            distTo[stop.getId()] = Double.POSITIVE_INFINITY;
            relaxed[stop.getId()] = false;
            edgeTo[stop.getId()] = null;
        }
        distTo[startNode] = 0;

        int currentNode = startNode;

        for (int i = 0; i < stopList.size(); i++) {
            relax(distTo, edgeTo, currentNode);
            relaxed[currentNode] = true;
            double min = Double.POSITIVE_INFINITY;
            for (Stop stop : stopList) {
                if (distTo[stop.getId()] < min && !relaxed[stop.getId()]) {
                    min = distTo[stop.getId()];
                    currentNode = stop.getId();
                }
            }
        }
        dists = distTo;
        return edgeTo;
    }
	  public void relax(double[] distTo, Edge[] edgeTo, int v) {
	        for (Edge e : systemGraph.getNode(v)) {
	            int w = e.to;
	            if (distTo[w] > distTo[v] + e.weight) {
	                distTo[w] = distTo[v] + e.weight;
	                edgeTo[w] = e;
	            }
	        }
	    }
	  public String getStopSequence(int start, int end) throws FileNotFoundException {
	        

	        if (getStopById(start) == null) {
	            return "Departure stop doen't exist";
	        }
	        if (getStopById(end) == null) {
	            return "Destination stop doesn't exist";
	        }

	        Edge[] edgeTo = runDijkstra(start);

	        ArrayList<String> stopSequence = new ArrayList<String>();

	        int currentStop = end;

	        stopSequence.add(getStopById(end));
	        while (currentStop != start){
	            try {
	                currentStop = edgeTo[currentStop].from;
	            } catch (NullPointerException e) {
	                return "Path does not exist!";
	            }
	            stopSequence.add(getStopById(currentStop));
	        } 
	        Collections.reverse(stopSequence);

	        stopSequence.add(0, dists[end] + "");

	        String returnString = "";
	        for (String string : stopSequence) {
	            returnString += string + "\n";
	        }
	        return returnString;
	    }

	    public String getStopById(int id) {
	        for (Stop s : stopList) {
	            if (s.getId() == id) {
	                return s.getName();
	            }
	        }
	        return "Specified stop doesn't exist!";
	    }
}
