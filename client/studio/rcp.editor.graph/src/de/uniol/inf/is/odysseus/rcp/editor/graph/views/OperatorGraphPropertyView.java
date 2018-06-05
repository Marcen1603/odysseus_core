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

/**
 * @author DGeesen
 * 
 */
public class OperatorGraphPropertyView extends ViewPart implements Observer {

	private static final int COLOR_DEPRECATED = SWT.COLOR_DARK_GRAY;
	private static final int COLOR_OK = SWT.COLOR_BLACK;
	private static final int COLOR_FAILED = SWT.COLOR_RED;

	private List<IParameterPresentation<?>> widgets = new ArrayList<>();
	private Map<Control, Label> labels = new HashMap<>();

	private Composite parameterContainer;
	private ScrolledComposite parameterScroller;
	private SchemaContainer outputSchema;
	private SchemaContainer inputSchema;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
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
		parameterScroller = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.V_SCROLL);
		parameterScroller.setLayoutData(new FillLayout());
		parameterContainer = new Composite(parameterScroller, SWT.NONE);
		parameterContainer.setLayout(new FillLayout());
		parameterScroller.setContent(parameterContainer);
		parameterScroller.setExpandVertical(true);
		parameterScroller.setExpandHorizontal(true);
		parameterScroller.setMinHeight(parameterContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		tabParameters.setControl(parameterScroller);

		inputSchema = new SchemaContainer();
		inputSchema.createContainer(tabFolder, "Input Schema");

		outputSchema = new SchemaContainer();
		outputSchema.createContainer(tabFolder, "Output Schema");

		tabFolder.setSelection(tabParameters);
		chooseCurrentlySelected();
	}

	private void chooseCurrentlySelected() {
		refreshModel(OperatorGraphSelectionProvider.getInstance().getCurrentlySelected());
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
		chooseCurrentlySelected();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void refreshModel(final OperatorNode node) {
		clearState();

		if (node != null) {
			LogicalOperatorInformation op = node.getOperatorInformation();
			setPartName("Graph Operator Properties (" + op.getOperatorName() + ")");

			TreeMap<LogicalParameterInformation, IParameterPresentation<?>> sortedCopy = new TreeMap<LogicalParameterInformation, IParameterPresentation<?>>(new Comparator<LogicalParameterInformation>() {

				@Override
				public int compare(LogicalParameterInformation o1, LogicalParameterInformation o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			sortedCopy.putAll(node.getParameterValues());

			// populate widgets
			Group requiredGroup = new Group(parameterContainer, SWT.None);
			requiredGroup.setText("Required");
			requiredGroup.setLayout(new GridLayout(2, false));
			Group optionalGroup = new Group(parameterContainer, SWT.None);
			optionalGroup.setText("Optional");
			optionalGroup.setLayout(new GridLayout(2, false));
			for (final Entry<LogicalParameterInformation, IParameterPresentation<?>> param : sortedCopy.entrySet()) {

				Group parentGroup = optionalGroup;
				if (param.getKey().isMandatory()) {
					parentGroup = requiredGroup;
				}

				Label label = new Label(parentGroup, SWT.None);

				if (param.getKey().isMandatory()) {
					parentGroup = requiredGroup;
					setLabelColor(label, param.getValue());
				} else {
					label.setForeground(parameterContainer.getShell().getDisplay().getSystemColor(COLOR_OK));
				}
				if (param.getKey().isDeprecated()) {
					label.setForeground(parameterContainer.getShell().getDisplay().getSystemColor(COLOR_DEPRECATED));
					label.setToolTipText("This value is DEPRECATED! " + param.getKey().getDoc());
				}
				label.setText(param.getKey().getName());
				

				IParameterPresentation<?> widget = param.getValue();
				Control control = widget.createWidget(parentGroup);
				control.setToolTipText(param.getKey().getDoc());

				labels.put(control, label);
				widgets.add(widget);
				if (param.getKey().isDeprecated()) {
					widget.getControl().setEnabled(false);
				}
				widget.addParameterValueChangedListener(new IParameterValueChangeListener() {
					@Override
					public void parameterValueChanged(IParameterPresentation widget) {
						Label label = labels.get(widget.getControl());
						setLabelColor(label, widget);
						saveToOperatorNode(node);
					}
				});

				GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
				gd_dataFolderText.widthHint = 287;
				control.setLayoutData(gd_dataFolderText);
			}

			parameterScroller.setMinHeight(parameterContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
			parameterContainer.layout();
			parameterScroller.layout(true);

			// update the inputSchemas
			updateSchemas(node);
		}
	}

	private void updateSchemas(OperatorNode node) {
		this.inputSchema.updateSchemas(node.getInputSchemas());
		this.outputSchema.updateSchemas(node.getOutputSchemas());

	}

	private void saveToOperatorNode(OperatorNode currentNode) {
		Map<LogicalParameterInformation, IParameterPresentation<?>> parameterValues = new HashMap<>();
		for (IParameterPresentation<?> entry : this.widgets) {
			parameterValues.put(entry.getLogicalParameterInformation(), entry);
		}
		currentNode.setParameterValues(parameterValues);
	}

	private void setLabelColor(Label label, IParameterPresentation<?> value) {
		if (value.getLogicalParameterInformation().isDeprecated()) {
			label.setForeground(parameterContainer.getShell().getDisplay().getSystemColor(COLOR_DEPRECATED));
		} else {
			if (value.getLogicalParameterInformation().isMandatory() && !value.hasValidValue()) {
				label.setForeground(parameterContainer.getShell().getDisplay().getSystemColor(COLOR_FAILED));
			} else {
				label.setForeground(parameterContainer.getShell().getDisplay().getSystemColor(COLOR_OK));
			}
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

	public void opeartorGraphEditorClosed() {
		clearState();
	}

	private void clearState() {
		setPartName("Graph Operator Properties (No operator selected)");
		for (Control c : parameterContainer.getChildren()) {
			c.dispose();
		}
		parameterContainer.layout(true);
		widgets.clear();
		labels.clear();

	}

}
