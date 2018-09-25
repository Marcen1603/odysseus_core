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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.rcp.editor.graph.Activator;

/**
 * @author DGeesen
 * 
 */
public class CreateSDFAttributeParameterPresentation extends AbstractParameterPresentation<Pair<String, SDFDatatype>> {

	private Text textName;	
	private Combo combo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getPQLString(de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public String getPQLString() {
		
		SDFDatatype datatype = getValue().getE2();
		String attributeFullName = getValue().getE1();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("'").append(attributeFullName).append("', '").append(datatype.getQualName()).append("'");
		sb.append("]");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.AbstractParameterPresentation#createParameterWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	protected Control createParameterWidget(Composite parent) {		
		String attributeName = "";
		SDFDatatype attributeDatatype = null;
		if (getValue() != null) {						
			attributeName = getValue().getE1();
			attributeDatatype = getValue().getE2();
		}
		Composite container = new Composite(parent, SWT.NONE);		
		container.setLayout(new GridLayout(2, true));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		PairModifyListener pml = new PairModifyListener();

		textName = new Text(container, SWT.BORDER);
		textName.setText(attributeName);
		textName.addModifyListener(pml);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));		
		
		
		combo = new Combo(container, SWT.BORDER | SWT.DROP_DOWN);
		int select = 0;
		combo.add("");
		for (SDFDatatype dt: Activator.getDefault().getInstalledDatatypes()) {
			combo.add(dt.getQualName());
			combo.setData(dt.getQualName(), dt);
			if (dt.equals(attributeDatatype)) {
				select = combo.getItemCount() - 1;
			}
		}
		combo.select(select);
		combo.addModifyListener(pml);
		return container;
	}
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.AbstractParameterPresentation#getHeaderControl()
	 */
	@Override
	public Control createHeaderWidget(Composite parent) {	
		Composite container = new Composite(parent, SWT.NONE);		
		container.setLayout(new GridLayout(2, true));		
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Label labelName = new Label(container, SWT.None);
		labelName.setText("Attributname");		
		labelName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label labelDatatype = new Label(container, SWT.None);
		labelDatatype.setText("Datatype");		
		labelDatatype.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		return container;
	}

	private class PairModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			SDFDatatype datatype = null;
			if(combo.getText()!=null && !combo.getText().isEmpty()){
				datatype = (SDFDatatype) combo.getData(combo.getText());
			}			
			Pair<String, SDFDatatype> attributePair = new Pair<String, SDFDatatype>(textName.getText(), datatype);
			setValue(attributePair);			
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.AbstractParameterPresentation#saveValueToXML(org.w3c.dom.Node, org.w3c.dom.Document)
	 */
	@Override
	public void saveValueToXML(Node parent, Document builder) {
		Element a = builder.createElement("attributename");
		a.setTextContent(getValue().getE1());
		parent.appendChild(a);
		
		Element d = builder.createElement("datatype");
		d.setTextContent(getValue().getE2().getQualName());
		parent.appendChild(d);		
	}
	
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#loadValueFromXML(org.w3c.dom.Node)
	 */
	@Override
	public void loadValueFromXML(Node parent) {
		NodeList childs = parent.getChildNodes();
		String attributename = null;
		String datatype = null;
		for(int i=0;i<childs.getLength();i++){
			if(childs.item(i) instanceof Element){
				Element el = (Element) childs.item(i);				
				if(el.getNodeName().equals("attributename")){
					attributename = el.getTextContent();
				}				
				if(el.getNodeName().equals("datatype")){
					datatype = el.getTextContent();
				}							
			}
		}
		
		if(attributename!=null && datatype!=null){
			SDFDatatype dt = Activator.getDefault().resolveDatatype(datatype);
			Pair<String, SDFDatatype> pair = new Pair<String, SDFDatatype>(attributename, dt);
			setValue(pair);
		}
		
		
	}
}
