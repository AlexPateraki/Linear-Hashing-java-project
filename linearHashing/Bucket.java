package linearHashing;

/**
 * this class represents the bucket, has variables to describe the table and
 * functions for Linear Hashing.
 * 
 * @author Pateraki Alexandra
 *
 */
public class Bucket {
	/**
	 * current key being interger
	 */
	int key;
	/**
	 * array of the keys
	 */
	int[] keys;
	/**
	 * a bucket that is not in the table, it depends on a specific bucket, keeps the
	 * keys of overflow
	 */
	Bucket overflow;
	/**
	 * variable to count the comparisons for an insertion
	 */
	int comparisonsForInserts = 0;

	/**
	 * constructor
	 * 
	 * @param bucketSize
	 *            ->the size of a bucket
	 */
	public Bucket(int bucketSize) {
		key = 0;
		keys = new int[bucketSize];
		overflow = null;
	}

	/**
	 * setters and getters of all variables
	 */

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int[] getKeys() {
		return keys;
	}

	public void setKeys(int[] keys) {
		this.keys = keys;
	}

	public Bucket getOverflow() {
		return overflow;
	}

	public void setOverflow(Bucket overflow) {
		this.overflow = overflow;
	}

	////////////////////////////////
	/**
	 * inserts a key to the node
	 * 
	 * @param key
	 *            -> number i want to insert
	 * @param lh
	 *            -> call the linear hashing
	 * @returnthe comparisons that take place
	 */
	public int insertKey(int key, LH lh) {
		int i;

		int bucketSize = lh.getBucketSize();
		int keysNum = lh.getKeysNum();
		int keySpace = lh.getKeySpace();

		for (i = 0; (i < this.key) && (i < bucketSize); i++) {
			comparisonsForInserts++;
			if (this.keys[i] == key) { // key already here. Ignore the new one
				return comparisonsForInserts;
			}
		}
		comparisonsForInserts++;
		if (i < bucketSize) { // bucket not full write the new key
			keys[i] = key;
			this.key++;
			keysNum++;
			lh.setKeysNum(keysNum); // update linear hashing class.
		} else {
			comparisonsForInserts++;
			if (this.overflow != null) { // pass key to the overflow
				this.overflow.insertKey(key, lh);
			} else { // create a new overflow and write the new key
				this.overflow = new Bucket(bucketSize);
				keySpace += bucketSize;
				lh.setKeySpace(keySpace); // update linear hashing class
				this.overflow.insertKey(key, lh);
			}
		}
		return comparisonsForInserts;
	}

	/**
	 * delete a key from binary search tree
	 * 
	 * @param key
	 *            -> value that the program wants to delete
	 * @param lh->
	 *            linear hashing calling
	 * @return compares
	 */
	public int deleteKey(int key, LH lh) {
		int compares = 0;
		int i;
		int bucketSize = lh.getBucketSize();
		int keysNum = lh.getKeysNum();
		int keySpace = lh.getKeySpace();

		for (i = 0; (i < this.key) && (i < bucketSize); i++) {
			compares++;
			if (this.keys[i] == key) {
				compares++;
				if (this.overflow == null) { // no overflow
					this.keys[i] = this.keys[this.key - 1];
					this.key--;
					keysNum--;
					lh.setKeysNum(keysNum); // update linear hashing class.
				} else { // bucket has an overflow so remove a key from there and bring it here
					this.keys[i] = this.overflow.removeLastKey(lh);
					keysNum--;
					lh.setKeysNum(keysNum); // update linear hashing class.
					compares++;
					if (this.overflow.getKey() == 0) { // overflow empty free it
						this.overflow = null;
						keySpace -= bucketSize;
						lh.setKeySpace(keySpace); // update linear hashing class.
					}
				}
				return compares;
			}
		}
		compares++;
		if (this.overflow != null) { // look at the overflow for the key to be deleted if one exists
			this.overflow.deleteKey(key, lh);
			compares++;
			if (this.overflow.getKey() == 0) { // overflow empty free it
				this.overflow = null;
				keySpace -= bucketSize;
				lh.setKeySpace(keySpace); // update linear hashing class.
			}
		}
		return compares;
	}

	/**
	 * remove bucket last key
	 * 
	 * @param lh->
	 *            linear hashing calling
	 * @return value of the last node
	 */
	public int removeLastKey(LH lh) {

		int retval;
		int bucketSize = lh.getBucketSize();
		int keySpace = lh.getKeySpace();

		if (this.overflow == null) {
			if (this.key != 0) {
				this.key--;
				return this.keys[this.key];
			}
			return 0;
		} else {
			retval = this.overflow.removeLastKey(lh);
			if (this.overflow.getKey() == 0) { // overflow empty free it
				this.overflow = null;
				keySpace -= bucketSize;
				lh.setKeySpace(keySpace); // update linear hashing class.
			}
			return retval;
		}
	}

	/**
	 * search a specific key of the binary search tree
	 * 
	 * @param key->
	 *            value i want to search
	 * @param lh->
	 *            linear hashing calling
	 * @return comparisons until the key is found
	 */
	public int searchKey(int key, LH lh) {
		int i;
		int bucketSize = lh.getBucketSize();
		for (i = 0; (i < this.key) && (i < bucketSize); i++) {
			if (this.keys[i] == key) { // key found
				return i + 1;
			}
		}
		if (this.overflow != null) { // look at the overflow for the key if one exists
			return this.overflow.searchKey(key, lh);
		} else {
			return i + 1;
		}
	}

	/**
	 * splits the current bucket
	 * 
	 * @param lh->
	 *            linear hashing calling
	 * @param n->
	 *            hash function
	 * @param bucketPos->
	 *            position in the bucket that the 'pointer' shows
	 * @param newBucket
	 */
	public void splitBucket(LH lh, int n, int bucketPos, Bucket newBucket) {
		int i;
		int bucketSize = lh.getBucketSize();
		int keySpace = lh.getKeySpace();
		int keysNum = lh.getKeysNum();
		for (i = 0; (i < this.key) && (i < bucketSize);) {
			if ((this.keys[i] % n) != bucketPos) { // key goes to new bucket
				newBucket.insertKey(this.keys[i], lh);
				this.key--;
				keysNum = lh.getKeysNum();
				keysNum--;
				lh.setKeysNum(keysNum); // update linear hashing class.
				// System.out.println("HashBucket.splitBucket.insertKey: KeysNum = " + keysNum
				// );
				this.keys[i] = this.keys[this.key];
			} else { // key stays here
				i++;
			}
		}

		if (this.overflow != null) { // split the overflow too if one exists
			this.overflow.splitBucket(lh, n, bucketPos, newBucket);
		}
		while (this.key != bucketSize) {
			if (this.overflow == null) {
				return;
			}
			if (this.overflow.getKey() != 0) {
				this.keys[this.key] = this.overflow.removeLastKey(lh);
				if (this.overflow.getKey() == 0) { // overflow empty free it
					this.overflow = null;
					keySpace -= bucketSize;
					lh.setKeySpace(keySpace); // update linear hashing class.
				}
				this.key++;
			} else { // overflow empty free it
				this.overflow = null;
				keySpace -= bucketSize;
				lh.setKeySpace(keySpace); // update linear hashing class.
			}
		}
	}

	/**
	 * merges the current bucket
	 * 
	 * @param lh->
	 *            calling linear hashing
	 * @param oldBucket
	 *            -> the previous Bucket that i was
	 */
	public void mergeBucket(LH lh, Bucket oldBucket) {
		while (oldBucket.getKey() != 0) {
			this.insertKey(oldBucket.removeLastKey(lh), lh);
		}
	}

}
