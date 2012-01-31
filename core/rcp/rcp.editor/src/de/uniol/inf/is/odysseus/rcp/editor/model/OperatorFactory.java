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
package de.uniol.inf.is.odysseus.rcp.editor.model;

import org.eclipse.gef.requests.CreationFactory;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.usermanagement.ISession;


public class OperatorFactory implements CreationFactory {

	private final String operatorBuilderName;
	
	public OperatorFactory(String operatorBuilderName) {
		this.operatorBuilderName = operatorBuilderName;
	}
	
	@Override
	public Object getNewObject() {
		final ISession user = OdysseusRCPPlugIn.getActiveSession();
		Operator op = new Operator( OperatorBuilderFactory.createOperatorBuilder(operatorBuilderName, user, OdysseusRCPPlugIn.getExecutor().getDataDictionary()), 
				operatorBuilderName);
		return op;
	}

	@Override
	public Object getObjectType() {
		return Operator.class;
	}

	
}
