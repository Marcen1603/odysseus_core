package de.uniol.inf.is.odysseus.rcp.editor.view;

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

import de.uniol.inf.is.odysseus.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterEditor;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.IParameterView;
import de.uniol.inf.is.odysseus.rcp.editor.parameter.ParameterEditorRegistry;
import de.uniol.inf.is.odysseus.rcp.editor.parts.OperatorEditPart;

public class ParameterViewPart extends ViewPart implements IViewPart, ISelectionListener, IParameterView {

	private static final String ERROR_PREFIX = "- ";
	
	private Composite parent;
	private Composite mainContainer;
	private Composite optionalContainer;
	private Composite mandatoryContainer;
	private Composite errorContainer;
	
	private List<IParameterEditor> openedParameterEditors = new ArrayList<IParameterEditor>();
	private Operator selectedOperator;
	private OperatorEditPart selectedOperatorEditPart;
	
	private static IParameterView instance = null;
	
	public ParameterViewPart() {
	}

	public static IParameterView getInstance() {
		return instance;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		parent.setLayout(new FillLayout());
				
		updateParameterEditors();
	
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		
		instance = this;
	}
	
	@Override
	public void dispose() {
		instance = null;
		super.dispose();
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		updateParameterEditors();
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.editor.view.IParameterView#refresh()
	 */
	@Override
	public void refresh() {
		if( selectedOperator != null && selectedOperatorEditPart != null ) {
			selectedOperatorEditPart.refresh();
			updateErrorList();
		}
	}
	
	private void updateSelection() {
		ISelection selection = getSite().getWorkbenchWindow().getSelectionService().getSelection();
		if( selection instanceof IStructuredSelection ) {
			IStructuredSelection structSelection = (IStructuredSelection)selection;
			
			if( structSelection.size() == 1 ) {
			
				Object selectedObject = structSelection.getFirstElement();
				if( selectedObject instanceof OperatorEditPart ) {
					selectedOperator = (Operator)((OperatorEditPart)selectedObject).getModel();
					selectedOperatorEditPart = ((OperatorEditPart)selectedObject);
					return;
				}
			}
		}
		selectedOperator = null;
		selectedOperatorEditPart = null;
		return;
	}
	
	private void updateParameterEditors() {
		deleteControls();
		updateSelection();
		createControls();
	}

	private void createControls() {		
		if( selectedOperator == null ) 
			return;
		
		IOperatorBuilder builder = selectedOperator.getOperatorBuilder();
		Set<IParameter<?>> parameters = builder.getParameters();
		
		mainContainer = new Composite(parent, SWT.NONE);
		mainContainer.setLayout(new GridLayout());
		Label title = new Label( mainContainer, SWT.BORDER | SWT.CENTER);
		title.setText(selectedOperator.getOperatorBuilderName());
		title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		mandatoryContainer = new Composite( mainContainer, SWT.BORDER);
		mandatoryContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mandatoryContainer.setLayout(new GridLayout());
		
		optionalContainer = new Composite( mainContainer, SWT.BORDER);
		optionalContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		optionalContainer.setLayout(new GridLayout());
		
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
			if( ParameterEditorRegistry.getInstance().exists(parameter.getName())) {
				IParameterEditor editor = ParameterEditorRegistry.getInstance().create(parameter.getName());
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
}
