
package telran.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TreeSet<T> extends AbstractSet<T> {
	private static class Node<T> {
		T obj;
		Node<T> left; // reference to all nodes containing objects less than obj
		Node<T> right; // reference to all nodes containing objects greater than obj
		Node<T> parent; // reference to a parent

		Node(T obj) {
			this.obj = obj;
		}
	}

	private static final int SPACE_LEVEL = 1;

	private Node<T> root;
	private Comparator<T> comp;

	public TreeSet(Comparator<T> comp) {
		this.comp = comp;
	}

	@SuppressWarnings("unchecked")
	public TreeSet() {
		this((Comparator<T>) Comparator.naturalOrder());
	}

	private Node<T> getMostLeftFrom(Node<T> from) {
		while (from.left != null) {
			from = from.left;
		}
		return from;
	}

	private Node<T> getFirstParentGreater(Node<T> node) {
		while (node.parent != null && node.parent.left != node) {
			node = node.parent;
		}
		return node.parent;
	}

	private class TreeSetIterator implements Iterator<T> {
		Node<T> current = root == null ? root : getMostLeftFrom(root);
		Node<T> previous = null;
		boolean isWasNext = false;

		@Override
		public boolean hasNext() {

			return current != null;
		}

		@Override
		public T next() {// TODO done
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T res = current.obj;
			previous = current;
			current = current.right != null ? getMostLeftFrom(current.right) : getFirstParentGreater(current);
			isWasNext = true;
			return res;
		}

		@Override
		public void remove() {// TODO done
			if (!isWasNext) {
				throw new IllegalStateException();
			}
			if (isJunction(previous)) {
				current = previous;
			}
			removeNode(previous);
			isWasNext = false;
		}

	}

	@Override
	public boolean add(T obj) {
		if (root == null) {
			addRoot(obj);
			size++;
			return true;
		}
		Node<T> parent = getParent(obj);
		// If obj already exists getParent will return null
		if (parent == null) {
			return false;
		}
		Node<T> node = new Node<>(obj);
		if (comp.compare(obj, parent.obj) < 0) {
			parent.left = node;
		} else {
			parent.right = node;
		}
		node.parent = parent;
		size++;

		return true;
	}

	private Node<T> getParent(T obj) {
		Node<T> current = root;
		Node<T> parent = null;
		while (current != null) {
			int res = comp.compare(obj, current.obj);
			if (res == 0) {
				return null;
			}
			parent = current;
			current = res < 0 ? current.left : current.right;

		}
		return parent;
	}

	private void addRoot(T obj) {
		root = new Node<>(obj);

	}

	@Override
	public T remove(T pattern) {
		Node<T> removedNode = getNode(pattern);
		if (removedNode == null) {
			return null;
		}
		removeNode(removedNode);
		return removedNode.obj;
	}

	private void removeNode(Node<T> removedNode) {
		if (isJunction(removedNode)) {
			removeJunction(removedNode);
		} else if (removedNode == root) {
			removeRoot();
		} else {
			removeNonJunction(removedNode);
		}
		size--;
	}

	private void removeNonJunction(Node<T> removedNode) {
		Node<T> child = removedNode.right == null ? removedNode.left : removedNode.right;
		Node<T> parent = removedNode.parent;
		if (parent.right == removedNode) {
			parent.right = child;
		} else {
			parent.left = child;
		}
		if (child != null) {
			child.parent = parent;
		}

	}

	private void removeJunction(Node<T> removedNode) {
		Node<T> substitute = getMostLeftFrom(removedNode.right);
		removedNode.obj = substitute.obj;
		removeNonJunction(substitute);

	}

	private boolean isJunction(Node<T> node) {

		return node.left != null && node.right != null;
	}

	private void removeRoot() {
		root = root.right == null ? root.left : root.right;
		if (root != null) {
			root.parent = null;
		}
	}

	private Node<T> getNode(T pattern) {
		Node<T> current = root;
		while (current != null && !current.obj.equals(pattern)) {
			current = comp.compare(pattern, current.obj) > 0 ? current.right : current.left;
		}
		return current;
	}

	@Override
	public Iterator<T> iterator() {

		return new TreeSetIterator();
	}

	@Override
	public boolean contains(T pattern) {

		return getParent(pattern) == null;
	}

	public int width() {

		return width(root);
	}

	private int width(Node<T> rootTmp) {
		if (rootTmp == null) {
			return 0;
		}
		if (rootTmp.left == null && rootTmp.right == null) {
			return 1;
		}
		return width(rootTmp.left) + width(rootTmp.right);
	}

	public int height() {
		return height(root);

	}

	private int height(Node<T> rootTmp) {
		if (rootTmp == null) {
			return 0;
		}
		int hl = height(rootTmp.left);
		int hr = height(rootTmp.right);
		return 1 + Math.max(hl, hr);

	}

	public void displayTree()  {
		
		displayTree(0, root);
	}

	private void displayTree(int level, Node<T> rootTmp) {
		if (rootTmp != null) {
			displayTree(level + 1, rootTmp.right);
			displayRoot(level, rootTmp);
			displayTree(level + 1, rootTmp.left);
		}

	}

	private void displayRoot(int level, Node<T> rootTmp) {
		System.out.print(" | ".repeat(level * SPACE_LEVEL));
		System.out.println(rootTmp.obj.toString());
	}

	public int sumOfMaxBranch()  { // TODO done
		if (root.obj instanceof Integer) {
			return sumOfMaxBrunch(root);
		}
		 return -1;
	}

	private int sumOfMaxBrunch(Node<T> rootTmp) {
		if (rootTmp == null) {
			return 0;
		}
		int sumLeft = sumOfMaxBrunch(rootTmp.left);
		int sumRight = sumOfMaxBrunch(rootTmp.right);
		return (Integer)(rootTmp.obj) + Math.max(sumRight, sumLeft);

	}

	public void displayTreeFileSystem() {
		displayRoot(0, root);
		displayTreeFileSystem(0, root);
	}

//TODO done
	private void displayTreeFileSystem(int level, Node<T> rootTmp) {
		if (rootTmp != null) {
			displayTreeFileSystem(level + 1, rootTmp.right);
			if (rootTmp != root) {
				displayRoot(level, rootTmp);
			}
			displayTreeFileSystem(level + 1, rootTmp.left);
		}
	}
}