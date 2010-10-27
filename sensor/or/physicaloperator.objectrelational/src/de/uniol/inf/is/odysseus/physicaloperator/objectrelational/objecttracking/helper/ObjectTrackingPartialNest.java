package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.helper;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;

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
public class ObjectTrackingPartialNest<M extends ObjectTrackingMetadata<Object>> 
	extends MetaAttributeContainer<M> { 

	private static final long serialVersionUID = 1L;
	private List<MVRelationalTuple<M>> partial;
	
	public ObjectTrackingPartialNest() {
		this.partial = new ArrayList<MVRelationalTuple<M>>();
	}
	
	/**
	 * Used for initializing a new partial nest in the update process.
	 * @param t incoming tuple
	 */
	public ObjectTrackingPartialNest(MVRelationalTuple<M> t) {
		this.partial = new ArrayList<MVRelationalTuple<M>>();
		this.partial.add(t);
		
		this.setMetadata(t.getMetadata());
	}

	/**
	 * Used for merging in the update process.
	 * 
	 * @param t tuples in the partial nest
	 * @param ti time interval to set
	 */
	public ObjectTrackingPartialNest(
		List<MVRelationalTuple<M>> t, 
		M meta
	) {
		this.partial = t;	
		this.setMetadata(meta);
	}	
	
	/**
	 * The clone method is essential for splitting and merging of 
	 * partial nests. 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ObjectTrackingPartialNest<M> clone() {
		ObjectTrackingPartialNest<M> klone = new ObjectTrackingPartialNest<M>();
		for(MVRelationalTuple<M> t : this.getNest()) {
			klone.add(t.clone());
		}
		klone.setMetadata((M) this.getMetadata().clone());
		return klone;
	}

	public void add(MVRelationalTuple<M> t) {
		this.partial.add(t);
	}
	
	public List<MVRelationalTuple<M>> getNest() {
		return this.partial;
	}
	
	public Integer getSize() {
		return partial.size();
	}
	
	@Override
	public String toString(){
		return this.getMetadata().getStreamTime().toString() + " Size: " + this.getSize();
	}
}
