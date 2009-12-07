package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
public class RelationalPredicate extends AbstractPredicate<RelationalTuple<?>> implements IRelationalPredicate {
	private static final long serialVersionUID = 1222104352250883947L;

	private SDFExpression expression;

	// stores which attributes are needed at which position for
	// variable bindings
	private int[] attributePositions;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	private boolean[] fromRightChannel;

	public RelationalPredicate(SDFExpression expression) {
		this.expression = expression;
	}

	@Override
	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		List<SDFAttribute> neededAttributes = expression.getAllAttributes();
		this.attributePositions = new int[neededAttributes.size()];
		this.fromRightChannel = new boolean[neededAttributes.size()];

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
				this.fromRightChannel[i] = true;
			}
			this.attributePositions[i++] = pos;
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

	public RelationalPredicate(RelationalPredicate predicate) {
		this.attributePositions = predicate.attributePositions.clone();
		this.fromRightChannel = predicate.fromRightChannel.clone();
		this.expression = predicate.expression.clone();
	}

	public boolean evaluate(RelationalTuple<?> input) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(this.attributePositions[i]);
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	public boolean evaluate(RelationalTuple<?> left, RelationalTuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			RelationalTuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = r.getAttribute(this.attributePositions[i]);
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public RelationalPredicate clone() {
		return new RelationalPredicate(this);
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
