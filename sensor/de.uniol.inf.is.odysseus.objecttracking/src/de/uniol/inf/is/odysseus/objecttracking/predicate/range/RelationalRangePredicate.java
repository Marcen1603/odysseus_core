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

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.util.ComparablePair;
import de.uniol.inf.is.odysseus.objecttracking.util.ObjectTrackingPredicateInitializer;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author André Bolles
 *
 */
@SuppressWarnings("unchecked")
public class RelationalRangePredicate extends AbstractRangePredicate<RelationalTuple<?>>{

	private static final long serialVersionUID = 1222104352250883947L;
	
	private static Logger logger = LoggerFactory.getLogger(RelationalRangePredicate.class);
	
	private LinkedList<ComparablePair<IPredicate, ISolution>> lastEvaluated;
	private int windowSize;
	
	/**
	 * EVALUATION: Alle changeWindow Evaluationen wird die Liste mit den
	 * Prädikaten so umsortiert, dass die bisher betrachteten Prädikate
	 * ganz nach hinten rutschen und die bisher nicht betrachteten
	 * Prädikate, ganz nach vorne. Entsprechend wird auch die Liste
	 * lastEvaluated umsortiert.
	 */
	private int changeWindow = 100;
	
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
		throw new UnsupportedOperationException();
//		this.attributePositions = predicate.attributePositions.clone();
//		this.fromRightChannel = predicate.fromRightChannel.clone();
//		this.expressions = predicate.expressions.clone();
	}

	private boolean found = false;
	
	public List<ITimeInterval> evaluate(RelationalTuple<?> input) {
		this.evaluationCount++;
		List<ITimeInterval> intervals = new ArrayList<ITimeInterval>();
		
		
		// first check, which solution maps for this tuple
		// so test every predicate in this.solutions for this tuple
		// the first predicate that is true will be taken.
		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
			if(entry.getKey().evaluate(input)){
//				logger.debug("Range predicate is true: " + entry.getKey().toString());
//				logger.debug("Found at predicate " + (this.solutions.indexOf(entry) + 1) + " from " + this.solutions.size());
//				this.found = true;
				
				
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
				
				this.moveEvaluationWindow(entry);
				break;
			}
		}
		
		return intervals;
	}

	public List<ITimeInterval> evaluate(RelationalTuple<?> left, RelationalTuple<?> right) {
		this.evaluationCount++;
		List<ITimeInterval> intervals = new ArrayList<ITimeInterval>();
		
		// first check, which solution maps for this tuples
		// so test every predicate in this.solutions for this tuple
		// the first predicate that is true will be taken.
		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
			if(entry.getKey().evaluate(left, right)){
//				logger.debug("Range predicate is true: " + entry.getKey().toString());
				// the solution can be empty
				// in this case the following must not be done.
				// However, if the solution is full
				// we have to create a full time interval
				if(entry.getValue().getSolution() != null && !entry.getValue().isFull()){
					int[] curAttributePositions = this.attributePositions.get(entry.getKey());
					Object[] values = new Object[curAttributePositions.length];
					for(int i = 0; i<values.length; ++i){
						RelationalTuple<?> r = this.fromRightChannel.get(entry.getKey())[i] ? right : left;
						if(curAttributePositions[i] == -1){
							System.out.println("AttrPos: " + this.attributePositions.get(entry.getKey()));
							System.out.println("frc: " + this.fromRightChannel.get(entry.getKey()));
							System.out.println("Solution: " + entry.getValue());
							System.out.println("NAttrs: " + entry.getValue().getSolution().getAllAttributes());
						}
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
		
		return intervals;
	}
	
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

	public boolean equals(Object other){
		if(!(other instanceof RelationalRangePredicate)){
			return false;
		}
		else{
			return this.solutions.equals(((RelationalRangePredicate)other).solutions);
		}
	}
	
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
		if(this.changeWindow > 0 && this.evaluationCount % this.changeWindow == 0){
			long start = System.nanoTime();
			this.changeOrder();
			long end = System.nanoTime();
			this.changeDuration += (end -start);
			return;
		}
		
		ComparablePair<IPredicate, ISolution> removed = null;
		if(this.lastEvaluated.size() == this.windowSize){
			removed = this.lastEvaluated.removeFirst();
			removed.decreasePriority();
		}
		this.lastEvaluated.addLast(lastEvaluatedPredicate);
		lastEvaluatedPredicate.increasePriority();
		
//		logger.debug("Last evaluated: " + this.lastEvaluated);
		
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
		Collections.reverse(this.solutions);
		for(ComparablePair<IPredicate, ISolution> entry: this.solutions){
			entry.setPriority(0);
		}
		
		this.lastEvaluated.clear();
		outer:
		for(int i = 0; i<this.solutions.size()-1; i++){
			for(int u = 0; u<=i; u++){
				if(this.lastEvaluated.size() < this.windowSize){
					ComparablePair<IPredicate, ISolution> curSolution = this.solutions.get(u);
					this.lastEvaluated.addLast(curSolution);
					curSolution.increasePriority();
				}
				else{
					break outer;
				}
			}
		}
	}

	@Override
	public long getAdditionalEvaluationDuration() {
		// TODO Auto-generated method stub
		return this.changeDuration;
	}
	
}
