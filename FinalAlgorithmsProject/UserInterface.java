import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class UserInterface {

    public static void main(String[] args) throws FileNotFoundException {

        boolean quit = false;
        try {
            File stopTimesFile = new File("stop_times.txt");
            File transfersFile = new File("transfers.txt");
            File stopsFile = new File("stops.txt");

            Scanner input = new Scanner(System.in);
            //loop to continue promting users until they decide to quit
            while (!quit) {



                //first ask users for 
                System.out.println("What would you like to do?\n" +
                    "1. Find the shortest path between two stops\n" +
                    "2. Search for a bus stop\n" +
                    "3. Search for all trips within a given arrival time\n" +
                    "4. Quit\n" +
                    "To select option 1,2,3, or 4 type the number and hit enter");

                if (!input.hasNextInt()) { //checks for valid input
                    System.out.println("Error invalid input.");
                    input.next();
                } else {
                    int choice = input.nextInt();
                    // case 1 - shortest path between two stops
                    if (choice == 1) {
                        //prompts for the two stop numbers
                        System.out.println("Please enter 2 bus stop numbers to find the shortest path between them\n" +
                            "Enter first stop number: ");
                        if (!input.hasNextInt()) {
                            System.out.println("Error invalid input,please check each stop number is valid\n");
                            input.next();
                        } else {
                            int stop1 = input.nextInt();
                            System.out.println("Enter second stop number: ");
                            if (!input.hasNextInt()) {
                                System.out.println("Error invalid input,please check each stop number is valid\n");
                                input.next();
                            } else {
                                int stop2 = input.nextInt();
                                // creates shortest path object which generates shortest path between the two stops
                                ShortestPath shortestPath = new ShortestPath(transfersFile, stopTimesFile, stopsFile);
                                try {
                                    // prints shortest path cost and stop sequence
                                    System.out.println(shortestPath.getStopSequence(stop1, stop2));
                                } catch (Exception ArrayIndexOutOfBoundsException) {
                                    System.out.println("Error invalid input,please check each stop number is valid\n");
                                }
                            }
                        }
                    }
                    // case 2 - searches for bus stops
                    else if (choice == 2) {
                        //calls takeInput function for BusStopSearch class - this takes input and proceeds to handle everything else
                        // finally printing the correct output for the user
                        BusStopSearch.takeInput(stopsFile);
                        input.nextLine();
                    }
                    //case 3 - outputs all trips for given arrival time 
                    else if (choice == 3) {
                        //prompts user for input
                        System.out.println("Please enter arrival time in the format HH:MM:SS to find the trips that match");
                        String arrivalTime = input.next();
                        // calls search function from SearchTrips class - this handles everything and prints the output to the UI
                        SearchTrips.search(arrivalTime, stopTimesFile);
                        // case 4 - quit
                    } else if (choice == 4) {
                        System.out.println("Goodbye!");
                        quit = true;
                    }
                    //case 5 - input error
                    else {
                        System.out.println("Error invalid input,please select a valid option\n");
                        input.next();
                    }

                }

            }
            input.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }


    }
}
