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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author DGeesen
 * 
 */
public class IntegerParameterPresentation extends AbstractParameterPresentation<Integer> {

	private Spinner sp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#createWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation)
	 */
	@Override
	public Control createParameterWidget(Composite parent) {
		int val = 0;
		if (getValue() != null) {
			val = Integer.parseInt(getValue().toString());
		}
		sp = new Spinner(parent, SWT.BORDER);
		sp.setMaximum(Integer.MAX_VALUE);
		sp.setSelection(val);
		sp.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				setValue(sp.getSelection());
				super.widgetSelected(e);
			}
		});

		return sp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getPQLString(java.lang.Object)
	 */
	@Override
	public String getPQLString() {
		return String.valueOf(getValue());
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
		if (text.equalsIgnoreCase("null") || text.isEmpty()) {
			setValue(null);
		} else {
			setValue(Integer.parseInt(text));
		}
	}

}
