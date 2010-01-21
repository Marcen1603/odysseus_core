package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * 
 * @author André Bolles
 *
 */
public class RelationalRangePredicate extends AbstractRangePredicate<RelationalTuple<?>>{

	private static final long serialVersionUID = 1222104352250883947L;

	private SDFExpression[] expressions;
	private String[] operators;

	private Map<IPredicate, ISolution> solutions;
	
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
	 * 
	 * @param expression
	 */
	public RelationalRangePredicate(Map<IPredicate, ISolution> solutions) {
		this.solutions = solutions;
	}

	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		this.attributePositions = new HashMap<IPredicate, int[]>();
		this.fromRightChannel = new HashMap<IPredicate, boolean[]>();
		
		for(Entry<IPredicate, ISolution> entry: this.solutions.entrySet()){
			IPredicate predicate = entry.getKey();
			ISolution solution = entry.getValue();
			
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

	public List<ITimeInterval> evaluate(RelationalTuple<?> input) {
		List<ITimeInterval> intervals = new ArrayList<ITimeInterval>();
		
		// first check, which solution maps for this tuple
		// so test every predicate in this.solutions for this tuple
		// the first predicate that is true will be taken.
		for(Entry<IPredicate, ISolution> entry: this.solutions.entrySet()){
			if(entry.getKey().evaluate(input)){
				int[] curAttributePositions = this.attributePositions.get(entry.getKey());
				Object[] values = new Object[curAttributePositions.length];
				for(int i = 0; i<values.length; ++i){
					values[i] = input.getAttribute(curAttributePositions[i]);
				}
				
				entry.getValue().getSolution().bindVariables(values);
				double result = (Double)entry.getValue().getSolution().getValue();
				
				// TODO at the moment we only have one solution for each predicate
				// because of the use of only linear prediction functions.
				String compareOperator = entry.getValue().getCompareOperator();
				ITimeInterval timeInterval = null;
				if(compareOperator.equals("<")){
					// since the dsm only uses discrete time, we have to round
					// the result
					long discretePointInTime = (long)Math.floor(result);
					
					// -infinity has the meaning of zero time in our context
					timeInterval = new TimeInterval(PointInTime.getZeroTime(), new PointInTime(discretePointInTime, 0));
				}
				else if(compareOperator.equals("<=")){
					long discretePointInTime = (long)Math.floor(result);
					
					// -infinity has the meaning of zero time in our context
					timeInterval = new TimeInterval(PointInTime.getZeroTime(), new PointInTime(discretePointInTime+1, 0));
				}
				else if(compareOperator.equals(">=")){
					long discretePointInTime = (long)Math.ceil(result);
					
					// -infinity has the meaning of zero time in our context
					timeInterval = new TimeInterval(new PointInTime(discretePointInTime, 0), PointInTime.getZeroTime());
				}
				else if(compareOperator.equals(">")){
					long discretePointInTime = (long)Math.ceil(result);
					
					// -infinity has the meaning of zero time in our context
					timeInterval = new TimeInterval(new PointInTime(discretePointInTime+1, 0), PointInTime.getZeroTime());
				}
				
				intervals.add(timeInterval);
				break;
			}
		}
		
		return intervals;
	}

	public List<ITimeInterval> evaluate(RelationalTuple<?> left, RelationalTuple<?> right) {
		List<ITimeInterval> intervals = new ArrayList<ITimeInterval>();
		
		// first check, which solution maps for this tuple
		// so test every predicate in this.solutions for this tuple
		// the first predicate that is true will be taken.
		for(Entry<IPredicate, ISolution> entry: this.solutions.entrySet()){
			if(entry.getKey().evaluate(left, right)){
				int[] curAttributePositions = this.attributePositions.get(entry.getKey());
				Object[] values = new Object[curAttributePositions.length];
				for(int i = 0; i<values.length; ++i){
					RelationalTuple<?> r = this.fromRightChannel.get(entry.getKey())[i] ? right : left;
					values[i] = r.getAttribute(curAttributePositions[i]);
				}
				
				entry.getValue().getSolution().bindVariables(values);
				double result = (Double)entry.getValue().getSolution().getValue();
				
				// TODO at the moment we only have one solution for each predicate
				// because of the use of only linear prediction functions.
				String compareOperator = entry.getValue().getCompareOperator();
				ITimeInterval timeInterval = null;
				if(compareOperator.equals("<")){
					// since the dsm only uses discrete time, we have to round
					// the result
					long discretePointInTime = (long)Math.floor(result);
					
					// -infinity has the meaning of zero time in our context
					timeInterval = new TimeInterval(PointInTime.getZeroTime(), new PointInTime(discretePointInTime, 0));
				}
				else if(compareOperator.equals("<=")){
					long discretePointInTime = (long)Math.floor(result);
					
					// -infinity has the meaning of zero time in our context
					timeInterval = new TimeInterval(PointInTime.getZeroTime(), new PointInTime(discretePointInTime+1, 0));
				}
				else if(compareOperator.equals(">=")){
					long discretePointInTime = (long)Math.ceil(result);
					
					// -infinity has the meaning of zero time in our context
					timeInterval = new TimeInterval(new PointInTime(discretePointInTime, 0), PointInTime.getZeroTime());
				}
				else if(compareOperator.equals(">")){
					long discretePointInTime = (long)Math.ceil(result);
					
					// -infinity has the meaning of zero time in our context
					timeInterval = new TimeInterval(new PointInTime(discretePointInTime+1, 0), PointInTime.getZeroTime());
				}
				
				intervals.add(timeInterval);
				break;
			}
		}
		
		return intervals;
	}

	@Override
	public RelationalRangePredicate clone() {
		return new RelationalRangePredicate(this);
	}

	@Override
	public String toString() {
		String res = "";
		for(Entry<IPredicate, ISolution> entry: this.solutions.entrySet()){
			res += "if " + entry.getKey().toString() + 
					" then " + entry.getValue().getVariable().toString() + " " +
					entry.getValue().getCompareOperator() + " " + 
					entry.getValue().getSolution().toString() + "\n"; 
		}
		return res;
	}

	public Map<IPredicate, List<SDFAttribute>> getAttributes() {
		Map<IPredicate, List<SDFAttribute>> attributes = new HashMap<IPredicate, List<SDFAttribute>>();
		for(Entry<IPredicate, ISolution> entry: this.solutions.entrySet()){
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
	
}
