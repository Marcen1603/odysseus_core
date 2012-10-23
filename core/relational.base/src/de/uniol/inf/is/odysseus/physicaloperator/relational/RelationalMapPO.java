/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
public class RelationalMapPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	private int[][] variables;
	private SDFExpression[] expressions;
	private SDFSchema inputSchema;

	public RelationalMapPO(SDFSchema inputSchema, SDFExpression[] expressions) {
		this.inputSchema = inputSchema;
		init(inputSchema, expressions);
	}

	private void init(SDFSchema schema, SDFExpression[] expressions) {
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
		init(relationalMapPO.inputSchema ,relationalMapPO.expressions);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	final protected void process_next(Tuple<T> object, int port) {
		Tuple<T> outputVal = new Tuple<T>(
				this.expressions.length, false);
		outputVal.setMetadata((T) object.getMetadata().clone());
		synchronized (this.expressions) {
			for (int i = 0; i < this.expressions.length; ++i) {
				Object[] values = new Object[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					values[j] = object.getAttribute(this.variables[i][j]);
				}
				this.expressions[i].bindVariables(values);
				outputVal.setAttribute(i, this.expressions[i].getValue());
				if (this.expressions[i].getType().requiresDeepClone()) {
					outputVal.setRequiresDeepClone(true);
				}
			}
		}
		transfer(outputVal);
	}

	@Override
	public RelationalMapPO<T> clone() {
		return new RelationalMapPO<T>(this);
	}
	
	@Override
	@SuppressWarnings({"rawtypes"})
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof RelationalMapPO)) {
			return false;
		}
		RelationalMapPO rmpo = (RelationalMapPO) ipo;
		if(this.hasSameSources(rmpo) &&
					this.inputSchema.compareTo(rmpo.inputSchema) == 0) {
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
		}
        return false;
	}

}
