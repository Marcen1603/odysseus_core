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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
@LogicalOperator(name = "RENAME", minInputPorts = 1, maxInputPorts = 1, doc = "Renames the attributes", url = "http://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Rename+operator", category = {
		LogicalOperatorCategory.BASE })
public class RenameAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4218605858465342011L;
	private List<String> aliases = new ArrayList<>();
	private boolean aliasesAsPairs;
	private String typeName;
//	private boolean calculated;
	private boolean noOp;
	private boolean keepPosition = false;

	public RenameAO() {
		super();
	}

	public RenameAO(AbstractLogicalOperator ao) {
		super(ao);
	}

	public RenameAO(RenameAO ao) {
		super(ao);
		aliases = ao.aliases;
		aliasesAsPairs = ao.aliasesAsPairs;
		noOp = ao.noOp;
		keepPosition = ao.keepPosition;
	}

	public boolean isKeepPosition() {
		return keepPosition;
	}

	public void setKeepPosition(boolean keepPosition) {
		this.keepPosition = keepPosition;
	}

	@Parameter(type = StringParameter.class, isList = true, optional = true, doc = "The new list of attributes. Must be exactly the same length as in the input schema.")
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	@Parameter(name = "Type", type = StringParameter.class, optional = true, doc = "The new type name of the output schema.")
	public void setType(String typeName) {
		this.typeName = typeName;
	}

	@Parameter(name = "Pairs", type = BooleanParameter.class, optional = true, doc = "If set to true, aliases will be interpreted as pairs oldAttribute, new Attribute.")
	public void setPairs(boolean aliasesAsPairs) {
		this.aliasesAsPairs = aliasesAsPairs;
	}

	@Parameter(name = "isNoOp", type = BooleanParameter.class, optional = true, doc = "A flag to avoid removing this operator even if nothing in the schema is changed.", deprecated = true)
	public void setNoOpOld(boolean isNoOp) {
		this.noOp = isNoOp;
	}

	@Parameter(name = "NoOp", type = BooleanParameter.class, optional = true, doc = "A flag to avoid removing this operator even if nothing in the schema is changed.", deprecated = false)
	public void setNoOp(boolean isNoOp) {
		this.noOp = isNoOp;
	}

	public boolean isNoOp() {
		return noOp;
	}

	public boolean getPairs() {
		return this.aliasesAsPairs;
	}

	public String getType() {
		return this.typeName;
	}

	@GetParameter(name = "setAliases")
	public List<String> getAliases() {
		return this.aliases;
	}

	public Map<String, String> getAliasesAsMap() {
		return RenameAO.toMap(aliases);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (recalcOutputSchemata ||  super.getOutputSchemaIntern(pos) == null) {
			initialize();
		}
//		if (!calculated) {
//			SDFSchema output = super.getOutputSchema(pos);
//			SDFSchema input = super.getInputSchema();
//			List<SDFAttribute> newOutput = new ArrayList<SDFAttribute>();
//			// check, if types are equal
//			for (int i = 0; i < output.size(); i++) {
//				newOutput.add(output.get(i).clone(input.get(i).getDatatype()));
//			}
//		}
		return super.getOutputSchemaIntern(pos);
	}

	@Override
	public void initialize() {
		SDFSchema inputSchema = getInputSchema();
		// if((inputSchema.getType() != KeyValueObject.class)) {
		if (!aliasesAsPairs) {
			if (!aliases.isEmpty()) {
				if (inputSchema.size() != aliases.size()) {
					throw new IllegalArgumentException(
							"number of aliases does not match number of input attributes for rename");
				}
				Iterator<SDFAttribute> it = inputSchema.iterator();
				List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
				for (String str : aliases) {
					// use clone, so we have a datatype etc.
					SDFAttribute attribute = it.next().clone(null, str);
					attrs.add(attribute);
				}
				String uri = typeName != null ? typeName : inputSchema.getURI();
				setOutputSchema(SDFSchemaFactory.createNewWithAttributes(uri, attrs, inputSchema));
			} else {
				//
				if (typeName != null) {
					// only set type name!
					List<SDFAttribute> attrs = Lists.newArrayList();
					for (SDFAttribute oldAttr : inputSchema) {
						SDFAttribute newOne = new SDFAttribute(typeName, oldAttr.getAttributeName(), oldAttr);
						attrs.add(newOne);
					}
					setOutputSchema(SDFSchemaFactory.createNewWithAttributes(typeName, attrs, inputSchema));
				} else {
					setOutputSchema(inputSchema);
				}
			}
		} else {
			if (aliases.isEmpty()) {
				throw new IllegalArgumentException("number of aliases interpreted as pairs must be at least two");
			}
			if (aliases.size() % 2 != 0) {
				throw new IllegalArgumentException("number of aliases interpreted as pairs must be even");
			}

			Map<String, String> aliasesMap = toMap(aliases);
			List<SDFAttribute> attrs = Lists.newArrayList();
			for (SDFAttribute oldAttr : inputSchema) {
				String alias = aliasesMap.get(oldAttr.getAttributeName());
				if (alias != null) {
					// alias found!
					SDFAttribute newAttr = oldAttr.clone(null, alias);
					attrs.add(newAttr);
				} else {
					attrs.add(oldAttr.clone());
				}
				String uri = typeName != null ? typeName : inputSchema.getURI();
				setOutputSchema(SDFSchemaFactory.createNewWithAttributes(uri, attrs, inputSchema));
			}
		}
		// } else {
		// String uri = typeName != null ? typeName : inputSchema.getURI();
		// List<SDFAttribute> attrs = Lists.newArrayList();
		// setOutputSchema(SDFSchemaFactory.createNewWithAttributes(uri, attrs,
		// inputSchema));
		// }
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RenameAO(this);
	}

	private static Map<String, String> toMap(List<String> aliases) {
		Map<String, String> map = Maps.newHashMap();
		for (int i = 0; i < aliases.size(); i += 2) {
			map.put(aliases.get(i), aliases.get(i + 1));
		}
		return map;
	}
}
