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
	final private int[] restrictList;
	final private boolean allowNull;
	final private boolean suppressErrors;

	final private boolean evaluateOnPunctuation;
	final private boolean expressionsUpdateable;
	final private boolean catchUpdateExpressionsPunctuation;

	private Tuple<T> lastTuple;

	private boolean requiresDeepClone;

	public RelationalMapPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean allowNullInOutput,
			boolean evaluateOnPunctuation, boolean expressionsUpdateable, boolean catchUpdateExpressionsPunctuation,
			boolean suppressErrors, boolean keepInput, int[] restrictList) {
		this.inputSchema = inputSchema;
		this.allowNull = allowNullInOutput;

		// Punctuation handling
		this.evaluateOnPunctuation = evaluateOnPunctuation;
		this.expressionsUpdateable = expressionsUpdateable;
		this.catchUpdateExpressionsPunctuation = catchUpdateExpressionsPunctuation;

		this.suppressErrors = suppressErrors;
		init(inputSchema, expressions);
		requiresDeepClone = false;
		for (int i = 0; i < expressions.length; i++) {
			if (this.expressions[i].getType().requiresDeepClone()) {
				requiresDeepClone = true;
			}
		}
		this.keepInput = keepInput;
		this.restrictList = restrictList;
	}

	protected RelationalMapPO(SDFSchema inputSchema, boolean allowNullInOutput, boolean evaluateOnPunctuation,
			boolean expressionsUpdateable, boolean catchUpdateExpressionsPunctuation, boolean suppressErrors,
			boolean keepInput, int[] restrictList) {
		this.inputSchema = inputSchema;
		this.allowNull = allowNullInOutput;

		// Punctuation handling
		this.evaluateOnPunctuation = evaluateOnPunctuation;
		this.expressionsUpdateable = expressionsUpdateable;
		this.catchUpdateExpressionsPunctuation = catchUpdateExpressionsPunctuation;

		this.suppressErrors = suppressErrors;
		this.keepInput = keepInput;
		this.restrictList = restrictList;
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

			final int offset;
			if (keepInput) {
				if (restrictList == null) {
					offset = getInputSchema().size();
					outputVal.setAttributes(object);
				} else {
					offset = restrictList.length;
					outputVal.setAttributes(object.restrict(restrictList, true));
				}
			} else {
				offset = 0;
			}

			for (int i = 0; i < this.expressions.length; ++i) {

				try {
					Object expr = this.expressions[i].evaluate(object, getSessions(), preProcessResult);

					outputVal.setAttribute(offset + i, expr);
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

		// Expressions can be updated with punctuations
		if (this.expressionsUpdateable && punctuation instanceof UpdateExpressionsPunctuation) {
			// Set new expressions
			UpdateExpressionsPunctuation updateExpressionsPuctuation = (UpdateExpressionsPunctuation) punctuation;
			this.expressions = updateExpressionsPuctuation.getExpressions();

			// Initialize all expressions
			for (int i = 0; i < this.expressions.length; i++) {
				this.expressions[i].initVars(this.getOutputSchema());
			}
			
			/*
			 * These punctuations can be catched to not influence other
			 * MAP-operators later in the graph
			 */
			if (!this.catchUpdateExpressionsPunctuation) {
				sendPunctuation(punctuation, port);
			}
		} else {
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
			sendPunctuation(punctuation, port);
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
