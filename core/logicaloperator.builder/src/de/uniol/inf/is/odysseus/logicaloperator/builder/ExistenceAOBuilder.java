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

import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ExistenceAO.Type;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;

public class ExistenceAOBuilder extends AbstractOperatorBuilder {

	PredicateParameter predicate = new PredicateParameter("PREDICATE", REQUIREMENT.MANDATORY);
	DirectParameter<String> type = new DirectParameter<String>("TYPE", REQUIREMENT.MANDATORY);
	
	public ExistenceAOBuilder() {
		super(2,2);
		setParameters(predicate, type);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		ExistenceAO ao = new ExistenceAO();
		ao.setPredicate(predicate.getValue());
		if (type.getValue().equalsIgnoreCase("EXISTS")) {
			ao.setType(Type.EXISTS);
		} else {
			ao.setType(Type.NOT_EXISTS);
		}
		
		return ao;
	}

	@Override
	protected boolean internalValidation() {
		String typeStr = type.getValue();
		if (typeStr.equalsIgnoreCase("EXISTS") || typeStr.equalsIgnoreCase("NOT EXISTS")) {
			return true;
		} else {
			addError(new IllegalParameterException("Illegal value for type of existence AO, EXISTS or NOT EXISTS expected"));
			return false;
		}
	}

}
