package de.uniol.inf.is.odysseus.rcp.evaluation.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import de.uniol.inf.is.odysseus.rcp.evaluation.QueryTreeSelectionDialog;

public class ChooseQueryFilePage extends WizardPage {


	private Button chooseFileButton;
	private Text inputFile;
	private IResource queryResource;

	protected ChooseQueryFilePage(String pageName) {
		super(pageName);		
		setPageComplete(false);
	}


	@Override
	public void createControl(final Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData((GridData.FILL_BOTH)));
		composite.setLayout(new GridLayout(1, true));
		
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("Choose the query file that should be evaluated");
		inputFile = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		inputFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		chooseFileButton = new Button(composite, SWT.PUSH);
		chooseFileButton.setText("Choose from project");
		chooseFileButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IPath path = getPreviousPage().getContainerFullPath();
				IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
				
				QueryTreeSelectionDialog dialog = new QueryTreeSelectionDialog(parent.getShell(), resource);
				if (dialog.open() == Window.OK) {
					IResource queryResource = (IResource) dialog.getFirstResult();
					inputFile.setText(queryResource.getProjectRelativePath().toString());
					setQueryFile(queryResource);
					setPageComplete(true);
				}
			}

		});
		setControl(composite);

	}

	protected void setQueryFile(IResource queryResource) {
		this.queryResource = queryResource;	
	}
	
	public IResource getQueryFile(){
		return this.queryResource;
	}


	

	@Override
	public WizardNewFileCreationPage getPreviousPage() {	
		return (WizardNewFileCreationPage) super.getPreviousPage();
	}
	
}
