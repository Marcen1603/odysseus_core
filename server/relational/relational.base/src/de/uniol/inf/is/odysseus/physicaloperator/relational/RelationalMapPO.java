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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalMapPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {

	static private Logger logger = LoggerFactory.getLogger(RelationalMapPO.class);

	private VarHelper[][] variables; // Expression.Index
	private SDFExpression[] expressions;
	private final SDFSchema inputSchema;
	final private Map<Long, LinkedList<Tuple<T>>> groupsLastObjects = new HashMap<>();
	final private IGroupProcessor<Tuple<T>, Tuple<T>> groupProcessor;
	private int maxHistoryElements = 0;
	final private boolean statebased;
	final private boolean allowNull;

	public RelationalMapPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean statebased, boolean allowNullInOutput, IGroupProcessor<Tuple<T>, Tuple<T>> groupProcessor) {
		this.inputSchema = inputSchema;
		this.statebased = statebased;
		this.allowNull = allowNullInOutput;
		this.groupProcessor = groupProcessor;
		init(inputSchema, expressions);
	}

	private void init(SDFSchema schema, SDFExpression[] expressions) {
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
				if (curAttribute.getNumber() > 0) {
					if (!statebased) {
						throw new RuntimeException("Map cannot be used with __last_! Used StateMap instead!");
					}
					int pos = curAttribute.getNumber();
					if (pos > maxHistoryElements) {
						maxHistoryElements = pos + 1;
					}
					int index = schema.indexOf(curAttribute);
					newArray[j++] = new VarHelper(index, pos);
				} else {
					newArray[j++] = new VarHelper(schema.indexOf(curAttribute), 0);
				}
			}
		}
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	final protected void process_next(Tuple<T> object, int port) {
		boolean nullValueOccured = false;
		Long groupId = groupProcessor.getGroupID(object);
		LinkedList<Tuple<T>> lastObjects = groupsLastObjects.get(groupId);
		
		if (lastObjects == null){
			lastObjects = new LinkedList<>();
			groupsLastObjects.put(groupId, lastObjects);
		}
		
		Tuple<T> outputVal = new Tuple<T>(this.expressions.length, false);
		outputVal.setMetadata((T) object.getMetadata().clone());
		int lastObjectSize = lastObjects.size();
		if (lastObjectSize > maxHistoryElements) {
			lastObjects.removeLast();
			lastObjectSize--;
		}
		lastObjects.addFirst(object);
		lastObjectSize++;
		synchronized (this.expressions) {
			for (int i = 0; i < this.expressions.length; ++i) {
				Object[] values = new Object[this.variables[i].length];
				IMetaAttribute[] meta = new IMetaAttribute[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					Tuple<T> obj = null;
					if (lastObjectSize > this.variables[i][j].objectPosToUse) {
						obj = lastObjects.get(this.variables[i][j].objectPosToUse);
					}
					if (obj != null) {
						values[j] = obj.getAttribute(this.variables[i][j].pos);
						meta[j] = obj.getMetadata();
					}
				}

				try {
					this.expressions[i].bindMetaAttribute(object.getMetadata());
					this.expressions[i].bindAdditionalContent(object.getAdditionalContent());
					this.expressions[i].bindVariables(meta, values);
					Object expr = this.expressions[i].getValue();
					outputVal.setAttribute(i, expr);
					if (expr == null) {
						nullValueOccured = true;
					}
				} catch (Exception e) {
					nullValueOccured = true;
					if (!(e instanceof NullPointerException)) {
						logger.error("Cannot calc result for "+object+" with expression "+expressions[i], e);
						// Not needed. Value is null, if not set!
						// outputVal.setAttribute(i, null);
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
		
		if (!this.getOutputSchema().equals(rmpo.getOutputSchema())){
			return false;
		}
		
		if ((this.groupProcessor == null && rmpo.groupProcessor != null) || 
				!this.groupProcessor.equals(rmpo.groupProcessor)){
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

	public boolean isStatebased() {
		return statebased;
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

}