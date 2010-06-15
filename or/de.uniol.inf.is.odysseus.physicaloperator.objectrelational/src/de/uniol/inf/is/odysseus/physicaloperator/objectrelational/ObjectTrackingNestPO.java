package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.
    physicaloperator.objectrelational.helper.ObjectTrackingNestTISweepArea;

import de.uniol.inf.is.odysseus.
    physicaloperator.objectrelational.helper.ObjectTrackingPartialNest;

import de.uniol.inf.is.odysseus.physicaloperator.
    objectrelational.helper.ObjectTrackingPriorityQueueG;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * NestPO
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingNestPO extends
		AbstractPipe<MVRelationalTuple<ObjectTrackingMetadata<Object>>, MVRelationalTuple<ObjectTrackingMetadata<Object>>> {
	
	private SDFAttributeList inputSchema;
	private SDFAttributeList outputSchema;
	private SDFAttributeList groupingAttributes;
	private SDFAttribute nestingAttribute;

	private int maxIdOfKeyMap = 0;
	private int groupingAttributesPos[];
	private int nonGroupingAttributesPos[];
	private int nestingAttributePos;
	private int inputAttributesCount;
	private int outputAttributesCount;
	
	/*
	 * groups is a HashMap groupid to sweeparea, the elements in the 
	 * sweep area are partial sets with time interval approach metadata
	 */
	
	private Map<Integer, ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>>> groups;
	
	/*
	 * keyMap maps restricted tuples (to grouping attributes) to a key, 
	 * if grouping doesn't match a key, maxId is incremented and used. 
	 */
	
	private Map<MVRelationalTuple<ObjectTrackingMetadata<Object>>, Integer> keyMap;
	
	/*
	 * MVPriorityQueueG stores the sweep areas ascending to the smallest 
	 * start time stamp of containing partial nests.   
	 */
	
	private ObjectTrackingPriorityQueueG<ObjectTrackingMetadata<Object>> g;
	
	/*
	 * q is a default min-priority queue for relational tuples according 
	 * to their time stamps.
	 */
	
	private DefaultTISweepArea<MVRelationalTuple<ObjectTrackingMetadata<Object>>> q;
	
	/**
	 * @param groupingAttributes attributes to group by
	 * @param nestingAttribute nesting attribute
	 */
	
	public ObjectTrackingNestPO() {
		this.groups = 
			new HashMap<
				Integer, 
				ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>>
			>();
		
		this.keyMap = 
			new HashMap<MVRelationalTuple<ObjectTrackingMetadata<Object>>, Integer>();
		
		this.g = new ObjectTrackingPriorityQueueG<ObjectTrackingMetadata<Object>>();
		this.q = new DefaultTISweepArea<MVRelationalTuple<ObjectTrackingMetadata<Object>>>();
	}

	public void setInputSchema(SDFAttributeList inputSchema) {
	    this.inputSchema = inputSchema;
	    this.inputAttributesCount = this.inputSchema.getAttributeCount();
	}
	
    public void setOutputSchema(SDFAttributeList outputSchema) {
        this.outputSchema = outputSchema;
    }
    
    public void setNestAttribute(SDFAttribute nestAttribute) {
        this.nestingAttribute = nestingAttribute.clone();
    }
    
    public void setGroupingAttributes(SDFAttributeList groupingAttributes) {
        this.groupingAttributes = groupingAttributes.clone();
        this.groupingAttributesPos = this.getGroupingAttributesPos();
        this.outputAttributesCount = this.groupingAttributesPos.length + 1;
        this.nonGroupingAttributesPos = this.getNonGroupingAttributePos();
        this.nestingAttributePos = this.groupingAttributesPos.length;
    }
	
	/**
	 * A lot of code for creating a deep copy of maps groups and keyMap.
	 * 
	 * @param relationalNestPO nesting plan operator to copy
	 */	
	public ObjectTrackingNestPO(ObjectTrackingNestPO nestPO) {
		super(nestPO);
		
		this.inputSchema = nestPO.inputSchema;
		this.outputSchema = nestPO.outputSchema;
		this.nestingAttribute = nestPO.nestingAttribute.clone();
		this.groupingAttributes = nestPO.groupingAttributes.clone();
		
		Iterator<Entry<MVRelationalTuple<ObjectTrackingMetadata<Object>>, Integer>> keyMapIter;
		Iterator<Entry<Integer, ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>>>> groupsIter;
		
		Entry<MVRelationalTuple<ObjectTrackingMetadata<Object>>, Integer> entryOfkeyMap;
		Entry<Integer, ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>>> entryOfGroups;
		
		keyMapIter = nestPO.keyMap.entrySet().iterator();
		groupsIter = nestPO.groups.entrySet().iterator();
	
		while(keyMapIter.hasNext()) {
		    entryOfkeyMap = keyMapIter.next();
		    
		    MVRelationalTuple<ObjectTrackingMetadata<Object>> tuple = 
                entryOfkeyMap.getKey();
		 
		    this.keyMap.put(
		       tuple.clone(),
		       entryOfkeyMap.getValue()
		    );
		}
		
		// FATAL ERROR IN RULE
		
		while(groupsIter.hasNext()) {
		    entryOfGroups = groupsIter.next();
		    ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>> sa = entryOfGroups.getValue();
		    
		    this.groups.put(
		        entryOfGroups.getKey(), 
		        sa.clone()
		    );
		}
		
		this.g = nestPO.g.clone();
		this.q = nestPO.q.clone();
		
	}

	@Override
	final protected void process_next(
		MVRelationalTuple<ObjectTrackingMetadata<Object>> object, 
		int port
	) {
		try {
			// do nothing
			transfer(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instead of transfering the objects to the subscriber, we return the 
	 * values to assert correctness in a test case.
	 * 
	 * @param object
	 * @param port
	 */	
	public final void processNextTest
		(MVRelationalTuple<ObjectTrackingMetadata<Object>> incomingTuple, int port) {
		
		ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>> sa;
		PointInTime minStart;
		PointInTime incomingTupleStart;
		Integer groupId;
		Boolean emptySA;
		
		minStart = null;
		incomingTupleStart = incomingTuple.getMetadata().getStart();
		
		/*
		 * Getting the groupId and the according sweep area with 
		 * partial nests. If not existent in groups, then create.
		 */
		
		groupId = this.getGroupId(incomingTuple);
		if(groups.containsKey(groupId)) {
			sa = groups.get(groupId);
		} else {
			
			/*
			 * Storing the values of the groupingAttributes in the 
			 * MVNestTISweepArea<ObjectTrackingMetadata<Object>> for evaluating (transform to relational 
			 * tuple output)
			 */
			
			Object[] groupingValues = this.getGroupingValues(incomingTuple);
			sa = new ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>>(groupingValues);
			groups.put(groupId, sa);
		}
		
		/*
		 * If the sweep area is newly created we have to put the 
		 * new groupId into the MVPriorityQueueG, later.
		 */
		
		if(sa.size() == 0) {
			emptySA = true;
		} else {
			emptySA = false;
		}
		
		MVRelationalTuple<ObjectTrackingMetadata<Object>> restrictedToNonGrouping 
			= (MVRelationalTuple<ObjectTrackingMetadata<Object>>) 
			incomingTuple.restrict(this.nonGroupingAttributesPos, false);
			
		this.update(sa, restrictedToNonGrouping);
		
		if(emptySA) {
			g.insert(sa, groupId);
		}
	
		/*
		 * Now we search for those partial nests which have minimum start
		 * timestamp. And if there are equal or less than write out.
		 */
		
		while(!g.isEmpty()) {
			Integer groupIdOfMin = g.min();
			
			ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>> saOfMin = 
				groups.get(groupIdOfMin);
			
			ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> minPartial = 
				saOfMin.iterator().next();
						
			if(
				minStart != null && 
				minStart == minPartial.getMetadata().getStart()
			) {
				break;
			}
			
			minStart = minPartial.getMetadata().getStart();
						
			if(minStart.before(incomingTupleStart) ||
				minStart.equals(incomingTupleStart)) {
	
				g.removeLastMin();
				Iterator<ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>> results = 
					saOfMin.extractElementsBefore(incomingTupleStart);
				
				/*
				 * Here happens the evaluating 
				 */
				
				while(results.hasNext()) {
					ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> result = results.next();
					
					Object[] groupingValues = saOfMin.getGroupingValues();
					MVRelationalTuple<ObjectTrackingMetadata<Object>> output = 
						this.createOutputTuple(groupingValues, result);
						
					q.insert(output);
				}
				
				if(saOfMin.size() > 0) {
					g.insert(saOfMin, groupIdOfMin);
				}				
			} else break;
		}
	}

	/**
	 * Instead of using the abstrace pipe method the isDoneTest is called
	 * by the NestPOTest when all tuples were sent. All unevaluated partials
	 * are written out. 
	 */
	public final void allTuplesSent() {
		while(!g.isEmpty()) {
			Integer groupIdOfMin = g.min();
			g.removeLastMin();
			
			ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>> saOfMin =
				groups.get(groupIdOfMin);
			
			Iterator<ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>> results =
				saOfMin.iterator();
			
			while(results.hasNext()) {
				ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> result = results.next();
				Object[] groupingValues = saOfMin.getGroupingValues();
				MVRelationalTuple<ObjectTrackingMetadata<Object>> output = 
					this.createOutputTuple(groupingValues, result);
					
				q.insert(output);
			}
		}
	}

	/**
	 * Deliver elements 
	 */
	public MVRelationalTuple<ObjectTrackingMetadata<Object>> deliver() {
		return this.q.poll();
	}

	public boolean isDone() {
		return (q.size() == 0);
	}
	
	@Override
    public void processPunctuation(PointInTime timestamp, int port) {
    	sendPunctuation(timestamp);
    }

    /*
     * Getter and setter for copy constructor. 
     */
    public SDFAttributeList getInputSchema() {
    	return this.inputSchema;
    }

    public SDFAttributeList getOutputSchema() {
    	return this.outputSchema;
    }

    public SDFAttributeList getGroupingAttributes() {
    	return this.groupingAttributes;
    }

    public SDFAttribute getNestingAttribute() {
    	return this.nestingAttribute;
    }

    @Override
    public ObjectTrackingNestPO clone() {
    	return new ObjectTrackingNestPO(this);
    }

    @Override
    public OutputMode getOutputMode() {
    	return OutputMode.MODIFIED_INPUT;
    }

    @Override
    public void addMonitoringData(String type, IMonitoringData<?> item) {
    	// TODO Auto-generated method stub
    	
    }

    @Override
    public <T> IMonitoringData<T> getMonitoringData(String type) {
    	// TODO Auto-generated method stub
    	return null;
    }

    @Override
    public <T> IPeriodicalMonitoringData<T> getMonitoringData(String type,
    		long period) {
    	// TODO Auto-generated method stub
    	return null;
    }

    @Override
    public Collection<String> getProvidedMonitoringData() {
    	// TODO Auto-generated method stub
    	return null;
    }

    @Override
    public boolean providesMonitoringData(String type) {
    	// TODO Auto-generated method stub
    	return false;
    }

    @Override
    public void removeMonitoringData(String type) {
    	// TODO Auto-generated method stub    	
    }
    
    /**
	 * 
	 * This method is used initially for determining the group attributes
	 * position with grouping attribute names as input.
	 * 
	 * @TODO optimize reduce to array
	 * @return array of positions
	 * 
	 */
	private int[] getGroupingAttributesPos() {
		int positions[];
		ArrayList<Integer> restrictIndex = new ArrayList<Integer>();
		
		for(SDFAttribute gAttribute : this.groupingAttributes) {			
			String gAttributeName = gAttribute.getAttributeName();
			for(int i = 0; i < this.inputSchema.getAttributeCount(); i++) {
				SDFAttribute tempA = this.inputSchema.getAttribute(i);
				if(tempA.getAttributeName().equals(gAttributeName)) {
					restrictIndex.add(i);
				}
			}
		}	
		
		positions = new int[restrictIndex.size()];
		for(int i = 0; i < restrictIndex.size(); i++) {
			positions[i] = restrictIndex.get(i);
		}
		return positions;
	} 
	
	/**
	 * 
	 * This method is called initially at constructor call to get an 
	 * int[] of positions of non-groupingAttributes for restricting 
	 * every relational tuple to them and convert them into a partial nest.
	 *  
	 * @return integer array of positions of non-grouping attribute positions
	 * 
	 */
	private int[] getNonGroupingAttributePos() {
		
		int index;
		int[] positions;
		
		if(this.groupingAttributesPos == null) 
			this.groupingAttributesPos = this.getGroupingAttributesPos();
		
		index = 0;
		
		positions = new int[
			this.inputAttributesCount - 
			this.getGroupingAttributesPos().length
		];
		
		for(int i = 0; i < this.inputAttributesCount; i++) {
			boolean isGroupingAttribute = false;
			for(int k = 0; k < this.groupingAttributesPos.length; k++) {
				if(this.groupingAttributesPos[k] == i)
					isGroupingAttribute = true;
			}
			if(!isGroupingAttribute) { 
				positions[index] = i;
				index++;
			}
		}
		return positions;
	}

	/**
	 * 
	 * Storing the values of the groupingAttributes in the 
	 * MVNestTISweepArea<ObjectTrackingMetadata<Object>> for evaluating (transform to relational 
	 * tuple output)
	 * 
	 * @param tuple
	 * @return
	 * 
	 */
	private Object[] getGroupingValues(
		MVRelationalTuple<ObjectTrackingMetadata<Object>> tuple) {
		
		int gAttributesCount;
		Object values[];
		
		gAttributesCount = this.groupingAttributes.size();
		values = new Object[gAttributesCount];
	
		for(int i = 0; i < gAttributesCount; i++) {			
			values[i] = tuple.getAttribute(this.groupingAttributesPos[i]);
		}
		
		return values;
	}

	/**
	 * associates the grouping attributes restricted relational tuple to 
	 * an integer as id.
	 *  
	 * @param elem
	 * @return group id of the relational tuple
	 */
	private int getGroupId(MVRelationalTuple<ObjectTrackingMetadata<Object>> elem) {
		
		MVRelationalTuple<ObjectTrackingMetadata<Object>> gTuple 
			= (MVRelationalTuple<ObjectTrackingMetadata<Object>>) 
			elem.restrict(this.getGroupingAttributesPos(), true);
		
		Integer id = keyMap.get(gTuple);
		if (id == null) {
			id = ++maxIdOfKeyMap;
			keyMap.put(gTuple, id);
		}
		
		return id;
	}

	/**
	 * The method is used for writing out ready partials, input schema,
	 * output schema, grouping attributes and nesting attribute is used.
	 * 
	 * @param partial
	 */
	@SuppressWarnings("unchecked")
	private MVRelationalTuple<ObjectTrackingMetadata<Object>> 
		createOutputTuple(
			Object[] groupingValues,
			ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> partial
			) {
		
		MVRelationalTuple<ObjectTrackingMetadata<Object>> outputTuple;
		SetEntry<MVRelationalTuple<ObjectTrackingMetadata<Object>>> outputSet[];
		List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> nest;
		
		outputTuple = 
			new MVRelationalTuple<ObjectTrackingMetadata<Object>>(this.outputAttributesCount);
		
		outputSet = new SetEntry[partial.getSize()];				
		nest = partial.getNest();
		
		for(int i = 0; i < groupingValues.length; i++) {
			outputTuple.setAttribute(i, groupingValues[i]);	
		}
				
		for(int i = 0; i < partial.getSize(); i++) {
			outputSet[i] = new SetEntry(nest.get(i).clone());
		}
				
		outputTuple.setAttribute(this.nestingAttributePos, outputSet);
		outputTuple.setMetadata((ObjectTrackingMetadata<Object>) partial.getMetadata().clone());
		
		return outputTuple;
	}

	/**
	 * merge partial nests to one partial nest with the 
	 * time interval equals the intersection of both. A little optimization 
	 * uses the partial nest with more tuples as merge base.  
	 * 
	 * @param a partial nest
	 * @param b partial nest
	 */
	private ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> merge
		(ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> a, ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> b) {
		
		ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> partial;
		ObjectTrackingMetadata<Object> meta;
		List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> tuples;
		
		meta = new ObjectTrackingMetadata<Object>();
	
		meta.setTimeInterval(
		   TimeInterval.intersection(
		      a.getMetadata(), 
		      b.getMetadata()
		   )
		);
		   
		tuples = new ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>>();
		
		/*
		 * Small optimization; addAll tuples of the bigger partial to 
		 * the new partial, then check it against the smaller one, 
		 * if the smaller one got tuples not existent in the bigger 
		 * one, add it. 
		 */
		
		if(a.getSize() < b.getSize()) {
			tuples.addAll(b.getNest());
			for(MVRelationalTuple<ObjectTrackingMetadata<Object>> t : a.getNest()) {
				if(!tuples.contains(t)) {
					tuples.add(t);
				}
			}
		} else {
			tuples.addAll(a.getNest());
			for(MVRelationalTuple<ObjectTrackingMetadata<Object>> t : b.getNest()) {
				if(!tuples.contains(t)) {
					tuples.add(t);
				}
			}
		}
		
		for(MVRelationalTuple<ObjectTrackingMetadata<Object>> t : tuples) {
			t.setMetadata((ObjectTrackingMetadata<Object>) meta.clone());
		}
		
		partial = new ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>(tuples, meta);
		
		return partial;
	}
	
	/**
	 * the method updates the fillInitialTI by subtracting the 
	 * partial, the method is used in the update process of a sweep area
	 * 
	 * @param fillInitialTI
	 * @param partial
	 */
	private void updateFillInitialTI(
		List<TimeInterval> fillInitialTI, 
		ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> partial
	) {

		List<TimeInterval> addTI;
		List<TimeInterval> removeTI;
		
		addTI = new ArrayList<TimeInterval>();
		removeTI = new ArrayList<TimeInterval>();
		
		for(TimeInterval ti : fillInitialTI) {
			addTI.addAll(TimeInterval.minus(
				ti, 
				partial.getMetadata()
			));
					
			removeTI.add(ti);
		} 
		
		for(TimeInterval ti : removeTI) {
			fillInitialTI.remove(ti);	
			fillInitialTI.addAll(addTI);
		}
	}
	
	/**
	 * update splits and merges partial nests according to the 
	 * algorithm 9 described by Kraemer with correcting modifications.  
	 * 
	 * @TODO wrap if clause bodies to methods.
	 * 
	 * @param sa sweep area of a specific group
	 * @param incomingTuple incoming tuple 
	 */	
	private void update(ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>> sa,
			MVRelationalTuple<ObjectTrackingMetadata<Object>> incomingTuple) {
		
		TimeInterval incomingPartialTI;
		TimeInterval partialTI;
		
		List<TimeInterval> fillInitialTI;

		PointInTime incomingPartialStart;
		PointInTime incomingPartialEnd;
		PointInTime partialStart;
		PointInTime partialEnd;
		
		/* 
		 * Convert the tuple to a partial nest to query it against 
		 * the other partial nests in the sweep area.
		 */
		
		ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> incomingPartial = new 
			ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>(incomingTuple);
		
		/*
		 * Get all other partial nests that qualify (overlap in time)
		 * for a merge.
		 */
		
		Iterator<ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>> qualifies = 
			sa.queryOverlaps(incomingPartial.getMetadata());
		
		/*
		 * If no other partial nest qualifies then insert a initial 
		 * partial nest with start and end time stamp of incomingTuple.
		 */
		
		if(!qualifies.hasNext()) {
			sa.insert(incomingPartial);
		}
		
		else { 						
			incomingPartialTI = incomingPartial.getMetadata();
			incomingPartialStart = incomingPartialTI.getStart();
			incomingPartialEnd = incomingPartialTI.getEnd();
			
			/*
			 * The initial fillInitialTI time interval covers the 
			 * whole time interval of the incoming partial, at first, 
			 * then time intervals of merged partial nests are subtracted.
			 * 
			 * In the end, the fillInitialTI time intervals are used to 
			 * create initial partial nests of incoming partial nest.
			 */
						
			fillInitialTI = new ArrayList<TimeInterval>();
			fillInitialTI.add(incomingPartialTI.clone());
			
			while(qualifies.hasNext()) {
				ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> partial = qualifies.next();
				qualifies.remove();
		
				partialTI = partial.getMetadata();
				partialStart = partialTI.getStart();
				partialEnd = partialTI.getEnd();
				
				/*
				 * Because the partial and the incoming partial overlap 
				 * we remove the partial out of the sweep area, because 
				 * we split it into new ones (for example initial copies
				 * but other time intervals or new merged partials.
				 */
				
				sa.remove(partial);				
								
				if(partialStart.before(incomingPartialStart)) {
					
					ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> overlapPartial1 = 
						partial.clone();
					
					overlapPartial1.setMetadata(
						new TimeInterval(
							partialStart.clone(),
							incomingPartialStart.clone()
						)
					);
					
					sa.insert(overlapPartial1);
				
					if(incomingPartialEnd.before(partialEnd)) {
						
						/*
						 * Incoming partial TI is contained in partial TI.
						 *
						 * Here we need to merge the partial nest with 
						 * time interval equals intersection
						 */
											
						ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> mergePartial = 
							this.merge(partial, incomingPartial);
						
						/*
						 * time interval of partial is reduced to 
						 * [incomingPartialEnd, partialEnd)
						 */
						
						ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> leftOverlapPartial = 
							partial.clone();
						
						leftOverlapPartial.setMetadata(
							new TimeInterval(
								incomingPartialEnd.clone(),
								partialEnd.clone()
							)
						);
						
						sa.insert(leftOverlapPartial);
						sa.insert(mergePartial);			
												
						this.updateFillInitialTI(fillInitialTI, mergePartial);
						
					} else {	
						
						/*
						 * Incoming partial TI right overlaps the partial TI.
						 */
					
						ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> mergePartial = 
							this.merge(partial, incomingPartial);
						
						sa.insert(mergePartial);	
						this.updateFillInitialTI(fillInitialTI, mergePartial);	
					}
				
				// remind: if(partialStart.before(incomingPartialStart)) ...
					
				} else {					
					if(partialTI.equals( 
						TimeInterval.union(
							partialTI, 
							incomingPartialTI
						))) {
												
						/*
						 * Incoming partial TI is equal to partial TI.
						 */			
						
						ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> mergePartial = 
							this.merge(partial, incomingPartial);
						
						sa.insert(mergePartial);
						this.updateFillInitialTI(fillInitialTI, mergePartial);					
					} else 
					if(partialTI.getEnd().before(incomingPartialTI.getEnd())) {
						
						/*
						 * Incoming partial TI contains whole partial TI.
						 */									

						ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> mergePartial = 
							this.merge(incomingPartial, partial);
														
						sa.insert(mergePartial);							
						this.updateFillInitialTI(fillInitialTI, mergePartial);
					}					
					else {
					
						/*
						 * Incoming partial TI left overlaps the partial TI.
						 */
						
						ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> mergePartial = 
							this.merge(partial, incomingPartial);				

						/*
						 * Special case if incomingPartialEnd is equal
						 * to partialEnd the interval is empty. Therefore 
						 * check.
						 */
						
						if(!incomingPartialEnd.equals(partialEnd)) {
						
							ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> overlapPartialRight = 
								partial.clone();
							
							overlapPartialRight.setMetadata(
								new TimeInterval(
									incomingPartialEnd.clone(),
									partialEnd.clone()					
								)
							);
							
							sa.insert(overlapPartialRight);
						}
						
						sa.insert(mergePartial);
						this.updateFillInitialTI(fillInitialTI, mergePartial);	
							
					} // else
				} // else				
			} // while			
			
			for(TimeInterval ti : fillInitialTI) {
				ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> fillPartial = incomingPartial.clone();
				fillPartial.setMetadata(ti);
				sa.insert(fillPartial);
			}
		}
		
	}
	
}
