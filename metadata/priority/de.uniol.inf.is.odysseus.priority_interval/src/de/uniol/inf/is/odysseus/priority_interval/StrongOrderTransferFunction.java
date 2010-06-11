package de.uniol.inf.is.odysseus.priority_interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferFunction;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class StrongOrderTransferFunction<K extends ITimeInterval & IPriority, T extends IMetaAttributeContainer<K>>
		extends TITransferFunction<T> {

	@SuppressWarnings("unchecked")
	@Override
	public void newHeartbeat(PointInTime heartbeat, int port) {
		ArrayList<T> output = new ArrayList<T>();
		PointInTime minimum = null;
		synchronized (minTs) {
			minTs[port] = heartbeat;
			if (minTs[0] != null && minTs[1] != null) {
				minimum = PointInTime.min(minTs[0], minTs[1]);
			}
		}
		if (minimum != null) {
			synchronized (this.out) {
				// don't use an iterator, it does NOT guarantee ordered
				// traversal!
				T elem = this.out.peek();
				while (elem != null
						&& elem.getMetadata().getStart()
								.beforeOrEquals(minimum)) {
					this.out.poll();
					output.add(elem);
					elem = this.out.peek();
				}

				Object[] sortedOutput = output.toArray();
				Arrays.sort(sortedOutput, new Comparator<Object>() {

					@Override
					public int compare(Object o1, Object o2) {
						T t1 = (T) o1;
						T t2 = (T) o2;
						byte t1priority = t1.getMetadata().getPriority();
						byte t2priority = t2.getMetadata().getPriority();
						if (t1priority > t2priority) {
							return -1;
						} else {
							if (t1priority < t2priority) {
								return 1; 
							} else {
								return t1.getMetadata().compareTo(t2.getMetadata());
							}
						}
					}
				});
				for (Object element : sortedOutput) {
					transfer((T) element);
				}
			}
		}
	}

}
