package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.SerializablePair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.TuplePunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * This operator calculates the top k elements from the input with a scoring
 * function
 * 
 * @author Marco Grawunder
 * 
 * @param <T>
 * @param <M>
 */
abstract public class AbstractRelationalTopKPO<T extends Tuple<M>, M extends ITimeInterval>
		extends AbstractPipe<T, T> implements IPhysicalOperatorKeyValueProvider {

	private class TopKComparatorAsc implements Comparator<SerializablePair<Double, T>> {

		@Override
		public int compare(SerializablePair<Double, T> left, SerializablePair<Double, T> right) {
			return left.getE1().compareTo(right.getE1());
		}
	}

	private class TopKComparatorAscTS extends TopKComparatorAsc {

		@Override
		public int compare(SerializablePair<Double, T> left, SerializablePair<Double, T> right) {
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

	private class TopKComparatorDesc implements Comparator<SerializablePair<Double, T>> {

		@Override
		public int compare(SerializablePair<Double, T> left, SerializablePair<Double, T> right) {
			return right.getE1().compareTo(left.getE1());
		}
	}

	private class TopKComparatorDescTS extends TopKComparatorDesc {

		@Override
		public int compare(SerializablePair<Double, T> left, SerializablePair<Double, T> right) {
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

	final private RelationalExpression<M> setupExpression;
	final private RelationalExpression<M> preScoreExpression;
	final private RelationalExpression<M> scoreExpression;
	final private RelationalExpression<M> tearDownExpression;
	final private RelationalExpression<M> cleanupPredicate;
	
	final private int[] uniqueAttributePos;

	protected final int k;
	final private Map<Object, TopKDataStructure<T,M>> topKMap = new HashMap<>();
	final Comparator<SerializablePair<Double, T>> comparator;
	final private IGroupProcessor<T, T> groupProcessor;
	final boolean addScore;
	
	private Map<Object, LinkedList<T>> lastResultMap = new HashMap<>();
	private long elementsRead;

	private boolean suppressDuplicates;
	protected boolean orderByTimestamp = true;

	final private boolean triggerOnlyByPunctuation;

	public AbstractRelationalTopKPO(SDFSchema inputSchema, SDFSchema outputSchema, SDFExpression setupFunction,
			SDFExpression preScoringFunction, SDFExpression scoringFunction, SDFExpression tearDownFunction, SDFExpression cleanupPredicate, int k, boolean descending,
			boolean suppressDuplicates, List<SDFAttribute> uniqueAttributes, IGroupProcessor<T, T> groupProcessor,
			boolean triggerOnlyByPunctuation, boolean addScore) {
		super();
		
		this.addScore = addScore;
		
		if (setupFunction != null){
			this.setupExpression = new RelationalExpression<>(setupFunction);
			this.setupExpression.initVars(inputSchema);
		}else{
			this.setupExpression = null;
		}
		if (preScoringFunction != null){
			this.preScoreExpression = new RelationalExpression<>(preScoringFunction);
			this.preScoreExpression.initVars(inputSchema);
		}else{
			this.preScoreExpression = null;
		}
		this.scoreExpression = new RelationalExpression<M>(scoringFunction);
		this.scoreExpression.initVars(inputSchema);
		if (tearDownFunction != null){
			this.tearDownExpression = new RelationalExpression<>(tearDownFunction);
			this.tearDownExpression.initVars(inputSchema);
		}else{
			this.tearDownExpression = null;
		}
		
		if (cleanupPredicate != null){
			this.cleanupPredicate = new RelationalExpression<>(cleanupPredicate);
			this.cleanupPredicate.initVars(outputSchema);
		}else{
			this.cleanupPredicate = null;
		}
		
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
		if (uniqueAttributes != null){
			this.uniqueAttributePos = new int[uniqueAttributes.size()];
			int counter=0;
			for (SDFAttribute a:uniqueAttributes){
				this.uniqueAttributePos[counter++] = inputSchema.indexOf(a);
			}
		}else{
			this.uniqueAttributePos = null;
		}
		this.triggerOnlyByPunctuation = triggerOnlyByPunctuation;
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
		elementsRead = 0;
	}

	@Override
	protected synchronized void process_next(T object, int port) {
		if (setupExpression != null){
			setupExpression.evaluate(object, getSessions(), null);
		}
		
		elementsRead++;
		Object gId = groupProcessor.getGroupID(object);

		
		TopKDataStructure<T,M> topK = topKMap.get(gId);
		
		if (topK == null) {
			topK = new TopKDataStructure<T,M>(comparator, orderByTimestamp, uniqueAttributePos);
			topKMap.put(gId, topK);
		}

		if (uniqueAttributePos!=null){
			topK.removeSame(object);
		}

		if (cleanupPredicate != null){
			cleanUp(topK);
		}else{
			cleanUp(object.getMetadata().getStart(), topK);
		}
		
		updateTopKList(object, topK);
		
		if (tearDownExpression != null){
			tearDownExpression.evaluate(object, getSessions(), null);
		}
		
		if (!triggerOnlyByPunctuation) {
			produceResult(object, topK, gId);
		}
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
		if (triggerOnlyByPunctuation && punctuation instanceof TuplePunctuation) {
			@SuppressWarnings("unchecked")
			T object = ((TuplePunctuation<T, M>) punctuation).getTuple();
			Object gId = groupProcessor.getGroupID(object);
			TopKDataStructure<T,M> topK = topKMap.get(gId);
			if (topK != null) {
				produceResult(object, topK, gId);
			}
		}
	}

	private void cleanUp(PointInTime start,
			TopKDataStructure<T,M> topK) {
		Iterator<SerializablePair<Double, T>> iter = topK.iterator();
		while (iter.hasNext()) {
			M metadata = iter.next().getE2().getMetadata();
			if (metadata.getEnd()
					.beforeOrEquals(start)) {
				iter.remove();
			}
			if (metadata.getStart().afterOrEquals(start)){
				break;
			}
			
		}
	}
	
	private void cleanUp(TopKDataStructure<T,M> topK) {
		Iterator<SerializablePair<Double, T>> iter = topK.iterator();
		while (iter.hasNext()) {
			T elem = iter.next().getE2();
			if (((boolean)cleanupPredicate.evaluate(elem, getSessions(), null))){
				iter.remove();
			}
		}
	}
	
	protected abstract void updateTopKList(T object,
			TopKDataStructure<T,M> topK);
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void produceResult(T object,
			TopKDataStructure<T,M> topK, Object groupID) {
		// Produce result
		T groupingPart = groupProcessor.getGroupingPart(object);
		final T result;
		if (groupingPart == null) {
			result = (T) new Tuple(2, false);
		} else {
			result = (T) new Tuple(2 + groupingPart.getAttributes().length,
					false);
		}
		Iterator<SerializablePair<Double, T>> iter = topK.iterator();
		List<T> resultList = new LinkedList<T>();
		int limit = k<=0?topK.size():k;
		for (int i = 0; i < limit && iter.hasNext(); i++) {
			SerializablePair<Double, T> next = iter.next();		
			T out;
			if (addScore){
				out = (T) next.getE2().append(next.getE1());
			}else{
				out = (T) next.getE2().clone();				
			}
			out.setMetadata(null);
			resultList.add(out);
		}
		boolean sameAsLastResult = suppressDuplicates ? compareWithLastResult(
				resultList, groupID) : false;

		if (!sameAsLastResult) {
			lastResultMap.put(groupID, new LinkedList<T>(resultList));
			result.setAttribute(0, resultList);
			result.setAttribute(1, object);
			if (groupingPart != null) {
				for (int i = 0; i < groupingPart.getAttributes().length; ++i) {
					result.setAttribute(i + 2, groupingPart.getAttributes()[i]);
				}
			}
			M meta = (M) object.getMetadata().clone();
			result.setMetadata(meta);
			transfer(result);
		}

	}

	private boolean compareWithLastResult(List<T> resultList, Object groupID) {
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

	protected SerializablePair<Double, T> calcScore(T object) {
	
		if (preScoreExpression != null){
			preScoreExpression.evaluate(object, getSessions(), null);
		}
		
		SerializablePair<Double, T> scoredObject = new SerializablePair<>();
		Double score = null;
		scoredObject.setE2(object);

		// TODO: Fill history
		LinkedList<Tuple<M>> history = null;

		try {
			score = ((Number) this.scoreExpression.evaluate(object, getSessions(),
					history)).doubleValue();
			
		} catch (Exception e) {
			if (!(e instanceof NullPointerException)) {
				sendWarning("Cannot calc result for " + object
						+ " with expression " + scoreExpression, e);
			}
			e.printStackTrace();
		}
		
		if (score == null){
			sendWarning("Cannot calc result for " + object
						+ " with expression " + scoreExpression);
		}

		scoredObject.setE1(score);

		return scoredObject;
	}

	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> kv = new HashMap<String, String>();
		kv.put("Elements processed", elementsRead + "");
		kv.put("No Of Groups", topKMap.size() + "");
		// Show at least 10 groups
		int i = 10;
		for (Entry<Object, TopKDataStructure<T,M>> e : topKMap
				.entrySet()) {
			kv.put("Top-k-Map size group " + e.getKey(), e.getValue().size()
					+ "");
			if (i-- < 0) {
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
	 * @param orderByTimestamp
	 *            the orderByTimestamp to set
	 */
	public void setOrderByTimestamp(boolean orderByTimestamp) {
		this.orderByTimestamp = orderByTimestamp;
	}

}
