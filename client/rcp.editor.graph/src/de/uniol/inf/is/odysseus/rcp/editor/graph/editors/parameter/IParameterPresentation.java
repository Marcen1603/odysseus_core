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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 *
 */
public interface IParameterPresentation<V>{
	public void addParameterValueChangedListener(IParameterValueChangeListener<V> listener);
	public void removeParameterValueChangedListener(IParameterValueChangeListener<V> listener);
	public void init(LogicalParameterInformation parameterInformation, OperatorNode operator, V currentValue);
	public Control createWidget(Composite parent);
	
	public V getValue();
	public void setValue(V name);
	public boolean hasValidValue();
	
	public String getPQLString();
	public void setLogicalParameterInformation(LogicalParameterInformation lpi);
	public LogicalParameterInformation getLogicalParameterInformation();
	
	public Control getControl();
	public Control createHeaderWidget(Composite parent);
	
	public void saveValueToXML(Node parent, Document builder);
	public void loadValueFromXML(Node parent);
	
}
