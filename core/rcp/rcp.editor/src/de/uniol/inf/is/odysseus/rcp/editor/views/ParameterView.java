/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp.editor.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.ParameterEditorRegistry;
import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parts.OperatorEditPart;


public class ParameterView extends ViewPart implements IViewPart, ISelectionListener, IParameterView, IDataDictionaryListener{

	private static final String ERROR_PREFIX = "- ";
	
	private Composite parent;
	private Composite mainContainer;
	private Composite optionalContainer;
	private Composite mandatoryContainer;
	private Composite errorContainer;
	
	private List<IParameterEditor> openedParameterEditors = new ArrayList<IParameterEditor>();
	private Operator selectedOperator;
	private OperatorEditPart selectedOperatorEditPart;
	private ISelection selection;
	
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(new FillLayout());
				
		OdysseusRCPPlugIn.getExecutor().getDataDictionary().addListener(this);
		updateParameterEditors(true);
	
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}
	
	@Override
	public void setFocus() {

	}

	@Override
	public void dispose() {
		super.dispose();
		OdysseusRCPPlugIn.getExecutor().getDataDictionary().removeListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if( selection != this.selection ) {
			this.selection = selection;
			updateParameterEditors(false);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.view.IParameterView#refresh()
	 */
	@Override
	public void refresh() {
		if( selectedOperator != null && selectedOperatorEditPart != null ) {
			((Operator)selectedOperatorEditPart.getModel()).build();
			
			selectedOperatorEditPart.refresh();
			updateErrorList();
		}
	}
	
	// liefert true zur�ck, wenn ein anderer Operator als zuvor
	// selektiert wurde
	private boolean updateSelection() {
		ISelection selection = getSite().getWorkbenchWindow().getSelectionService().getSelection();
		if( selection instanceof IStructuredSelection ) {
			IStructuredSelection structSelection = (IStructuredSelection)selection;
			
			if( structSelection.size() == 1 ) {
			
				Object selectedObject = structSelection.getFirstElement();
				if( selectedObject instanceof OperatorEditPart ) {
					Operator selectedOperator = (Operator)((OperatorEditPart)selectedObject).getModel();
					if( selectedOperator != this.selectedOperator ) {
						this.selectedOperator = selectedOperator;
						selectedOperatorEditPart = ((OperatorEditPart)selectedObject);
						return true;
					}
					return false;
				}
			}
		}
		// war vorher was ausgew�hlt, dann hat sich etwas ge�ndert,
		// sonst nix
		boolean result = selectedOperator != null;
		selectedOperator = null;
		selectedOperatorEditPart = null;
		return result;
	}
	
	private void updateParameterEditors( boolean force) {
		if( force || updateSelection() ) {
			deleteControls();
			createControls();
		}
	}

	private void createControls() {		
		if( selectedOperator == null ) 
			return;
		
		IOperatorBuilder builder = selectedOperator.getOperatorBuilder();
		Set<IParameter<?>> parameters = builder.getParameters();
		
		mainContainer = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 1;
		mainContainer.setLayout(layout);
		Label title = new Label( mainContainer, SWT.BORDER | SWT.CENTER);
		title.setText(selectedOperator.getOperatorBuilderName());
		title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		mandatoryContainer = new Composite( mainContainer, SWT.BORDER);
		mandatoryContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout mandatoryLayout = new GridLayout();
		mandatoryLayout.verticalSpacing = 1;
		mandatoryContainer.setLayout(mandatoryLayout);
		
		optionalContainer = new Composite( mainContainer, SWT.BORDER);
		optionalContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout optionalLayout = new GridLayout();
		optionalLayout.verticalSpacing = 1;
		optionalContainer.setLayout(optionalLayout);
		
		errorContainer = new Composite( mainContainer, SWT.BORDER);
		errorContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		errorContainer.setLayout(new GridLayout());
		
		for( IParameter<?> parameter : parameters) {

			Composite editorParent;
			if( parameter.getRequirement() == REQUIREMENT.MANDATORY )
				editorParent = new Composite(mandatoryContainer, SWT.NONE);
			else
				editorParent = new Composite(optionalContainer, SWT.NONE);
			
			editorParent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			editorParent.setLayout(new FillLayout());
			
			String editorName = selectedOperator.getOperatorBuilderName() + 
				ParameterEditorRegistry.NAME_SEPARATOR + 
				parameter.getName();
			
			if( ParameterEditorRegistry.getInstance().exists(editorName)) {
				IParameterEditor editor = ParameterEditorRegistry.getInstance().create(editorName);
				editor.init(builder, parameter, this);
				editor.createControl(editorParent);
				openedParameterEditors.add(editor);
			} else {
				Label label = new Label(editorParent, SWT.NONE);
				label.setText("No ParameterEditor for " + parameter.getName());
			}
		}
		
		// Container unsichtbar, wenn darin keine Controls sind
		if( mandatoryContainer.getChildren().length == 0 ) {
			mandatoryContainer.setVisible(false);
		}
		if( optionalContainer.getChildren().length == 0 ) {
			optionalContainer.setVisible(false);
		}
		
		updateErrorList();
		
//		parent.pack();
		parent.layout();
	}
	
	private void deleteControls() {
		for( IParameterEditor editor : openedParameterEditors) {
			editor.close();
		}
		openedParameterEditors.clear();
		
		if( mainContainer != null ) 
			mainContainer.dispose();
	}
	
	private void updateErrorList() {
		for( Control ctrl : errorContainer.getChildren() ) {
			ctrl.dispose();
		}
		
		List<Exception> errors = selectedOperator.getOperatorBuilder().getErrors();
		if( errors.size() == 0) 
			errorContainer.setVisible(false);
		else {
			for( Exception ex : errors ) {
				Label label = new Label( errorContainer, SWT.WRAP);
				label.setLayoutData(new GridData(GridData.FILL_BOTH));
				label.setForeground(ColorConstants.red);
				label.setText(ERROR_PREFIX + ex.getMessage());
			}
			errorContainer.layout();
			mainContainer.layout();
			errorContainer.setVisible(true);
		}
	}

	@Override
	public void layout() {
		this.mainContainer.layout();
		this.mandatoryContainer.layout();
		this.optionalContainer.layout();
		this.errorContainer.layout();
		this.parent.layout();
	}

	@Override
	public void addedViewDefinition(AbstractDataDictionary sender, String name, ILogicalOperator op) {
		updateParameterEditors(true);
	}

	@Override
	public void removedViewDefinition(AbstractDataDictionary sender, String name, ILogicalOperator op) {
		updateParameterEditors(true);
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		updateParameterEditors(true);		
	}
}
