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
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

/**
 * @author DGeesen
 * 
 */
public class StringParameterPresentation extends AbstractParameterPresentation {

	private Text text;
	private Combo combo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.dialogs.parameter.IParameterWidget#createWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public Control createParameterWidget(Composite parent, LogicalParameterInformation parameterInformation, Object currentValue) {
		String currentStr = "";
		if (currentValue != null) {
			currentStr = currentValue.toString();
		}

		if (parameterInformation.getPossibleValues().isEmpty()) {
			text = new Text(parent, SWT.BORDER);
			text.setText(currentStr);
			text.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					setValue(text.getText());
				}
			});
			return text;
		} else {
			combo = new Combo(parent, SWT.BORDER | SWT.DROP_DOWN);
			int select = 0;
			for(String posVal : parameterInformation.getPossibleValues()){
				combo.add(posVal);
				if(posVal.equals(currentValue)){
					select = combo.getItemCount()-1;
				}
			}
			combo.select(select);
			combo.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					setValue(combo.getText());
				}
			});
			return combo;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getPQLString(de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public String getPQLString(LogicalParameterInformation parameterInformation, Object value) {
		return parameterInformation.getName() + "='" + String.valueOf(value) + "'";
	}

}
