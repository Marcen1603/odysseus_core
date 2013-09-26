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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

/**
 * @author DGeesen
 * 
 */
public abstract class AbstractParameterPresentation implements IParameterPresentation {

	private Object value = null;
	private List<IParameterValueChangeListener> listeners = new ArrayList<>();
	private Control control;
	private LogicalParameterInformation logicalParameterInformation;

	protected void setValue(Object value) {
		Object oldValue = this.value;
		this.value = value;
		if (this.value == null) {
			if (oldValue != null) {
				valueChanged();
			}
			return;
		} else {
			if (!this.value.equals(oldValue)) {
				valueChanged();
			}
		}

	}

	private void valueChanged() {
		for (IParameterValueChangeListener listener : this.listeners) {
			listener.parameterValueChanged(this.value, this);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#createWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public Control createWidget(Composite parent, LogicalParameterInformation parameterInformation, Object currentValue) {
		this.value = currentValue;
		if (this.control != null) {
			throw new IllegalStateException("Widget can only be created once!");
		}
		this.control = createParameterWidget(parent, parameterInformation, currentValue);
		this.logicalParameterInformation = parameterInformation;
		return control;
	}

	protected abstract Control createParameterWidget(Composite parent, LogicalParameterInformation parameterInformation, Object currentValue);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#getValue()
	 */
	@Override
	public Object getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#addParameterValueChangedListener(de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterValueChangeListener)
	 */
	@Override
	public void addParameterValueChangedListener(IParameterValueChangeListener listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#removeParameterValueChangedListener(de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterValueChangeListener)
	 */
	@Override
	public void removeParameterValueChangedListener(IParameterValueChangeListener listener) {
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

}
