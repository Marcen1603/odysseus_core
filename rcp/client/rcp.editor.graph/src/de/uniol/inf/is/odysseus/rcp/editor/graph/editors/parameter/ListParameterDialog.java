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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

/**
 * @author DGeesen
 * 
 */
public class ListParameterDialog<V> extends TitleAreaDialog {

	private LogicalParameterInformation logicalParameterInfos;
	private List<IParameterPresentation<V>> parameters;
	private OperatorNode operator;

	/**
	 * @param shell
	 * @param childs
	 */
	public ListParameterDialog(Shell shell, LogicalParameterInformation lpi, OperatorNode operator, List<IParameterPresentation<V>> values) {
		super(shell);
		this.logicalParameterInfos = lpi;
		this.parameters = new ArrayList<>(values);
		this.operator = operator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("List Parameters for " + logicalParameterInfos.getName());
		setMessage("Add or remove list parameters of type " + logicalParameterInfos.getParameterClass().getSimpleName());

		Composite area = new Composite(parent, SWT.None);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout gl_area = new GridLayout(1, false);
		gl_area.horizontalSpacing = 0;
		gl_area.verticalSpacing = 0;
		gl_area.marginWidth = 4;
		gl_area.marginHeight = 0;
		area.setLayout(gl_area);
		// create the header
		IParameterPresentation<?> emptyPresenter = ParameterPresentationFactory.createPresentationByClass(logicalParameterInfos, null, null);
		emptyPresenter.createHeaderWidget(area);
		// create the rest
		final ScrolledComposite scroller = new ScrolledComposite(area, SWT.V_SCROLL);
		scroller.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		final Composite container = new Composite(scroller, SWT.NONE);
		scroller.setContent(container);
		scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		scroller.setMinHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);

		for (final IParameterPresentation<?> presenter : this.parameters) {			
			createSubParameterWidget(presenter, container, scroller);
		}
		Composite buttons = new Composite(parent, SWT.None);
		buttons.setLayout(new GridLayout(1, true));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		buttons.setLayoutData(gd);
		Button addButton = new Button(buttons, SWT.PUSH);
		addButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addButton.setText("Add");

		addButton.addSelectionListener(new SelectionAdapter() {			

			@Override
			public void widgetSelected(SelectionEvent e) {
				final IParameterPresentation<V> presenter = ParameterPresentationFactory.createPresentationByClass(logicalParameterInfos, operator, null);
				parameters.add(presenter);
				createSubParameterWidget(presenter, container, scroller);
			}
		});

		relayout(container, scroller);
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.verticalSpacing = 1;
		gl_container.horizontalSpacing = 1;
		gl_container.marginWidth = 0;
		gl_container.marginHeight = 0;
		container.setLayout(gl_container);
		return parent;
	}

	private void createSubParameterWidget(final IParameterPresentation<?> presenter, final Composite parent, final ScrolledComposite scroller){
		final Composite container = new Composite(parent, SWT.None);
		GridLayout layout = new GridLayout(2, false);
		layout.marginBottom = 0;
		layout.marginHeight = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Control widget = presenter.createWidget(container);
		widget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));		
		
		Button delButton = new Button(container, SWT.PUSH);
		delButton.setText("X");
		delButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				parameters.remove(presenter);
				container.dispose();
				relayout(parent, scroller);
			}
		});
		relayout(parent, scroller);
		
	}

	private void relayout(Composite container, ScrolledComposite scroller) {
		scroller.setMinHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		container.layout();
		scroller.layout(true);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		super.okPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.TitleAreaDialog#getInitialSize()
	 */
	@Override
	protected Point getInitialSize() {
		Point p = super.getInitialSize();
		p.y = 500;
		// p.x = 800;
		return p;
	}

	public List<IParameterPresentation<V>> getParameters() {
		return parameters;
	}

}
