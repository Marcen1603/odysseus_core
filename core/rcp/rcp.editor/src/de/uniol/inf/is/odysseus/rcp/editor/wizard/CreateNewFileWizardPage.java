package de.uniol.inf.is.odysseus.rcp.editor.wizard;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class CreateNewFileWizardPage extends WizardNewFileCreationPage {

	private String fileDescription;
	private String fileName;

	public CreateNewFileWizardPage(String pageName, IStructuredSelection selection, String fileDescription, String filename) {
		super(pageName, selection);
		setFileDescription(fileDescription);
		setFilename(filename);
		
		setTitle("Create a new " + getFileDescription());
		setDescription("Create a new " + getFileDescription());
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		setFileName(getFilename());
		setPageComplete(validatePage());
	}
	
	private void setFileDescription( String desc ) {
		Assert.isNotNull(desc);
		
		this.fileDescription = desc;
	}
	
	public final String getFileDescription() {
		return this.fileDescription;
	}
	
	private void setFilename( String fileName ) {
		Assert.isNotNull(fileName);
		
		this.fileName = fileName;
	}
	
	public final String getFilename() {
		return this.fileName;
	}
}
