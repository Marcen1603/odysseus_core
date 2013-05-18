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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * Implementation of a probabilistic Map operator.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * @param <T>
 */
public class ProbabilisticMapPO<T extends IMetaAttribute> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	/** Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticMapPO.class);
	/** Attribute positions list required for variable bindings. */
	private int[][] variables;
	/** The expressions. */
	private SDFProbabilisticExpression[] expressions;
	/** The input schema used for semantic equal operations during runtime. */
	private final SDFSchema inputSchema;

	/**
	 * Default constructor used for probabilistic expression.
	 * 
	 * @param inputSchema
	 *            The input schema
	 * @param expressions
	 *            The probabilistic expression.
	 */
	public ProbabilisticMapPO(final SDFSchema inputSchema, final SDFProbabilisticExpression[] expressions) {
		this.inputSchema = inputSchema;
		init(inputSchema, expressions);
	}

	/**
	 * Default constructor used for expression.
	 * 
	 * @param inputSchema
	 *            The input schema
	 * @param expressions
	 *            The expression.
	 */
	public ProbabilisticMapPO(final SDFSchema inputSchema, final SDFExpression[] expressions) {
		this.inputSchema = inputSchema;
		init(inputSchema, expressions);
	}

	/**
	 * Initialize the operator with the given expressions.
	 * 
	 * @param schema
	 *            The schema
	 * @param expressionsList
	 *            The expressions
	 */
	private void init(final SDFSchema schema, final SDFExpression[] expressionsList) {
		SDFProbabilisticExpression[] probabilisticExpressions = new SDFProbabilisticExpression[expressionsList.length];
		for (int i = 0; i < expressionsList.length; ++i) {
			probabilisticExpressions[i] = new SDFProbabilisticExpression(expressionsList[i]);
		}
		init(schema, probabilisticExpressions);
	}

	/**
	 * Initialize the operator with the given probabilistic expressions.
	 * 
	 * @param schema
	 *            The schema
	 * @param expressionsList
	 *            The expressions
	 */
	private void init(final SDFSchema schema, final SDFProbabilisticExpression[] expressionsList) {
		this.expressions = expressionsList;
		this.variables = new int[expressionsList.length][];
		int i = 0;
		for (SDFExpression expression : expressionsList) {
			List<SDFAttribute> neededAttributes = expression.getAllAttributes();
			int[] newArray = new int[neededAttributes.size()];
			this.variables[i++] = newArray;
			int j = 0;
			for (SDFAttribute curAttribute : neededAttributes) {
				newArray[j++] = schema.indexOf(curAttribute);
			}
		}
	}

	/**
	 * Clone constructor.
	 * 
	 * @param probabilisticMapPO
	 *            The copy
	 */
	public ProbabilisticMapPO(final ProbabilisticMapPO<T> probabilisticMapPO) {
		this.inputSchema = probabilisticMapPO.inputSchema.clone();
		init(probabilisticMapPO.inputSchema, probabilisticMapPO.expressions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# getOutputMode()
	 */
	@Override
	public final OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe# process_next(de.uniol.inf.is.odysseus.core.metadata.IStreamObject, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected final void process_next(final ProbabilisticTuple<T> object, final int port) {
		ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(this.expressions.length, false);
		outputVal.setMetadata((T) object.getMetadata().clone());
		synchronized (this.expressions) {
			for (int i = 0; i < this.expressions.length; ++i) {
				Object[] values = new Object[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					values[j] = object.getAttribute(this.variables[i][j]);
				}
				this.expressions[i].bindMetaAttribute(object.getMetadata());
				this.expressions[i].bindDistributions(object.getDistributions());
				this.expressions[i].bindAdditionalContent(object.getAdditionalContent());
				this.expressions[i].bindVariables(values);
				outputVal.setAttribute(i, this.expressions[i].getValue());
				if (this.expressions[i].getType().requiresDeepClone()) {
					outputVal.setRequiresDeepClone(true);
				}
			}
		}
		// FIXME !!! Handle pointer to distributions !!! i.e. changing index,
		// missing pointer, and changing order
		outputVal.setDistributions(object.getDistributions().clone());
		transfer(outputVal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone ()
	 */
	@Override
	public final ProbabilisticMapPO<T> clone() {
		return new ProbabilisticMapPO<T>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource# process_isSemanticallyEqual (de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	public final boolean process_isSemanticallyEqual(final IPhysicalOperator ipo) {
		if (!(ipo instanceof ProbabilisticMapPO)) {
			return false;
		}
		ProbabilisticMapPO mapPo = (ProbabilisticMapPO) ipo;
		if (this.hasSameSources(mapPo) && this.inputSchema.compareTo(mapPo.inputSchema) == 0) {
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
