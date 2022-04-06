import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


public class SearchTrips < Key extends Comparable < Key > , Value > {

    // public variable for class - hash map of the trips, arrival times and more info
    public HashMap < String, ArrayList < Trip >> arrivalTimeMap = new HashMap < String, ArrayList < Trip >> ();

    //function to sort list of trips with specified arrival time
    public ArrayList < Trip > getSortedTrips(String arrivalTime) {

        ArrayList < Trip > sortedTrips = this.arrivalTimeMap.get(arrivalTime);

        Collections.sort(sortedTrips, (trip1, trip2) -> trip1.tripID.compareTo(trip2.tripID));

        return sortedTrips;

    }



    // takes in input arrival time and searches for it in hash map, gets string of trips and prints them to interface
    public static void search(String arrivalTime, File stopTimesFile) {

    	//checks for valid input formatting
        if (!SearchTrips.timeCheck(arrivalTime)) {
            System.out.println("Error: Invalid input format , Please use the format HH:MM:SS, thank you");
            return;
        }

        //creates SearchTrips object
        SearchTrips < String, String > c = new SearchTrips < String, String > ();
        //creates hashMap of all of the trips and their details - assigns to arrivalTimeMap public variable
        createHashMap(stopTimesFile, c);
        try {
        	//creates string with list of relevant trips
            String result = createTripListString(arrivalTime, c);
            //checks for null 
            if (result == null) {
                System.out.println("There are no trips that match this criteria. ");
                return;
            } else {
    		//prints output to user
                System.out.println("Arrival Time " + arrivalTime + "\n" + result);
                return;
            }
        } catch (Exception e) {
            System.out.println("No trips were found that match this criteria.");
            return;

        }

    }
    //checks if input arrival time is formatted correctly and a valid time
    private static Boolean timeCheck(String time) {
    	//splits time input into hours mins and seconds
    	try {
	        String[] timeCharArray = time.split(":");
	        if (timeCharArray.length != 3) {
	            return false;
	        }
	        //checks for valid hour
	        int hr = Integer.parseInt(timeCharArray[0]);
	        if (hr > 24 || hr < 0) {
	            return false;
	        }
	        //checks for valid minute
	        int min = Integer.parseInt(timeCharArray[1]);
	        if (min < 0 || min > 60) {
	            return false;
	        }
	        //checks for valid second
	        int sec = Integer.parseInt(timeCharArray[2]);
	        if (sec < 0 || sec > 60) {
	            return false;
	        }
	        return true;
    	} catch (NumberFormatException e) {
    		//catches error due to non number being entered
            return false;
    	}


    }
    // creates string with sorted list of relevant trips
    public static String createTripListString(String arrivalTime, SearchTrips < String, String > c) {
    	//sorts trips
        ArrayList < Trip > sortedTrips = c.getSortedTrips(arrivalTime);
        String results = "";
        //formats string and info correctly with details
        for (int i = 0; i < sortedTrips.size(); i++) {
            Trip trip = sortedTrips.get(i);
            results += sortedTrips.get(i).tripID +
                " departure Time: " + trip.departureTime +
                " Stop Id: " + trip.stopID +
                " Stop Sequence: " + trip.stopSequence +
                " Stop Headsign: " + trip.stopHeadSign +
                " Pick Up type: " + trip.pickUpTypes +
                " Drop Off Type: " + trip.dropOffType +
                " Distance Travelled: " + trip.distanceTravelled + "\n";

        }
        return results;

    }
    //creates hash map of trips and details from input file
    public static void createHashMap(File fileToRead, SearchTrips < String, String > st) {
        try {
        	//reads file in
            Scanner fileReader = new Scanner(new FileInputStream(fileToRead));

            int line = 0;
            //loops through lines of file
            while (fileReader.hasNextLine()) {
                if (line == 0) {
                    fileReader.nextLine();
                    line++;
                }
                // breaks each line into string array split by commas - separates into trip ID, time etc
                String[] lineData = fileReader.nextLine().trim().split(",");
                line++;
                //formats all times in HH:MM:SS format
                String arrivalTime = lineData[1].replace(" ", "0");
                //assigns distance travelled if applicable
                String distanceTravelled = "";
                if (lineData.length > 8) {
                    distanceTravelled = lineData[8];
                }
                
                //initialises new trib object with details
                Trip newTrip = new Trip(lineData[0], lineData[2], lineData[3], lineData[4], lineData[5], lineData[6], lineData[7], distanceTravelled);
                // checks if key is already present and if not adds to hash map
                if (!st.arrivalTimeMap.containsKey(arrivalTime)) {
                    st.arrivalTimeMap.put(arrivalTime, new ArrayList < > ());

                }
                // adds trip to hashmap by key of arrival time
                st.arrivalTimeMap.get(arrivalTime).add(newTrip);

            }

            fileReader.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}