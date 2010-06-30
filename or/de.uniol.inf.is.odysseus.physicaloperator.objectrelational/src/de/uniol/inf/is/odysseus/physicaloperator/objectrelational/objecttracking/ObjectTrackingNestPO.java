package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.helper.ObjectTrackingNestTISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.helper.ObjectTrackingPartialNest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.helper.ObjectTrackingPriorityQueueG;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
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
 * Object Tracking NestPO, this one is for MVRelationalTuple processing,
 * for testing the nest po use the test suite of
 *  
 * /de.uniol.inf.is.odysseus.physicaloperator.objectrelational/src/de/uniol
 * /inf/is/odysseus/physicaloperator/objectrelational/objecttracking/test/
 * nest/NestPOAllTests.java
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingNestPO
		<M extends ObjectTrackingMetadata<Object>> 
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

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
	
	private Map<Integer, ObjectTrackingNestTISweepArea<M>> groups;
	
	/*
	 * keyMap maps restricted tuples (to grouping attributes) to a key, 
	 * if grouping doesn't match a key, maxId is incremented and used. 
	 */
	
	private Map<MVRelationalTuple<M>, Integer> keyMap;
	
	/*
	 * MVPriorityQueueG stores the sweep areas ascending to the smallest 
	 * start time stamp of containing partial nests.   
	 */
	
	private ObjectTrackingPriorityQueueG<M> g;
	
	/*
	 * q is a default min-priority queue for relational tuples according 
	 * to their time stamps.
	 */
	
	private DefaultTISweepArea<MVRelationalTuple<M>> q;
	
	/**
	 * @param groupingAttributes attributes to group by
	 * @param nestingAttribute nesting attribute
	 */
	
	public ObjectTrackingNestPO(
	       SDFAttributeList inputSchema,
	       SDFAttributeList outputSchema,
	       SDFAttribute nestingAttribute,
	       SDFAttributeList groupingAttributes
	    ) {
	    
	    this.inputSchema = inputSchema;
	    this.outputSchema = outputSchema;
	    this.nestingAttribute = nestingAttribute.clone();
	    this.groupingAttributes = groupingAttributes.clone();
	    
	    this.inputAttributesCount = this.inputSchema.getAttributeCount();       
        this.groupingAttributesPos = this.getGroupingAttributesPos();
        this.outputAttributesCount = this.groupingAttributesPos.length + 1;
        this.nonGroupingAttributesPos = this.getNonGroupingAttributePos();
        this.nestingAttributePos = this.groupingAttributesPos.length;
	        
		this.groups = new HashMap<Integer, ObjectTrackingNestTISweepArea<M>>();		
		this.keyMap = new HashMap<MVRelationalTuple<M>,Integer>();		
		this.g = new ObjectTrackingPriorityQueueG<M>();	
		this.q = new DefaultTISweepArea<MVRelationalTuple<M>>();
	}
	
	/**
	 * A lot of code for creating a deep copy of maps groups and keyMap.
	 * 
	 * @param relationalNestPO nesting plan operator to copy
	 */	
	public ObjectTrackingNestPO(ObjectTrackingNestPO<M> nestPO) {
		super(nestPO);
		
		this.inputSchema = nestPO.inputSchema;
		this.outputSchema = nestPO.outputSchema;
		this.nestingAttribute = nestPO.nestingAttribute.clone();
		this.groupingAttributes = nestPO.groupingAttributes.clone();
		
		Iterator<Entry<MVRelationalTuple<M>, Integer>> keyMapIter;		
		Iterator<Entry<Integer, ObjectTrackingNestTISweepArea<M>>> groupsIter;
		
		Entry<MVRelationalTuple<M>, Integer> entryOfkeyMap;
		Entry<Integer, ObjectTrackingNestTISweepArea<M>> entryOfGroups;
		
		keyMapIter = nestPO.keyMap.entrySet().iterator();
		groupsIter = nestPO.groups.entrySet().iterator();
	
		while(keyMapIter.hasNext()) {
		    entryOfkeyMap = keyMapIter.next();
		    
		    MVRelationalTuple<M> tuple = 
                entryOfkeyMap.getKey();
		 
		    this.keyMap.put(tuple.clone(), entryOfkeyMap.getValue());
		}	
		
		while(groupsIter.hasNext()) {
		    entryOfGroups = groupsIter.next();
		    ObjectTrackingNestTISweepArea<M> sa = entryOfGroups.getValue();
		    
		    this.groups.put(
		        entryOfGroups.getKey(), 
		        sa.clone()
		    );
		}
		
		this.g = nestPO.g.clone();
		this.q = nestPO.q.clone();
	}

	/**
	 * processing
	 */
	private void process(
		MVRelationalTuple<M> incomingTuple,
		int port
	) {
		ObjectTrackingNestTISweepArea<M> sa;
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
			 * MVNestTISweepArea<M> for evaluating (transform to relational 
			 * tuple output)
			 */
			
			Object[] groupingValues = this.getGroupingValues(incomingTuple);
			sa = new ObjectTrackingNestTISweepArea<M>(groupingValues);
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

		MVRelationalTuple<M> restrictedToNonGrouping 
			= (MVRelationalTuple<M>) 
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
			
			ObjectTrackingNestTISweepArea<M> saOfMin = 
				groups.get(groupIdOfMin);
			
			ObjectTrackingPartialNest<M> minPartial = 
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
				Iterator<ObjectTrackingPartialNest<M>> results = 
					saOfMin.extractElementsBefore(incomingTupleStart);
				
				/*
				 * Here happens the evaluating 
				 */
				
				while(results.hasNext()) {
					ObjectTrackingPartialNest<M> result = results.next();
					
					Object[] groupingValues = saOfMin.getGroupingValues();
					MVRelationalTuple<M> output = 
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
	 * process_next method is a primary function for an operator, tuples of 
	 * subscripted sources will be sent to this method. The port is not 
	 * relevant due to an unary operator.
	 * 
	 * @param object
	 * @param port
	 */	
	final protected void process_next(
		MVRelationalTuple<M> incomingTuple, 
		int port
	) {		
		this.process(incomingTuple, port);
		
		MVRelationalTuple<M> delivery = this.deliver();
		if(delivery != null) {
			this.transfer(delivery);
//			System.out.println(delivery);
//			for(SetEntry<MVRelationalTuple<M>> sub : 
//				(SetEntry[]) delivery.getAttribute(this.nestingAttributePos)) 
//			{
//				System.out.println("|_ " + sub.getValue());
//			}
		} 
	}	

	/**
	 * Instead of transfering the objects to the subscriber, we return the 
	 * values to assert correctness in a test case.
	 * 
	 * @param object
	 * @param port
	 */	
	public final void processNextTest(
		MVRelationalTuple<M> incomingTuple, 
		int port
	) {
			this.process(incomingTuple, port);
	}	
	
	@Override
	public void process_done() {
		for(MVRelationalTuple<M> t : this.q) {
			this.transfer(t);
			System.out.println(t);
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
			
			ObjectTrackingNestTISweepArea<M> saOfMin =
				groups.get(groupIdOfMin);
			
			Iterator<ObjectTrackingPartialNest<M>> results =
				saOfMin.iterator();
			
			while(results.hasNext()) {
				ObjectTrackingPartialNest<M> result = results.next();
				Object[] groupingValues = saOfMin.getGroupingValues();
				MVRelationalTuple<M> output = 
					this.createOutputTuple(groupingValues, result);
					
				q.insert(output);
			}
		}
	}

	/**
	 * Deliver elements 
	 */
	public MVRelationalTuple<M> deliver() {
		return this.q.poll();
	}

	public boolean isDone() {
		return (q.size() == 0);
	}
	
	@Override
    public void processPunctuation(PointInTime timestamp, int port) {
    	sendPunctuation(timestamp);    	
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
	 * MVNestTISweepArea<M> for evaluating (transform to relational 
	 * tuple output)
	 * 
	 * @param tuple
	 * @return
	 * 
	 */
	private Object[] getGroupingValues(
		MVRelationalTuple<M> tuple) {
		
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
	private int getGroupId(MVRelationalTuple<M> elem) {
		
		// here the restrict method of MVRelationalTuple is used.
		
		MVRelationalTuple<M> gTuple 
			= elem.restrict(this.getGroupingAttributesPos(), null, true);
		
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
	private MVRelationalTuple<M> 
		createOutputTuple(
			Object[] groupingValues,
			ObjectTrackingPartialNest<M> partial
			) {
		
		MVRelationalTuple<M> outputTuple;
		SetEntry<MVRelationalTuple<M>> outputSet[];
		List<MVRelationalTuple<M>> nest;
		
		outputTuple = 
			new MVRelationalTuple<M>(this.outputAttributesCount);
		
		outputSet = new SetEntry[partial.getSize()];				
		nest = partial.getNest();
		
		for(int i = 0; i < groupingValues.length; i++) {
			outputTuple.setAttribute(i, groupingValues[i]);	
		}
				
		for(int i = 0; i < partial.getSize(); i++) {
			outputSet[i] = new SetEntry(nest.get(i).clone());
		}
				
		outputTuple.setAttribute(this.nestingAttributePos, outputSet);
		outputTuple.setMetadata((M) partial.getMetadata().clone());
		
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
	@SuppressWarnings("unchecked")
	private ObjectTrackingPartialNest<M> merge
		(ObjectTrackingPartialNest<M> a, ObjectTrackingPartialNest<M> b) {
		
		ObjectTrackingMetadata<Object> meta = 
			new ObjectTrackingMetadata<Object>();
		
		List<MVRelationalTuple<M>> tuples;
	
		TimeInterval ti = TimeInterval.intersection(
		      a.getMetadata().clone(), 
		      b.getMetadata().clone()
		);				
				
		meta.setStreamTime(ti);
		
		tuples = new ArrayList<MVRelationalTuple<M>>();		
		
		/*
		 * Small optimization; addAll tuples of the bigger partial to 
		 * the new partial, then check it against the smaller one, 
		 * if the smaller one got tuples not existent in the bigger 
		 * one, add it. 
		 */
		
		
		boolean exists = false;
		
		if(a.getSize() < b.getSize()) {
			tuples.addAll(b.getNest());	
			for(MVRelationalTuple<M> t : a.getNest()) {
				for(MVRelationalTuple<M> t2 : tuples) {
					if(t.equals(t2)) {
						exists = true;
					} 
				}
				if(!exists) {
					tuples.add(t);
				}
				exists = false;
			}
			
		} else {
			exists = false;
			tuples.addAll(a.getNest());
			for(MVRelationalTuple<M> t : b.getNest()) {
				for(MVRelationalTuple<M> t2 : tuples) {
					if(t.equals(t2)) {
						exists = true;
					}
				}
				if(!exists) {
					tuples.add(t);
				}
				exists = false;
			}
		}
		
		for(MVRelationalTuple<M> t : tuples) {
			M metaForTuple = t.getMetadata();
			metaForTuple.setStreamTime(meta.getStreamTime().clone());
		}
		
		ObjectTrackingPartialNest<M> partial;
		partial = new ObjectTrackingPartialNest<M>(tuples, (M)meta);
		
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
		List<ITimeInterval> fillInitialTI, 
		ObjectTrackingPartialNest<M> partial
	) {

		List<ITimeInterval> addTI;
		List<ITimeInterval> removeTI;
		
		addTI = new ArrayList<ITimeInterval>();
		removeTI = new ArrayList<ITimeInterval>();
		
		for(ITimeInterval ti : fillInitialTI) {
			addTI.addAll(TimeInterval.minus(
				ti, 
				partial.getMetadata()
			));
					
			removeTI.add(ti);
		} 
		
		for(ITimeInterval ti : removeTI) {
			fillInitialTI.remove(ti);	
			fillInitialTI.addAll(addTI);
		}
	}
	
	/**
	 * the method updates all tuples in a partial to new time interval
	 * this is mostly used for filling partials and cutting of old partials.
	 * 
	 * @param nest specific partial to set the intervals of tuples
	 * @param ti the time interval to set.
	 */
	private void setTimeIntervalOnSubtuples(
		ObjectTrackingPartialNest<M> partial,
		ITimeInterval ti
	) {
		List<MVRelationalTuple<M>> subTuples;		
		subTuples = partial.getNest();
		
		for(MVRelationalTuple<M> t : subTuples) {
			M meta = t.getMetadata();
			meta.setStreamTime(ti.clone());
		}
	}
		
	/**
	 * update splits and merges partial nests inspired from 
	 * algorithm 9 described by Kraemer. 
	 * 
	 * @param sa sweep area of a specific group
	 * @param incomingTuple incoming tuple 
	 */	
	private void update(
		ObjectTrackingNestTISweepArea<M> sa,
		MVRelationalTuple<M> incomingTuple
	) {
		
		ITimeInterval incomingPartialTI;
		ITimeInterval partialTI;
		
		List<ITimeInterval> fillInitialTI;

		PointInTime incomingPartialStart;
		PointInTime incomingPartialEnd;
		PointInTime partialStart;
		PointInTime partialEnd;
		
		/* 
		 * Convert the tuple to a partial nest to query it against 
		 * the other partial nests in the sweep area.
		 */
		
		final ObjectTrackingPartialNest<M> incomingPartial = new 
			ObjectTrackingPartialNest<M>(incomingTuple);
		
		/*
		 * Get all other partial nests that qualify (overlap in time)
		 * for a merge.
		 */
		
		Iterator<ObjectTrackingPartialNest<M>> qualifies = 
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
						
			fillInitialTI = new ArrayList<ITimeInterval>();
			fillInitialTI.add(incomingPartialTI.clone());
			
			while(qualifies.hasNext()) {
				ObjectTrackingPartialNest<M> partial = qualifies.next();
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
					
					ObjectTrackingPartialNest<M> leftOverlapPartial = 
						partial.clone();						
					
					M meta = leftOverlapPartial.getMetadata();
					
					TimeInterval ti = new TimeInterval(
						partialStart.clone(),
						incomingPartialStart.clone()
					);
					
					meta.setStreamTime(ti);					
					
					/*
					 * Setting the time interval to subtuples to the 
					 * time interval of the partial.
					 */
					
					this.setTimeIntervalOnSubtuples(leftOverlapPartial, ti);
					
					sa.insert(leftOverlapPartial);
				
					if(incomingPartialEnd.before(partialEnd)) {
						
						/*
						 * Incoming partial TI is contained in partial TI.
						 *
						 * Here we need to merge the partial nest with 
						 * time interval equals intersection
						 */
											
						ObjectTrackingPartialNest<M> mergePartial = 
							this.merge(partial.clone(), incomingPartial.clone());
						
						/*
						 * time interval of partial is reduced to 
						 * [incomingPartialEnd, partialEnd)
						 */
						
						ObjectTrackingPartialNest<M> rightOverlapPartial = 
							partial.clone();
						
						meta = rightOverlapPartial.getMetadata();
						
						TimeInterval ti2 = new TimeInterval(
							incomingPartialEnd.clone(),
							partialEnd.clone()
						);
						
						meta.setStreamTime(ti2);
						
						/*
						 * Setting the time interval to subtuples to the 
						 * time interval of the partials
						 */
						
						this.setTimeIntervalOnSubtuples(
							rightOverlapPartial, 
							ti
						);
						
						sa.insert(rightOverlapPartial);
						sa.insert(mergePartial);			
												
						this.updateFillInitialTI(fillInitialTI, mergePartial);
						
					} else {	
						
						/*
						 * Incoming partial TI right overlaps the partial TI.
						 */
					
						ObjectTrackingPartialNest<M> mergePartial = 
							this.merge(partial.clone(), incomingPartial.clone());
						
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
						
						ObjectTrackingPartialNest<M> mergePartial = 
							this.merge(partial.clone(), incomingPartial.clone());
						
						sa.insert(mergePartial);
						this.updateFillInitialTI(fillInitialTI, mergePartial);					
					} else 
					if(partialTI.getEnd().before(incomingPartialTI.getEnd())) {
						
						/*
						 * Incoming partial TI contains whole partial TI.
						 */									

						ObjectTrackingPartialNest<M> mergePartial = 
							this.merge(incomingPartial.clone(), partial.clone());
														
						sa.insert(mergePartial);							
						this.updateFillInitialTI(fillInitialTI, mergePartial);
					}					
					else {
					
						/*
						 * Incoming partial TI left overlaps the partial TI.
						 */
						
						ObjectTrackingPartialNest<M> mergePartial = 
							this.merge(partial.clone(), incomingPartial.clone());				

						/*
						 * Special case if incomingPartialEnd is equal
						 * to partialEnd the interval is empty. Therefore 
						 * check.
						 */
						
						if(!incomingPartialEnd.equals(partialEnd)) {
						
							ObjectTrackingPartialNest<M> overlapPartialRight = 
								partial.clone();
							
							M meta = overlapPartialRight.getMetadata();
							
							TimeInterval ti3 = new TimeInterval(
								incomingPartialEnd.clone(),
								partialEnd.clone()
							);
							
							meta.setStreamTime(ti3);
							
							/*
							 * Setting the time interval of the subtuples 
							 * of the overlapPartialRight
							 */
							
							this.setTimeIntervalOnSubtuples(
								overlapPartialRight, 
								ti3
							);
							
							sa.insert(overlapPartialRight);
						}
						
						sa.insert(mergePartial);
						this.updateFillInitialTI(fillInitialTI, mergePartial);	
							
					} // else
				} // else				
			} // while			
			
			/*
			 * This for-loop is for filling the last filling time intervals
			 * of the incoming partial (which have not been merged) 
			 */
			
			for(ITimeInterval ti : fillInitialTI) {
				ObjectTrackingPartialNest<M> fillPartial = 
					incomingPartial.clone();			
				
				M meta = fillPartial.getMetadata();				
				meta.setStreamTime(ti.clone());
				
				/*
				 * Setting the time interval of the sub tuples of the 
				 * filling partial. 
				 */
				
				this.setTimeIntervalOnSubtuples(fillPartial, ti);
				
				sa.insert(fillPartial);
			}
		}
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
    public ObjectTrackingNestPO<M> clone() {
    	return new ObjectTrackingNestPO<M>(this);
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
}
