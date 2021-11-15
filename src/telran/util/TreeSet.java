package telran.util;

import java.util.Comparator;
import java.util.Iterator;

public class TreeSet<T> extends AbstractSet<T> {
	private static class Node<T> {
		T obj;
		Node<T> left;// ref to all nodes containing objects less then obj
		Node<T> right;// ref to all nodes containing objects grater then obj
		Node<T> parent;// ref to parent

		Node(T obj) {
			this.obj = obj;
		}
	}

	private Node<T> root;
	private Comparator<T> comp;

	public TreeSet(Comparator<T> comp) {
		this.comp = comp;
	}

	@SuppressWarnings("unchecked")
	public TreeSet() {
		this.comp = (Comparator<T>) Comparator.naturalOrder();
	}

	private class TreeSetIterator<T> implements Iterator<T> {
		private Node<T> current = getMostLeftNode((Node<T>) root);
		private Node<T> mostRightNode = getMostRightNode();

		// TODO
		// Done!
		@Override
		public boolean hasNext() {
			return current != null;
		}

		// TODO
		// Done!
		@Override
		public T next() {
			T res = current.obj;
			current = current.right != null 
					? getMostLeftNode(current.right) 
						: getFirstGreaterParent(current);
			return res;
		}

		private Node<T> getMostLeftNode(Node<T> node) {
			Node<T> curNode = node;
			while (curNode.left != null) {
				curNode = curNode.left;
			}
			return curNode;
		}

		private Node<T> getFirstGreaterParent(Node<T> node) {
			if (mostRightNode == node) {
				return null;
			}
			Node<T> curNode = node;
			if (node.parent != null) {
				while (curNode.parent.right == node) {
					curNode = curNode.parent;
				}
			}
			
			return curNode.parent;
		}

		private Node<T> getMostRightNode() {
			Node<T> curNode = (Node<T>) root;
			while (curNode.right != null) {
				curNode = curNode.right;
			}
			return curNode;
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
		// if obj already exists get parent will return null
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
		// TODO next HW
		return null;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO
		// Done!
		return new TreeSetIterator<T>();
	}

	@Override
	public boolean contains(T pattern) {
		return getParent(pattern) == null;
	}

}
