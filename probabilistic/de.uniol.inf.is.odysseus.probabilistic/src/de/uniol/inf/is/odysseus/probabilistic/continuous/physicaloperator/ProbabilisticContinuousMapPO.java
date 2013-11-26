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
package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.VarHelper;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * Implementation of a probabilistic Map operator.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 */
public class ProbabilisticContinuousMapPO<T extends IMetaAttribute> extends AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticContinuousMapPO.class);
	/** Attribute positions list required for variable bindings. */
	private VarHelper[][] variables; // Expression.Index
	/** The expressions. */
	private SDFProbabilisticExpression[] expressions;
	/** The input schema used for semantic equal operations during runtime. */
	private final SDFSchema inputSchema;
	/** Internal cache for the last objects. */
	private final LinkedList<ProbabilisticTuple<T>> lastObjects = new LinkedList<>();
	/** Size of the history. */
	private int maxHistoryElements = 0;
	/** Flag indicating whether this Map is stateful. */
	private final boolean statebased;
	/** Flag indicating if this Map allows Null values. */
	private final boolean allowNull;
	/** The number of output distributions. */
	private int distributions;
	/** Positions of needed attributes. */
	private int[] neededAttributePos;

	/**
	 * Default constructor used for probabilistic expression.
	 * 
	 * @param inputSchema
	 *            The input schema
	 * @param expressions
	 *            The probabilistic expression.
	 * @param statebased
	 *            Flag indicating whether this Map is stateful
	 * @param allowNullInOutput
	 *            Flag indicating if this Map allows Null values
	 */
	public ProbabilisticContinuousMapPO(final SDFSchema inputSchema, final SDFProbabilisticExpression[] expressions, final boolean statebased, final boolean allowNullInOutput) {
		this.inputSchema = inputSchema;
		this.statebased = statebased;
		this.allowNull = allowNullInOutput;
		this.init(inputSchema, expressions);
	}

	/**
	 * Default constructor used for expression.
	 * 
	 * @param inputSchema
	 *            The input schema
	 * @param expressions
	 *            The expression.
	 * @param statebased
	 *            Flag indicating whether this Map is stateful
	 * @param allowNullInOutput
	 *            Flag indicating if this Map allows Null values
	 */
	public ProbabilisticContinuousMapPO(final SDFSchema inputSchema, final SDFExpression[] expressions, final boolean statebased, final boolean allowNullInOutput) {
		this.inputSchema = inputSchema;
		this.statebased = statebased;
		this.allowNull = allowNullInOutput;
		this.init(inputSchema, expressions);
	}

	/**
	 * Clone constructor.
	 * 
	 * @param probabilisticMapPO
	 *            The copy
	 */
	public ProbabilisticContinuousMapPO(final ProbabilisticContinuousMapPO<T> probabilisticMapPO) {
		this.inputSchema = probabilisticMapPO.inputSchema.clone();
		this.statebased = probabilisticMapPO.statebased;
		this.allowNull = probabilisticMapPO.allowNull;
		this.init(probabilisticMapPO.inputSchema, probabilisticMapPO.expressions);
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
		final SDFProbabilisticExpression[] probabilisticExpressions = new SDFProbabilisticExpression[expressionsList.length];
		for (int i = 0; i < expressionsList.length; ++i) {
			probabilisticExpressions[i] = new SDFProbabilisticExpression(expressionsList[i].clone());
		}
		this.init(schema, probabilisticExpressions);
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
		this.distributions = 0;

		this.expressions = new SDFProbabilisticExpression[expressionsList.length];
		for (int i = 0; i < expressionsList.length; ++i) {
			this.expressions[i] = expressionsList[i].clone();
		}
		this.variables = new VarHelper[expressionsList.length][];
		final Set<SDFAttribute> neededAttributesSet = new HashSet<>();
		int i = 0;
		for (final SDFExpression expression : expressionsList) {
			final List<SDFAttribute> neededAttributes = expression.getAllAttributes();
			neededAttributesSet.addAll(neededAttributes);
		}
		this.neededAttributePos = SchemaUtils.getAttributePos(this.inputSchema, neededAttributesSet);
		final SDFSchema restrictedSchema = new SDFSchema(schema.getURI(), schema.getType(), neededAttributesSet);
		for (final SDFExpression expression : expressionsList) {
			final List<SDFAttribute> neededAttributes = expression.getAllAttributes();

			final VarHelper[] newArray = new VarHelper[neededAttributes.size()];

			this.variables[i++] = newArray;
			int j = 0;
			for (final SDFAttribute curAttribute : neededAttributes) {
				if ((curAttribute.getSourceName() != null) && curAttribute.getSourceName().startsWith("__last_")) {
					if (!this.statebased) {
						throw new RuntimeException("Map cannot be used with __last_! Used StateMap instead!");
					}
					final int pos = Integer.parseInt(curAttribute.getSourceName().substring("__last_".length(), curAttribute.getSourceName().indexOf('.')));
					if (pos > this.maxHistoryElements) {
						this.maxHistoryElements = pos + 1;
					}
					final String realAttrStr = curAttribute.getURI().substring(curAttribute.getURI().indexOf('.') + 1);
					String newSource = realAttrStr.substring(0, realAttrStr.indexOf('.'));
					final String newName = realAttrStr.substring(realAttrStr.indexOf('.') + 1);
					if ("null".equals(newSource)) {
						newSource = null;
					}
					final SDFAttribute newAttribute = new SDFAttribute(newSource, newName, curAttribute);
					final int index = schema.indexOf(newAttribute);
					newArray[j++] = new VarHelper(index, pos, newAttribute.getDatatype() instanceof SDFProbabilisticDatatype);
				} else {
					newArray[j++] = new VarHelper(restrictedSchema.indexOf(curAttribute), 0, curAttribute.getDatatype() instanceof SDFProbabilisticDatatype);
				}
			}
			if (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE)) {
				this.distributions++;
			}
		}
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
		boolean nullValueOccured = false;
		final ProbabilisticTuple<T> outputVal = new ProbabilisticTuple<T>(this.expressions.length, this.distributions, false);
		// FIXME restrict object for each expression or we will get an error if multiple expressions assume a univariate distribution
		final ProbabilisticTuple<T> restrictedObject = object.restrict(this.neededAttributePos, false);
		outputVal.setMetadata((T) restrictedObject.getMetadata().clone());
		int lastObjectSize = this.lastObjects.size();
		if (lastObjectSize > this.maxHistoryElements) {
			this.lastObjects.removeLast();
			lastObjectSize--;
		}
		this.lastObjects.addFirst(restrictedObject);
		lastObjectSize++;
		synchronized (this.expressions) {
			for (int i = 0, d = 0; i < this.expressions.length; ++i) {
				final Object[] values = new Object[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					ProbabilisticTuple<T> obj = null;
					if (lastObjectSize > this.variables[i][j].getObjectPosToUse()) {
						obj = this.lastObjects.get(this.variables[i][j].getObjectPosToUse());
					}
					if (obj != null) {
						Object attribute = obj.getAttribute(this.variables[i][j].getPos());
						if (attribute.getClass() == ProbabilisticContinuousDouble.class) {
							// FIXME: if more than one dimension of a distribution is used in a MAP expression, the dimensions are also propagated to MEP functions that only use less dimensions
							attribute = restrictedObject.getDistribution(((ProbabilisticContinuousDouble) attribute).getDistribution());
						}
						values[j] = attribute;
					}
				}

				try {
					this.expressions[i].bindMetaAttribute(restrictedObject.getMetadata());
					this.expressions[i].bindDistributions(restrictedObject.getDistributions());
					this.expressions[i].bindAdditionalContent(restrictedObject.getAdditionalContent());
					this.expressions[i].bindVariables(values);
					final Object expr = this.expressions[i].getValue();
					if (this.expressions[i].getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE)) {
						final NormalDistributionMixture distribution = (NormalDistributionMixture) expr;
						distribution.getAttributes()[0] = i;
						outputVal.setDistribution(d, distribution);
						outputVal.setAttribute(i, new ProbabilisticContinuousDouble(d));
						d++;
					} else {
						outputVal.setAttribute(i, expr);
					}
					if (expr == null) {
						nullValueOccured = true;
					}
				} catch (final Exception e) {
					nullValueOccured = true;
					if (!(e instanceof NullPointerException)) {
						ProbabilisticContinuousMapPO.LOG.error("Cannot calc result ", e);
						// Not needed. Value is null, if not set!
						// outputVal.setAttribute(i, null);
					}
				}
				if (this.expressions[i].getType().requiresDeepClone()) {
					outputVal.setRequiresDeepClone(true);
				}
			}
		}
		if (!nullValueOccured || (nullValueOccured && this.allowNull)) {
			// KTHXBYE
			this.transfer(outputVal);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone ()
	 */
	@Override
	public final ProbabilisticContinuousMapPO<T> clone() {
		return new ProbabilisticContinuousMapPO<T>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource# process_isSemanticallyEqual (de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	public final boolean process_isSemanticallyEqual(final IPhysicalOperator ipo) {
		if (!(ipo instanceof ProbabilisticContinuousMapPO)) {
			return false;
		}
		final ProbabilisticContinuousMapPO pmpo = (ProbabilisticContinuousMapPO) ipo;

		if (!this.getOutputSchema().equals(pmpo.getOutputSchema())) {
			return false;
		}

		if (this.hasSameSources(pmpo) && (this.inputSchema.compareTo(pmpo.inputSchema) == 0)) {
			if (this.expressions.length == pmpo.expressions.length) {
				for (int i = 0; i < this.expressions.length; i++) {
					if (!this.expressions[i].equals(pmpo.expressions[i])) {
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

	/**
	 * Gets the value of the statebased property.
	 * 
	 * @return <code>true</code> if this Map operator is a stateful Map operator
	 */
	public final boolean isStatebased() {
		return this.statebased;
	}

}