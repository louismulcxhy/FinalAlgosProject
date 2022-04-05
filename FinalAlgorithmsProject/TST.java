
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * This class provides the functionality of a Terenary Search Tree
 *
 *
 * Reference: https://algs4.cs.princeton.edu/50strings/
 */

public class TST<Value> {
    private int n;              // size
    private Node<Value> root;   // root of TST

    private static class Node<Value> {
        private char c;                        // character
        private Node<Value> left, mid, right;  // left, middle, and right subtries
        private Value val;                     // value associated with string
    }

    /**
     * Initializes an empty string symbol table.
     */
    public TST() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) return null;
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) return null;
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!contains(key)) n++;
        else if(val == null) n--;       // delete existing key
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        Queue<String> queue = new LinkedList<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new LinkedList<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.add(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }


    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != null) queue.add(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }


    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new LinkedList<String>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }

    private void collect(Node<Value> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pattern, queue);
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.val != null) queue.add(prefix.toString() + x.c);
            if (i < pattern.length() - 1) {
                collect(x.mid, prefix.append(x.c), i+1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pattern, queue);
    }

    //Own Code begins here
    
    
    /**
     * This method creates and populates the TST using the stops.txt file, filling it with the stops,
     * and their respective stop id's as values to differentiate between the different stops
     *
     * @param fileToRead
     * @param st
     *
     */
    public static void CreateTST(String fileToRead, TST<String> st )
    {
        try{
            Scanner input = new Scanner(new FileInputStream(fileToRead));

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

    /**
     * This method manages the users requests providing them with the bus stop names in the appropriate
     * manner as requested
     *
     * @param option
     * @param key
     */
    public static void manageRequest(int option, String key)
    {
        TST<String> st = new TST<String>();

        CreateTST("stops.txt", st);

        //User requested to search by bus stops full name
        if(option==0)
        {
            if(st.get(key) == null)
            {
                System.out.println("The bus stop you have entered does no exist! Try again");
            }
            else
            {
                JOptionPane.showMessageDialog(null, key + st.get(key));
            }

        }

        //User requested to search by first few characters
        else if(option==1)
        {
            String results = "";
            for(String s : st.keysWithPrefix(key)){
                results += s+st.get(s) + "\n";
            }
            if(results =="")
            {
                JOptionPane.showMessageDialog(null, "No bus stops matching them first " +
                        "few characters were found. \n                              Please try again");
            }
            else
            {
                JOptionPane.showMessageDialog(null, results);
            }

        }
    }

}
