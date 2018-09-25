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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;

/**
 * @author DGeesen
 * 
 */
public class StringParameterPresentation extends AbstractParameterPresentation<String> {

	private Text text;
	private Combo combo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#createWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public Control createParameterWidget(Composite parent) {
		String currentStr = "";
		if (getValue() != null) {
			currentStr = getValue().toString();
		}
		
		List<String> possibleValues = new ArrayList<String>(getLogicalParameterInformation().getPossibleValues());
		
		if (getLogicalParameterInformation().arePossibleValuesDynamic()){
			LogicalOperatorInformation loi = Activator.getDefault().getExecutor().getOperatorInformation(getOperator().getOperatorInformation().getOperatorName(), Activator.getDefault().getCaller());
			possibleValues = new ArrayList<>(loi.getParameter(getLogicalParameterInformation().getName()).getPossibleValues());			
		}

		if (possibleValues.isEmpty()) {
			text = new Text(parent, SWT.BORDER);
			text.setText(currentStr);
			text.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					if (text.getText().isEmpty()) {
						setValue(null);
					} else {
						setValue(text.getText());
					}
				}
			});
			return text;
		} 
		combo = new Combo(parent, SWT.BORDER | SWT.DROP_DOWN);
		int select = 0;
		combo.add("");
		for (String posVal : possibleValues) {
			combo.add(posVal);
			if (posVal.equals(getValue())) {
				select = combo.getItemCount() - 1;
			}
		}
		combo.select(select);
		combo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (combo.getText().isEmpty()) {
					setValue(null);
				} else {
					setValue(combo.getText());
				}

			}
		});
		
		return combo;		
	}

	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getPQLString(de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public String getPQLString() {
		return "'" + String.valueOf(getValue()) + "'";
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#saveValueToXML(org.w3c.dom.Node, org.w3c.dom.Document)
	 */
	@Override
	public void saveValueToXML(Node parent, Document builder) {
		parent.setTextContent(String.valueOf(getValue()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#loadValueFromXML(org.w3c.dom.Node)
	 */
	@Override
	public void loadValueFromXML(Node parent) {
		String text = parent.getTextContent();
		if (text.equalsIgnoreCase("null")) {
			setValue(null);
		} else {
			setValue(text);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.AbstractParameterPresentation#hasValidValue()
	 */
	@Override
	public boolean hasValidValue() {
		if(super.hasValidValue()){
			if(!getValue().isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Control createHeaderWidget(Composite parent) {
		Label labelName = new Label(parent, SWT.None);
		labelName.setText("Value");		
		labelName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));		
		return labelName;
	}

}
