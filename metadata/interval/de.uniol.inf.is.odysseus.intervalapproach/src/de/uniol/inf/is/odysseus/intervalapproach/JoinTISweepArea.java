package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

public class JoinTISweepArea<T extends IMetaAttribute<? extends ITimeInterval>>
		extends DefaultTISweepArea<T> {

	public void insert(T s) {
		synchronized (this.elements) {
			this.elements.addLast(s);
		}
	};
	
	public Iterator<T> queryCopy(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		synchronized(this.elements){
			switch (order) {
			case LeftRight:
				for (T next : this.elements) {
					if (TimeInterval.totallyBefore(next.getMetadata(), element
							.getMetadata())) {
						continue;
					}
					if (TimeInterval.totallyAfter(next.getMetadata(), element
							.getMetadata())) {
						break;
					}
					if (getQueryPredicate().evaluate(element, next)) {
						result.add(next);
					}
				}
				break;
			case RightLeft:
				for (T next : this.elements) {
					if (TimeInterval.totallyBefore(next.getMetadata(), element
							.getMetadata())) {
						continue;
					}
					if (TimeInterval.totallyAfter(next.getMetadata(), element
							.getMetadata())) {
						break;
					}
					if (getQueryPredicate().evaluate(next, element)) {
						result.add(next);
					}
				}
				break;
			}
		}
		return result.iterator();
	}
}
