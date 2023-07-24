package linearHashing;

import java.io.IOException;
import java.util.Random;

import main.Main;
/**
 * Represents linear hashing procedure, has the search, insert and delete procedure and 
 * calls the right methods in the class of Bucket
 * 
 * @author Pateraki Alexandra
 *<p>
 */
public class LH {
	/**
	 * pointer to the hash buckets
	 */
	private Bucket[] hashBuckets;

	/**
	 * max number of keys in each bucket
	 */
	private int bucketSize;
	/**
	 * number of keys currently stored in the table
	 */
	private int keysNum;
	/**
	 * total space the hash table has for keys
	 */
	private int keySpace;
	/**
	 * pointer to the next bucket to be split
	 */
	private int p;
	/**
	 * current number of buckets
	 */
	private int n;
	/**
	 * the n used for the hash function
	 */
	private int j;
	/**
	 * minimum number of buckets this hash table can have
	 */
	private int minBuckets;
	/**
	 * number of random numbers i want to find or delete from the list
	 */
	int numRandomsDeletOrSearch = 50;
	/**
	 * instance of main to call some variables
	 */
	Main m = new Main();
	/**
	 * comparisons used to increase this variable when i campare something to
	 * insert/delete/search
	 */
	int comparisons = 0;

	/**
	 * setters and getters
	 */
	public int getComparisons() {
		return comparisons;
	}

	public void setComparisons(int comparisons) {
		this.comparisons = comparisons;
	}

	public int getBucketSize() {
		return bucketSize;
	}

	public int getKeysNum() {
		return keysNum;
	}

	public int getKeySpace() {
		return keySpace;
	}

	public void setBucketSize(int size) {
		bucketSize = size;
	}

	public void setKeysNum(int num) {
		keysNum = num;
	}

	public void setKeySpace(int space) {
		keySpace = space;
	}

	/**
	 * Constructor
	 * 
	 * @param itsBucketSize
	 * @param initPages
	 */
	public LH(int itsBucketSize, int initPages) {
		int i;
		bucketSize = itsBucketSize;
		keysNum = 0;
		p = 0;
		n = initPages;
		j = initPages;
		minBuckets = initPages;
		keySpace = n * bucketSize;
		if ((bucketSize == 0) || (n == 0)) {
			System.out.println("error: space for the table cannot be 0");
			System.exit(1);
		}
		hashBuckets = new Bucket[n];
		for (i = 0; i < n; i++) {
			hashBuckets[i] = new Bucket(bucketSize);
		}
	}

	/**
	 * counts the hash function
	 * 
	 * @param key
	 *            -> what is the value of the key
	 * @return a hash function based on the key
	 */
	private int hashFunction(int key) {

		int retval;

		retval = key % this.j;
		if (retval < 0)
			retval *= -1;
		if (retval >= p) {
			// System.out.println( "Retval = " + retval);
			return retval;
		} else {
			retval = key % (2 * this.j);
			if (retval < 0)
				retval *= -1;
			// System.out.println( "Retval = " + retval);
			return retval;
		}
	}

	/**
	 * loads the right factor
	 * 
	 * @return the current load factor of the hash table.
	 */
	private float loadFactor() {
		return ((float) this.keysNum) / ((float) this.keySpace);
	}

	/**
	 * Splits the bucket pointed by p.
	 */
	private void bucketSplit() {
		int i;
		Bucket[] newHashBuckets;
		newHashBuckets = new Bucket[n + 1];
		for (i = 0; i < this.n; i++) {
			newHashBuckets[i] = this.hashBuckets[i];
		}
		hashBuckets = newHashBuckets;
		hashBuckets[this.n] = new Bucket(this.bucketSize);
		this.keySpace += this.bucketSize;
		this.hashBuckets[this.p].splitBucket(this, 2 * this.j, this.p, hashBuckets[this.n]);
		this.n++;
		if (this.n == 2 * this.j) {
			this.j = 2 * this.j;
			this.p = 0;
		} else {
			this.p++;
		}
	}

	/**
	 * Merges the last bucket that was split
	 */
	private void bucketMerge() {
		int i;
		Bucket[] newHashBuckets;
		newHashBuckets = new Bucket[n - 1];
		for (i = 0; i < this.n - 1; i++)
			newHashBuckets[i] = this.hashBuckets[i];
		if (this.p == 0) {
			this.j = (this.n) / 2;
			this.p = this.j - 1;
		} else
			this.p--;
		this.n--;
		this.keySpace -= this.bucketSize;
		this.hashBuckets[this.p].mergeBucket(this, hashBuckets[this.n]);
		hashBuckets = newHashBuckets;
	}

	/**
	 * Insert a new key and counts the comparisons that happens until the key go
	 * into the right position
	 * 
	 * @param key
	 *            -> value of the key
	 * @return how many comparisons have been done to place the key into the right
	 *         position
	 */
	public int insertKey(int key) {
		comparisons = this.hashBuckets[this.hashFunction(key)].insertKey(key, this);
		if (this.loadFactor() > m.getMaxThreshold())
			this.bucketSplit();
		return comparisons;
	}

	/**
	 * Delete a key
	 * 
	 * @param key
	 *            -> value of the key
	 * @return the number of the comparisons have been done until find the key that
	 *         has to be deleted
	 */
	public int deleteKey(int key) {
		int comparisons = this.hashBuckets[this.hashFunction(key)].deleteKey(key, this);
		comparisons++;
		if (this.loadFactor() > m.getMaxThreshold())
			this.bucketSplit();
		else if ((this.loadFactor() < m.getMinThreshold()) && (this.n > this.minBuckets)) {
			comparisons++;
			this.bucketMerge();
		}
		return comparisons;
	}

	/**
	 * Search for a key
	 * 
	 * @param key
	 *            -> value of the key
	 * @return how many comparisons have been done to find the key
	 */
	public int searchKey(int key) {
		return this.hashBuckets[this.hashFunction(key)].searchKey(key, this);
	}

	/**
	 * computes average number of comparisons
	 * 
	 * @param average
	 *            -> an array of numbers to compute and
	 * @return the average of the numbers of the array
	 */
	public double computeAverage(double[] average) {
		double averageNum = 0.0;
		double sum = 0.0;
		for (int k = 0; k < average.length; k++) {
			sum = sum + average[k];
		}
		averageNum = sum / (average.length);
		return averageNum;
	}

	/**
	 * handle the procedure, by adding 100 keys, search 50 keys of the 'list' and
	 * delete 50 keys and then print the average number of the comparisons that have
	 * been done.
	 * 
	 * @param posInFile
	 *            -> in which hundred of the file i am
	 * @param keyValues
	 *            -> the array that keeps all the number in raw of the file coming
	 *            fdrom the main class
	 * @param numOfElements
	 *            -> how many elements i want to to insert
	 * @throws IOException
	 */
	public void linearHandle(int posInFile, int[] keyValues, int numOfElements, int[] randomKeys) throws IOException {
		Random rand = new Random();
		double[] comparesForInsert = new double[m.getInitPages()];
		double[] searching = new double[numRandomsDeletOrSearch];
		double[] delete = new double[numRandomsDeletOrSearch];

		// insert
		for (int i = 0; i < numOfElements; i++) {
			comparesForInsert[i] = m.getLinear().insertKey(keyValues[i + posInFile]);
			setComparisons(0);
		}
		//print average
		double compareInsert = m.getLinear().computeAverage(comparesForInsert);
		System.out.printf("%15.2f", compareInsert);
		
		
		// search random numbers
		for (int j = 0; j < 30; j++) {
			searching[j] = m.getLinear().searchKey(randomKeys[j]);
			setComparisons(0);
		}
		int[] random= new int[numRandomsDeletOrSearch];
		// find 50 random numbers to delete
		for (int i = 0; i < randomKeys.length; i++) {
			random[i] = keyValues[rand.nextInt(keyValues.length)];
		}
		// random
		for (int j = 0; j < 30; j++) {
			delete[j] = m.getLinear().deleteKey((random[j]));
			setComparisons(0);

		}
//print averages
		double compareSearch = m.getLinear().computeAverage(searching);
		double compareDelete = m.getLinear().computeAverage(delete);
		System.out.printf("%15.2f",  compareSearch);
		System.out.printf("%15.2f", compareDelete);
	}

}