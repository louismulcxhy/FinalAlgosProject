import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class BusStopSearch {
	// opens dialog asking which option, manages request, handles file
	// then finally calls TST to search
	File stopsFile;
	BusStopSearch(File stopsFile) throws FileNotFoundException{
		this.stopsFile = stopsFile;
		takeInput();
		
	}
	public void takeInput() throws FileNotFoundException {
		boolean quit = true;
		Scanner input = new Scanner(System.in);
		do {
		System.out.println("Would you like to: \n "
				+ "1. Search by complete bus stop name \n"
				+ "2. Search by first few characters ");
		if (!input.hasNextInt()) {
			System.out.println("Error invalid input,please check each stop number is valid\n");
			quit = false;
		}
		int choice = input.nextInt();
		handleRequest(choice);
		} while(!quit);
	
	}
	public void handleRequest(int option) throws FileNotFoundException {
		  TST<String> st = new TST<String>();
		  	Scanner input = new Scanner(System.in);
		  	String key;
	        CreateTST(stopsFile, st);

	        //User requested to search by bus stops full name
	        boolean quit;
	        do {
	        	quit = true;
		        if(option==1)
		        {
		        	System.out.println("Enter full bus stop name: \n");
		        	key = input.next();
		        	
		            if(st.get(key) == null)
		            {
		                System.out.println("That Bus Stop Does Not Exist \n" +
		                        "Please Enter a Valid Bus Stop");
		                quit = false;
		            }
		            else
		            {
		            	System.out.println( key + st.get(key));
		            }
	
		        }
	
		        //User requested to search by first few characters
		        else if(option==2)
		        {
		        	System.out.println("Enter first few characters of bus stop name:  ");
		        	key = input.next();
		            String results = "";
		            for(String s : st.keysWithPrefix(key)){
		                results += s+st.get(s) + "\n";
		            }
		            if(results =="")
		            {
		            	System.out.println("No bus stops matching them first " +
		                        "few characters were found. \n                              Please try again");
		            	quit = false;
		            }
		            else
		            {
		            	System.out.println(results);
		            }
	
		        }
	        } while(!quit);
	       
	
	}
	  public static String MoveKeyword(String stop)
	    {

	        // Checking if the stop name contains keyword flagstops that need to be moved
	        char[] checkingCh = new char[stop.length()];
	        for (int i = 0; i < stop.length(); i++) {
	            checkingCh[i] = stop.charAt(i);
	        }

	        char checking = checkingCh[2];
	        if (Character.isWhitespace(checking))
	        {
	            String newStop ="";

	            char[] ch = new char[stop.length()+3];

	            int chLength = stop.length() + 3;

	            for (int i = 0; i < stop.length(); i++) {
	                ch[i] = stop.charAt(i);
	            }

	            char firstLetter = ch[0];
	            char secondLetter = ch[1];
	            char space = ' ';

	            ch[chLength-3] = space;
	            ch[chLength-2] = firstLetter;
	            ch[chLength-1] = secondLetter;

	            String str = String.valueOf(ch);

	            newStop = str.substring(3);

	            return newStop;

	        }
	        else
	        {
	            return stop;
	        }

	    }
	  public static void CreateTST(File fileToRead, TST<String> st ) throws FileNotFoundException
	    {
	        Scanner input = new Scanner(fileToRead);

			while(input.hasNextLine())
			{
			    String [] lineData = input.nextLine().trim().split(",");

			    String stopName = lineData[2];
			    stopName = MoveKeyword(stopName);

			    String stopInformation = "// Stop id: " + lineData[0] + "// Stop Code: " + lineData[1] +
			            "// Stop Desc : " + lineData[3] + "// Stop Lat: " + lineData[4] +"// Stop Lon: " +
			            lineData[5] + "// Zone Id: " + lineData[6];

			    st.put(stopName, stopInformation);
			}

			input.close();
	    }
}
