package de.uniol.inf.is.odysseus.objecttracking.predicate.range;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
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

	/**
	 * Stores, which attributes to use for the variable bindings
	 * [0][]: attributes for the first expression
	 * [1][]: attributes for the second expression
	 * 
	 * and so on.
	 */
	private int[][] attributePositions;

	/** 
	 * fromRightChannel[u][i] stores if the getAttribute(attributePositions[u][i])
	 * should be called on the left or on the right input tuple for expression no u
	 *
	 */
	private boolean[][] fromRightChannel;

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
	public RelationalRangePredicate(SDFExpression[] expressions, String[] operators) {
		this.expressions = expressions;
		this.operators = operators;
	}

	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		this.attributePositions = new int[this.expressions.length][];
		this.fromRightChannel = new boolean[this.expressions.length][];
		for(int exprNo = 0; exprNo <this.expressions.length; exprNo++){
			List<SDFAttribute> neededAttributes = this.expressions[exprNo].getAllAttributes();
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
			
			this.attributePositions[exprNo] = curAttributePositions;
			this.fromRightChannel[exprNo] = curFromRightChannel;
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
		this.attributePositions = predicate.attributePositions.clone();
		this.fromRightChannel = predicate.fromRightChannel.clone();
		this.expressions = predicate.expressions.clone();
	}

	public List<ITimeInterval> evaluate(RelationalTuple<?> input) {
		for(int exprNo = 0; exprNo<this.expressions.length; exprNo++){
			Object[] values = new Object[this.attributePositions[exprNo].length];
			for (int i = 0; i < values.length; ++i) {
				values[i] = input.getAttribute(this.attributePositions[exprNo][i]);
			}
			this.expressions[exprNo].bindVariables(values);
			double result = (Double)this.expressions[exprNo].getValue();
			
			
		}
		return (Boolean) this.expression.getValue();
	}

	public List<ITimeInterval> evaluate(RelationalTuple<?> left, RelationalTuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			RelationalTuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = r.getAttribute(this.attributePositions[i]);
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public RelationalRangePredicate clone() {
		return new RelationalRangePredicate(this);
	}

	@Override
	public String toString() {
		return this.expression.toString();
	}

	public List<SDFAttribute> getAttributes() {
		return this.expression.getAllAttributes();
	}
	
	public boolean isSetOperator(String symbol) {
		return expression.isSetOperator(symbol);
	}

	
}
