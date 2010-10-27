package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.helper;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;

/**
 * This class represents a partial set of relational tuples. The partial 
 * can be initialized, merged and evaluated. While initializing the first
 * relational tuple candidate is added to a list, representing the set. 
 * While merging more relational tuples are added to the list. While 
 * evaluating the list is nested into a relational tuple. 
 * 
 * size attribute is used to optimize the merge process between to 
 * partial nests.
 * 
 * @author Jendrik Poloczek
 */
public class PartialNest<M extends ITimeInterval> 
	extends MetaAttributeContainer<TimeInterval> { 

	private static final long serialVersionUID = 1L;
	private List<ObjectRelationalTuple<TimeInterval>> partial;
	
	public PartialNest() {
		this.partial = new ArrayList<ObjectRelationalTuple<TimeInterval>>();
	}
	
	/**
	 * Used for initializing a new partial nest in the update process.
	 * @param t incoming tuple
	 */
	public PartialNest(ObjectRelationalTuple<TimeInterval> t) {
		this.partial = new ArrayList<ObjectRelationalTuple<TimeInterval>>();
		this.partial.add(t);
		this.setMetadata(
			new TimeInterval(
				new PointInTime(t.getMetadata().getStart()),
				new PointInTime(t.getMetadata().getEnd())
			)
		);
	}

	/**
	 * Used for merging in the update process.
	 * 
	 * @param t tuples in the partial nest
	 * @param ti time interval to set
	 */
	public PartialNest(
		List<ObjectRelationalTuple<TimeInterval>> t, 
		TimeInterval ti
	) {
		this.partial = t;
		this.setMetadata(ti);
	}	
	
	/**
	 * The clone method is essential for splitting and merging of 
	 * partial nests. 
	 */
	@Override
	public PartialNest<TimeInterval> clone() {
		PartialNest<TimeInterval> klone = new PartialNest<TimeInterval>();
		for(ObjectRelationalTuple<TimeInterval> t : this.getNest()) {
			klone.add(t.clone());
		}
		klone.setMetadata(this.getMetadata().clone());
		return klone;
	}

	public void add(ObjectRelationalTuple<TimeInterval> t) {
		this.partial.add(t);
	}
	
	public List<ObjectRelationalTuple<TimeInterval>> getNest() {
		return this.partial;
	}
	
	public Integer getSize() {
		return partial.size();
	}
}
