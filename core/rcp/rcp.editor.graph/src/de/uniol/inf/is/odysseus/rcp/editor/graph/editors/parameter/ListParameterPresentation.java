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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

/**
 * @author DGeesen
 * 
 */
public class ListParameterPresentation extends AbstractParameterPresentation {

	private Combo dropDown;	
	private Button editButton;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation#getPQLString(de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@Override
	public String getPQLString(LogicalParameterInformation parameterInformation, Object value) {
		return parameterInformation.getName() + "=[]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.AbstractParameterPresentation#createParameterWidget(org.eclipse.swt.widgets.Composite, de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Control createParameterWidget(final Composite parent, final LogicalParameterInformation parameterInformation, Object currentValue) {
		List<Object> childs = new ArrayList<>();
		if (currentValue != null) {
			childs = (List<Object>) currentValue;
		}
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = 0;
		layout.marginRight = 0;
		container.setLayout(layout);

		dropDown = new Combo(container, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		ParameterPresentationFactory presentationFactory = new ParameterPresentationFactory();
		for (Object child : childs) {
			IParameterPresentation singleParamPresentation = presentationFactory.createPresentation(parameterInformation.getParameterClass());
			dropDown.add(singleParamPresentation.getPQLString(parameterInformation, child));
		}
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		dropDown.setLayoutData(gridData);
		editButton = new Button(container, SWT.PUSH);
		editButton.setText("...");
		
		final List<Object> copiedChilds = new ArrayList<>(childs);
		
		editButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ListParameterDialog dialog = new ListParameterDialog(parent.getShell(), parameterInformation, copiedChilds);
				if(dialog.open()==TitleAreaDialog.OK){
					
				}
			}
		});
		
		
		//
		//
		//
		// textName = new Text(container, SWT.BORDER);
		// textName.setText(attributeName);
		// textName.addModifyListener(pml);
		//
		// textDatatype = new Text(container, SWT.BORDER);
		// textDatatype.setText(attributeDatatype);
		// textDatatype.addModifyListener(pml);

		return container;
	}
		

}
