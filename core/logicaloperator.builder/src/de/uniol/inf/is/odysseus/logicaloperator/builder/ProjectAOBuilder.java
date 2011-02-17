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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ProjectAOBuilder extends AbstractOperatorBuilder {

	private static final String ATTRIBUTES = "ATTRIBUTES";
	private ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>(
			ATTRIBUTES, REQUIREMENT.MANDATORY,
			new ResolvedSDFAttributeParameter("project attribute",
					REQUIREMENT.MANDATORY));

	public ProjectAOBuilder() {
		super(1, 1);
		setParameters(attributes);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		SDFAttributeList outputSchema = new SDFAttributeList(attributes
				.getValue());

		ProjectAO projectAO = new ProjectAO();
		projectAO.setOutputSchema(outputSchema);

		return projectAO;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
