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
package de.uniol.inf.is.odysseus.rcp.editor.graph.views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.OperatorGraphSelectionProvider;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterPresentation;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.IParameterValueChangeListener;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.parameter.ParameterPresentationFactory;

/**
 * @author DGeesen
 * 
 */
public class OperatorGraphPropertyView extends ViewPart implements Observer {
	
	
	
	private List<IParameterPresentation> widgets = new ArrayList<>();
	private Map<Control, Label> labels = new HashMap<>();
	private ParameterPresentationFactory widgetFactory = new ParameterPresentationFactory();
	private Composite container;
	private ScrolledComposite scroller;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		// ((FillLayout)parent.getLayout()).type = SWT.VERTICAL;
		OperatorGraphSelectionProvider.getInstance().addObserver(this);

		final CTabFolder tabFolder = new CTabFolder(parent, SWT.BORDER | SWT.FLAT | SWT.BOTTOM);
		tabFolder.setBorderVisible(false);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tabParameters = new CTabItem(tabFolder, SWT.NONE);
		tabParameters.setText("Parameters");

		scroller = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.V_SCROLL);
		scroller.setLayoutData(new FillLayout());

		container = new Composite(scroller, SWT.NONE);
		container.setLayout(new FillLayout());

		scroller.setContent(container);
		scroller.setExpandVertical(true);
		scroller.setExpandHorizontal(true);
		scroller.setMinHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		
		tabParameters.setControl(scroller);

		CTabItem tabSchema = new CTabItem(tabFolder, SWT.NONE);
		tabSchema.setText("Schema");

		ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tabSchema.setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		tabFolder.setSelection(tabParameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		OperatorGraphSelectionProvider.getInstance().deleteObserver(this);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

	private void refreshModel(final OperatorNode node) {
		setPartName("Graph Operator Properties (No operator selected)");

		for (Control c : container.getChildren()) {
			c.dispose();
		}
		container.layout(true);
		widgets.clear();
		labels.clear();
		
		if (node != null) {
			LogicalOperatorInformation op = node.getOperatorInformation();
			setPartName("Graph Operator Properties (" + op.getOperatorName() + ")");

			TreeMap<LogicalParameterInformation, Object> sortedCopy = new TreeMap<LogicalParameterInformation, Object>(new Comparator<LogicalParameterInformation>() {

				@Override
				public int compare(LogicalParameterInformation o1, LogicalParameterInformation o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			sortedCopy.putAll(node.getParameterValues());

			// populate widgets
			Group requiredGroup = new Group(container, SWT.None);
			requiredGroup.setText("Required");
			requiredGroup.setLayout(new GridLayout(2, false));
			Group optionalGroup = new Group(container, SWT.None);
			optionalGroup.setText("Optional");
			optionalGroup.setLayout(new GridLayout(2, false));
			for (final Entry<LogicalParameterInformation, Object> param : sortedCopy.entrySet()) {

				Group parentGroup = optionalGroup;
				if (param.getKey().isMandatory()) {
					parentGroup = requiredGroup;
				}
				
				
				Label label = new Label(parentGroup, SWT.None);
				
				if (param.getKey().isMandatory()) {
					parentGroup = requiredGroup;
					setLabelColor(label, param.getKey(), param.getValue());
				} else {
					label.setForeground(container.getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK));
				}
				label.setText(param.getKey().getName());
				label.setToolTipText(param.getKey().getDoc());
				
				IParameterPresentation widget = widgetFactory.createPresentation(param.getKey());
				Control control = widget.createWidget(parentGroup, param.getKey(), param.getValue());
				control.setToolTipText(param.getKey().getDoc());
				
				labels.put(control, label);				
				widgets.add(widget);		
				widget.addParameterValueChangedListener(new IParameterValueChangeListener() {					
					@Override
					public void parameterValueChanged(Object newValue, IParameterPresentation widget) {
						Label label = labels.get(widget.getControl());						
						setLabelColor(label, widget.getLogicalParameterInformation(), newValue);
						saveToOperatorNode(node);
					}
				});
						

				GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
				gd_dataFolderText.widthHint = 287;
				control.setLayoutData(gd_dataFolderText);
			}
					
			scroller.setMinHeight(container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
			container.layout();
			scroller.layout(true);
		}
	}
	
	
	private void saveToOperatorNode(OperatorNode currentNode){
		Map<LogicalParameterInformation, Object> parameterValues = new HashMap<>(); 
		for (IParameterPresentation entry : this.widgets) {			
			parameterValues.put(entry.getLogicalParameterInformation(), entry.getValue());
		}
		currentNode.setParameterValues(parameterValues);
	}
	
	
	
	private void setLabelColor(Label label, LogicalParameterInformation lpi, Object value){
		if (lpi.isMandatory() && (value == null || value.toString().isEmpty())) {
			label.setForeground(container.getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		} else {
			label.setForeground(container.getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		OperatorNode operator = (OperatorNode) arg1;
		refreshModel(operator);
	}

}
