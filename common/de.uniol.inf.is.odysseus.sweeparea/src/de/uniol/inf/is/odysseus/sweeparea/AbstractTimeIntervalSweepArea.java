package de.uniol.inf.is.odysseus.sweeparea;

import java.util.Collections;
import java.util.List;

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
	
	/**
	 * <p>
	 * This is <code>true</code> iff the end timestamp of the data stream
	 * elements in this sweep area are in ascending order. We need this
	 * information to decide if we can use the binary search in
	 * {@link AbstractTimeIntervalSweepArea#extractElements(IStreamObject, de.uniol.inf.is.odysseus.core.Order)}
	 * (e.g., in DefaultTISweepArea) or similar methods.
	 * 
	 * <p>
	 * XXX: This flag is set to <code>true</code> only if the end timestamp has
	 * a strictly increasing order (no equal values). For the current
	 * implementation of the binary search it is necessary to ensure that the
	 * resulting element of the binary search is the last invalid element in the
	 * list. The alternative would be to check after the binary search if there
	 * other elements with the same end ts.
	 */
	protected boolean hasEndTsOrder = true;

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
		if (this.comparator == null || this.getElements().size() == 0
				|| this.comparator.compare(this.getElements().get(this.getElements().size() - 1), element) <= 0) {
			// add the element to the end of the list

			hasEndTsOrder = isStillInEndTsOrderAfterInsert(element, this.getElements().size());
			setLatestTimeStamp(element);
			this.getElements().add(element);
		} else {
			// find the position where the element should be inserted
			int pos = Collections.binarySearch(this.getElements(), element, this.comparator);
			if (pos >= 0) {
				// there is one ore more elements with the same ordering key

				// find the position of the last element with the same ordering
				// key
				while (this.comparator.compare(this.getElements().get(pos), element) == 0) {
					++pos;
				}
			} else {
				pos = (-(pos) - 1);
			}
			hasEndTsOrder = isStillInEndTsOrderAfterInsert(element, pos);
			setLatestTimeStamp(element);
			this.getElements().add(pos, element);
		}
	}
	
	/**
	 * <p>
	 * Checks if the end timestamp order is given after insertion of
	 * <code>element</code> at pos <code>pos</code>.
	 * 
	 * <p>
	 * If the list of elements is empty, this method returns always true.
	 * 
	 * <p>
	 * If the elements are unordered already (hasEndTsOrder is false and the
	 * list of elements is not empty), this method returns false.
	 * 
	 * @param element
	 *            The element to insert.
	 * @param pos
	 *            The position where the element should be inserted.
	 * @return True, if the ordering of the end timestamp is still given after
	 *         the insert, falser otherwise.
	 */
	protected boolean isStillInEndTsOrderAfterInsert(T element, int pos) {
		if (this.getElements().size() == 0) {
			// If there are no elements in this sweep area, the end ts order is
			// obviously not false. So, we give it a new chance.
			return true;
		}
		if(!hasEndTsOrder) {
			return false;
		}
		if (pos == 0) {
			// insertion at start of the list
			PointInTime nextEndTs = this.getElements().get(1).getMetadata().getEnd();
			return nextEndTs.after(element.getMetadata().getEnd());
		} else if (pos == this.getElements().size()) {
			// insertion at the end of the list
			PointInTime prevEndTs = this.getElements().get(this.getElements().size() - 1).getMetadata().getEnd();
			return prevEndTs.before(element.getMetadata().getEnd());
		} else {
			PointInTime prevEndTs = this.getElements().get(pos - 1).getMetadata().getEnd();
			PointInTime nextEndTs = this.getElements().get(pos).getMetadata().getEnd();
			return nextEndTs.after(element.getMetadata().getEnd()) && prevEndTs.before(element.getMetadata().getEnd());
		}
	}


	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.AbstractSweepArea#insertAll(java.util.List)
	 */
	@Override
	public void insertAll(List<T> toBeInserted) {
		// XXX: check if it is ordered by end ts?
		// otherwise we could not make sure that it is ordered by end ts, therefore:
		hasEndTsOrder = false;
		super.insertAll(toBeInserted);
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
		if(LOG.isDebugEnabled()) {
			LOG.debug("Latest tuple: {}", this.lastInserted);
		}
		return (this.lastInserted != null ? this.lastInserted.getMetadata().getEnd() : null);
	}
	
	@Override
	public String getName() {
		return this.getClass().getName();
	}
	
	public boolean hasEndTsOrder() {
		return hasEndTsOrder;
	}
}
