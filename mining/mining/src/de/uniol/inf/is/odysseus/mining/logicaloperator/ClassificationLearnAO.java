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
package de.uniol.inf.is.odysseus.mining.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * @author Dennis Geesen
 *
 */
@LogicalOperator(name = "CLASSIFICATION_LEARN", minInputPorts = 1, maxInputPorts = 1)
public class ClassificationLearnAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1231999597473176237L;

	private SDFAttribute classAttribute;
	
	public ClassificationLearnAO(){
		
	}
	
	public ClassificationLearnAO(ClassificationLearnAO classificationLearnAO) {
		this.classAttribute = classificationLearnAO.classAttribute;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ClassificationLearnAO(this);
	}

	public SDFAttribute getClassAttribute() {
		return classAttribute;
	}

	@Parameter(name="class", type = ResolvedSDFAttributeParameter.class)
	public void setClassAttribute(SDFAttribute classAttribute) {
		this.classAttribute = classAttribute;
	}

}
