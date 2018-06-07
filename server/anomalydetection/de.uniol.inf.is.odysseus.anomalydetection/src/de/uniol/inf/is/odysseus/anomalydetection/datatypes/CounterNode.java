package de.uniol.inf.is.odysseus.anomalydetection.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a tree data structure which saves the given objects as children and
 * counts, how often they were added. If you add an equal child twice, there
 * will be just one child, but with the count = 2.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 *            The datatype you want to save in the tree, e.g. Tuple, String,
 *            etc.
 */
public class CounterNode {

	private long count;
	private Object[] object;
	private transient CounterNode parent;
	private List<CounterNode> children;

	/**
	 * Creates a new node in the tree (or a new tree at all).
	 * 
	 * @param parent
	 *            The parent of this node. If it's the root, just set the parent
	 *            to null.
	 */
	public CounterNode(CounterNode parent) {
		this.parent = parent;
		children = new ArrayList<CounterNode>();
		this.count = 1;
	}

	/**
	 * Adds a child. If there's already an equal child, the counter of the child
	 * will be incremented by 1. Else, there will be a new child with the given
	 * object and count = 1
	 * 
	 * @param object
	 *            The object to add to the tree
	 * @return The child node (either the new child or the child which already
	 *         existed)
	 */
	public CounterNode addChild(Object[] object) {

		CounterNode child = new CounterNode(this);
		child.setObject(object);

		if (children.contains(child)) {
			// There is already a child with this object
			child = children.get(children.indexOf(child));
			child.count++;
			return child;
		}
		// We have to create a new child
		children.add(child);
		return child;
	}

	/**
	 * If the tree has a (direct) child with an object equal to the given
	 * object, the child will be returned. If not, null will be returned.
	 * 
	 * @param object
	 *            The object which is equal to the object of the child you
	 *            search for.
	 * @return The child, if it exists.
	 */
	@SuppressWarnings("unlikely-arg-type")
	public CounterNode getChild(Object[] object) {
		if (children.contains(object)) {
			// There is already a child with this object
			return children.get(children.indexOf(object));
		}
		return null;
	}

	/**
	 * Calculates the relative frequency of the path. The relative frequency of
	 * each node from the root to this node is multiplied. E.g. if, the parent
	 * node has a relative frequency of 0.5 (which means, that half of the
	 * objects are in the parent node and the other half in the siblings of the
	 * parents node) and this node has a frequency of 0.5 and the parent has
	 * only the root on top of it, the relative frequency of the path is 0.5 *
	 * 0.5 = 0.25.
	 * 
	 * @return The relative frequency of the path.
	 */
	public double calcRelativeFrequencyPath() {
		double relativeFrequency = 1;
		CounterNode currentNode = this;
		// The second check is needed to avoid that we use the root
		while (currentNode != null && currentNode.getParent() != null) {
			relativeFrequency *= currentNode.calcRelativeFrequencyToSiblings();
			currentNode = currentNode.getParent();
		}
		return relativeFrequency;
	}

	/**
	 * Checks, if a node in the path to the root has a lower frequency than the
	 * given minimal relative frequency.
	 * 
	 * @param minRelativeFrequencyNode
	 *            The minimal allowed relative frequency
	 * @return True, is there is a node with a lower relative frequency, false,
	 *         if not
	 */
	public boolean pathHasSeldomNode(double minRelativeFrequencyNode) {
		CounterNode currentNode = this;
		while (currentNode != null) {
			double relativeFrequency = currentNode.calcRelativeFrequencyToSiblings();
			if (relativeFrequency < minRelativeFrequencyNode)
				return true;
			currentNode = currentNode.getParent();
		}
		return false;
	}

	/**
	 * Calculates the relative frequency of this node in relation to the
	 * siblings. If there are this node and two siblings (means, three children
	 * of the parent) and every node has a count of 5, the parent count is 15.
	 * The relative frequency of this node is 5 / 15 = 0.333.
	 * 
	 * @return The relative frequency of this node
	 */
	public double calcRelativeFrequencyToSiblings() {
		if (this.getParent() != null) {
			// This is the case if our direct parent is the root
			// As the root does not count (cause the case "root" never occurs)
			// we have to sum the counts of all children of the root
			double subTreeCount = 0;
			for (CounterNode child : this.getParent().getChildren()) {
				subTreeCount += child.getCount();
			}
			return this.getCount() / subTreeCount;
		}
		return 1;
	}

	/**
	 * 
	 * @return The object which specifies this node
	 */
	public Object[] getObject() {
		return object;
	}

	public void setObject(Object[] object) {
		this.object = object;
	}

	/**
	 * 
	 * @return The count of this node. How often there was something added to
	 *         this node.
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 
	 * @return The parent node of this node
	 */
	public CounterNode getParent() {
		return parent;
	}

	/**
	 * Set the parent of this node
	 * 
	 * @param parent
	 *            The new parent of this node
	 */
	public void setParent(CounterNode parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * @return The children of this node
	 */
	public List<CounterNode> getChildren() {
		return children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(object);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CounterNode other = (CounterNode) obj;
		if (!Arrays.equals(object, other.object))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String result = "(";
		boolean first = true;
		for (Object o : object) {
			if (!first) {
				result += ",";
				first = false;
			}
			result += o.toString();
		}
		result += " | " + calcRelativeFrequencyToSiblings() + ")";
		return result;
	}

}
