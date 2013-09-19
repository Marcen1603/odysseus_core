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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.ParameterWidgetFactory;

/**
 * @author DGeesen
 * 
 */
public class OperatorSettingsDialog extends TitleAreaDialog {

	private LogicalOperatorInformation operatorInformation;
	private Map<LogicalParameterInformation, Object> parameterValues;
	private Map<LogicalParameterInformation, IParameterWidget> controls = new HashMap<>();

	public OperatorSettingsDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		setMessage("Configure Operator");
		setTitle("Operator "+operatorInformation.getOperatorName());
		ParameterWidgetFactory widgetFactory = new ParameterWidgetFactory();

		// sort by name ascending
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));		
		TreeMap<LogicalParameterInformation, Object> sortedCopy = new TreeMap<LogicalParameterInformation, Object>(new Comparator<LogicalParameterInformation>() {

			@Override
			public int compare(LogicalParameterInformation o1, LogicalParameterInformation o2) {
				return o1.getName().compareTo(o2.getName());				
			}
		});
		sortedCopy.putAll(parameterValues);
		parameterValues = sortedCopy;
		
		// populate widgets
		for (final Entry<LogicalParameterInformation, Object> param : parameterValues.entrySet()) {
			Label label = new Label(container, SWT.NONE);
			label.setText(param.getKey().getName());			
			IParameterWidget widget = widgetFactory.createParameterWidget(param.getKey().getParameterClass());			
			Control control = widget.createWidget(container, param.getKey(), param.getValue());
			controls.put(param.getKey(), widget);
			
			GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
			gd_dataFolderText.widthHint = 287;
			control.setLayoutData(gd_dataFolderText);
			
			
			

		}

		return parent;
	}

	@Override
	protected void okPressed() {		
		this.parameterValues.clear();
		for(Entry<LogicalParameterInformation, IParameterWidget> entry : this.controls.entrySet()){			
			this.parameterValues.put(entry.getKey(), entry.getValue().getValue());			
		}
		super.okPressed();
	}

	/**
	 * @param operatorInformation
	 */
	public void setOperator(LogicalOperatorInformation operatorInformation) {
		this.operatorInformation = operatorInformation;

	}

	/**
	 * @param parameterValues
	 */
	public void setParameterValues(Map<LogicalParameterInformation, Object> parameterValues) {
		this.parameterValues = parameterValues;
	}

	public Map<LogicalParameterInformation, Object> getParameterValues() {
		return this.parameterValues;
	}

}
