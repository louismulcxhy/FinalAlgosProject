import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
        int sequenceValue = 0;
        boolean skip = false;

        while (stopTimeScanner.hasNextLine()) {
            trip = stopTimeScanner.nextLine();
            tripValues = trip.split(",");
            sequenceValue = Integer.parseInt(tripValues[4]);

            skip = false;

            if (sequenceValue != 1) {
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
}
