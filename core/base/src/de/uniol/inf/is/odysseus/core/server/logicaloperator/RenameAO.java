/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Jonas Jacobi
 */
@LogicalOperator(name = "RENAME", minInputPorts = 1, maxInputPorts = 1)
public class RenameAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4218605858465342011L;
	private List<String> aliases;

	public RenameAO() {
		super();
	}

	public RenameAO(AbstractLogicalOperator po) {
		super(po);
	}

	public RenameAO(RenameAO ao) {
		super(ao);
		aliases = ao.aliases;
	}

	@Parameter(type = StringParameter.class, isList = true)
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
		SDFSchema inputSchema = getInputSchema();
		if (inputSchema.size() != aliases.size()) {
			throw new IllegalArgumentException(
					"number of aliases does not match number of input attributes for rename");
		}
		Iterator<SDFAttribute> it = inputSchema.iterator();
		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		for (String str : aliases) {
			// use clone, so we have a datatype etc.
			SDFAttribute attribute = it.next().clone(null,str);
//			attribute.setAttributeName(str);
//			attribute.setSourceName(null);
			attrs.add(attribute);
		}
		setOutputSchema(new SDFSchema(inputSchema.getURI(), attrs));

	}
	
	@GetParameter(name="setAliases")
	public List<String> getAliases(){
		return this.aliases;
	}


	@Override
	public AbstractLogicalOperator clone() {
		return new RenameAO(this);
	}


}
