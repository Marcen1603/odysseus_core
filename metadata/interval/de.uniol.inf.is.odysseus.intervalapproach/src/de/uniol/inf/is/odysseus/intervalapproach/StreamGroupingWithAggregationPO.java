package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.logicaloperator.base.AggregateAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;
import de.uniol.inf.is.odysseus.metadata.base.PairMap;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.GroupingHelper;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.PartialAggregate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class StreamGroupingWithAggregationPO<Q extends ITimeInterval, R extends IMetaAttributeContainer<Q>> extends AggregateTIPO<Q,R> {
	
	
	private class G{
		
		private List<DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>>> groups_queue = new ArrayList<DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>>>();
		private HashMap<DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>>, Integer> gToId = new HashMap<DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>>, Integer>();
		
		synchronized void insert(DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>> sa, Integer groupID){
			groups_queue.add(sa);
			gToId.put(sa, groupID);
		}
		
		boolean isEmpty(){
			return groups_queue.size() == 0;
		}
		
		synchronized Integer min(){
			if (isEmpty()) return -1;
			
			// die Elemente der queue k�nnen sich �ndern ...
			Collections.sort(groups_queue);
			return gToId.get(groups_queue.get(0));
		}

		synchronized public void removeLastMin() {
			if (isEmpty()) return;
			gToId.remove(groups_queue.remove(0));
		}
		
	}
	
	private G g;
	private DefaultTISweepArea<R> q = new DefaultTISweepArea<R>();
	private Map<Integer, DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>>> groups = new HashMap<Integer, DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>>>();
	
	// Nur f�r Debug-Zwecke
	//private PointInTime baseTime = null;


	public StreamGroupingWithAggregationPO(AggregateAO aggregateAO, GroupingHelper<R> grHelper, Class<Q> metadataType){
		super(aggregateAO, metadataType);
		setGroupingHelper(grHelper);
	}
	
	
	@Override
	public void setGroupingHelper(GroupingHelper<R> groupingHelper) {
		super.setGroupingHelper(groupingHelper);
		initAggFunctions();
	}
	public StreamGroupingWithAggregationPO(AggregateAO aggregateAO) {
		super(aggregateAO);
	}
	
	public StreamGroupingWithAggregationPO(StreamGroupingWithAggregationPO<Q, R> agg) {
		super(agg);
	}

	protected void initAggFunctions(){
		AggregateAO aggregateAO = getAlgebraOp();
		SDFAttributeList inputSchema = aggregateAO.getInputSchema();
		for (SDFAttribute a:inputSchema){
			
			Map<AggregateFunction, SDFAttribute> funcs = aggregateAO.getAggregationFunctions(a);			
			if (funcs != null){
				for (Entry<AggregateFunction, SDFAttribute> e: funcs.entrySet()){		
					FESortedPair<SDFAttribute, AggregateFunction> p = new FESortedPair<SDFAttribute, AggregateFunction>(a, e.getKey());
					setAggregationFunction(p, getGroupingHelper().getInitAggFunction(p));
					setAggregationFunction(p, getGroupingHelper().getMergerAggFunction(p));
					setAggregationFunction(p, getGroupingHelper().getEvaluatorAggFunction(p));
					
				}
			}
		}		
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@Override
	protected void process_open() {
		getGroupingHelper().init();
		g = new G();
	}
	
	@Override
	protected void process_done() {
		while (!g.isEmpty()){
			// Die Gruppe mit den �ltesten Elementen heraussuchen
			Integer groupID = g.min();
			// Sowie die passenden SweepArea
			DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>, Q>> sa = groups.get(groupID);
			g.removeLastMin();
			Iterator<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>> results = sa.iterator();
			produceResults(results, groupID);
			sa.clear();
		}
		
		
		R ret;
		while((ret = deliverElements()) != null){
			this.transfer(ret);
		}
	}
	
	@Override
	protected boolean isDone() {
		return super.isAllInputsDone() && q.size() == 0;
	}
	
	@Override
	protected synchronized void process_next(R object, int port) {
		
		// ReadOnly ist egal
		
		process(object);
		
		R ret;
		while((ret = deliverElements()) != null){
			this.transfer(ret);
		}
	}

	private R deliverElements() {		
		if (q.size() == 0) return null;
		
		R r = q.poll();
		//System.out.println("SGA: Liefere "+r);		 
		return r;
	}
	
	
	private synchronized void process(R s) {
		//System.out.println(this+" process "+s);
		
		// Eindeutige ID f�r die Gruppe anhand der Aggregierungsattribute ermitteln
		Integer groupID = getGroupingHelper().getGroupID(s);
		// Passende SweepArea finden (oder ggf. erzeugen)
		DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>> sa = groups.get(groupID);
		if (sa == null){
			sa = new DefaultTISweepArea<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>>();
			groups.put(groupID, sa);
		}
		// Falls diese SA noch nicht verwaltet wurde
		if (sa.size() == 0){
			g.insert(sa, groupID);
		}
		
		// Das neue Element s in die SweepArea einf�gen
		updateSA(sa, s);
		//System.out.println("SGA: SA "+sa);
					
		PointInTime minTs = null;
		while (!g.isEmpty()){
			// Die Gruppe mit den �ltesten Elementen heraussuchen
			groupID = g.min();
			// Sowie die passenden SweepArea
			sa = groups.get(groupID);

			// Das �lteste Element der SweepArea auslesen
			PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q> e_dach = sa.peek();
			
			// Wenn das �lteste Element schon minTs als Startzeitpunkt hat,
			// verlasse Schleife
			if (minTs != null && minTs.equals(e_dach.getMetadata().getStart())) break;
						
			minTs = e_dach.getMetadata().getStart();
			
			if (minTs.before(s.getMetadata().getStart())){
				g.removeLastMin();
				Iterator<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>> results = sa.extractElements(s.getMetadata());
				produceResults(results, groupID);
				// Falls noch nicht alle Elemente der SweepArea verarbeitet wurden
				if (sa.size() > 0){
					g.insert(sa, groupID);
				}
			}else{
				break;
			}
		}				
	}

	private synchronized void produceResults(Iterator<PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>,Q>> results, Integer groupID) {
		while (results.hasNext()){
			PairMap<SDFAttribute, AggregateFunction, PartialAggregate<R>, Q> e = results.next();
			PairMap<SDFAttribute, AggregateFunction, R, ? extends ITimeInterval> r = calcEval(e);
			R out = getGroupingHelper().createOutputElement(groupID, r);
			out.setMetadata(e.getMetadata());
			q.insert(out);
			//System.out.println("SGA: (nach q) "+out);
		}
	}

	@Override
	public StreamGroupingWithAggregationPO<Q,R> clone() {
		return new StreamGroupingWithAggregationPO<Q,R>(this);
	}



}
