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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
@LogicalOperator(name = "RENAME", minInputPorts = 1, maxInputPorts = 1)
public class RenameAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4218605858465342011L;
	private List<String> aliases = new ArrayList<>();
	private boolean aliasesAsPairs = false;
	private String typeName;
	private boolean calculated = false;

	public RenameAO() {
		super();
	}

	public RenameAO(AbstractLogicalOperator po) {
		super(po);
	}

	public RenameAO(RenameAO ao) {
		super(ao);
		aliases = ao.aliases;
		aliasesAsPairs = ao.aliasesAsPairs;
	}

	@Parameter(type = StringParameter.class, isList = true, optional = true)
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	@Parameter(name = "Type", type = StringParameter.class, optional = true)
	public void setType(String typeName) {
		this.typeName = typeName;
	}

	@Parameter(name = "Pairs", type = BooleanParameter.class, optional = true)
	public void setPairs(boolean aliasesAsPairs) {
		this.aliasesAsPairs = aliasesAsPairs;
	}

	@GetParameter(name = "setAliases")
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (!calculated) {
			SDFSchema output = super.getOutputSchema(pos);
			SDFSchema input = super.getInputSchema();
			List<SDFAttribute> newOutput = new ArrayList<SDFAttribute>();
			// check, if types are equal
			for (int i = 0; i < output.size(); i++) {
				newOutput.add(output.get(i).clone(input.get(i).getDatatype()));
			}
		}
		return super.getOutputSchemaIntern(pos);
	}

	@Override
	public void initialize() {
		if (!aliasesAsPairs) {
			SDFSchema inputSchema = getInputSchema();
			if (!aliases.isEmpty()) {
				if (inputSchema.size() != aliases.size()) {
					throw new IllegalArgumentException("number of aliases does not match number of input attributes for rename");
				}
				Iterator<SDFAttribute> it = inputSchema.iterator();
				List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
				for (String str : aliases) {
					// use clone, so we have a datatype etc.
					SDFAttribute attribute = it.next().clone(null, str);
					attrs.add(attribute);
				}
				String uri = typeName != null ? typeName : inputSchema.getURI();
				setOutputSchema(new SDFSchema(uri, attrs));
			} else {
				//
				if (typeName == null) {
					throw new IllegalArgumentException("if aliases for attributes is not used, type must be used to rename anyway");
				}
				// only set type name!
				List<SDFAttribute> attrs = Lists.newArrayList();
				for (SDFAttribute oldAttr : inputSchema) {
					SDFAttribute newOne = new SDFAttribute(typeName, oldAttr.getAttributeName(), oldAttr);
					attrs.add(newOne);
				}
				setOutputSchema(new SDFSchema(typeName, attrs));
			}
		} else {
			SDFSchema inputSchema = getInputSchema();
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
				setOutputSchema(new SDFSchema(uri, attrs));
			}
		}
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
