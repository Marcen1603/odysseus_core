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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

/**
 * @author DGeesen
 * 
 */
public class CreateSDFAttributeParameterPresentation extends AbstractParameterPresentation {

	private Text textName;
	private Text textDatatype;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getPQLString(de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public String getPQLString(LogicalParameterInformation parameterInformation, Object value) {
		@SuppressWarnings("unchecked")
		Pair<String, String> attribute = (Pair<String, String>) value;
		String attributeFullName = attribute.getE1();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("'").append(attributeFullName).append("','").append(attribute.getE2()).append("'");
		sb.append("]");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.AbstractParameterPresentation#createParameterWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	protected Control createParameterWidget(Composite parent, LogicalParameterInformation parameterInformation, Object currentValue) {
		String attributeName = "";
		String attributeDatatype = "";
		if (currentValue != null) {
			@SuppressWarnings("unchecked")
			Pair<String, String> attribute = (Pair<String, String>) currentValue;
			attributeName = attribute.getE1();
			attributeDatatype = attribute.getE2();
		}
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));

		PairModifyListener pml = new PairModifyListener();

		textName = new Text(container, SWT.BORDER);
		textName.setText(attributeName);
		textName.addModifyListener(pml);

		textDatatype = new Text(container, SWT.BORDER);
		textDatatype.setText(attributeDatatype);
		textDatatype.addModifyListener(pml);

		return container;
	}

	private class PairModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			Pair<String, String> attributePair = new Pair<String, String>(textName.getText(), textDatatype.getText());
			setValue(attributePair);			
		}
	}
}
