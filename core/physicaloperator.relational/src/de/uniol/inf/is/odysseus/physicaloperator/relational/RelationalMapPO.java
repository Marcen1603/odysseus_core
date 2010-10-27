package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
public class RelationalMapPO<T extends IMetaAttribute> extends
		AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	private int[][] variables;
	private SDFExpression[] expressions;
	private SDFAttributeList schema;

	public RelationalMapPO(SDFAttributeList schema, SDFExpression[] expressions) {
		this.schema = schema;
		init(schema, expressions);
	}

	private void init(SDFAttributeList schema, SDFExpression[] expressions) {
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

	public RelationalMapPO(RelationalMapPO<T> relationalMapPO) {
		init(relationalMapPO.schema ,relationalMapPO.expressions);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@Override
	final protected void process_next(RelationalTuple<T> object, int port) {
		RelationalTuple<T> outputVal = new RelationalTuple<T>(
				this.expressions.length);
		outputVal.setMetadata((T) object.getMetadata().clone());
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

	@Override
	public RelationalMapPO<T> clone() {
		return new RelationalMapPO(this);
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof RelationalMapPO)) {
			return false;
		}
		RelationalMapPO rmpo = (RelationalMapPO) ipo;
		if(this.getSubscribedToSource().equals(rmpo.getSubscribedToSource()) &&
					this.schema.compareTo(rmpo.schema) == 0) {
			if(this.expressions.length == rmpo.expressions.length) {
				for(int i=0; i<this.expressions.length; i++) {
					if(!this.expressions[i].equals(rmpo.expressions[i])) {
						return false;
					}
				}
			} else {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

}
