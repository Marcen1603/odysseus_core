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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public abstract class AbstractParameterPresentation<V> implements IParameterPresentation<V> {

	private V value = null;
	private List<IParameterValueChangeListener<V>> listeners = new ArrayList<>();
	private Control control;
	private LogicalParameterInformation logicalParameterInformation;
	private OperatorNode operator;

	@Override
	public synchronized void setValue(V value) {
		Object oldValue = this.value;
		this.value = value;
		if (this.value == null) {
			if (oldValue != null) {
				valueChanged();
			}
			return;
		} 
		if (!this.value.equals(oldValue)) {
			valueChanged();
		}
	}

	private synchronized void valueChanged() {
		for (IParameterValueChangeListener<V> listener : this.listeners) {
			listener.parameterValueChanged(this);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#init(de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public void init(LogicalParameterInformation parameterInformation, OperatorNode operator, V currentValue) {
		this.value = currentValue;
		this.logicalParameterInformation = parameterInformation;
		this.operator = operator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#createWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public Control createWidget(Composite parent) {
		try {
			this.control = createParameterWidget(parent);
			return control;
		} catch (Exception e) {
			RuntimeException ex = new RuntimeException("Error in creating param value for " + getLogicalParameterInformation(), e);
			throw ex;
		}
	}

	protected abstract Control createParameterWidget(Composite parent);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#getValue()
	 */
	@Override
	public V getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#addParameterValueChangedListener(de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterValueChangeListener)
	 */
	@Override
	public synchronized void addParameterValueChangedListener(IParameterValueChangeListener<V> listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#removeParameterValueChangedListener(de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterValueChangeListener)
	 */
	@Override
	public synchronized void removeParameterValueChangedListener(IParameterValueChangeListener<V> listener) {
		listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getLogicalParameterInformation()
	 */
	@Override
	public LogicalParameterInformation getLogicalParameterInformation() {
		return logicalParameterInformation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#setLogicalParameterInformation(de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation)
	 */
	@Override
	public void setLogicalParameterInformation(LogicalParameterInformation lpi) {
		this.logicalParameterInformation = lpi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getControl()
	 */
	@Override
	public Control getControl() {
		return this.control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getHeaderControl()
	 */
	@Override
	public Control createHeaderWidget(Composite parent) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#hasValidValue()
	 */
	@Override
	public boolean hasValidValue() {
		if(value==null){
			return false;
		}
		return true;
	}
	
	public OperatorNode getOperator(){
		return this.operator;
	}
	
	protected IProject getProject(){
		return this.operator.getGraph().getProject();
	}
	
	protected String getListPQLString(List<String> value){
		// ['count', 'id', 'count', 'PartialAggregate']
		StringBuffer str = new StringBuffer("[");
		if (getValue() != null) {
			for (String s:value){
				str.append("'").append(s).append("',");
			}
			str.delete(str.length()-1,str.length());
		}
		str.append("]");
		return str.toString();
	}
	
	protected String getStringValue(List<String> value, int i) {
		String ret = "";
		if (value != null && value.size() > i){
			ret = value.get(i);
		}
	
		return ret;
	}

	
}
