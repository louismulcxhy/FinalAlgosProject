import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;


public class BusStopSearch {


    //takes in user input as to which option to take and handles the rest

	@SuppressWarnings("resource")
	public static void takeInput(File stopsFile) {
        boolean quit = true;
        Scanner input = new Scanner(System.in);
        do { //loop taking user choice
            System.out.println("Would you like to: \n" +
                "1. Search by complete bus stop name \n" +
                "2. Search by first few characters ");
            if (!input.hasNextInt()) {
                System.out.println("Error invalid input,please enter either '1' or '2'. ");
                quit = false;
                input.next();
            }
            int choice = input.nextInt();
            //calls function to handle this choice and take further input
            handleRequest(choice, stopsFile);
        } while (!quit);
        
    }
    @SuppressWarnings("resource")
	public static void handleRequest(int option, File stopsFile) {
        // creates TST object to eventually search for input
    	TST < String > st = new TST < String > ();
		Scanner input = new Scanner(System.in);
        String key;

        CreateTST(stopsFile, st); // puts stop info into TST

        boolean quit;
        do { //loop taking stop name input from user and outputting results
            quit = true;
            if (option == 1) // for full stop name
            {
                System.out.println("Enter full bus stop name: ");
                key = input.nextLine();
                key = key.toUpperCase();

                if (st.get(key) == null) //checks for null
                {
                    System.out.println("Error: That Bus Stop Does Not Exist \n");
                    input.next();
                } else {
                    System.out.println(key + st.get(key)); //prints stop and stop details
                }

            } else if (option == 2) // for partial input - first few letters
            {
                //takes input
                System.out.println("Enter the first few characters of the bus stop name:  ");
                key = input.next();
                key = key.toUpperCase(); // makes all uppercase to make input case irrelevant
                String results = "";
                for (String s: st.keysWithPrefix(key)) { // adds stops to string 
                    results += s + st.get(s) + "\n";
                }
                if (results == "") //checks if empty
                {
                    System.out.println("No bus stops matching those first " +
                        "few characters were found. Please try again");
                    quit = false;
                } else //prints results
                {
                    System.out.println(results);
                }

            }
        } while (!quit);
        

    }
    //creates tst containing file data
    public static void CreateTST(File fileToRead, TST < String > st) {
        try {
            Scanner input = new Scanner(new FileInputStream(fileToRead));

            while (input.hasNextLine()) {
            	//takes in stop data for each line
                String[] data = input.nextLine().trim().split(",");

                String stopName = data[2];
                stopName = formatStopName(stopName); //formats stop name so searchable
                //reformats stop info with context
                String stopInfo = "// Stop ID: " + data[0] + "// Stop Code: " + data[1] +
                    "// Stop Descripton : " + data[3] + "// Stop Latitiude: " + data[4] + "// Stop Longitude: " +
                    data[5] + "// Zone ID: " + data[6];
                //puts stop name and info into tst
                st.put(stopName, stopInfo);
            }

            input.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
    //formats stop name putting letters before stop name to the end
    public static String formatStopName(String stop) {
    	// turns stop string into char array
        char[] stopCharArray = new char[stop.length()];
        for (int i = 0; i < stop.length(); i++) {
            stopCharArray[i] = stop.charAt(i);
        }
        //checks for if stop name has the 2 letters at start and if so removes them and adds to end of name
        char charToBeChecked = stopCharArray[2];
        if (Character.isWhitespace(charToBeChecked)) {
            String formattedStop = "";
            int stopCharLength = stop.length() + 3;

            char[] newStopCharArray = new char[stopCharLength];



            for (int i = 0; i < stop.length(); i++) {
                newStopCharArray[i] = stop.charAt(i);
            }

            char firstLetter = newStopCharArray[0];
            char secondLetter = newStopCharArray[1];
            char space = ' ';

            newStopCharArray[stopCharLength - 3] = space;
            newStopCharArray[stopCharLength - 2] = firstLetter;
            newStopCharArray[stopCharLength - 1] = secondLetter;

            String str = String.valueOf(newStopCharArray);

            formattedStop = str.substring(3);

            return formattedStop;

        } else {
            return stop;
        }


    }
}

