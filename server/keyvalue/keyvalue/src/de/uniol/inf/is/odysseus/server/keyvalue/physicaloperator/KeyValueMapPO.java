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
package de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 * Implementation of map operator for key value objects. Adapted from relational
 * map.
 *
 * @author Jan Soeren Schwarz
 * @author Marco Grawunder
 *
 * @param <T>
 */
public class KeyValueMapPO<K extends IMetaAttribute, T extends KeyValueObject<K>> extends AbstractPipe<T, T> {

	static private Logger logger = LoggerFactory.getLogger(KeyValueMapPO.class);

	protected String[][] variables; // Expression.Index
	private List<NamedExpression> expressions;
	private final SDFSchema inputSchema;
	final private boolean allowNull;

	public KeyValueMapPO(MapAO mapAO) {
		this.inputSchema = mapAO.getInputSchema();
		initExpressions(mapAO.getExpressions());
		this.allowNull = false;
		if (mapAO.isKeepInput()) {
			throw new RuntimeException("KeepInput is not possible for KeyValueMap. Use $ as expression instead");
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	final protected void process_next(T object, int port) {
		if (object != null) {

			T outputVal = (T) KeyValueObject.createInstance();

			outputVal.setMetadata(object.getMetadata() == null ? null : (K) object.getMetadata().clone());
			boolean nullValueOccured = false;

			synchronized (this.expressions) {
				for (int i = 0; i < this.expressions.size(); ++i) {
					Object[] values = new Object[this.variables[i].length];
					for (int j = 0; j < this.variables[i].length; ++j) {
						if (this.variables[i][j].equals("$")) {
							values[j] = object.clone();
							((T)values[j]).setMetadata(null);
						} else {
							values[j] = object.getAttribute(this.variables[i][j]);
							// Could be metadata value
							if (values[j] == null) {
								Pair<Integer, Integer> pos = getInputSchema()
										.indexOfMetaAttribute(this.variables[i][j]);
								if (pos != null) {
									values[j] = object.getMetadata().getValue(pos.getE1(), pos.getE2());
								}
							}
							// Could be path
							if (values[j] == null) {
								List<Object> pathResult = object.path(this.variables[i][j]);
								if (pathResult.size() == 1) {
									values[j] = pathResult.get(0);
								} else {
									values[j] = pathResult;
								}
							}
						}
					}
					SDFExpression expr = this.expressions.get(i).expression;
					String name = this.expressions.get(i).name;
					try {

						expr.bindVariables(values);
						Object val = expr.getValue();

						outputVal.setAttribute(name, val);

						if (val == null) {
							nullValueOccured = true;
						}
					} catch (Exception e) {
						nullValueOccured = true;
						if (!(e instanceof NullPointerException)) {
							logger.warn("Cannot calc result for " + object + " with expression " + expr, e);
							// Not needed. Value is null, if not set!
							// outputVal.setAttribute(i, null);
							sendWarning("Cannot calc result for " + object + " with expression " + expr, e);
						}
					}
				}

			} // synchronized
			if (!nullValueOccured || (nullValueOccured && allowNull)) {
				transfer(outputVal);
			}
		}

	}

	protected void initExpressions(List<NamedExpression> exprToInit) {
		this.variables = new String[exprToInit.size()][];
		this.expressions = new ArrayList<>(exprToInit.size());
		int i = 0;
		for (NamedExpression expression : exprToInit) {
			List<SDFAttribute> exprAttributes = expression.expression.getAllAttributes();
			// Check, if there are some attributes from the meta schema and set datatype
			List<SDFAttribute> neededAttributes = new ArrayList<>();
			boolean changedSchema = false;
			for (SDFAttribute a: exprAttributes){
				Pair<Integer, Integer> pos = getInputSchema()
						.indexOfMetaAttribute(a.getURI());
				if (pos != null){
					neededAttributes.add(getInputSchema().getMetaschema().get(pos.getE1()).getAttribute(pos.getE2()));
					changedSchema = true;
				}else{
					neededAttributes.add(a);
				}

			}
			if (changedSchema){
				SDFSchema newSchema = SDFSchemaFactory.createNewWithAttributes(inputSchema, neededAttributes);
				List<SDFSchema> schemaList = new ArrayList<>();
				schemaList.add(newSchema);
				expression.expression.setSchema(schemaList);
			}
			String[] newArray = new String[neededAttributes.size()];
			this.variables[i++] = newArray;
			int j = 0;
			for (SDFAttribute curAttribute : neededAttributes) {
				newArray[j++] = removePoint(curAttribute.toString());
			}
			if (Strings.isNullOrEmpty(expression.name)) {
				String newName = expression.expression.getExpressionString();
				newName = newName.replaceAll("(", "_").replaceAll(")", "_");
				this.expressions.add(new NamedExpression(newName, expression));
			} else {
				this.expressions.add(expression);
			}
		}
	}

	private String removePoint(String name) {
		if (name.substring(0, 1).equals(".")) {
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
			if (this.expressions.size() == rmpo.expressions.size()) {
				for (int i = 0; i < this.expressions.size(); i++) {
					if (!this.expressions.get(i).equals(rmpo.expressions.get(i))) {
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

	public List<NamedExpression> getExpressions() {
		return expressions;
	}

	public SDFSchema getInputSchema() {
		return inputSchema;
	}
}
