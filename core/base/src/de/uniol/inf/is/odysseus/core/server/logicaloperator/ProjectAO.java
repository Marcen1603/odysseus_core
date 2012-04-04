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
/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Marco Grawunder
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "PROJECT")
public class ProjectAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 5487345119018834806L;

	public ProjectAO() {
		super();
	}

	public ProjectAO(ProjectAO ao) {
		super(ao);
	}

	public @Override
	ProjectAO clone() {
		return new ProjectAO(this);
	}

	public int[] determineRestrictList() {
		return calcRestrictList(this.getInputSchema(), this.getOutputSchema());
	}

	// Must be another name than setOutputSchema, else this method is not found!
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true)
	public void setOutputSchemaWithList(List<SDFAttribute> outputSchema) {
		setOutputSchema(new SDFSchema("", outputSchema));
	}
	
	@Override
	@GetParameter(name ="ATTRIBUTES")
	public SDFSchema getOutputSchemaIntern() {
		// TODO: Dies ist nur wg. des GetParameters. Braucht man den überhaupt?
		return getOutputSchema();
	}

	public static int[] calcRestrictList(SDFSchema in,
			SDFSchema out) {
		int[] ret = new int[out.size()];
		int i = 0;
		for (SDFAttribute a : out) {
			int j = 0;
			int k = i;
			for (SDFAttribute b : in) {
				if (b.equals(a)) {
					ret[i++] = j;
				}
				++j;
			}
			if (k == i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}

	
	@Override
	public boolean isValid() {
		//init
		setOutputSchema(new SDFSchema(getInputSchema().getURI(), getOutputSchema()));
		return super.isValid();
	}

}
