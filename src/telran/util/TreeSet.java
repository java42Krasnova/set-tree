package telran.util;

import java.util.Comparator;
import java.util.Iterator;

import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

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
		Node<T> prevNode;

		@Override
		public boolean hasNext() {

			return current != null;
		}

		@Override
		public T next() {
			T res = current.obj;
			prevNode = current;
			current = current.right != null ? getMostLeftFrom(current.right) : getFirstParentGreater(current);
			return res;
		}
		
		@Override
		public void remove() {
			// TODO	
			removeNode(prevNode);

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
		// TODO update a method by applying another algorithm
		//Done!
		 if (isJunction(removedNode)) {
			removeJunction(removedNode);
		} else {
			removeNonJunction(removedNode);
		}
		size--;
	}

	private boolean isJunction(Node<T> removedNode) {
		return removedNode.left != null && removedNode.right != null;
	}
	private void removeJunction(Node<T> removedNode) {
		Node<T> substitution = getMostLeftFrom(removedNode.right);
		
		Node<T> parent = removedNode.parent;
		if(size==1)
		{
			root = null;
		}
		if(removedNode!=root)
		{	
		 if (parent.right==removedNode) {
			 substitution = getMostLeftFrom(removedNode);
			 parent.right=substitution;
			 if(substitution.parent.left==substitution)
			 {
				 removedNode.left=null;
			 }
			
		}  else {
			parent.left=substitution;
		}
		}  else {
			root = substitution;
			root.parent = null;
			substitution.left=removedNode.left;
			//substitution.right=removedNode.right;
			//substitution.parent.left=null;
			return;
		}
		substitution.left=removedNode.left;
		substitution.right=removedNode.right;
		if(substitution.left!=null) {
		substitution.left.parent=substitution;}
		substitution.parent=parent;
		
	}

	private void removeNonJunction(Node<T> removedNode) {
		if(removedNode==root) {
			 root =removedNode.right == null ? removedNode.left:removedNode.right;
		removedNode.parent=null;
		}
		else {
			
			Node<T> parent = removedNode.parent;
			Node<T> child = removedNode.right == null ? removedNode.left:removedNode.right;
				if(parent.left==removedNode) {
					parent.left=child;
				}else {
					parent.right=child;
				}
				if(child!=null) {
					child.parent=parent;
				}
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

}