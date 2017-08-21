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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.UpdateExpressionsPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalMapPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	static private Logger logger = LoggerFactory.getLogger(RelationalMapPO.class);

	protected RelationalExpression<T>[] expressions;
	final private SDFSchema inputSchema;
	final private boolean keepInput;
	// The attributes that are kept in the output
	final private int[] keepList;
	// The number of attributes that are kept - used as the offset for the
	// expressions
	final private int keepInputLength;
	final private boolean allowNull;
	final private boolean suppressErrors;

	final private boolean evaluateOnPunctuation;
	final private boolean expressionsUpdateable;

	private Tuple<T> lastTuple;

	private boolean requiresDeepClone;

	public RelationalMapPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean allowNullInOutput,
			boolean evaluateOnPunctuation, boolean expressionsUpdateable, boolean suppressErrors, boolean keepInput,
			int[] keepList) {
		this.inputSchema = inputSchema;
		this.allowNull = allowNullInOutput;

		// Punctuation handling
		this.evaluateOnPunctuation = evaluateOnPunctuation;
		this.expressionsUpdateable = expressionsUpdateable;

		this.suppressErrors = suppressErrors;
		init(inputSchema, expressions);
		requiresDeepClone = false;
		for (int i = 0; i < expressions.length; i++) {
			if (this.expressions[i].getType().requiresDeepClone()) {
				requiresDeepClone = true;
			}
		}
		this.keepInput = keepInput;
		this.keepList = keepList;

		/*
		 * If we want to keep the input, the length is the length of the
		 * keepList. But in case the keepList is null, we want to keep
		 * everything -> then the length is defined by the length of the input
		 * schema
		 */
		this.keepInputLength = this.keepInput ? this.keepList != null ? this.keepList.length : this.inputSchema.size()
				: 0;
	}

	protected RelationalMapPO(SDFSchema inputSchema, boolean allowNullInOutput, boolean evaluateOnPunctuation,
			boolean expressionsUpdateable, boolean suppressErrors, boolean keepInput, int[] restrictList) {
		this.inputSchema = inputSchema;
		this.allowNull = allowNullInOutput;

		// Punctuation handling
		this.evaluateOnPunctuation = evaluateOnPunctuation;
		this.expressionsUpdateable = expressionsUpdateable;

		this.suppressErrors = suppressErrors;
		this.keepInput = keepInput;
		this.keepList = restrictList;

		/*
		 * If we want to keep the input, the length is the length of the
		 * keepList. But in case the keepList is null, we want to keep
		 * everything -> then the length is defined by the length of the input
		 * schema
		 */
		this.keepInputLength = this.keepInput ? this.keepList != null ? this.keepList.length : this.inputSchema.size()
				: 0;
	}

	@SuppressWarnings("unchecked")
	protected void init(SDFSchema schema, SDFExpression[] expr) {
		this.expressions = new RelationalExpression[expr.length];
		for (int i = 0; i < expr.length; ++i) {
			this.expressions[i] = new RelationalExpression<T>(expr[i]);
			this.expressions[i].initVars(schema);
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public boolean deliversStoredElement(int outputPort) {
		return false;
	}

	@Override
	final protected void process_next(Tuple<T> object, int port) {
		if (evaluateOnPunctuation) {
			lastTuple = object;
		}
		boolean nullValueOccured = false;
		List<Tuple<T>> preProcessResult = preProcess(object);

		Tuple<T> outputVal = Tuple.createEmptyTupleWithMeta(this.getOutputSchema().size(), object, requiresDeepClone);

		synchronized (this.expressions) {

			if (this.keepInput) {
				// If we want to keep all input values, the keepList is null
				if (this.keepList == null) {
					outputVal.setAttributes(object);
				} else {
					outputVal.setAttributes(object.restrict(this.keepList, true));
				}
			}

			// Evaluate all expressions and add the results to the output tuple
			for (int i = 0; i < this.expressions.length; ++i) {

				try {
					Object expr = this.expressions[i].evaluate(object, getSessions(), preProcessResult);

					outputVal.setAttribute(this.keepInputLength + i, expr);
					if (expr == null) {
						nullValueOccured = true;
					}
				} catch (Exception e) {
					nullValueOccured = true;
					if (!suppressErrors) {
						if (!(e instanceof NullPointerException)) {
							logger.warn("Cannot calc result for " + object + " with expression " + expressions[i], e);
							// Not needed. Value is null, if not set!
							// outputVal.setAttribute(i, null);
							sendWarning("Cannot calc result for " + object + " with expression " + expressions[i], e);
						}
					}
				}
			}
		}
		if (!nullValueOccured || (nullValueOccured && allowNull)) {
			transfer(outputVal);
		}
	}

	public List<Tuple<T>> preProcess(Tuple<T> object) {
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

		/*
		 * (1) Expressions can be updated with punctuations, (2) this is a
		 * punctuation to update the expressions and (3) this operator is in the
		 * list with the targets for this punctuation
		 */
		if (this.expressionsUpdateable && punctuation instanceof UpdateExpressionsPunctuation
				&& ((UpdateExpressionsPunctuation) punctuation).getTargetOperatorNames().contains(this.getName())) {
			// Set new expressions
			UpdateExpressionsPunctuation updateExpressionsPuctuation = (UpdateExpressionsPunctuation) punctuation;
			setNewExpressions(updateExpressionsPuctuation.getExpressions());

			// Initialize all expressions
			for (int i = 0; i < this.expressions.length; i++) {
				this.expressions[i].initVars(this.getInputSchema());
			}
		}

		// TODO: By this, we make it implicit interval approach...
		// Maybe we should move this to another bundle?
		if (evaluateOnPunctuation) {
			if (lastTuple == null) {
				lastTuple = new Tuple(expressions.length, false);
				lastTuple.setMetadata((T) new TimeInterval(punctuation.getTime()));
			} else {
				((ITimeInterval) lastTuple.getMetadata()).setStartAndEnd(punctuation.getTime(),
						PointInTime.getInfinityTime());
			}
			process_next(lastTuple, port);
		}

		// Always send the punctuation to the next operator(s)
		sendPunctuation(punctuation, port);
	}

	/**
	 * Sets all expressions which are given in the punctuation and have a name
	 * that also existed in the previous output-schema.
	 * 
	 * @param newExpressions
	 *            The map with the new expressions that are given by the
	 *            punctuation
	 */
	private void setNewExpressions(Map<String, RelationalExpression<T>> newExpressions) {
		for (String key : newExpressions.keySet()) {
			// Check, if this attribute already exists
			int index = this.getOutputSchema().findAttributeIndex(key);
			if (index >= 0) {
				// The attribute exists
				this.expressions[index - this.keepInputLength] = newExpressions.get(key);
				// The new expression needs to be initialized
				this.expressions[index - this.keepInputLength].initVars(this.inputSchema);
			}
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RelationalMapPO)) {
			return false;
		}
		RelationalMapPO rmpo = (RelationalMapPO) ipo;

		if (!this.getOutputSchema().equals(rmpo.getOutputSchema())) {
			return false;
		}

		if (this.inputSchema.compareTo(rmpo.inputSchema) == 0) {
			if (this.expressions.length == rmpo.expressions.length) {
				for (int i = 0; i < this.expressions.length; i++) {
					if (!this.expressions[i].equals(rmpo.expressions[i])) {
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

	public RelationalExpression<T>[] getExpressions() {
		return expressions;
	}

	public SDFSchema getInputSchema() {
		return inputSchema;
	}

}
