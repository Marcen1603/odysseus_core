package de.uniol.inf.is.odysseus.core.server.physicaloperator.sa;

import java.util.ListIterator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataComparator;


public abstract class AbstractTimeIntervalSweepArea<T extends IStreamObject<? extends ITimeInterval>> extends
		AbstractSweepArea<T> implements ITimeIntervalSweepArea<T>{

	protected PointInTime lastInserted = null;

	public AbstractTimeIntervalSweepArea() {
		super();
	}

	
	public AbstractTimeIntervalSweepArea(FastArrayList<T> fastArrayList,
			MetadataComparator<ITimeInterval> metadataComparator) {
		super(fastArrayList, metadataComparator);
	}

	public AbstractTimeIntervalSweepArea(IFastList<T> list,
			MetadataComparator<ITimeInterval> metadataComparator) {
		super(list, metadataComparator);
	}

	public AbstractTimeIntervalSweepArea(
			AbstractTimeIntervalSweepArea<T> defaultTISweepArea) throws InstantiationException, IllegalAccessException {
		super(defaultTISweepArea);
	}

	@Override
	abstract public AbstractTimeIntervalSweepArea<T> clone();
	
	@Override
	public void insert(T element) {
		if (this.comparator == null) {
			setLatestTimeStamp(element.getMetadata().getEnd());
			this.getElements().add(element);
			return;
		}
		ListIterator<T> li = this.getElements().listIterator(
				this.getElements().size());
		// starts from end and inserts the element s if li.previous is at least
		// equal (<=0) to s
		// 0 instead of -1 ensures that the area is insertion safe
		while (li.hasPrevious()) {
			if (this.comparator.compare(li.previous(), element) <= 0) {
				li.next();
				li.add(element);
				return;
			}
		}
		setLatestTimeStamp(element.getMetadata().getEnd());
		this.getElements().add(0, element);
	}
	
	/**
	 * Set the latest end timestamp for this sweeparea. Should happen on insert.
	 * 
	 * @param current end timestamp of the currently inserted element.
	 */
	protected void setLatestTimeStamp(PointInTime current) {
		if (this.lastInserted == null || current.after(this.lastInserted)) {
			this.lastInserted = current;
		}
	}
	
	@Override
	public PointInTime getMaxEndTs() {
		return this.lastInserted;
	}
}
