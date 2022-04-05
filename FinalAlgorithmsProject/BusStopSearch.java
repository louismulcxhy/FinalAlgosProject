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
	BusStopSearch(File stopsFile) {
		
			this.stopsFile = stopsFile;
			takeInput();
		
	}
	public void takeInput()  {
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
	public void handleRequest(int option)  {
		
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
		        	System.out.println("Enter full bus stop name: ");
		        	key = input.nextLine();
		        	
		            if(st.get(key) == null)
		            {
		                System.out.println("That Bus Stop Does Not Exist \n" +
		                        "Please Enter a Valid Bus Stop");
		                quit = false;
		                input.next();
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
		            	System.out.println("No bus stops matching those first " +
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
	  public static void CreateTST(File fileToRead, TST<String> st )
	    {
	        try{
	            Scanner input = new Scanner(new FileInputStream(fileToRead));

	            while(input.hasNextLine())
	            {
	                String [] data = input.nextLine().trim().split(",");

	                String stopName = data[2];
	                stopName = formatStopName(stopName);

	                String stopInfo = "// Stop id: " + data[0] + "// Stop Code: " + data[1] +
	                        "// Stop Desc : " + data[3] + "// Stop Lat: " + data[4] +"// Stop Lon: " +
	                        data[5] + "// Zone Id: " + data[6];

	                st.put(stopName, stopInfo);
	            }

	            input.close();
	        }catch(IOException e)
	        {
	            System.out.println("File not found");
	        }
	    }

	    /**
	     * This method takes a string representation of the stop, and then returns the stop name
	     * with the keyword flagstops moved to the end of the stop
	     *
	     *
	     * @param stop
	     * @return an String representation of the stop with they words moved appropriately
	     */
	    public static String formatStopName(String stop)
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
	}


