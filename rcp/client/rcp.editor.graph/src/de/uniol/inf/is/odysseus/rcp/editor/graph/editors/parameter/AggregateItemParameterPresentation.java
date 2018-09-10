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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;

/**
 * @author DGeesen
 * 
 */
public class AggregateItemParameterPresentation extends
		AbstractParameterPresentation<List<String>> {

	private Text newAttributeText;
	private Combo functionCombo;
	private Combo onAttributeCombo;

	private int port = 0;
	private Combo comboDatatype;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * IParameterPresentation#getPQLString()
	 */
	@Override
	public String getPQLString() {
		// ['count', 'id', 'count', 'PartialAggregate']
		return getListPQLString(getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * IParameterPresentation#saveValueToXML(org.w3c.dom.Node,
	 * org.w3c.dom.Document)
	 */
	@Override
	public void saveValueToXML(Node parent, Document builder) {
		if (getValue().size() >= 1) {
			Element f = builder.createElement("function");
			f.setTextContent(getValue().get(0));
			parent.appendChild(f);
		}

		if (getValue().size() >= 2) {
			Element a = builder.createElement("onAttribute");
			a.setTextContent(getValue().get(1));
			parent.appendChild(a);
		}

		if (getValue().size() >= 3) {
			Element n = builder.createElement("newAttribute");
			n.setTextContent(getValue().get(2));
			parent.appendChild(n);
		}

		if (getValue().size() == 4) {
			Element d = builder.createElement("datatype");
			d.setTextContent(getValue().get(3));
			parent.appendChild(d);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * IParameterPresentation#loadValueFromXML(org.w3c.dom.Node)
	 */
	@Override
	public void loadValueFromXML(Node parent) {
		NodeList childs = parent.getChildNodes();
		String function = null;
		String onAttribute = null;
		String newAttribute = null;
		String datatype = null;
		ArrayList<String> aggItem = new ArrayList<>();
		for (int i = 0; i < childs.getLength(); i++) {
			if (childs.item(i) instanceof Element) {
				Element el = (Element) childs.item(i);
				if (el.getNodeName().equals("function")) {
					function = el.getTextContent();
					aggItem.add(function);
				}
				if (el.getNodeName().equals("onAttribute")) {
					onAttribute = el.getTextContent();
					aggItem.add(onAttribute);
				}
				if (el.getNodeName().equals("newAttribute")) {
					newAttribute = el.getTextContent();
					aggItem.add(newAttribute);
				}
				if (el.getNodeName().equals("datatype")) {
					datatype = el.getTextContent();
					aggItem.add(datatype);
				}
			}
		}
		setValue(aggItem);
	}

	@Override
	public Control createHeaderWidget(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(4, true));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label labelFunction = new Label(container, SWT.None);
		labelFunction.setText("Function");
		labelFunction.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		Label labelAttribute = new Label(container, SWT.None);
		labelAttribute.setText("Attribute");
		labelAttribute.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		Label labelName = new Label(container, SWT.None);
		labelName.setText("Name");
		labelName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label labelDatatype = new Label(container, SWT.None);
		labelDatatype.setText("Datatype");
		labelDatatype.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * AbstractParameterPresentation
	 * #createParameterWidget(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createParameterWidget(Composite parent) {
		String function = getStringValue(getValue(), 0);
		String onAttribute = getStringValue(getValue(), 1);
		String newAttribute = getStringValue(getValue(), 2);
		String datatype = getStringValue(getValue(), 3);

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(4, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		EntryModifyListener ml = new EntryModifyListener();

		@SuppressWarnings("rawtypes")
		Class<? extends IStreamObject> datamodel = getOperator()
				.getInputSchemas().get(0).getType();

		functionCombo = new Combo(container, SWT.BORDER | SWT.DROP_DOWN);
		int funcSelect = 0;
		functionCombo.add("");
		for (String func : Activator.getDefault()
				.getInstalledAggregateFunctions(datamodel)) {
			functionCombo.add(func);
			if (func.equals(function)) {
				funcSelect = functionCombo.getItemCount() - 1;
			}
		}
		functionCombo.select(funcSelect);
		functionCombo.addModifyListener(ml);

		onAttributeCombo = new Combo(container, SWT.BORDER | SWT.DROP_DOWN);
		int onSelect = 0;
		onAttributeCombo.add("");
		if (getOperator().getInputSchemas() != null
				&& getOperator().getInputSchemas().get(port) != null) {
			for (SDFAttribute posVal : getOperator().getInputSchemas()
					.get(port).getAttributes()) {
				onAttributeCombo.add(posVal.getAttributeName());
				if (posVal.getAttributeName().equals(onAttribute)) {
					onSelect = onAttributeCombo.getItemCount() - 1;
				}
			}
		} else {
			if (onAttribute != null) {
				onAttributeCombo.add(onAttribute);
				onSelect = 1;
			}
		}
		onAttributeCombo.select(onSelect);
		onAttributeCombo.addModifyListener(ml);

		newAttributeText = new Text(container, SWT.BORDER);
		newAttributeText.setText(newAttribute);
		newAttributeText.addModifyListener(ml);
		newAttributeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));

		comboDatatype = new Combo(container, SWT.BORDER | SWT.DROP_DOWN);
		int dataTypeSelect = 0;
		comboDatatype.add("");
		for (SDFDatatype dt : Activator.getDefault().getInstalledDatatypes()) {
			comboDatatype.add(dt.getQualName());
			if (dt.getQualName().equals(datatype)) {
				dataTypeSelect = comboDatatype.getItemCount() - 1;
			}
		}
		comboDatatype.select(dataTypeSelect);
		comboDatatype.addModifyListener(ml);

		return container;
	}

	private class EntryModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			String function = functionCombo.getText();
			String onAttribute = onAttributeCombo.getText();
			String newAttribute = newAttributeText.getText();
			String datatype = comboDatatype.getText();

			if (newAttribute.isEmpty()) {
				if (!function.isEmpty() && !onAttribute.isEmpty()) {
					newAttribute = function + "_" + onAttribute;
					newAttributeText.setText(newAttribute);
				}
			}

			if (!function.isEmpty() && !onAttribute.isEmpty()) {
				ArrayList<String> aggItem = new ArrayList<>();
				aggItem.add(function);
				aggItem.add(onAttribute);
				aggItem.add(newAttribute);
				if (!datatype.isEmpty()) {
					aggItem.add(datatype);
				}
				setValue(aggItem);
			} else {
				setValue(null);
			}
		}
	}

	@Override
	public boolean hasValidValue() {
		if (getValue() == null) {
			return false;
		}

		if (getValue().isEmpty()) {
			return false;
		}

		return true;
	}

}
