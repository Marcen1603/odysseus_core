package de.uniol.inf.is.odysseus.processmining.lossycounting.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.processmining.common.AbstractLCTuple;
import de.uniol.inf.is.odysseus.processmining.common.CaseTuple;
import de.uniol.inf.is.odysseus.processmining.common.DFRTuple;
import de.uniol.inf.is.odysseus.processmining.common.DirectlyFollowLoopTuple;
import de.uniol.inf.is.odysseus.processmining.common.InductiveMinerTransferTupleHelper;
import de.uniol.inf.is.odysseus.processmining.common.LCTupleType;
import de.uniol.inf.is.odysseus.processmining.common.LCTuplesFactory;

public class LossyCountingPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	private int bucketWidth; // corresponds to w
	private double minFreqBoundary = 0.01;
	private HashMap<Object, AbstractLCTuple<T>> activities = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple<T>> endActivities = Maps
			.newHashMap();
	private HashMap<Object, AbstractLCTuple<T>> cases = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple<T>> directlyFollowRelations = Maps
			.newHashMap();
	private Multimap<Object, AbstractLCTuple<T>> directlyFollowRelationLoops = ArrayListMultimap
			.create();
	private HashMap<Object, AbstractLCTuple<T>> shortLoops = Maps.newHashMap();
	private HashMap<Object, AbstractLCTuple<T>> starts = Maps.newHashMap();
	private int iterations = 0; // corresponds to N
	private int currentBucket = 1;
	private String caseId;
	private String activityName;
	InductiveMinerTransferTupleHelper<T> transferHelper = new InductiveMinerTransferTupleHelper<T>();

	public LossyCountingPO(int bucketWidth, double minFreqBoundary) {
		super();
		this.bucketWidth = bucketWidth;
		this.minFreqBoundary = minFreqBoundary;
	}

	public LossyCountingPO(LossyCountingPO<T> lossyCountinPO) {
		super(lossyCountinPO);
		this.bucketWidth = lossyCountinPO.bucketWidth;
		this.minFreqBoundary = lossyCountinPO.minFreqBoundary;
		this.activities = lossyCountinPO.activities;
		this.iterations = lossyCountinPO.iterations;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<T> object, int port) {
		if (object instanceof Tuple) {
			Object[] attributes = object.getAttributes();
			// Values of the object Tuple
			caseId = (String) attributes[0];
			activityName = (String) attributes[1];

			AbstractLCTuple<T> aTuple = LCTuplesFactory.createActivityTuple(
					activityName, currentBucket - 1);
			AbstractLCTuple<T> cTuple = LCTuplesFactory.createCaseTuple(caseId,
					activityName, currentBucket - 1);
			accumulate(aTuple, activities);
			accumulate(cTuple, cases);

			iterations++;
			if (iterations % bucketWidth == 0) {
//				Console Output
//				System.out.println("BREIT: " + bucketWidth);
//				System.out.println("MIN FREQ: " + minFreqBoundary);
				dataCleansing();
				filterLowFrequences(directlyFollowRelations);
				Tuple<T> transferTuple = new Tuple<T>(5, true);
				transferHelper
						.setDirectlyFollowRelations(
								transferTuple,
								(HashMap<Object, AbstractLCTuple<T>>) filterLowFrequences(directlyFollowRelations));
				transferHelper.setStartActivites(transferTuple, starts);
				transferHelper.setShortLoops(transferTuple, shortLoops);
				System.out.println("Transfering...");
				calculateEndActivities();
				transferHelper.setEndActivities(transferTuple, endActivities);
				transfer(transferTuple);
			}
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	private Map<Object, AbstractLCTuple<T>> filterLowFrequences(
			Map<Object, AbstractLCTuple<T>> directlyFollowRelations) {
		Map<Object, AbstractLCTuple<T>> filteredMap = Maps.newHashMap();
		int highestFrequence = getHighestItemFrequenceOf(directlyFollowRelations);
		Set<Object> keys = directlyFollowRelations.keySet();
		int minimumFrequenz = (int) (highestFrequence * minFreqBoundary);

		for (Object key : keys) {
			AbstractLCTuple<T> tuple = directlyFollowRelations.get(key);
			if (tuple.getFrequency() > minimumFrequenz) {
				filteredMap.put(key, tuple);
			} 
		}
		return filteredMap;

	}

	/**
	 * Gets returns the string of the key with the highest frequency
	 * 
	 * @param mostRecentNodes
	 * @return
	 */
	private int getHighestItemFrequenceOf(
			Map<Object, AbstractLCTuple<T>> mostRecentNodes) {
		int freq = 0;
		for (Object key : mostRecentNodes.keySet()) {
			if (freq < mostRecentNodes.get(key).getFrequency()) {
				freq = mostRecentNodes.get(key).getFrequency();
			}
		}
		return freq;
	}

	@SuppressWarnings("unchecked")
	private void calculateEndActivities() {

		for (Object key : cases.keySet()) {

			if (endActivities.containsKey(cases.get(key).getActivity())) {
				endActivities.get(cases.get(key).getActivity())
						.incrementFrequency();
			} else {
				endActivities.put(cases.get(key).getActivity(), LCTuplesFactory
						.createActivityTuple(cases.get(key).getActivity(), 0));
			}

		}

	}

	/**
	 * Updates the tuples frequency or adds the tuple to the given map
	 * 
	 * @param key
	 * @param map
	 */
	private void accumulate(AbstractLCTuple<T> tuple,
			HashMap<Object, AbstractLCTuple<T>> tupleMap) {
		if (tupleMap.containsKey(tuple.getIdentifier())) {
			tupleMap.get(tuple.getIdentifier()).incrementFrequency();
			if (tuple.getType().equals(LCTupleType.Case)) {
				checkForDFGCreationAndLoopCheck((CaseTuple<T>) tuple);
			}
		} else {
			tupleMap.put(tuple.getIdentifier(), tuple);
			// the first activity of a case is its start activity
			if (tuple.getType().equals(LCTupleType.Case)) {
				if (starts.containsKey(activityName)) {
					starts.get(activityName).incrementFrequency();
				} else {
					@SuppressWarnings("unchecked")
					AbstractLCTuple<T> newInstance = LCTuplesFactory
							.createActivityTuple(tuple.getActivity(),
									tuple.getMaxError());
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
	@SuppressWarnings("unchecked")
	private void checkForDFGCreationAndLoopCheck(CaseTuple<T> caseTuple) {
		// Create directly follow Relation and add or increment it
		// temporary variables
		AbstractLCTuple<T> tempCase = cases.get(caseTuple.getIdentifier());
		String activity = tempCase.getActivity();
		String newerActivity = caseTuple.getActivity();

		// create directly follow Relation and accumulate

		AbstractLCTuple<T> rTuple = LCTuplesFactory.createDFRTuple(activity,
				newerActivity, currentBucket - 1);
		accumulate(rTuple, directlyFollowRelations);

		// check for loops of length 1 and add them to shortLoops if needed
		if (directlyFollowRelationLoops.containsKey(((DFRTuple<T>) rTuple)
				.getFollowActivity())) {
			String loopIdentifier = ((DFRTuple<T>) rTuple).getFollowActivity();
			Multimap<Object, AbstractLCTuple<T>> tempMultiMap = ArrayListMultimap
					.create(directlyFollowRelationLoops);

			for (AbstractLCTuple<T> loopTuple : tempMultiMap
					.get(loopIdentifier)) {
				String loopStart = loopTuple.getActivity();
				String loopEnd = ((DFRTuple<T>) rTuple).getFollowActivity();
				boolean hasSameMiddle = rTuple.getActivity().equals(
						((DirectlyFollowLoopTuple<T>) loopTuple)
								.getFollowActivity()) ? true : false;

				if (loopStart.equals(loopEnd)
						&& hasSameMiddle
						&& ((DirectlyFollowLoopTuple<T>) loopTuple).getCaseID()
								.equals(caseTuple.getCaseID())) {

					String loopBody = ((DFRTuple<T>) rTuple)
							.getFollowActivity() + rTuple.getActivity();
					// Add a new DFRTuple representing a Loop
					shortLoops.put(loopBody, LCTuplesFactory.createDFRTuple(
							((DFRTuple<T>) rTuple).getFollowActivity(),
							rTuple.getActivity(), currentBucket - 1));
					directlyFollowRelationLoops.remove(loopIdentifier,
							loopTuple);
				}
			}

		} else {
			// Add directly follow relation with the associated case id
			DirectlyFollowLoopTuple<T> loopTuple = (DirectlyFollowLoopTuple<T>) LCTuplesFactory
					.createDFRLoopTuple(caseId, rTuple.getActivity(),
							((DFRTuple<T>) rTuple).getFollowActivity(),
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
		// Only clean starts if there is more than 1 activity
		if (!(starts.size() == 1)) {
			starts = cleanMap(starts);
		}
		shortLoops = cleanMap(shortLoops);
		currentBucket++;
	}

	/**
	 * Removes all infrequent tuple of map
	 * 
	 * @param map
	 * @return
	 */
	private HashMap<Object, AbstractLCTuple<T>> cleanMap(
			HashMap<Object, AbstractLCTuple<T>> map) {
		HashMap<Object, AbstractLCTuple<T>> newMap = Maps.newHashMap();
		for (Map.Entry<Object, AbstractLCTuple<T>> entry : map.entrySet()) {
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
	 * 
	 * @param map
	 * @return
	 */
	private Multimap<Object, AbstractLCTuple<T>> cleanMap(
			Multimap<Object, AbstractLCTuple<T>> map) {
		Multimap<Object, AbstractLCTuple<T>> newMap = ArrayListMultimap
				.create();

		for (Object key : map.keySet()) {
			for (AbstractLCTuple<T> tuple : map.get(key)) {
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
		return OutputMode.INPUT;
	}

}