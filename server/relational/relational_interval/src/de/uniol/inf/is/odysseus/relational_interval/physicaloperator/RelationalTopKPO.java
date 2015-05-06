package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.SerializablePair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.VarHelper;

/**
 * This operator calculates the top k elements from the input with a scoring
 * function
 * 
 * @author Marco Grawunder
 *
 * @param <T>
 * @param <M>
 */
public class RelationalTopKPO<T extends Tuple<M>, M extends ITimeInterval>
		extends AbstractPipe<T, T> implements IPhysicalOperatorKeyValueProvider {

	private class TopKComparatorAsc implements
			Comparator<SerializablePair<Double, T>> {

		@Override
		public int compare(SerializablePair<Double, T> left,
				SerializablePair<Double, T> right) {
			return left.getE1().compareTo(right.getE1());
		}
	}

	private class TopKComparatorAscTS extends TopKComparatorAsc {

		@Override
		public int compare(SerializablePair<Double, T> left,
				SerializablePair<Double, T> right) {
			int v = super.compare(left, right);
			if (v == 0) {
				PointInTime leftTS = left.getE2().getMetadata().getStart();
				PointInTime rightTS = right.getE2().getMetadata().getStart();
				return leftTS.compareTo(rightTS);
			} else {
				return v;
			}
		}
	}

	private class TopKComparatorDesc implements
			Comparator<SerializablePair<Double, T>> {

		@Override
		public int compare(SerializablePair<Double, T> left,
				SerializablePair<Double, T> right) {
			return right.getE1().compareTo(left.getE1());
		}
	}

	private class TopKComparatorDescTS extends TopKComparatorDesc {

		@Override
		public int compare(SerializablePair<Double, T> left,
				SerializablePair<Double, T> right) {
			int v = super.compare(left, right);
			if (v == 0) {
				PointInTime leftTS = left.getE2().getMetadata().getStart();
				PointInTime rightTS = right.getE2().getMetadata().getStart();
				return rightTS.compareTo(leftTS);
			} else {
				return v;
			}
		}
	}

	final private SDFExpression scoringFunction;
	final private int k;
	final private Map<Long, ArrayList<SerializablePair<Double, T>>> topKMap = new HashMap<Long, ArrayList<SerializablePair<Double, T>>>();
	final private Comparator<SerializablePair<Double, T>> comparator;
	final private IGroupProcessor<T, T> groupProcessor;

	private Map<Long, LinkedList<T>> lastResultMap = new HashMap<>();
	private long elementsRead;

	private VarHelper[] variables;
	private boolean suppressDuplicates;
	private boolean orderByTimestamp = true;

	public RelationalTopKPO(SDFSchema inputSchema,
			SDFExpression scoringFunction, int k, boolean descending,
			boolean suppressDuplicates, IGroupProcessor<T, T> groupProcessor) {
		super();
		this.scoringFunction = scoringFunction;
		initScoringFunction(inputSchema);
		this.k = k;
		if (isOrderByTimestamp()) {
			comparator = descending ? new TopKComparatorDescTS()
			: new TopKComparatorAscTS();
		} else {
			comparator = descending ? new TopKComparatorDesc()
					: new TopKComparatorAsc();
		}
		this.suppressDuplicates = suppressDuplicates;
		this.groupProcessor = groupProcessor;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		topKMap.clear();
		lastResultMap.clear();
		groupProcessor.init();
		elementsRead=0;
	}

	@Override
	protected synchronized void process_next(T object, int port) {
		elementsRead++;
		Long gId = groupProcessor.getGroupID(object);

		ArrayList<SerializablePair<Double, T>> topK = topKMap.get(gId);
		if (topK == null) {
			topK = new ArrayList<SerializablePair<Double, T>>();
			topKMap.put(gId, topK);
		}

		cleanUp(object.getMetadata().getStart(), topK);

		addObject(calcScore(object), topK);

		produceResult(object, topK, gId);
	}

	@Override
	protected void process_close() {
		topKMap.clear();
		lastResultMap.clear();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO: how to handle punctuations
		// sendPunctuation(punctuation);
	}

	private void cleanUp(PointInTime start,
			ArrayList<SerializablePair<Double, T>> topK) {
		Iterator<SerializablePair<Double, T>> iter = topK.iterator();
		while (iter.hasNext()) {
			if (iter.next().getE2().getMetadata().getEnd()
					.beforeOrEquals(start)) {
				iter.remove();
			}
		}
	}

	private void addObject(SerializablePair<Double, T> scoredObject,
			ArrayList<SerializablePair<Double, T>> topK) {
		// add object to list
		// 1. find position to insert with binary search

		int pos = Collections.binarySearch(topK, scoredObject, comparator);
		if (pos < 0) {
			topK.add((-(pos) - 1), scoredObject);
		} else {
			topK.add(pos, scoredObject);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void produceResult(T object,
			ArrayList<SerializablePair<Double, T>> topK, Long groupID) {
		// Produce result
		T result = (T) new Tuple(2, false);
		Iterator<SerializablePair<Double, T>> iter = topK.iterator();
		List<T> resultList = new LinkedList<T>();
		for (int i = 0; i < k && iter.hasNext(); i++) {
			T out = (T) iter.next().getE2().clone();
			out.setMetadata(null);
			resultList.add(out);
		}
		boolean sameAsLastResult = suppressDuplicates ? compareWithLastResult(
				resultList, groupID) : false;

		if (!sameAsLastResult) {
			lastResultMap.put(groupID, new LinkedList<T>(resultList));
			result.setAttribute(0, resultList);
			result.setAttribute(1, object);
			M meta = (M) object.getMetadata().clone();
			result.setMetadata(meta);
			transfer(result);
		}
	}

	private boolean compareWithLastResult(List<T> resultList, Long groupID) {
		LinkedList<T> lastResult = lastResultMap.get(groupID);
		if (lastResult == null) {
			return false;
		}

		if (lastResult.size() != resultList.size()) {
			return false;
		}

		Iterator<T> iter = resultList.iterator();
		for (T e : lastResult) {
			if ((!e.equals(iter.next()))) {
				return false;
			}
		}

		return true;

	}

	private SerializablePair<Double, T> calcScore(T object) {
		SerializablePair<Double, T> scoredObject = new SerializablePair<>();
		Double score = null;
		scoredObject.setE2(object);

		Object[] values = new Object[this.variables.length];
		IMetaAttribute[] meta = new IMetaAttribute[this.variables.length];
		for (int j = 0; j < this.variables.length; ++j) {

			if (object != null) {
				if (variables[j].getSchema() == -1){
					values[j] = object.getAttribute(this.variables[j].getPos());
				}else{
					values[j] = object.getMetadata().getValue(variables[j].getSchema(),variables[j].getPos());
				}
				meta[j] = object.getMetadata();
			}
		}

		try {
			this.scoringFunction.bindMetaAttribute(object.getMetadata());
			this.scoringFunction.bindAdditionalContent(object
					.getAdditionalContent());
			this.scoringFunction.bindVariables(meta, values);
			score = ((Number) this.scoringFunction.getValue()).doubleValue();

		} catch (Exception e) {
			if (!(e instanceof NullPointerException)) {
				sendWarning("Cannot calc result for " + object
						+ " with expression " + scoringFunction, e);
			}
		}

		scoredObject.setE1(score);

		return scoredObject;
	}

	protected void initScoringFunction(SDFSchema schema) {
		List<SDFAttribute> neededAttributes = scoringFunction
				.getAllAttributes();
		
		this.variables = new VarHelper[neededAttributes.size()];
		int j = 0;
		for (SDFAttribute curAttribute : neededAttributes) {
			variables[j++] = initAttribute(schema, curAttribute);
		}
	}

	public VarHelper initAttribute(SDFSchema schema, SDFAttribute curAttribute) {
		int index = schema.indexOf(curAttribute);
		// Attribute is part of payload
		if (index >= 0) {
			return new VarHelper(index, 0);
		} else { // Attribute is (potentially) part of meta data;
			Pair<Integer, Integer> pos = schema.indexOfMetaAttribute(curAttribute);
			if (pos != null){
				return new VarHelper(pos.getE1(), pos.getE2(), 0);
			}
		}
		throw new RuntimeException("Cannot find attribute "+curAttribute+" in input stream!");
	}

	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> kv = new HashMap<String, String>();
		kv.put("Elements processed",elementsRead+"");
		kv.put("No Of Groups", topKMap.size() + "");		
		// Show at least 10 groups
		int i = 10;
		for (Entry<Long, ArrayList<SerializablePair<Double, T>>> e: topKMap.entrySet()){
			kv.put("Top-k-Map size group "+e.getKey(), e.getValue().size()+ "");
			if (i-- < 0){
				break;
			}
		}
		return kv;
	}

	/**
	 * @return the orderByTimestamp
	 */
	public boolean isOrderByTimestamp() {
		return orderByTimestamp;
	}

	/**
	 * @param orderByTimestamp the orderByTimestamp to set
	 */
	public void setOrderByTimestamp(boolean orderByTimestamp) {
		this.orderByTimestamp = orderByTimestamp;
	}

}
