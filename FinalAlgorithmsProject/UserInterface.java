import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class UserInterface {

	public static void main(String[] args) throws FileNotFoundException {
		
		boolean quit=false;
		try {
			File stopTimesFile = new File("stop_times.txt");
	        File transfersFile = new File("transfers.txt");
	        File stopsFile = new File("stops.txt");
	        
	        Scanner input = new Scanner(System.in);
			while (!quit) {
				/* asks users which they want to perform, handles for 
					errors, ouputs results, prompts if want to continue
			
			*/
				
				System.out.println("What would you like to do?\n" 
						+ "1. Find the shortest path between two stops\n"
						+ "2. Search for a bus stop\n" 
						+ "3. Search for all trips within a given arrival time\n"
						+"4. Quit\n"
						+ "To select option 1,2,3, or 4 type the number and hit enter");
				
				if (!input.hasNextInt()) {
					System.out.println("Error invalid input.");
					input.next();
				}
				else {
				
						int choice = input.nextInt();
		
						if (choice == 1) {
							System.out.println("Please enter 2 bus stop numbers to find the shortest path between them\n"
									+ "Enter first stop number: ");
							if (!input.hasNextInt()) {
								System.out.println("Error invalid input,please check each stop number is valid\n");
							}
							int stop1 = input.nextInt();
							System.out.println("Enter second stop number: ");
							if (!input.hasNextInt()) {
								System.out.println("Error invalid input,please check each stop number is valid\n");
							}
							int stop2 = input.nextInt();
							ShortestPath shortestP = new ShortestPath(transfersFile,stopTimesFile,stopsFile);
							try {
								System.out.println(shortestP.getStopSequence(stop1, stop2));		
							}
							catch(Exception ArrayIndexOutOfBoundsException) {
								System.out.println("Error invalid input,please check each stop number is valid\n");
							}
						} 
						else if (choice == 2) {
							BusStopSearch a = new BusStopSearch(stopsFile);
							a.takeInput();
							input.nextLine();
						}
						else if (choice == 3) {
							System.out.println("Please enter arrival time in the format HH:MM:SS to find the trips that match");
							String arrivalTime = input.next();	
							SearchTrips.search(arrivalTime, stopTimesFile);
						}else if(choice == 4) {
							System.out.println("Goodbye!");
							quit=true;
						}
						else {
							System.out.println("Error invalid input,please select a valid option\n");
							input.next();
						}
						
						
				}
				
				
			
				}
		 }catch(IOException e)
        {
            System.out.println("File not found");
        }

	}
}


