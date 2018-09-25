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

import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author DGeesen
 * 
 */
public class ResolvedSDFAttributeParameterPresentation extends AbstractParameterPresentation<String> {

	private Combo combo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * IParameterPresentation#getPQLString()
	 */
	@Override
	public String getPQLString() {
		return "'" + String.valueOf(getValue()) + "'";
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
		parent.setTextContent(String.valueOf(getValue()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * IParameterPresentation#loadValueFromXML(org.w3c.dom.Node)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.
	 * AbstractParameterPresentation
	 * #createParameterWidget(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createParameterWidget(Composite parent) {
		String attribute = getValue();
		combo = new Combo(parent, SWT.BORDER | SWT.DROP_DOWN);
		int select = 0;
		combo.add("");
		if (getOperator().getInputSchemas() != null) {
			for (Entry<Integer, SDFSchema> e : getOperator().getInputSchemas().entrySet()) {
				for (SDFAttribute posVal : e.getValue().getAttributes()) {
					combo.add(posVal.getURI());
					if (posVal.getURI().equals(attribute)) {
						select = combo.getItemCount() - 1;
					}
				}
			}
		} else {
			if (attribute != null) {
				combo.add(attribute);
				select = 1;
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
}
