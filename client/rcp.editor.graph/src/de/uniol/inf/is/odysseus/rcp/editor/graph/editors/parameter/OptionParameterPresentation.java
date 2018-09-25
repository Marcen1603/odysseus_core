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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Pair;

/**
 * @author DGeesen
 * 
 */
public class OptionParameterPresentation extends AbstractParameterPresentation<Pair<String, String>> {

	private Text textKey;
	private Text textValue;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getPQLString()
	 */
	@Override
	public String getPQLString() {
		if (getValue() != null) {
			return "['" + getValue().getE1() + "', '" + getValue().getE2() + "']";
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#saveValueToXML(org.w3c.dom.Node, org.w3c.dom.Document)
	 */
	@Override
	public void saveValueToXML(Node parent, Document builder) {
		Element a = builder.createElement("key");
		a.setTextContent(getValue().getE1());
		parent.appendChild(a);

		Element d = builder.createElement("value");
		d.setTextContent(getValue().getE2());
		parent.appendChild(d);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#loadValueFromXML(org.w3c.dom.Node)
	 */
	@Override
	public void loadValueFromXML(Node parent) {
		NodeList childs = parent.getChildNodes();
		String key = null;
		String value = null;
		for (int i = 0; i < childs.getLength(); i++) {
			if (childs.item(i) instanceof Element) {
				Element el = (Element) childs.item(i);
				if (el.getNodeName().equals("key")) {
					key = el.getTextContent();
				}
				if (el.getNodeName().equals("value")) {
					value = el.getTextContent();
				}
			}
		}

		if (key != null && value != null) {
			Pair<String, String> pair = new Pair<String, String>(key, value);
			setValue(pair);
		}

	}

	@Override
	protected Control createParameterWidget(Composite parent) {
		String key = "";
		String value = "";
		if (getValue() != null) {
			key = getValue().getE1();
			value = getValue().getE2();
		}
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		PairModifyListener pml = new PairModifyListener();

		textKey = new Text(container, SWT.BORDER);
		textKey.setText(key);
		textKey.addModifyListener(pml);
		textKey.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		textValue = new Text(container, SWT.BORDER);
		textValue.setText(value);
		textValue.addModifyListener(pml);
		textValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		return container;
	}

	@Override
	public Control createHeaderWidget(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label labelName = new Label(container, SWT.None);
		labelName.setText("Key");
		labelName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label labelDatatype = new Label(container, SWT.None);
		labelDatatype.setText("Value");
		labelDatatype.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		return container;
	}

	private class PairModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			String key = "";
			String value = "";
			if (textKey.getText() != null && !textKey.getText().isEmpty()) {
				key = textKey.getText();
				value = textValue.getText();
			}
			if (!key.isEmpty()) {
				Pair<String, String> pair = new Pair<String, String>(key, value);
				setValue(pair);
			}
		}
	}

}
