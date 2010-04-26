package de.uniol.inf.is.odysseus.broker.physicaloperator.predicate;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

/**
 * This comparator compares two {@link ITimeInterval} objects.
 * The comparison is only based on the start point of the {@link ITimeInterval}s
 * 
 *  @author Dennis Geesen
 *
 * @param <T> the generic type
 */
public class TimeIntervalComparator<T extends IMetaAttributeContainer<ITimeInterval>> implements Comparator<T> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(T one, T two) {
		if(TimeInterval.startsBefore(one.getMetadata(), two.getMetadata())){
			return -1;
		}else{
			if(one.getMetadata().getStart().equals(two.getMetadata().getStart())){
				return 0;
			}else{
				return 1;
			}
		}
	}
}
