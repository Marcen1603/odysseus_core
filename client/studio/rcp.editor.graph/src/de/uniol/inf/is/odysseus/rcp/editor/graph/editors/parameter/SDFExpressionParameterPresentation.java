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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author DGeesen
 * 
 */
public class SDFExpressionParameterPresentation extends AbstractParameterPresentation<Pair<String, String>> {

	private int port = 0;
	private Text textName;
	private Combo combo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * IParameterPresentation
	 * #getPQLString(de.uniol.inf.is.odysseus.core.logicaloperator
	 * .LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public String getPQLString() {

		String expression = getValue().getE1();
		String name = getValue().getE2();

		if (name == null) {
			return "'" + expression + "'";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("'").append(expression).append("', '").append(name).append("'");
		sb.append("]");
		return sb.toString();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * AbstractParameterPresentation
	 * #createParameterWidget(org.eclipse.swt.widgets.Composite,
	 * de.uniol.inf.is.
	 * odysseus.core.logicaloperator.LogicalParameterInformation,
	 * java.lang.Object)
	 */
	@Override
	protected Control createParameterWidget(Composite parent) {
		String expression = "";
		String name = "";
		if (getValue() != null) {
			expression = getValue().getE1();
			name = getValue().getE2();
		}
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		PairModifyListener pml = new PairModifyListener();

		combo = new Combo(container, SWT.BORDER | SWT.DROP_DOWN);
		if (getOperator().getInputSchemas() != null && getOperator().getInputSchemas().get(port) != null) {
			for (SDFAttribute posVal : getOperator().getInputSchemas().get(port).getAttributes()) {
				combo.add(posVal.getURI());
			}
		}
		combo.setText(expression);

		combo.addModifyListener(pml);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		textName = new Text(container, SWT.BORDER);
		textName.setText(name);
		textName.addModifyListener(pml);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * AbstractParameterPresentation#getHeaderControl()
	 */
	@Override
	public Control createHeaderWidget(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label labelName = new Label(container, SWT.None);
		labelName.setText("Expression");
		labelName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label labelDatatype = new Label(container, SWT.None);
		labelDatatype.setText("Name (Optional)");
		labelDatatype.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		return container;
	}

	private class PairModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			Pair<String, String> attributePair = new Pair<String, String>(combo.getText(), textName.getText());
			setValue(attributePair);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * AbstractParameterPresentation#saveValueToXML(org.w3c.dom.Node,
	 * org.w3c.dom.Document)
	 */
	@Override
	public void saveValueToXML(Node parent, Document builder) {
		Element a = builder.createElement("expression");
		a.setTextContent(getValue().getE1());
		parent.appendChild(a);

		Element d = builder.createElement("name");
		d.setTextContent(getValue().getE2());
		parent.appendChild(d);
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
		String expression = null;
		String name = null;
		for (int i = 0; i < childs.getLength(); i++) {
			if (childs.item(i) instanceof Element) {
				Element el = (Element) childs.item(i);
				if (el.getNodeName().equals("expression")) {
					expression = el.getTextContent();
				}
				if (el.getNodeName().equals("name")) {
					name = el.getTextContent();
				}
			}
		}

		if (expression != null && name != null) {
			Pair<String, String> pair = new Pair<String, String>(expression, name);
			setValue(pair);
		}

	}
}
