package de.uniol.inf.is.odysseus.processmining.lossycounting.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.processmining.common.AbstractLCTuple;
import de.uniol.inf.is.odysseus.processmining.common.CaseTuple;
import de.uniol.inf.is.odysseus.processmining.common.DFRTuple;
import de.uniol.inf.is.odysseus.processmining.common.DirectlyFollowLoopTuple;
import de.uniol.inf.is.odysseus.processmining.common.InductiveMinerTransferTuple;
import de.uniol.inf.is.odysseus.processmining.common.LCTupleType;
import de.uniol.inf.is.odysseus.processmining.common.LCTuplesFactory;

public class LossyCountingPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	private int error = 100; // corresponds to w
	private HashMap<Object, AbstractLCTuple> activities = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple> endActivities = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple> cases = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple> directlyFollowRelations = Maps
			.newHashMap();
	private Multimap<Object, AbstractLCTuple> directlyFollowRelationLoops = ArrayListMultimap
			.create();
	private HashMap<Object, AbstractLCTuple> shortLoops = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple> starts = Maps.newHashMap();
	private int iterations = 0; // corresponds to N
	private int currentBucket = 1;
	private String caseId;
	private String activityName;

	public LossyCountingPO() {
		super();
	}

	public LossyCountingPO(LossyCountingPO<T> lossyCountinPO) {
		super(lossyCountinPO);
		this.error = lossyCountinPO.error;
		this.activities = lossyCountinPO.activities;
		this.iterations = lossyCountinPO.iterations;
		System.out.print("ERROR PO: " + this.error + "\n");
	}

	@Override
	protected void process_next(Tuple<T> object, int port) {
		if (object instanceof Tuple) {
			Object[] attributes = object.getAttributes();
			// Values of the object Tuple
			caseId = (String) attributes[0];
			activityName = (String) attributes[1];

			AbstractLCTuple aTuple = LCTuplesFactory.createActivityTuple(
					activityName, currentBucket - 1);
			AbstractLCTuple cTuple = LCTuplesFactory.createCaseTuple(caseId,
					activityName, currentBucket - 1);
			accumulate(aTuple, activities);
			accumulate(cTuple, cases);

			iterations++;
			if (iterations % 1000 == 0) {
				dataCleansing();
				InductiveMinerTransferTuple iMTransferTuple = new InductiveMinerTransferTuple();
				iMTransferTuple
						.setDirectlyFollowRelations(directlyFollowRelations);
				iMTransferTuple.setStartActivites(starts);
				iMTransferTuple.setShortLoops(shortLoops);
				System.out.println("Transfering...");
				calculateEndActivities();
				iMTransferTuple.setEndActivities(endActivities);
				transfer(iMTransferTuple);
			}
		}
	}

	private void calculateEndActivities(){
		
		for(Object key : cases.keySet()){
			
			if(endActivities.containsKey(cases.get(key).getActivity())){
				endActivities.get(cases.get(key).getActivity()).incrementFrequency();
			} else {
				endActivities.put(cases.get(key).getActivity(), LCTuplesFactory.createActivityTuple(cases.get(key).getActivity(),0));
			}
			
		}
		
	}
	/**
	 * Updates the tuples frequency or adds the tuple to the given map
	 * 
	 * @param key
	 * @param map
	 */
	private void accumulate(AbstractLCTuple tuple,
			HashMap<Object, AbstractLCTuple> tupleMap) {
		if (tupleMap.containsKey(tuple.getIdentifier())) {
			tupleMap.get(tuple.getIdentifier()).incrementFrequency();
			if (tuple.getType().equals(LCTupleType.Case)) {
				checkForDFGCreationAndLoopCheck((CaseTuple) tuple);
			}
		} else {
			tupleMap.put(tuple.getIdentifier(), tuple);
			// the first activity of a case is its start activity
			if (tuple.getType().equals(LCTupleType.Case)) {
				if (starts.containsKey(activityName)) {
					starts.get(activityName).incrementFrequency();
				} else {
					AbstractLCTuple newInstance = LCTuplesFactory.createActivityTuple(tuple.getActivity(), tuple.getMaxError());
					starts.put(activityName, newInstance);
				}
			}
		}
	}

	/**
	 * Checks if its needed to create a directly follow Relation and accumulates
	 * it to the relation map Futhermore it changes the activityname of the
	 * contained tuple of the case map by the new one
	 * 
	 * @param caseTuple
	 */
	private void checkForDFGCreationAndLoopCheck(CaseTuple caseTuple) {
		// Create directly follow Relation and add or increment it
		// temporary variables
		AbstractLCTuple tempCase = cases.get(caseTuple.getIdentifier());
		String activity = tempCase.getActivity();
		String newerActivity = caseTuple.getActivity();

		// create directly follow Relation and accumulate
		AbstractLCTuple rTuple = LCTuplesFactory.createDFRTuple(activity,
				newerActivity, currentBucket - 1);
		accumulate(rTuple, directlyFollowRelations);

		// check for loops of length 1 and add them to shortLoops if needed
		if (directlyFollowRelationLoops.containsKey(((DFRTuple) rTuple)
				.getFollowActivity())) {
			String loopIdentifier = ((DFRTuple) rTuple).getFollowActivity();
			Multimap<Object, AbstractLCTuple> tempMultiMap = ArrayListMultimap
					.create(directlyFollowRelationLoops);

			for (AbstractLCTuple loopTuple : tempMultiMap.get(loopIdentifier)) {
				String loopStart = loopTuple.getActivity();
				String loopEnd = ((DFRTuple) rTuple).getFollowActivity();
				boolean hasSameMiddle = rTuple.getActivity().equals(
						((DirectlyFollowLoopTuple) loopTuple)
								.getFollowActivity()) ? true : false;
				
				if (loopStart.equals(loopEnd)
						&& hasSameMiddle
						&& ((DirectlyFollowLoopTuple) loopTuple).getCaseID()
								.equals(caseTuple.getCaseID())) {

					String loopBody = ((DFRTuple) rTuple).getFollowActivity()
							+ rTuple.getActivity();
					// Add a new DFRTuple representing a Loop
					shortLoops.put(loopBody, LCTuplesFactory.createDFRTuple(
							((DFRTuple) rTuple).getFollowActivity(),
							rTuple.getActivity(), currentBucket - 1));
					directlyFollowRelationLoops.remove(loopIdentifier,
							loopTuple);
				}
			}

		} else {
			// Add directly follow relation with the associated case id
			DirectlyFollowLoopTuple loopTuple = (DirectlyFollowLoopTuple) LCTuplesFactory
					.createDFRLoopTuple(caseId, rTuple.getActivity(),
							((DFRTuple) rTuple).getFollowActivity(),
							currentBucket - 1);
			directlyFollowRelationLoops.put(activity, loopTuple);
		}
		// Replace Activity
		tempCase.setActivity(caseTuple.getActivity());
	}

	// Datacleaning methods
	private void dataCleansing() {
			activities = cleanMap(activities);
			cases = cleanMap(cases);
			directlyFollowRelations = cleanMap(directlyFollowRelations);
			directlyFollowRelationLoops = cleanMap(directlyFollowRelationLoops);
			starts = cleanMap(starts);
			shortLoops = cleanMap(shortLoops);
			currentBucket++;
	}

	
	/**
	 * Removes all infrequent tuple of map
	 * @param map
	 * @return
	 */
	private HashMap<Object, AbstractLCTuple> cleanMap(
			HashMap<Object, AbstractLCTuple> map) {
		HashMap<Object, AbstractLCTuple> newMap = Maps.newHashMap();
		for (Map.Entry<Object, AbstractLCTuple> entry : map.entrySet()) {
			int actualFrequenz = entry.getValue().getFrequency();
			int actualBucket = entry.getValue().getMaxError();
			if (!(actualFrequenz + actualBucket <= currentBucket)) {
				newMap.put(entry.getKey(), entry.getValue());
			}
		}
		return newMap;
	}

	/**
	 * Removes all infrequent tuple of map
	 * @param map
	 * @return
	 */
	private Multimap<Object, AbstractLCTuple> cleanMap(
			Multimap<Object, AbstractLCTuple> map) {
		Multimap<Object, AbstractLCTuple> newMap = ArrayListMultimap.create();

		for (Object key : map.keySet()) {
			for (AbstractLCTuple tuple : map.get(key)) {
				int actualFrequenz = tuple.getFrequency();
				int actualBucket = tuple.getMaxError();
				if (!(actualFrequenz + actualBucket <= currentBucket)) {
					newMap.put(key, tuple);
				}
			}

		}
		return newMap;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (this == ipo)
			return true;
		if (!super.equals(ipo))
			return false;
		if (getClass() != ipo.getClass())
			return false;
		return true;
	}
}