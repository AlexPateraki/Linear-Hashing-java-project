package bst;

/**
 * A Binary search tree using node from the reference:
 * https://algorithms.tutorialhorizon.com/binary-search-tree-complete-implementation/
 * @author Pateraki Alexandra
 */
import java.util.Random;
import main.Main;

public class BST {
	/**
	 * keeps the root of the tree
	 */
	public static Node root;
	/**
	 * how many random deletion and searches the exercise says have to been done
	 */
	int numRandomsDeletOrSearch = 50;
	/**
	 * how many compares have been done to delete/search/insert a key
	 */
	int compares = 0;

	/**
	 * constructor
	 */
	public BST() {
		BST.root = null;
	}

	/**
	 * setters and getters
	 * 
	 * @return
	 */
	public int getCompares() {
		return compares;
	}

	public void setCompares(int compares) {
		this.compares = compares;
	}

	/**
	 * find / search for a key from the keys that ara already on the list
	 * 
	 * @param id
	 *            -> the key that i am looking for
	 * @return how many comparisons have bben done until the key is found
	 */
	public int find(int id) {
		Node current = root;
		int counter = 1;
		while (current != null) {
			compares++;
			if (current.data == id) {
				break;
			} else if (current.data > id) {
				compares++;
				current = current.left;
				counter++;
			} else {
				current = current.right;
				counter++;
			}
		}
		return counter;
	}

	/**
	 * delete a key from the keys are already in the tree
	 * 
	 * @param id
	 *            -> the key that i am looking for
	 * @return how many comparisons have been done to delete successfully the key
	 */
	public int delete(int id) {
		Node parent = root;
		Node current = root;
		boolean isLeftChild = false;
		while (current.data != id) {
			compares++;
			parent = current;
			compares++;
			if (current.data > id) {
				isLeftChild = true;
				current = current.left;
			} else {
				isLeftChild = false;
				current = current.right;
			}
			compares++;
			if (current == null) {
				return compares;
			}
		}
		// if i am here that means we have found the node
		// Case 1: if node to be deleted has no children
		compares++;
		if (current.left == null && current.right == null) {
			if (current == root) {
				root = null;
			}
			compares++;
			if (isLeftChild == true) {
				parent.left = null;
			} else {
				parent.right = null;
			}
		}
		// Case 2 : if node to be deleted has only one child

		else if (current.right == null) {
			compares++;
			if (current == root) {
				compares++;
				root = current.left;
			} else if (isLeftChild) {
				compares++;
				parent.left = current.left;
			} else {
				parent.right = current.left;
			}
		} else if (current.left == null) {
			compares++;
			if (current == root) {
				root = current.right;
			} else if (isLeftChild) {
				compares++;
				parent.left = current.right;
			} else {
				parent.right = current.right;
			}
		} else if (current.left != null && current.right != null) {
			compares++;
			// now we have found the minimum element in the right sub tree
			Node successor = getSuccessor(current);
			compares++;
			if (current == root) {
				root = successor;
			} else if (isLeftChild) {
				compares++;
				parent.left = successor;
			} else {
				parent.right = successor;
			}
			successor.left = current.left;
		}
		return compares;
	}

	/**
	 * fix pright left child of the key
	 * 
	 * @param deleteNode
	 *            ->the node that the program found and have to be deleted
	 * @return the node that will be deleted
	 */
	public Node getSuccessor(Node deleteNode) {
		Node successsor = null;
		Node successsorParent = null;
		Node current = deleteNode.right;
		while (current != null) {
			compares++;
			successsorParent = successsor;
			successsor = current;
			current = current.left;
		}
		compares++;
		if (successsor != deleteNode.right) {
			successsorParent.left = successsor.right;
			successsor.right = deleteNode.right;
		}
		return successsor;
	}

	/**
	 * calls the insert method in order to insert the key on the bst
	 * 
	 * @param id->
	 *            the value of the key that will be inserted in the bst
	 * @return how many compares have been until find the right spot to place the
	 *         key
	 */
	public int insertInt(int id) {
		insert(id);
		return compares;
	}

	/**
	 * insert the jekey in best by finding and compare left and right child values
	 * 
	 * @param id
	 *            -> the value of the key i want to insert
	 * @return how many comparisons have been done to insert the right place the key
	 */
	public int insert(int id) {
		Node newNode = new Node(id);
		compares++;
		if (root == null) {
			root = newNode;
			return compares;
		}
		Node current = root;
		Node parent = null;
		while (true) {
			parent = current;
			compares++;
			if (id < current.data) {
				current = current.left;
				compares++;
				if (current == null) {
					parent.left = newNode;
					return compares;
				}
			} else {
				current = current.right;
				compares++;
				if (current == null) {
					parent.right = newNode;
					return compares;
				}
			}
		}
	}

	/**
	 * handling the procedure of the exercise in bst by adding 100 keys, search 50
	 * keys of the 'list' and delete 50 keys and then print the average number of
	 * the comparisons that have been done.
	 * 
	 * @param posInFile
	 * @param keyValues
	 * @param numOfElements
	 */
	public void binaryHandl(int posInFile, int[] keyValues, int numOfElements,int[] randomKeys) {
		Random rand = new Random();
		Main m = new Main();
		double[] searching = new double[numRandomsDeletOrSearch];
		double[] delete = new double[numRandomsDeletOrSearch];
		double[] insertion = new double[m.getInitPages()];

		// insert
		for (int i = 0; i < numOfElements; i++) {
			insertion[i] = m.getBst().insert(keyValues[i + posInFile]);
			setCompares(0);
		}
		double compareInsert = m.getBst().computeAverage(insertion);
		System.out.printf("%15.2f", compareInsert);
		setCompares(0);

		// search random numbers
		for (int j = 0; j < 30; j++) {
			searching[j] = m.getBst().find(randomKeys[j]);
			setCompares(0);
		}
		
		int[] randoms= new int[numRandomsDeletOrSearch];

		// find 50 random numbers to delete
		for (int i = 0; i < randoms.length; i++) {
			randoms[i] = keyValues[rand.nextInt(keyValues.length)];
		}
		setCompares(0);
		// random
		for (int j = 0; j < 30; j++) {
			delete[j] = m.getBst().delete((randomKeys[j]));
			setCompares(0);
		}

		double compareSearch = m.getBst().computeAverage(searching);
		double compareDelete = m.getBst().computeAverage(delete);
		System.out.printf("%15.2f", compareSearch);
		System.out.printf("%15.2f", compareDelete);
	}

	/**
	 * computes average number of comparisons 
	 * @param average -> an array that keeps all the compares of deletion/seraching/insertion
	 * @return the average number of the array
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
}
