package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.List;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
public class RelationalMapPO<T extends IClone> extends
		AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	final private int[][] variables;
	final private SDFExpression[] expressions;

	public RelationalMapPO(SDFAttributeList schema, SDFExpression[] expressions) {
		this.expressions = new SDFExpression[expressions.length];
		for (int i = 0; i < expressions.length; ++i) {
			this.expressions[i] = expressions[i].clone();
		}
		this.variables = new int[expressions.length][];
		int i = 0;
		for (SDFExpression expression : expressions) {
			List<SDFAttribute> neededAttributes = expression.getAllAttributes();
			int[] newArray = new int[neededAttributes.size()];
			this.variables[i++] = newArray;
			int j = 0;
			for (SDFAttribute curAttribute : neededAttributes) {
				newArray[j++] = schema.indexOf(curAttribute);
			}
		}
	}

	@Override
	public boolean modifiesInput() {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	final protected void process_next(RelationalTuple<T> object, int port) {
		RelationalTuple<T> outputVal = new RelationalTuple<T>(
				this.expressions.length);
		outputVal.setMetadata((T) object.getMetadata());
		synchronized (this.expressions) {
			for (int i = 0; i < this.expressions.length; ++i) {
				Object[] values = new Object[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					values[j] = object.getAttribute(this.variables[i][j]);
				}
				this.expressions[i].bindVariables(values);
				outputVal.setAttribute(i, this.expressions[i].getValue());
			}
		}
		transfer(outputVal);
	}

}
