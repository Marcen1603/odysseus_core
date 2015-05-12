package de.uniol.inf.is.odysseus.condition.datatypes;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class CounterNode<T> {

	private long count;
	private T object;
	private CounterNode<T> parent;
	private List<CounterNode<T>> children;

	public CounterNode(CounterNode<T> parent) {
		this.parent = parent;
		children = new ArrayList<CounterNode<T>>();
		this.count = 1;
	}

	public CounterNode<T> addChild(T object) {

		CounterNode<T> child = new CounterNode<T>(this);
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

	public CounterNode<T> getChild(T object) {
		if (children.contains(object)) {
			// There is already a child with this object
			return children.get(children.indexOf(object));
		}
		return null;
	}

	public double calcRelativeFrequencyPath() {
		double relativeFrequency = 1;
		CounterNode<T> currentNode = this;
		// The second check is needed to avoid that we use the root
		while (currentNode != null && currentNode.getParent() != null) {
			relativeFrequency *= currentNode.calcRelativeFrequencyToSiblings();
			currentNode = currentNode.getParent();
		}
		return relativeFrequency;
	}

	public double calcRelativeFrequencyToSiblings() {
		// The second check is needed to avoid that we use the root
		if (this.getParent() != null && this.getParent().getParent() != null) {
			double subTreeCount = this.getParent().getCount();
			return this.getCount() / subTreeCount;
		} else if (this.getParent() != null) {
			// This is the case if our direct parent is the root
			// As the root does not count (cause the case "root" never occurs)
			// we have to sum the counts of all children of the root
			double subTreeCount = 0;
			for (CounterNode<T> child : this.getParent().getChildren()) {
				subTreeCount += child.getCount();
				return this.getCount() / subTreeCount;
			}
		}
		return 1;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public long getCount() {
		return count;
	}

	public CounterNode<T> getParent() {
		return parent;
	}

	public List<CounterNode<T>> getChildren() {
		return children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CounterNode other = (CounterNode) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String toString() {

		String result = "";
		if (object instanceof Tuple) {
			result = "(";
			Tuple objectTuple = (Tuple) object;
			Object[] attributes = objectTuple.getAttributes();
			for (int i = 0; i < attributes.length; i++) {
				result += attributes[i];
			}
			result += " | " + this.calcRelativeFrequencyToSiblings() + ")";
		} else {
			result = "(" + object + ")";
		}

		return result;
	}

}
