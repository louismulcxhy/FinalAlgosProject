import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class BusStopSearch {

	File stopsFile;
	
	public static void takeInput(File stopsFile)  {
		boolean quit = true;
		Scanner input = new Scanner(System.in);
		do {
		System.out.println("Would you like to: \n "
				+ "1. Search by complete bus stop name \n"
				+ "2. Search by first few characters ");
		if (!input.hasNextInt()) {
			System.out.println("Error invalid input,please enter either '1' or '2'. ");
			quit = false;
			input.next();
		}
		int choice = input.nextInt();
		handleRequest(choice, stopsFile);
		} while(!quit);
	
	}
	public static void handleRequest(int option, File stopsFile)  {
		
			TST<String> st = new TST<String>();
		  	Scanner input = new Scanner(System.in);
		  	String key;
	        CreateTST(stopsFile, st);

	        boolean quit;
	        do {
	        	quit = true;
		        if(option==1)
		        {
		        	System.out.println("Enter full bus stop name: ");
		        	key = input.nextLine();
		        	
		            if(st.get(key) == null)
		            {
		                System.out.println("Error: That Bus Stop Does Not Exist \n" +
		                        "Please Enter a Valid Bus Stop");
		                quit = false;
		                input.next();
		            }
		            else
		            {
		            	System.out.println( key + st.get(key));
		            }
	
		        }

		        else if(option==2)
		        {
		        	System.out.println("Enter the first few characters of the bus stop name:  ");
		        	key = input.next();
		            String results = "";
		            for(String s : st.keysWithPrefix(key)){
		                results += s+st.get(s) + "\n";
		            }
		            if(results =="")
		            {
		            	System.out.println("No bus stops matching those first " +
		                        "few characters were found. Please try again");
		            	quit = false;
		            }
		            else
		            {
		            	System.out.println(results);
		            }
	
		        }
	        } while(!quit);
		
	
	}
	  public static void CreateTST(File fileToRead, TST<String> st )
	    {
	        try{
	            Scanner input = new Scanner(new FileInputStream(fileToRead));

	            while(input.hasNextLine())
	            {
	                String [] data = input.nextLine().trim().split(",");

	                String stopName = data[2];
	                stopName = formatStopName(stopName);

	                String stopInfo = "// Stop ID: " + data[0] + "// Stop Code: " + data[1] +
	                        "// Stop Descripton : " + data[3] + "// Stop Latitiude: " + data[4] +"// Stop Longitude: " +
	                        data[5] + "// Zone ID: " + data[6];

	                st.put(stopName, stopInfo);
	            }

	            input.close();
	        }catch(IOException e)
	        {
	            System.out.println("File not found");
	        }
	    }

	    public static String formatStopName(String stop)
	    {

	        char[] stopCharArray = new char[stop.length()];
	        for (int i = 0; i < stop.length(); i++) {
	            stopCharArray[i] = stop.charAt(i);
	        }

	        char charToBeChecked = stopCharArray[2];
	        if (Character.isWhitespace(charToBeChecked))
	        {
	            String formattedStop ="";
	            int stopCharLength = stop.length() + 3;
	            
	            char[] newStopCharArray = new char[stopCharLength];

	            

	            for (int i = 0; i < stop.length(); i++) {
	                newStopCharArray[i] = stop.charAt(i);
	            }

	            char firstLetter = newStopCharArray[0];
	            char secondLetter = newStopCharArray[1];
	            char space = ' ';

	            newStopCharArray[stopCharLength-3] = space;
	            newStopCharArray[stopCharLength-2] = firstLetter;
	            newStopCharArray[stopCharLength-1] = secondLetter;

	            String str = String.valueOf(newStopCharArray);

	            formattedStop = str.substring(3);

	            return formattedStop;

	        }
	        else
	        {
	            return stop;
	        }

	    
	    }
	}


