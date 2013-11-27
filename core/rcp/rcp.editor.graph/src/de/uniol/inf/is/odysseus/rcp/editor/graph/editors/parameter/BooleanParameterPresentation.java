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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author DGeesen
 * 
 */
public class BooleanParameterPresentation extends AbstractParameterPresentation<Boolean> {

	private Combo dropDown;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#createWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public Control createParameterWidget(Composite parent) {
		Boolean newValue = null;
		if (getValue() != null) {
			newValue = getValue();
		}
		dropDown = new Combo(parent, SWT.DROP_DOWN | SWT.BORDER);
		if (!getLogicalParameterInformation().isMandatory()) {
			dropDown.add("");

		}
		dropDown.add("true");
		dropDown.add("false");
		if (newValue != null) {
			if (newValue.equals(Boolean.TRUE)) {
				dropDown.select(1);
			} else if (newValue.equals(Boolean.FALSE)) {
				dropDown.select(2);
			}
		} else {
			dropDown.select(0);
		}

		dropDown.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (dropDown.getText().equals("true")) {
					setValue(Boolean.TRUE);
				} else if (dropDown.getText().equals("false")) {
					setValue(Boolean.FALSE);
				} else {
					setValue(null);
				}				
			}
		});
		return dropDown;

	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getPQLString(de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public String getPQLString() {
		return "'" + String.valueOf(getValue())+"'";
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
			setValue(Boolean.parseBoolean(text));
		}
	}

	

}
