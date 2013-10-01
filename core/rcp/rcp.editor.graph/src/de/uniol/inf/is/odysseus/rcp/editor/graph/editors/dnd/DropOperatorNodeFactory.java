/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dnd;

import org.eclipse.gef.requests.CreationFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.StringParameterPresentation;

/**
 * @author DGeesen
 * 
 */
public class DropOperatorNodeFactory implements CreationFactory {

	private LogicalOperatorInformation operatorInformation;
	private String name;	

	public DropOperatorNodeFactory(LogicalOperatorInformation op, String resourceName) {
		this.operatorInformation = op;		
		this.name = resourceName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	@Override
	public Object getNewObject() {
		OperatorNode node = new OperatorNode(operatorInformation);
		// set source name
		for (LogicalParameterInformation lpi : node.getParameterValues().keySet()) {
			if (lpi.getName().equalsIgnoreCase("Source")) {
				StringParameterPresentation param = (StringParameterPresentation) node.getParameterValues().get(lpi);
				param.setValue(this.name);
				break;
			}
			if (lpi.getName().equalsIgnoreCase("Sink")) {
				StringParameterPresentation param = (StringParameterPresentation) node.getParameterValues().get(lpi);
				param.setValue(this.name);
				break;
			}
		}
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	@Override
	public Object getObjectType() {
		return OperatorNode.class;
	}

}
