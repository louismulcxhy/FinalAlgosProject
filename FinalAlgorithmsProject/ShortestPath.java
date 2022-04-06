import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class ShortestPath {
    //class variables initialised - fairly self explanatory
    int numberOfEdges = 0;
    ArrayList < Stop > stopList; //list of stops
    Graph systemGraph; // graph object to store graph of system needed for dijkstra

    double[] dists; // distances between stops/nodes
    File transfersFile, stopTimesFile; //store input files class wide
    
    //object constructor - reads in files, gets stop list and number of edges, sets up edges of graph
    public ShortestPath(File transfersFile, File stopTimesFile, File stopsFile) throws FileNotFoundException {

        stopList = new ArrayList < Stop > ();
        this.transfersFile = transfersFile;
        this.stopTimesFile = stopTimesFile;


        Scanner stopsScanner = new Scanner(stopsFile);
        stopsScanner.nextLine();

        String stop;
        String[] stopInfo;
        int stopId;

        while (stopsScanner.hasNextLine()) {
            stop = stopsScanner.nextLine();
            stopInfo = stop.split(",");
            stopId = Integer.parseInt(stopInfo[0]);
            stopList.add(new Stop(stopId, stopInfo[2]));
            numberOfEdges = Math.max(stopId, numberOfEdges);
        }
        numberOfEdges++;
        stopsScanner.close();
        setupEdges();




    }
    // sets up edges of graph/adjacency list using info from stop times file and transfer file
    public void setupEdges() throws FileNotFoundException {
    	//reads in transfers file
        Scanner transferScanner = new Scanner(transfersFile);
        transferScanner.nextLine();
        // new graph object with correct number of edges
        systemGraph = new Graph(numberOfEdges);

        String transfer = "";
        String[] transferValues;
        //reads in transfer data - to from type and cost
        while (transferScanner.hasNextLine()) {
            transfer = transferScanner.nextLine();
            transferValues = transfer.split(",");

            int from = Integer.parseInt(transferValues[0]);
            int to = Integer.parseInt(transferValues[1]);
            int type = Integer.parseInt(transferValues[2]);
            int cost;
            if (type == 0) {
                cost = 2;
            } else {
                cost = Integer.parseInt(transferValues[3]) / 100;
            }
            //adds info to graph
            systemGraph.addEdge(from, to, cost);
        }

        //reads in stop times file
        Scanner stopTimeScanner = new Scanner(stopTimesFile);
        stopTimeScanner.nextLine();

        String trip = "";
        String[] tripValues;
        //inititalises necessary variables
        int from = 1;
        int to = 0;
        int stopSequenceValue = 0;
        boolean skip = false;
        //loop reading in values
        while (stopTimeScanner.hasNextLine()) {
            trip = stopTimeScanner.nextLine();
            tripValues = trip.split(",");
            stopSequenceValue = Integer.parseInt(tripValues[4]);

            skip = false;
            
            if (stopSequenceValue != 1) { //checks if first stop in trip sequence
                to = Integer.parseInt(tripValues[3]); //otherwise sets to value
                for (Edge thisEdge: systemGraph.getNode(from)) {
                    if (thisEdge.to == to) {
                        skip = true;
                        break;
                    }
                }
                if (!skip) {
                    systemGraph.addEdge(from, to, 1); // adds relevant edges to graph, skips non applicable edges
                }
            }
            //sets from for next loop
            from = Integer.parseInt(tripValues[3]);
        }
        stopTimeScanner.close();

        transferScanner.close();
    }
    //runs dijkstra search
    public Edge[] runDijkstra(int startNode) {
        double[] distTo = new double[numberOfEdges];
        boolean[] relaxed = new boolean[numberOfEdges];
        Edge[] edgeTo = new Edge[numberOfEdges];

        for (Stop stop: stopList) {
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
            for (Stop stop: stopList) {
                if (distTo[stop.getId()] < min && !relaxed[stop.getId()]) {
                    min = distTo[stop.getId()];
                    currentNode = stop.getId();
                }
            }
        }
        dists = distTo;
        return edgeTo;
    }
    //relax function for dijkstra
    public void relax(double[] distTo, Edge[] edgeTo, int v) {
    	//relaxes nodes up to v
        for (Edge e: systemGraph.getNode(v)) {
            int w = e.to;
            if (distTo[w] > distTo[v] + e.weight) {
                distTo[w] = distTo[v] + e.weight;
                edgeTo[w] = e;
            }
        }
    }
    
    // returns sequence of stops dijkstra has calculated to be shortest path
    public String getStopSequence(int start, int destination) throws FileNotFoundException {

    	//checks stops exist
        if (getStopById(start) == null) {
            return "Departure stop doen't exist";
        }
        if (getStopById(destination) == null) {
            return "Destination stop doesn't exist";
        }
        //runs dijkstra for start node as origin
        Edge[] edgeTo = runDijkstra(start);
        
        ArrayList < String > stopSequence = new ArrayList < String > ();
        //starts from destination stop
        int currentStop = destination;
        //adds to stop sequence
        stopSequence.add(getStopById(destination));
        //adds all other stops to sequence in reverse order
        while (currentStop != start) {
            try {
                currentStop = edgeTo[currentStop].from;
            } catch (NullPointerException e) {
                return "Path does not exist!";
            }
            stopSequence.add(getStopById(currentStop));
        }
        //reverses sequence
        Collections.reverse(stopSequence);
        //adds distance to start of sequence to be printed as part of output to user
        stopSequence.add(0, dists[destination] + "");
        //turns array into string to be returned
        String returnString = "";
        for (String string: stopSequence) {
            returnString += string + "\n";
        }
        return returnString;
    }
    //fets stop name given stop id
    public String getStopById(int id) {
        for (Stop s: stopList) {
            if (s.getId() == id) {
                return s.getName();
            }
        }
        return "Specified stop doesn't exist!";
    }
}
