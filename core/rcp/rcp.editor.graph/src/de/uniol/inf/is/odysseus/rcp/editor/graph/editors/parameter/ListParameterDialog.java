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

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;

/**
 * @author DGeesen
 * 
 */
public class ListParameterDialog extends TitleAreaDialog {

	private LogicalParameterInformation logicalParameterInfos;
	private List<Object> parameters;
	private ParameterPresentationFactory factory = new ParameterPresentationFactory();

	/**
	 * @param shell
	 * @param childs
	 */
	public ListParameterDialog(Shell shell, LogicalParameterInformation lpi, List<Object> values) {
		super(shell);
		this.logicalParameterInfos = lpi;
		this.parameters = values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
//		parent.setLayoutData(new GridData(org.eclipse.draw2d.GridData.FILL_BOTH));
//		GridLayout layout = new GridLayout(1, true);
//		parent.setLayout(layout);		
		final Composite area = new Composite(parent, SWT.None);
		area.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true));
		area.setLayout(new GridLayout(1, false));

		for (Object e : this.parameters) {
			IParameterPresentation presenter = factory.createPresentation(logicalParameterInfos);
			presenter.createWidget(area, logicalParameterInfos, e);
		}
		Composite buttons = new Composite(parent, SWT.None);
		buttons.setLayout(new GridLayout(2, true));
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		buttons.setLayoutData(gd);
		Button addButton = new Button(buttons, SWT.PUSH);
		addButton.setText("Add");
		
		addButton.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				IParameterPresentation presenter = factory.createPresentation(logicalParameterInfos);
				presenter.createWidget(area, logicalParameterInfos, null);
			}
		});
		
		Button delButton = new Button(buttons, SWT.PUSH);
		delButton.setText("Remove");
		
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {		
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

}
