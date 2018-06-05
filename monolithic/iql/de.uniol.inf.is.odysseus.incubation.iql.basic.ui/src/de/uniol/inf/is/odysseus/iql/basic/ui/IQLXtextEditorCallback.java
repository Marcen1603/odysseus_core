package de.uniol.inf.is.odysseus.iql.basic.ui;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.xtext.builder.nature.ToggleXtextNatureAction;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.validation.ValidatingEditorCallback;



@SuppressWarnings("restriction")
public class IQLXtextEditorCallback extends ValidatingEditorCallback implements IPartListener2{

	@Inject
	private ToggleXtextNatureAction toggleNature;

	@Override
	public void afterCreatePartControl(XtextEditor editor) {
		super.afterCreatePartControl(editor);
		IResource resource = editor.getResource();
		if (resource != null) {
			IProject project = resource.getProject();
			if (project != null && project.isAccessible() && !project.isHidden() && !toggleNature.hasNature(project)) {
				toggleNature.toggleNature(project);
			}			
		}
	}
	


	@Override
	public void beforeSetInput(XtextEditor xtextEditor) {
		super.beforeSetInput(xtextEditor);
		xtextEditor.getEditorSite().getPage().addPartListener(this);
	}
	
	@Override
	public void afterSetInput(XtextEditor xtextEditor) {
		super.afterSetInput(xtextEditor);
	}
	

	@Override
	public void afterSave(XtextEditor editor) {
		super.afterSave(editor);
	}

	@Override
	public void beforeDispose(XtextEditor editor) {
		super.beforeDispose(editor);
		editor.getEditorSite().getPage().removePartListener(this);
	}

	@Override
	public boolean onValidateEditorInputState(XtextEditor editor) {
		return super.onValidateEditorInputState(editor);
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {

	}


	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		
	}
}
