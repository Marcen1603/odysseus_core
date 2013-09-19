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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;


/**
 * @author DGeesen
 *
 */
public abstract class AbstractParameterWidget implements IParameterWidget{

	private Object value = null;
	
	protected void setValue(Object value){
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#createWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public Control createWidget(Composite parent, LogicalParameterInformation parameterInformation, Object currentValue) {
		setValue(currentValue);
		return createParameterWidget(parent, parameterInformation, currentValue);
	}
	
	protected abstract Control createParameterWidget(Composite parent, LogicalParameterInformation parameterInformation, Object currentValue);

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#getValue()
	 */
	@Override
	public Object getValue() {	
		return value;
	}
}
