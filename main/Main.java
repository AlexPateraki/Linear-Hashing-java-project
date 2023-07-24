package main;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import bst.BST;
import linearHashing.LH;
/**
 * take the arguments of the file and 'give' them from args[0] in order to take some counts
 * when a key is inserted in linear hashing with different maximum threshold 
 * and a key is deleted(the key is on the list) and when a key is searched on the list
 * @author Pateraki Alexandra
 *
 */
public class Main {
	/**
	 * initial max load factor threshold
	 */
	static double maxThreshold = 0.5;
	/**
	 *  min load factor threshold
	 */
	double minThreshold = 0.5; 
	/**
	 * how many elements in each 'page'/cell
	 */
	static int initPages = 100;
	/**
	 * capacity of every page
	 */
	static int pageSize = 10;
	/**
	 * linear hashing instance 
	 */
	static LH linear = new LH(pageSize, initPages);
	/**
	 * bst instance
	 */
	static BST bst=new BST();
	static int random2search=50;
	/**
	 * setters and getters
	 */
	public double getMaxThreshold() {
		return maxThreshold;
	}

	public double getMinThreshold() {
		return minThreshold;
	}

	public int getInitPages() {
		return initPages;
	}

	public static int getPageSize() {
		return pageSize;
	}

	public LH getLinear() {
		return linear;
	}

	public BST getBst() {
		return bst;
	}

	/**
	 * handls the procedure, save all the keys of the file in the array keyValues and use it to 
	 * count the comparisons if u>50%, 80% and in the case of using bst
	 * @param args-> arguments from run configurations
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int maxFile = 10000;		
		int[] keyValues = new int[maxFile];
		int[] randomKeys = new int[random2search];
		Random rand = new Random();

		//save in the keyValues array
		RandomAccessFile in = new RandomAccessFile(args[0], "r");
		for (int i = 0; i < keyValues.length; i++)
			keyValues[i] = in.readInt();
		in.close();

		System.out.println("\t\tu>50%\t\t\t\t\tu>80%\t\t\t\t\tBST\nSize N\t insertion\tsearch"
				+ "\tdeletion\tinsertion\t  search\tdeletion\t\tinsertion\tsearch\tdeletion\t");

		//handle procedure
		for (int i = 0; i < maxFile; i += 100) {
			maxThreshold = 0.5;
			System.out.print(i + 100);
			
			// find 50 random numbers to search
			for (int j = 0; j < randomKeys.length; j++) {
				randomKeys[j] = keyValues[rand.nextInt(keyValues.length)];
			}
			linear.linearHandle(i, keyValues, initPages,randomKeys);
			maxThreshold = 0.8;
			linear.linearHandle(i, keyValues, initPages,randomKeys);
			bst.binaryHandl(i, keyValues, initPages,randomKeys);
			System.out.println();
		}
		
		
	}
	

}
