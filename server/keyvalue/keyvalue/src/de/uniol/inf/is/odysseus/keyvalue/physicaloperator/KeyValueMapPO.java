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
package de.uniol.inf.is.odysseus.keyvalue.physicaloperator;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;

/**
 * Implementation of map operator for key value objects.
 * Adapted from relational map.
 * 
 * @author Jan Sören Schwarz
 *
 * @param <T>
 */
public class KeyValueMapPO<K extends IMetaAttribute, T extends KeyValueObject<K>> extends AbstractPipe<T, T> {

	static private Logger logger = LoggerFactory.getLogger(KeyValueMapPO.class);

	protected String[][] variables; // Expression.Index
	private List<String[]> expressionStrings;
	private SDFExpression[] expressions;
	private final SDFSchema inputSchema;
	final private boolean allowNull;
	final private boolean keepAllAttributes;
	final private List<String> removeAttributes;
	
	public KeyValueMapPO(MapAO mapAO) {
		this.inputSchema = mapAO.getInputSchema();
		this.expressionStrings = mapAO.getKVExpressions();
		this.allowNull = false;
		this.keepAllAttributes = mapAO.isKeepAllAttributes();
		this.removeAttributes = mapAO.getRemoveAttributes();
	}
	
	public KeyValueMapPO(KeyValueMapPO<?,?> mapPO) {
		this.inputSchema = mapPO.getInputSchema();
		this.expressionStrings = mapPO.getExpressionStrings();
		this.expressions = mapPO.getExpressions();
		this.allowNull = mapPO.allowNull;
		this.keepAllAttributes = mapPO.keepAllAttributes;
		this.removeAttributes = mapPO.removeAttributes;
	}

	@SuppressWarnings("unchecked")
	@Override
	final protected void process_next(T object, int port) {
		generateExpressions(expressionStrings);
		
		T outputVal;
		if(this.keepAllAttributes) {
			if(this.getOutputSchema().getType() == NestedKeyValueObject.class) {
				outputVal = (T) new NestedKeyValueObject<>((NestedKeyValueObject<?>) object);
			} else {
				outputVal = (T) new KeyValueObject<>(object);
			}
		} else {
			if(this.getOutputSchema().getType() == NestedKeyValueObject.class) {
				outputVal = (T) new NestedKeyValueObject<>();
			} else {
				outputVal = (T) new KeyValueObject<>();
			}
			outputVal.setMetadata((K) object.getMetadata().clone());
			if (object.getMetadataMap() != null) {
				for (Entry<String, Object> entry : object.getMetadataMap().entrySet()) {
					outputVal.setMetadata(entry.getKey(), entry.getValue());
				}
			}
		}

		boolean nullValueOccured = false;
		synchronized (this.expressions) {
			for (int i = 0; i < this.expressions.length; ++i) {
				Object[] values = new Object[this.variables[i].length];
//				IMetaAttribute[] meta = new IMetaAttribute[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					if (object != null) {
						values[j] = object.getAttribute(removePoint(this.variables[i][j]));
//						meta[j] = object.getMetadata();
					}
				}

				try {
//					this.expressions[i].bindMetaAttribute(object.getMetadata());
//					this.expressions[i].bindAdditionalContent(object.getAdditionalContent());
					this.expressions[i].bindVariables(values);
					Object expr = this.expressions[i].getValue();

					outputVal.setAttribute(this.expressionStrings.get(i)[0], expr);
					
					if (expr == null) {
						nullValueOccured = true;
					}
				} catch (Exception e) {
					nullValueOccured = true;
					if (!(e instanceof NullPointerException)) {
						logger.warn("Cannot calc result for " + object
								+ " with expression " + expressions[i], e);
						// Not needed. Value is null, if not set!
						// outputVal.setAttribute(i, null);
						sendWarning("Cannot calc result for " + object
								+ " with expression " + expressions[i], e);
					}
				}
			}
		}
		if(this.keepAllAttributes) {
			if(this.removeAttributes != null && this.removeAttributes.size() > 0) {
				for(String att: this.removeAttributes) {
					outputVal.removeAttribute(att);
				}
			}
		}
		if (!nullValueOccured || (nullValueOccured && allowNull)) {
			transfer(outputVal);
		}
	}

	protected void generateExpressions(List<String[]> expressionStrings) {
		this.expressions = new SDFExpression[expressionStrings.size()];
		for (int i = 0; i < expressionStrings.size(); ++i) {
			this.expressions[i] = new SDFExpression("", expressionStrings.get(i)[1], null, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		}
		this.variables = new String[expressionStrings.size()][];
		int i = 0;
		for (SDFExpression expression : this.expressions) {
			List<SDFAttribute> neededAttributes = expression.getAllAttributes();
			String[] newArray = new String[neededAttributes.size()];
			this.variables[i++] = newArray;
			int j = 0;
			for (SDFAttribute curAttribute : neededAttributes) {
				newArray[j++] = curAttribute.toString();
			}
		}
	}
	
	private String removePoint(String name) {
		if(name.substring(0, 1).equals(".")) {
			return name.substring(1);
		}
		return name;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof KeyValueMapPO)) {
			return false;
		}
		KeyValueMapPO rmpo = (KeyValueMapPO) ipo;

		if (!this.getOutputSchema().equals(rmpo.getOutputSchema())) {
			return false;
		}

		if (this.inputSchema.compareTo(rmpo.inputSchema) == 0) {
			if (this.expressionStrings.size() == rmpo.expressionStrings.size()) {
				for (int i = 0; i < this.expressionStrings.size(); i++) {
					if (!this.expressionStrings.get(i).equals(rmpo.expressionStrings.get(i))) {
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

	public List<String[]> getExpressionStrings() {
		return expressionStrings;
	}
	
	public SDFExpression[] getExpressions() {
		return expressions;
	}

	public SDFSchema getInputSchema() {
		return inputSchema;
	}
}
