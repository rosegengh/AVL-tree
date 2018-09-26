import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<T>> implements Iterable<T> {
	private BinaryNode root;
	// Most of you will prefer to use NULL NODES once you see how to use them.
	private final BinaryNode NULL_NODE = new BinaryNode(); // A null node used  to avoid null pointer exception
	private int modifyCount; // recording for how many times the tree has been modified

	public BinarySearchTree() {
		this.root = NULL_NODE; // the tree is initialized as one without any element
		this.modifyCount = 0; // modify count is zero for i has not been modified yet
	}

	// For manual tests only
	void setRoot(BinaryNode n) {
		this.root = n;
	}

	public boolean isEmpty() {
		return this.root == NULL_NODE;
	}

	public int size() {
		return this.root.size(); // calculating the size of the tree by calculating the size of the root
	}

	public int height() {
		return this.root.height(); // calculating the size of the tree by calculating the height of the root
	}

	public ArrayList<T> toArrayList() {
		return root.toArrayList(); // convert the tree to an arraylist by convert the root to an arraylist
	}

	public String toString() {
		return this.toArrayList().toString(); // first convert the tree to an arraylist, then change the arraylist to string
	}

	public boolean containsNonBST(T i) {
		return this.root.containsNonBST(i); // check if the tree contains an element by checking if the root contains it
	}

	// Not private, since we need access for manual testing.
	class BinaryNode {
		private T data;
		private BinaryNode left;
		private BinaryNode right;

		public BinaryNode() {
			// initialize a node with no content
			this.data = null;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}

		public BinaryNode(T element) {
			// initialize a node with data
			this.data = element;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}

		public ArrayList<T> toArrayList() {
			ArrayList<T> result = new ArrayList<T>(); // initial an empty arraylist to be filled later
			toArrayListHelper(result); // fill the arraylist
			return result;
		}

		private void toArrayListHelper(ArrayList<T> result) {
			if (this != NULL_NODE) {
				// put the tree in the arraylist in in-order
				this.left.toArrayListHelper(result); // put the left sub-tree first
				result.add(this.data); // put the node
				this.right.toArrayListHelper(result); // put the right tree
			}
		}

		public T getData() {
			return this.data;
		}

		public BinaryNode getLeft() {
			return this.left;
		}

		public BinaryNode getRight() {
			return this.right;
		}

		// For manual testing
		public void setLeft(BinaryNode left) {
			this.left = left;
		}

		public void setRight(BinaryNode right) {
			this.right = right;
		}

		public int size() {
			if (this == NULL_NODE) // the size of the null node would be zero
				return 0;
			return this.left.size() + this.right.size() + 1; // if the root of a  tree exists, the size should be 1+left size+right size, solve recursively
		}

		public boolean containsNonBST(T item) {

			if (this == NULL_NODE)
				return false; // if the node is null node, return false for it contains nothing

			return this.data.equals(item) || this.left.containsNonBST(item)
					|| this.right.containsNonBST(item); // check if the node contains the element or the subtrees contain the element
		}

		public int height() {
			if (this == NULL_NODE)
				return -1; // if is null node the height would be -1
			return 1 + Math.max(this.left.height(), this.right.height()); // return the height of current node and the larger height if two sub-trees
		}

		public BinaryNode insert(T i, Result result) {

			if (i == null)
				throw new IllegalArgumentException(); // throw exception when the insertion is null

			if (this == NULL_NODE) {
				result.result = true; // indicating insertion result is true when the element is inserted at a null node
				return new BinaryNode(i); // insert new element
			}

			if (this.data.equals(i)) {
				result.result = false; // indicating insertion result is false when the element has existed
			} else {
				if (i.compareTo(this.data) > 0)
					this.right = this.right.insert(i, result); // if the element is larger, insert to the right
				else
					this.left = this.left.insert(i, result); // if the element is smaller, insert to the left
			}

			return this; // return this to override itself
		}

		public boolean contains(T i) {
			if (this == NULL_NODE)
				return false; // return false if the node is null node

			if (this.data.equals(i))
				return true; // return true if the node contains the element
			else {
				if (this.data.compareTo(i) > 0)
					return this.left.contains(i); // search the left tree if the item is smaller than current data
				else
					return this.right.contains(i); // search the right tree if the item is larger than current data
			}
		}

		public BinaryNode remove(T i, Result result)
				throws IllegalArgumentException {

			if (i == null)
				throw new IllegalArgumentException(); // throw exception when the parameter is illegal

			if (this == NULL_NODE) {
				result.result = false; // set the result as false for the element does not exist
				return this; // override the original tree
			}

			if (this.data.equals(i)) {
				// start the deletion if the element is in this node
				if (this.hasNoChild()) {
					// if the node has no child simply remove the node
					result.result = true;
					return NULL_NODE;
				} else if (this.hasTwoChildren()) {
					BinaryNode temp = this.left;
					while (temp.right != NULL_NODE)
						temp = temp.right; // find the largest data that is smaller than the item to be deleted
					T data = temp.data; // get the "largest smallest" data out
					this.left = this.left.remove(temp.data, result); // delete the node  with "largest smallest" data
					this.data = data; // replace current data with "largest smallest" data
				} else {
					// in this case, the node has only one child
					if (this.left == NULL_NODE)
						return this.right; // replace the node as the right child if the left one is null node
					else
						return this.left; // replace the node as the left child if the right one is null node
				}

			} else {
				if (this.data.compareTo(i) > 0)
					this.left = this.left.remove(i, result); // let the left tree remove the element if the item  to be deleted is smaller
				else
					this.right = this.right.remove(i, result); // let the right tree remove the element if the item to be deleted is larger
			}

			return this; // override the original tree
		}

		/*
		 * Helper method to check if the node has no child
		 */
		public boolean hasNoChild() {
			return this.left == NULL_NODE && this.right == NULL_NODE;
		}

		/*
		 * Helper method to check if the node has two children
		 */
		public boolean hasTwoChildren() {
			return this.left != NULL_NODE && this.right != NULL_NODE;
		}

	}

	/*
	 * A wrapper class used to contain and mutate the result boolean
	 */
	class Result {

		public boolean result;

		public Result(boolean result) {
			this.result = result;
		}
	}

	public boolean insert(T i) {
		this.modifyCount++; // increasing the modifyCount by one to indicate the tree has been modified
		Result result = new Result(true); // new wrapper for result
		this.root = this.root.insert(i, result); // override the original tree
		return result.result; // return insertion result
	}

	public boolean contains(T i) {
		return root.contains(i); // check if the tree contains the element by checking if the root contains the element
	}

	public boolean remove(T i) {
		this.modifyCount++; // increasing the modifyCount by one to indicate the tree has been modified
		Result result = new Result(true); // new wrapper for result
		this.root = this.root.remove(i, result); // override the original tree
		return result.result; // return removal result
	}

	public Object[] toArray() {
		return this.toArrayList().toArray(); // first convert to arraylist, then to array
	}

	@Override
	public Iterator<T> iterator() {
		return new InOrderIterator(); // return an in-order iterator
	}

	public Iterator<T> inefficientIterator() {
		return new ArrayListIterator(); // return a iterator based on arraylist
	}

	// DONE: Implement your 3 iterator classes here.

	class ArrayListIterator implements Iterator<T> {

		private ArrayList<T> array;
		private int atIndex;
		private int modifyCount;

		public ArrayListIterator() {
			this.array = BinarySearchTree.this.toArrayList(); // store the tree as an array list
			this.atIndex = -1; // set initial position index
			this.modifyCount = BinarySearchTree.this.modifyCount; // regulate initial modifycount
		}

		@Override
		public boolean hasNext() {
			return this.atIndex < this.array.size() - 1; // check if the index has come to the end
		}

		@Override
		public T next() throws NoSuchElementException,
				ConcurrentModificationException {

			if (this.modifyCount != BinarySearchTree.this.modifyCount)
				throw new ConcurrentModificationException(); // throw exception when the tree is modified

			if (!this.hasNext())
				throw new NoSuchElementException(); // throw exception when it come to the end

			this.atIndex++; // increase position index
			return this.array.get(atIndex); // return the element at position
		}

		@Override
		public void remove() {

		}

	}

	public Iterator<T> preOrderIterator() {
		return new PreOrderIterator(); // return a pre-order iterator
	}

	class PreOrderIterator implements Iterator<T> {

		private Stack<BinaryNode> nodeStatck;
		private int modifyCount;
		private BinaryNode current;
		private boolean canRemove;

		public PreOrderIterator() {
			this.modifyCount = BinarySearchTree.this.modifyCount; // regulate initial count
			this.nodeStatck = new Stack<BinaryNode>(); // stack to contain nodes
			this.current = null; // set current node
			this.canRemove = true; // boolean to check if is able to remove
			if (BinarySearchTree.this.root != NULL_NODE)
				this.nodeStatck.push(BinarySearchTree.this.root); // put root into stack
		}

		@Override
		public boolean hasNext() {
			return !this.nodeStatck.isEmpty(); // if the stack has nothing in it, tree has no next
		}

		@Override
		public T next() throws NoSuchElementException, ConcurrentModificationException {

			if (this.modifyCount != BinarySearchTree.this.modifyCount)
				throw new ConcurrentModificationException(); // throw exception if the tree has been modified

			if (!this.hasNext())
				throw new NoSuchElementException(); // throw exception when the iterator has come to the end

			BinaryNode current = this.nodeStatck.pop(); // pop the current element
			if (current.right != NULL_NODE)
				this.nodeStatck.push(current.right); // push in the right first
			if (current.left != NULL_NODE)
				this.nodeStatck.push(current.left); // push in the left

			this.current = current; // record current element
			return current.data; // return the data of current node
		}

		@Override
		public void remove() throws IllegalStateException, ConcurrentModificationException {
			if (this.current == null || !canRemove)
				throw new IllegalStateException(); // throw exception for illegal operation

			if (this.modifyCount != BinarySearchTree.this.modifyCount)
				throw new ConcurrentModificationException(); // throw exception if the tree has been modified

			BinarySearchTree.this.remove(this.current.data); // remove current data
			this.canRemove = false; // nullify the ability to remove
		}
	}

	class InOrderIterator implements Iterator<T> {

		private Stack<BinaryNode> nodeStatck;
		private int modifyCount;
		private BinaryNode current;
		private boolean canRemove;

		public InOrderIterator() {
			this.modifyCount = BinarySearchTree.this.modifyCount; // regulate the initial modifycount
			this.nodeStatck = new Stack<BinaryNode>(); // Initialize stack to contain nodes
			this.current = null; // set current to be null
			this.canRemove = true; // boolean to check if is able to remove
			if (root != NULL_NODE) {
				// put in root until the leftest node to the system if root is not null node
				this.nodeStatck.push(root);
				BinaryNode current = root.left;
				while (current != NULL_NODE) {
					this.nodeStatck.push(current);
					current = current.left;
				}
			}
		}

		@Override
		public boolean hasNext() {
			return !this.nodeStatck.isEmpty(); // if the stack has nothing in it, tree has no next
		}

		@Override
		public T next() throws NoSuchElementException, ConcurrentModificationException {

			if (this.modifyCount != BinarySearchTree.this.modifyCount)
				throw new ConcurrentModificationException(); // throw exception when the tree has been modified

			if (!this.hasNext())
				throw new NoSuchElementException(); // throw exception when the tree comes to the end

			BinaryNode current = this.nodeStatck.pop(); // get the current node

			BinaryNode add = current.right; // get the right node of the current
			if (add != NULL_NODE) {
				// add the right node of the current and all nodes on the left of the right
				this.nodeStatck.push(add);
				add = add.left;
				while (add != NULL_NODE) {
					this.nodeStatck.push(add);
					add = add.left;
				}
			}
			this.current = current; // set current node
			return current.data; // return data of the current node
		}

		public void remove() throws IllegalStateException, ConcurrentModificationException {
			if (this.current == null || !canRemove)
				throw new IllegalStateException(); // throw exception for illegal operation

			if (this.modifyCount != BinarySearchTree.this.modifyCount)
				throw new ConcurrentModificationException(); // throw exception if the tree has been modified

			BinarySearchTree.this.remove(this.current.data); // remove current data
			this.canRemove = false; // nullify the ability to remove
		}
	}

}
