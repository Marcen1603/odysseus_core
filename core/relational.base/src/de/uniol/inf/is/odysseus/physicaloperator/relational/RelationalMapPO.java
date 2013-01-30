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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalMapPO<T extends IMetaAttribute> extends
		AbstractPipe<Tuple<T>, Tuple<T>> {
	
	static private Logger logger = LoggerFactory.getLogger(RelationalMapPO.class);

	private VarHelper[][] variables; // Expression.Index
	private SDFExpression[] expressions;
	private final SDFSchema inputSchema;
	final private LinkedList<Tuple<T>> lastObjects = new LinkedList<>();
	private int maxHistoryElements = 0;
	final private boolean statebased;

	public RelationalMapPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean statebased) {
		this.inputSchema = inputSchema;
		this.statebased = statebased;
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
				if (curAttribute.getSourceName() != null && curAttribute.getSourceName().startsWith("__last_")) {
					if (!statebased){
						throw new RuntimeException("Map cannot be used with __last_! Used StateMap instead!");
					}
					int pos = Integer.parseInt(curAttribute.getSourceName().substring("__last_".length(), curAttribute.getSourceName().indexOf('.')));
					if (pos > maxHistoryElements){
						maxHistoryElements = pos+1;
					}
					String realAttrStr = curAttribute.getURI().substring(
							curAttribute.getURI().indexOf('.') + 1);
					String newSource = realAttrStr.substring(0,
							realAttrStr.indexOf('.'));
					String newName = realAttrStr.substring(realAttrStr
							.indexOf('.') + 1);
					if ("null".equals(newSource)){
						newSource = null;
					}
					SDFAttribute newAttribute = new SDFAttribute(newSource,
							newName, curAttribute);
					int index = schema.indexOf(newAttribute);
					newArray[j++] = new VarHelper(index,
							pos);
				} else {
					newArray[j++] = new VarHelper(schema.indexOf(curAttribute),
							0);
				}
			}
		}
	}

	public RelationalMapPO(RelationalMapPO<T> relationalMapPO) {
		this.inputSchema = relationalMapPO.inputSchema.clone();
		this.statebased = relationalMapPO.statebased;
		init(relationalMapPO.inputSchema, relationalMapPO.expressions );
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings("unchecked")
	@Override
	final protected void process_next(Tuple<T> object, int port) {
		Tuple<T> outputVal = new Tuple<T>(this.expressions.length, false);
		outputVal.setMetadata((T) object.getMetadata().clone());
		int lastObjectSize = this.lastObjects.size();
		if (lastObjectSize > maxHistoryElements) {
			lastObjects.removeLast();
			lastObjectSize--;
		}
		lastObjects.addFirst(object);
		lastObjectSize++;
		synchronized (this.expressions) {
			for (int i = 0; i < this.expressions.length; ++i) {
				Object[] values = new Object[this.variables[i].length];
				for (int j = 0; j < this.variables[i].length; ++j) {
					Tuple<T> obj = null;
					if (lastObjectSize > this.variables[i][j].objectPosToUse) {
						obj = lastObjects.get(this.variables[i][j].objectPosToUse);
					}
					if (obj != null) {
						values[j] = obj.getAttribute(this.variables[i][j].pos);
					}
				}
				
				try{
					this.expressions[i].bindAdditionalContent(object
						.getAdditionalContent());
					this.expressions[i].bindVariables(values);
					outputVal.setAttribute(i, this.expressions[i].getValue());
				}catch(Exception e){
					logger.error("Cannot calc result "+e.getMessage());
					// Not needed. Value is null, if not set!
					//outputVal.setAttribute(i, null);
				}
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
	@SuppressWarnings({ "rawtypes" })
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RelationalMapPO)) {
			return false;
		}
		RelationalMapPO rmpo = (RelationalMapPO) ipo;
		if (this.hasSameSources(rmpo)
				&& this.inputSchema.compareTo(rmpo.inputSchema) == 0) {
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