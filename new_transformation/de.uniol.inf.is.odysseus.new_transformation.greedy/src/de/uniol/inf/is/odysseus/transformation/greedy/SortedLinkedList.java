package de.uniol.inf.is.odysseus.transformation.greedy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.costmodel.base.IPOTransformator;

/**
 * A {@link LinkedList} sorted by the Priority of the {@link IPOTransformator}s
 * for easier access.
 * 
 */
public class SortedLinkedList implements Iterable<IPOTransformator<ILogicalOperator>> {
	private LinkedList<IPOTransformator<ILogicalOperator>> list = new LinkedList<IPOTransformator<ILogicalOperator>>();

	public void addPrioritised(IPOTransformator<ILogicalOperator> transformator) {
		int priority = transformator.getPriority();

		ListIterator<IPOTransformator<ILogicalOperator>> iterator = list.listIterator();
		while (iterator.hasNext()) {
			IPOTransformator<ILogicalOperator> nextTransformator = iterator.next();
			if (nextTransformator.getPriority() <= priority) {
				break;
			}
		}

		iterator.add(transformator);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public Iterator<IPOTransformator<ILogicalOperator>> iterator() {
		return list.iterator();
	}
}
