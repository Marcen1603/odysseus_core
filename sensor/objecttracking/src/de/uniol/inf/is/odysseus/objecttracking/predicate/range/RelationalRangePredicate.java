package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.util.ComparablePair;
import de.uniol.inf.is.odysseus.objecttracking.util.ObjectTrackingPredicateInitializer;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author André Bolles
 *
 */
@SuppressWarnings("unchecked")
public class RelationalRangePredicate<M extends IApplicationTime> extends AbstractRangePredicate<RelationalTuple<M>>{

	private static final long serialVersionUID = 1222104352250883947L;
	
	private static Logger logger = LoggerFactory.getLogger(RelationalRangePredicate.class);
	
	private LinkedList<ComparablePair<IPredicate, ISolution>> lastEvaluated;
	private int windowSize;
	private ComparablePair<IPredicate, ISolution> truePred;
	private boolean changed;
	
	/**
	 * EVALUATION: Alle changeWindow Evaluationen wird die Liste mit den
	 * Prädikaten so umsortiert, dass die bisher betrachteten Prädikate
	 * ganz nach hinten rutschen und die bisher nicht betrachteten
	 * Prädikate, ganz nach vorne. Entsprechend wird auch die Liste
	 * lastEvaluated umsortiert.
	 */
	private int changeWindow = 1;
	
	/**
	 * EVLUATION: count the number of evaluations of this predicate
	 */
	private int evaluationCount;
	
	public long changeDuration;

	private List<ComparablePair<IPredicate, ISolution>> solutions;
//	private Map<IPredicate, ISolution> solutions;
	
	/**
	 * Stores the attribute positions for each solution,
	 * so that these do not have to be determined during runtime
	 * of the query.
	 */
	private Map<IPredicate, int[]> attributePositions;

	/** 
	 * fromRightChannel.get(p)[i] stores if the getAttribute(attributePositions.get(p)[i])
	 * should be called on the left or on the right input tuple for the solution corresponding to
	 * predicate p
	 *
	 */
	private Map<IPredicate, boolean[]> fromRightChannel;

	/**
	 * A relational range predicate has to evaluate expressions like
	 * t > x^2 + b
	 * t < x^3 - c
	 * 
	 * So, there must be an array of expressions that contains
	 * pos 0: x^2+b
	 * pos 1: x^3-c
	 * 
	 * and a corresponding array of compare operators
	 * pos 0: >
	 * pos 1: <
	 * 
	 * TODO: Das ist noch gar nicht drin. Was hier gemacht wird, ist, dass
	 * für die einzelnen Bedingungen die Attributpositionen bestimmt werden.
	 * 
	 * Falls a<b => t > c
	 * Falls a>b => t < c usw.
	 * 
	 * @param solutions The solutions with their conditions, that hold for this range predicate.
	 * @param windowSize The last windowSize evaluations of this predicate will be used to determine which condition
	 *                   has been most often been true. This conditions will then be evaluated first, the next time.
	 */
	public RelationalRangePredicate(Map<IPredicate, ISolution> solutions, int windowSize) {		
		this.solutions = new ArrayList<ComparablePair<IPredicate, ISolution>>();
		
		for(Entry<IPredicate, ISolution> entry : solutions.entrySet()){
			this.solutions.add(new ComparablePair<IPredicate, ISolution>(entry.getKey(), entry.getValue()));
		}
		Collections.shuffle(this.solutions);

		
		this.lastEvaluated = new LinkedList<ComparablePair<IPredicate, ISolution>>();
		this.windowSize = windowSize;
		
		
	}

	@Override
	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		this.attributePositions = new HashMap<IPredicate, int[]>();
		this.fromRightChannel = new HashMap<IPredicate, boolean[]>();
		
		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
			IPredicate predicate = entry.getKey();

			// could be a complex predicate, that contains relational
			// predicates. These must be initialized with the schema.
			ObjectTrackingPredicateInitializer.visitPredicates(predicate, leftSchema, rightSchema);
			
			ISolution solution = entry.getValue();
			
			// TODO: Was ist mit der FullSolution?
			if(solution.getSolution() != null){
				List<SDFAttribute> neededAttributes = solution.getSolution().getAllAttributes();
				int[] curAttributePositions = new int[neededAttributes.size()];
				boolean[] curFromRightChannel = new boolean[neededAttributes.size()];
				
				int i = 0;
				for (SDFAttribute curAttribute : neededAttributes) {
					SDFAttribute cqlAttr = (SDFAttribute) curAttribute;
					int pos = indexOf(leftSchema, cqlAttr);
					if (pos == -1) {
						// if you get here, there is an attribute
						// in the predicate that does not exist
						// in the left schema, so there must also be
						// a right schema
						pos = indexOf(rightSchema, cqlAttr);
						curFromRightChannel[i] = true;
					}
					curAttributePositions[i++] = pos;
				}
				
				this.attributePositions.put(predicate, curAttributePositions);
				this.fromRightChannel.put(predicate, curFromRightChannel);
			}
			else{
				// the solution is null, since it is the empty solution
				this.attributePositions.put(predicate, null);
				this.fromRightChannel.put(predicate, null);
			}
		}
	}

	private int indexOf(SDFAttributeList schema, SDFAttribute cqlAttr) {
		Iterator<SDFAttribute> it = schema.iterator();
		for (int i = 0; it.hasNext(); ++i) {
			if (cqlAttr.equalsCQL((SDFAttribute) it.next())) {
				return i;
			}
		}
		return -1;
	}

	public RelationalRangePredicate(RelationalRangePredicate predicate) {
//		throw new UnsupportedOperationException();
		this.attributePositions = new HashMap<IPredicate, int[]>(predicate.attributePositions);
		this.fromRightChannel = new HashMap<IPredicate, boolean[]>(predicate.fromRightChannel);
		this.solutions = new ArrayList<ComparablePair<IPredicate, ISolution>>();
		for(int i = 0; i < predicate.solutions.size(); i++){
			this.solutions.add((ComparablePair<IPredicate, ISolution>)predicate.solutions.get(i));
		}
	}
	
	
	@Override
	public List<ITimeInterval> evaluate(RelationalTuple<M> input) {
		this.evaluationCount++;
		List<ITimeInterval> intervals = new ArrayList<ITimeInterval>();
		
		
		// first check, which solution maps for this tuple
		// so test every predicate in this.solutions for this tuple
		// the first predicate that is true will be taken.
		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
			if(entry.getKey().evaluate(input)){
				// the solution could be empty
				// in this case the following must not be done
				if(entry.getValue().getSolution() != null && !entry.getValue().isFull()){
					int[] curAttributePositions = this.attributePositions.get(entry.getKey());
					Object[] values = new Object[curAttributePositions.length];
					for(int i = 0; i<values.length; ++i){
						values[i] = input.getAttribute(curAttributePositions[i]);
					}
					
					entry.getValue().getSolution().bindVariables(values);
					double result = (Double)entry.getValue().getSolution().getValue();
					
					// the result is the value for t and t is the delta time from start
					// timestamp on. This value must be > 0, since otherwise
					// the application time would not be in the future.
					// In ObjectTracking context only future results have useful semantics. 
					if(result > 0){
					
					
						// TODO at the moment we only have one solution for each predicate
						// because of the use of only linear prediction functions.
						String compareOperator = entry.getValue().getCompareOperator();
						ITimeInterval timeInterval = this.createApplicationTime(compareOperator, result);
						intervals.add(timeInterval);
					}
				}
				else if(entry.getValue().isFull()){
					intervals.add(new TimeInterval(PointInTime.getZeroTime(), PointInTime.getInfinityTime()));
				}
				
//				this.moveEvaluationWindow(entry);
				break;
			}
		}
		
		// now the intersection between elem and new must be calculated
		List<ITimeInterval> leftRanges = input.getMetadata().getAllApplicationTimeIntervals();
		List<ITimeInterval> resultRanges = ApplicationTime.intersectIntervals(leftRanges, intervals);
		
		// assuming input is already sorted
		Collections.sort(resultRanges);
		
		return intervals;
	}

	@Override
	public List<ITimeInterval> evaluate(RelationalTuple<M> left, RelationalTuple<M> right) {
		this.evaluationCount++;
		List<ITimeInterval> intervals = new ArrayList<ITimeInterval>();
		
		// first check, which solution maps for this tuples
		// so test every predicate in this.solutions for this tuple
		// the first predicate that is true will be taken.
		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
			if(entry.getKey().evaluate(left, right)){
				truePred = entry;
				// the solution can be empty
				// in this case the following must not be done.
				// However, if the solution is full
				// we have to create a full time interval
				if(entry.getValue().getSolution() != null && !entry.getValue().isFull()){
					int[] curAttributePositions = this.attributePositions.get(entry.getKey());
					Object[] values = new Object[curAttributePositions.length];
					for(int i = 0; i<values.length; ++i){
						RelationalTuple<?> r = this.fromRightChannel.get(entry.getKey())[i] ? right : left;
						values[i] = r.getAttribute(curAttributePositions[i]);
					}
					
					entry.getValue().getSolution().bindVariables(values);
					double result = (Double)entry.getValue().getSolution().getValue();
					
					// the result is the value for t and t is the absolute time
					// we cannot use t as delta time, since then we would have
					// two different t for each tuple from the left and right input.
					if(result > 0){
						
						String compareOperator = entry.getValue().getCompareOperator();
						ITimeInterval timeInterval = this.createApplicationTime(compareOperator, result);
						intervals.add(timeInterval);
						
					}
				}
				else if(entry.getValue().isFull()){
					intervals.add(new TimeInterval(PointInTime.getZeroTime(), PointInTime.getInfinityTime()));
				}
				
				this.moveEvaluationWindow(entry);
				break;
			}
		}
		
		// EVALUATION
		if(!changed && this.solutions.size() > 2){
			this.solutions.remove(truePred);
			this.solutions.add(3, truePred);
			changed = true;
		}
		
		// now the intersection between left, right new must be calculated
		List<ITimeInterval> leftRanges = left.getMetadata().getAllApplicationTimeIntervals();
		List<ITimeInterval> rightRanges = right.getMetadata().getAllApplicationTimeIntervals();
		
		// assuming left and right are sorted;
		Collections.sort(intervals);
		
		List<ITimeInterval> tmpResultRanges = ApplicationTime.intersectIntervals(leftRanges, rightRanges);
		List<ITimeInterval> resultRanges = ApplicationTime.intersectIntervals(tmpResultRanges, intervals);
		
		Collections.sort(resultRanges);
		
		return resultRanges;
	}
	
	/**
	 * TODO Wir behandeln nur Intervalle im positiven Bereich
	 * @param compareOperator
	 * @param pointInTime
	 * @return
	 */
	private ITimeInterval createApplicationTime(String compareOperator, double pointInTime){
		// TODO at the moment we only have one solution for each predicate
		// because of the use of only linear prediction functions.
		
		ITimeInterval timeInterval = null;
		if(compareOperator.equals("<")){
			// since the dsm only uses discrete time, we have to round
			// the result
			long discretePointInTime = (long)Math.floor(pointInTime);
			
			// -infinity has the meaning of zero time in our context
			timeInterval = new TimeInterval(PointInTime.getZeroTime(), new PointInTime(discretePointInTime));
		}
		else if(compareOperator.equals("<=")){
			long discretePointInTime = (long)Math.floor(pointInTime);
			
			// -infinity has the meaning of zero time in our context
			timeInterval = new TimeInterval(PointInTime.getZeroTime(), new PointInTime(discretePointInTime+1));
		}
		else if(compareOperator.equals(">=")){
			long discretePointInTime = (long)Math.ceil(pointInTime);
			
			timeInterval = new TimeInterval(new PointInTime(discretePointInTime), PointInTime.getInfinityTime());
		}
		else if(compareOperator.equals(">")){
			long discretePointInTime = (long)Math.ceil(pointInTime);
			
			timeInterval = new TimeInterval(new PointInTime(discretePointInTime+1), PointInTime.getInfinityTime());
		}
		
		return timeInterval;
	}

	@Override
	public RelationalRangePredicate clone() {
		return new RelationalRangePredicate(this);
	}

	@Override
	public String toString() {
		String res = "";
		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
			res += "if " + entry.getKey().toString() + 
					" then " + entry.getValue().getVariable().toString() + " " +
					entry.getValue().getCompareOperator() + " " + 
					entry.getValue().getSolution().toString() + "\n"; 
		}
		return res;
	}

	public Map<IPredicate, List<SDFAttribute>> getAttributes() {
		Map<IPredicate, List<SDFAttribute>> attributes = new HashMap<IPredicate, List<SDFAttribute>>();
		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
			attributes.put(entry.getKey(), entry.getValue().getSolution().getAllAttributes());
		}
		
		return attributes;
	}
	
	public boolean isSetOperator(String symbol) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object other){
		if(!(other instanceof RelationalRangePredicate)){
			return false;
		}
		else{
			return this.solutions.equals(((RelationalRangePredicate)other).solutions);
		}
	}
	
	@Override
	public int hashCode(){
		return 53 * this.solutions.hashCode();
	}
	
	public void sortSolutions(){
		Collections.sort(this.solutions);
	}
	
	/**
	 * This method moves a window of size windowSize over the last evaluations of the
	 * solution predicates. Everytime a predicate is added to the window, the priority of
	 * this predicate is increased. Everytime a predicate is removed from the window, the
	 * priority of this predicate is decreased. Furthermore, the list of predicates is
	 * sorted again according to their new priorities. This guarantees, that always the
	 * predicate ist evaluated first, that has been true most often during the last
	 * windowSize evaluations.
	 * 
	 * @param lastEvaluatedPredicate The predicate that hase been true this time.
	 */
	private void moveEvaluationWindow(ComparablePair<IPredicate, ISolution> lastEvaluatedPredicate){		
		// if window size is 0, then no
		// optimization is wanted.
		if(this.windowSize == 0){
			return;
		}
		
		
		// FOR EVALUATION
//		if(this.changeWindow > 0 && this.evaluationCount % this.changeWindow == 0){
//			long start = System.nanoTime();
//			this.changeOrder();
//			long end = System.nanoTime();
//			this.changeDuration += (end -start);
//			return;
//		}
		
		ComparablePair<IPredicate, ISolution> removed = null;
		if(this.lastEvaluated.size() == this.windowSize){
			removed = this.lastEvaluated.removeFirst();
			removed.decreasePriority();
		}
		this.lastEvaluated.addLast(lastEvaluatedPredicate);
		lastEvaluatedPredicate.increasePriority();
		
		// a sorting is necessary only if the removed and the added predicate are not equal
		if(removed == null || (removed != null && removed != lastEvaluatedPredicate)){
			Collections.sort(this.solutions);
		}
	}
	
	/**
	 * Dient der Evaluation, damit auch mal der Wechsel von Prädikaten
	 * vorkommt.
	 */
	private void changeOrder(){
		Random r = new Random();
		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
			entry.setPriority(0);
		}
		
		this.solutions.remove(this.truePred);
		this.solutions.add(0, this.truePred);
		
		this.lastEvaluated.clear();
		
		while(this.lastEvaluated.size() < this.windowSize){
			int nextInt = r.nextInt(3);
			this.lastEvaluated.add(this.solutions.get(nextInt));
			this.solutions.get(nextInt).increasePriority();
		}
		
//		long start = System.nanoTime();
		Collections.sort(this.solutions);
//		long end = System.nanoTime();
//		this.changeDuration -= (end - start);
		
//		====================================================================
		
//		Collections.reverse(this.solutions);
//		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
//			entry.setPriority(0);
//		}
//		
//		this.lastEvaluated.clear();
//		outer:
//		for(int i = 0; i<this.solutions.size()-1; i++){
//			for(int u = 0; u<=i; u++){
//				if(this.lastEvaluated.size() < this.windowSize){
//					ComparablePair<IPredicate, ISolution> curSolution = this.solutions.get(u);
//					this.lastEvaluated.addLast(curSolution);
//					curSolution.increasePriority();
//				}
//				else{
//					break outer;
//				}
//			}
//		}
	}

	@Override
	public long getAdditionalEvaluationDuration() {
		// TODO Auto-generated method stub
		return this.changeDuration;
	}
	
}
