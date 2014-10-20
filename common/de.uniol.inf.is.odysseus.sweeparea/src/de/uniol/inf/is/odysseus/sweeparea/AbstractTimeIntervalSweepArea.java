package de.uniol.inf.is.odysseus.sweeparea;

import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;


public abstract class AbstractTimeIntervalSweepArea<T extends IStreamObject<? extends ITimeInterval>> extends
		AbstractSweepArea<T> implements ITimeIntervalSweepArea<T>{
		
	private static final long serialVersionUID = -2093058769784619137L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractTimeIntervalSweepArea.class);
	
	protected T lastInserted = null;

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
			setLatestTimeStamp(element);
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
				setLatestTimeStamp(element);
				li.add(element);
				return;
			}
		}
		setLatestTimeStamp(element);
		this.getElements().add(0, element);
	}
	
	/**
	 * Set the latest end timestamp for this sweeparea. Should happen on insert.
	 * 
	 * @param current end timestamp of the currently inserted element.
	 */
	protected void setLatestTimeStamp(T current) {
		if (this.lastInserted == null || current.getMetadata().getEnd().after(this.lastInserted.getMetadata().getEnd())) {
			this.lastInserted = current;
		}
	}
	
	@Override
	public PointInTime getMaxEndTs() {
		LOG.debug("Latest tuple: {}", this.lastInserted);
		return (this.lastInserted != null ? this.lastInserted.getMetadata().getEnd() : null);
	}
}
