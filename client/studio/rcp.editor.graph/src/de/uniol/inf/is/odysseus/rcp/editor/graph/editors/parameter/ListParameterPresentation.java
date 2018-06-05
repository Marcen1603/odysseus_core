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

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author DGeesen
 * 
 */
public class ListParameterPresentation<V> extends AbstractParameterPresentation<List<IParameterPresentation<V>>> {

	private Button editButton;
	private Text currentValueText;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter. IParameterPresentation #getPQLString(de.uniol.inf.is.odysseus.core.logicaloperator .LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public String getPQLString() {
		String str = "[";
		String sep = "";
		if (getValue() != null) {
			for (IParameterPresentation<?> sub : getValue()) {
				str = str + sep + sub.getPQLString();
				sep = ", ";

			}
		}
		return str + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter. AbstractParameterPresentation #createParameterWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is. odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	protected Control createParameterWidget(final Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = 0;
		layout.marginRight = 0;
		container.setLayout(layout);

		currentValueText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		resetValues(getValue());
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		currentValueText.setLayoutData(gridData);
		editButton = new Button(container, SWT.PUSH);
		editButton.setText("...");

		editButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				List<IParameterPresentation<V>> childs = new ArrayList<>();
				if (getValue() != null) {
					childs = getValue();
				}
				List<IParameterPresentation<V>> copiedChilds = new ArrayList<>(childs);
				ListParameterDialog<V> dialog = new ListParameterDialog<V>(parent.getShell(), getLogicalParameterInformation(), getOperator(), copiedChilds);
				if (dialog.open() == Window.OK) {
					List<IParameterPresentation<V>> result = dialog.getParameters();
					resetValues(result);
				}
			}
		});

		return container;
	}

	private void resetValues(List<IParameterPresentation<V>> result) {
		setValue(result);
		currentValueText.setText(getPQLString());		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter. IParameterPresentation#loadValueFromXML(org.w3c.dom.Node)
	 */
	@Override
	public void loadValueFromXML(Node parent) {
		NodeList list = parent.getChildNodes();
		List<IParameterPresentation<V>> values = new ArrayList<IParameterPresentation<V>>();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element) {
				Element itemElement = (Element) list.item(i);
				IParameterPresentation<V> parameterPresentation = ParameterPresentationFactory.createPresentationByClass(getLogicalParameterInformation(), getOperator(), (V) null);
				parameterPresentation.loadValueFromXML(itemElement);
				values.add(parameterPresentation);
			}
		}
		setValue(values);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter. IParameterPresentation#saveValueToXML(org.w3c.dom.Node, org.w3c.dom.Document)
	 */
	@Override
	public void saveValueToXML(Node parent, Document builder) {
		if (getValue() != null) {
			for (IParameterPresentation<?> param : getValue()) {
				Element listElement = builder.createElement("item");
				param.saveValueToXML(listElement, builder);
				parent.appendChild(listElement);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter. AbstractParameterPresentation#hasValidValue()
	 */
	@Override
	public boolean hasValidValue() {
		if (super.hasValidValue()) {
			if (!getValue().isEmpty()) {
				return true;
			}
		}
		return false;
	}

}
