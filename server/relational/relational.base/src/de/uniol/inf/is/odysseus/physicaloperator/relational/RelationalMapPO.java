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

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalMapPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {

	static private Logger logger = LoggerFactory
			.getLogger(RelationalMapPO.class);

	protected VarHelper[][] variables; // Expression.Index
	private SDFExpression[] expressions;
	private final SDFSchema inputSchema;
	final private boolean allowNull;

	public RelationalMapPO(SDFSchema inputSchema, SDFExpression[] expressions,
			boolean allowNullInOutput) {
		this.inputSchema = inputSchema;
		this.allowNull = allowNullInOutput;
		init(inputSchema, expressions);
	}

	protected RelationalMapPO(SDFSchema inputSchema, boolean allowNullInOutput) {
		this.inputSchema = inputSchema;
		this.allowNull = allowNullInOutput;
	}

	protected void init(SDFSchema schema, SDFExpression[] expressions) {
		this.expressions = new SDFExpression[expressions.length];
		for (int i = 0; i < expressions.length; ++i) {
			this.expressions[i] = expressions[i].clone();
		}
		this.variables = new VarHelper[expressions.length][];
		int i = 0;
		for (SDFExpression expression : expressions) {
			List<SDFAttribute> neededAttributes = expression.getAllAttributes();
			VarHelper[] newArray = new VarHelper[neededAttributes.size()];
			this.variables[i++] = newArray;
			int j = 0;
			for (SDFAttribute curAttribute : neededAttributes) {
				newArray[j++] = initAttribute(schema, curAttribute);
			}
		}
	}

	public VarHelper initAttribute(SDFSchema schema, SDFAttribute curAttribute) {
		return new VarHelper(schema.indexOf(curAttribute), 0);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	final protected void process_next(Tuple<T> object, int port) {
		boolean nullValueOccured = false;

		LinkedList<Tuple<T>> preProcessResult = preProcess(object);

		Tuple<T> outputVal = new Tuple<T>(this.getOutputSchema().size(), false);
		outputVal.setMetadata((T) object.getMetadata().clone());
		if (object.getMetadataMap() != null) {
			for (Entry<String, Object> entry : object.getMetadataMap()
					.entrySet()) {
				outputVal.setMetadata(entry.getKey(), entry.getValue());
			}
		}

		synchronized (this.expressions) {
			int outAttrPos = 0;
			for (int i = 0; i < this.expressions.length; ++i) {
				Object[] values = new Object[this.variables[i].length];
				IMetaAttribute[] meta = new IMetaAttribute[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					Tuple<T> obj = determineObjectForExpression(object,
							preProcessResult, i, j);
					if (obj != null) {
						values[j] = obj.getAttribute(this.variables[i][j].pos);
						meta[j] = obj.getMetadata();
					}
				}

				try {
					this.expressions[i].bindMetaAttribute(object.getMetadata());
					this.expressions[i].bindAdditionalContent(object
							.getAdditionalContent());
					this.expressions[i].bindVariables(meta, values);
					Object expr = this.expressions[i].getValue();
					SDFDatatype retType = expressions[i].getMEPExpression()
							.getReturnType();

					if (retType == SDFDatatype.TUPLE) {
						Tuple tuple = ((Tuple) expr);
						for (Object o : tuple.getAttributes()) {
							outputVal.setAttribute(outAttrPos++, o);
						}
					}else if (retType == SDFDatatype.LIST) {
						for (Object o : (List) expr) {
							outputVal.setAttribute(outAttrPos++, o);
						}
					} else {
						outputVal.setAttribute(outAttrPos++, expr);
						if (expr == null) {
							nullValueOccured = true;
						}
					}
					// MG: 23.09.14: Added test for Tuple as return type, the
					// former implementation
					// did not make any sense ...
					// else{
					//
					//
					// for(Object o : (List)expr){
					// outputVal.setAttribute(outAttrPos++, o);
					// }
					// }
				} catch (Exception e) {
					nullValueOccured = true;
					if (!(e instanceof NullPointerException)) {
						logger.error("Cannot calc result for " + object
								+ " with expression " + expressions[i], e);
						// Not needed. Value is null, if not set!
						// outputVal.setAttribute(i, null);
						InfoService.error("Cannot calc result for " + object
								+ " with expression " + expressions[i], e);
					}
				}
				if (this.expressions[i].getType().requiresDeepClone()) {
					outputVal.setRequiresDeepClone(true);
				}
			}
		}
		if (!nullValueOccured || (nullValueOccured && allowNull)) {
			transfer(outputVal);
		}

	}

	public Tuple<T> determineObjectForExpression(Tuple<T> object,
			LinkedList<Tuple<T>> preProcessResult, int i, int j) {
		return object;
	}

	public LinkedList<Tuple<T>> preProcess(Tuple<T> object) {
		return null;
	}

	@Override
	public RelationalMapPO<T> clone() {
		throw new IllegalArgumentException("Not implemented!");
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
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

	public SDFExpression[] getExpressions() {
		return expressions;
	}

	public SDFSchema getInputSchema() {
		return inputSchema;
	}

}

class VarHelper {
	int pos;
	int objectPosToUse;

	public VarHelper(int pos, int objectPosToUse) {
		super();
		this.pos = pos;
		this.objectPosToUse = objectPosToUse;
	}

	@Override
	public String toString() {
		return pos + " " + objectPosToUse;
	}
}