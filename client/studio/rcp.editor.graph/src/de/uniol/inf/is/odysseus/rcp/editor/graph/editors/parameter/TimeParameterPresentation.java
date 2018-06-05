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

import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Pair;

/**
 * @author DGeesen
 * 
 */
public class TimeParameterPresentation extends AbstractParameterPresentation<Pair<Integer, String>> {	
	private Combo combo;
	private Spinner sp;

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

		int value = getValue().getE1();
		String unit = getValue().getE2();
		
		if (unit == null || unit.isEmpty()) {
			return Integer.toString(value);
		} 
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("").append(value).append(", '").append(unit).append("'");
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
		int value = 1;
		String unit = "";
		if (getValue() != null) {
			value = getValue().getE1();
			unit = getValue().getE2();
		}
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		PairModifyListener pml = new PairModifyListener();

				
		sp = new Spinner(container, SWT.BORDER);
		sp.setMaximum(Integer.MAX_VALUE);
		sp.setSelection(value);
		sp.addModifyListener(pml);		
		sp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		combo = new Combo(container, SWT.BORDER | SWT.DROP_DOWN);
		combo.add("");
		for(TimeUnit val : TimeUnit.values()){
			combo.add(val.name());
		}		
		combo.setText(unit);
		
		combo.addModifyListener(pml);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		
		
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
		labelName.setText("Value");
		labelName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label labelDatatype = new Label(container, SWT.None);
		labelDatatype.setText("Unit (Optional)");
		labelDatatype.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		return container;
	}

	private class PairModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {					
			if(sp.getSelection()>0){
				Pair<Integer, String> attributePair = new Pair<Integer, String>(sp.getSelection(), combo.getText());
				setValue(attributePair);
			}else{
				setValue(null);
			}
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
		Element a = builder.createElement("value");
		a.setTextContent(Integer.toString(getValue().getE1()));
		parent.appendChild(a);

		Element d = builder.createElement("unit");
		if(getValue().getE2()==null){
			d.setTextContent("");
		}else{
			d.setTextContent(getValue().getE2());
		}
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
		Integer value = null;
		String unit = null;
		for (int i = 0; i < childs.getLength(); i++) {
			if (childs.item(i) instanceof Element) {
				Element el = (Element) childs.item(i);
				if (el.getNodeName().equals("value")) {
					value = Integer.parseInt(el.getTextContent());
				}
				if (el.getNodeName().equals("unit")) {
					unit = el.getTextContent();
					if(unit.equals("null")){
						unit = "";
					}
				}
			}
		}

		if (value != null && unit != null) {			
			Pair<Integer, String> pair = new Pair<Integer, String>(value, unit);
			setValue(pair);
		}

	}
}
