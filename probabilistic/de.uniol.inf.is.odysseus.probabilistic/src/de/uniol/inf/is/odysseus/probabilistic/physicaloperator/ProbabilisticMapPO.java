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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticMapPO<T extends IMetaAttribute> extends
		AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {

	private int[][] variables;
	private SDFProbabilisticExpression[] expressions;
	private final SDFSchema inputSchema;

	public ProbabilisticMapPO(SDFSchema inputSchema,
			SDFProbabilisticExpression[] expressions) {
		this.inputSchema = inputSchema;
		init(inputSchema, expressions);
	}

	public ProbabilisticMapPO(SDFSchema inputSchema, SDFExpression[] expressions) {
		this.inputSchema = inputSchema;
		init(inputSchema, expressions);
	}

	private void init(SDFSchema schema, SDFExpression[] expressions) {
		SDFProbabilisticExpression[] probabilisticExpressions = new SDFProbabilisticExpression[expressions.length];
		for (int i = 0; i < expressions.length; ++i) {
			probabilisticExpressions[i] = new SDFProbabilisticExpression(
					expressions[i]);
		}
		init(schema, probabilisticExpressions);
	}

	private void init(SDFSchema schema, SDFProbabilisticExpression[] expressions) {
		this.expressions = expressions;
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

	public ProbabilisticMapPO(ProbabilisticMapPO<T> probabilisticMapPO) {
		this.inputSchema = probabilisticMapPO.inputSchema.clone();
		init(probabilisticMapPO.inputSchema, probabilisticMapPO.expressions);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	final protected void process_next(ProbabilisticTuple<T> object, int port) {
		ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(
				this.expressions.length, false);
		outputVal.setMetadata((T) object.getMetadata().clone());
		synchronized (this.expressions) {
			for (int i = 0; i < this.expressions.length; ++i) {
				Object[] values = new Object[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					values[j] = object.getAttribute(this.variables[i][j]);
				}
				this.expressions[i]
						.bindDistributions(object.getDistributions());
				this.expressions[i].bindAdditionalContent(object
						.getAdditionalContent());
				this.expressions[i].bindVariables(values);
				outputVal.setAttribute(i, this.expressions[i].getValue());
				if (this.expressions[i].getType().requiresDeepClone()) {
					outputVal.setRequiresDeepClone(true);
				}
			}
		}
		outputVal.setDistributions(object.getDistributions().clone());
		transfer(outputVal);
	}

	@Override
	public ProbabilisticMapPO<T> clone() {
		return new ProbabilisticMapPO<T>(this);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ProbabilisticMapPO)) {
			return false;
		}
		ProbabilisticMapPO mapPo = (ProbabilisticMapPO) ipo;
		if (this.hasSameSources(mapPo)
				&& this.inputSchema.compareTo(mapPo.inputSchema) == 0) {
			if (this.expressions.length == mapPo.expressions.length) {
				for (int i = 0; i < this.expressions.length; i++) {
					if (!this.expressions[i].equals(mapPo.expressions[i])) {
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
