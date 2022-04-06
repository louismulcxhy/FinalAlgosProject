import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


public class SearchTrips <Key extends Comparable <Key>,Value>  {
	
	
	public HashMap<String,ArrayList<Trip>> arrivalTimeMap = new HashMap<String,ArrayList<Trip>>();


    public  ArrayList<Trip> getSortedTrips(String arrivalTime) {

        ArrayList<Trip> sortedTrips = this.arrivalTimeMap.get(arrivalTime);

        Collections.sort(sortedTrips, (trip1, trip2) -> trip1.tripId.compareTo(trip2.tripId));

        return sortedTrips;

    }
	
	
	
	
	public static void search(String searchTerm, File stopTimesFile) {
		
		
		if(!SearchTrips.timeCheck(searchTerm)){
			System.out.println("Error: Invalid fileReader format , Please use the format HH:MM:SS, thank you");
			return ;
		}
		
		
		SearchTrips<String,String> c = new SearchTrips<String,String>();
        createHashMap(stopTimesFile,c);
    try {
            String result = createTripListString(searchTerm, c);

            if (result == null) {
                System.out.println("There are no items that match your criteria.");
                return ;
            } else {
            	
            	System.out.println("Arrival Time " + searchTerm + "\n" + result);
            	return;
            }
        }
        catch(Exception e){
        	System.out.println("Error: Invalid fileReader format , Please use the format HH:MM:SS, thank you");
        return ;

        }

}
    private static Boolean timeCheck(String time){
        String[] timeCharArray = time.split(":");
        if(timeCharArray.length != 3){
            return false;
        }
        int hr = Integer.parseInt(timeCharArray[0]);
        if(hr>24 || hr<0){
            return false;
        }
        int min = Integer.parseInt(timeCharArray[1]);
        if(min < 0 || min > 60){
            return false;
        }
        int sec = Integer.parseInt(timeCharArray[2]);
        if(sec < 0 || sec >60){
            return false;
        }
        return true;
        

    }
    
    public static String createTripListString(String arrivalTime,SearchTrips<String,String> c){

        ArrayList<Trip> sortedTrips = c.getSortedTrips(arrivalTime);
        String results = "";

        for (int i = 0; i < sortedTrips.size(); i++){
            Trip trip = sortedTrips.get(i);
            results += sortedTrips.get(i).tripId +
                    " departure Time: " +  trip.departureTime +
                    " Stop Id: "  + trip.stopId +
                    " Stop Sequence: " +  trip.stopSequence +
                    " Stop Headsign: "  + trip.stopHeadSign +
                    " Pick Up type: "  + trip.pickUpTypes +
                    " Drop Off Type: "  + trip.dropOffType  +
                    " Distance Travelled: "  +  trip.distanceTravelled + "\n";

        }
        return results;

    }
    
    public static void createHashMap(File fileToRead, SearchTrips<String, String> st) {
        try {
            Scanner fileReader = new Scanner(new FileInputStream(fileToRead));

            int line = 0;


            while (fileReader.hasNextLine()) {
                if(line == 0){
                    fileReader.nextLine();
                    line ++;
                }

                String[] lineData = fileReader.nextLine().trim().split(",");
                line ++;

                String arrivalTime = lineData[1].replace(" ","0");

                String distanceTravelled = "";
                if(lineData.length > 8)
                {
                    distanceTravelled = lineData[8];
                }


                Trip newTrip = new Trip(lineData[0],lineData[2],lineData[3],lineData[4],lineData[5],lineData[6],lineData[7],distanceTravelled);

                if(!st.arrivalTimeMap.containsKey(arrivalTime))
                {
                    st.arrivalTimeMap.put(arrivalTime,new ArrayList<>());

                }

                st.arrivalTimeMap.get(arrivalTime).add(newTrip);

            }

            fileReader.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
