package de.uniol.inf.is.odysseus.rcp.dashboard.windows;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;

public class DashboardConfigWindow extends TitleAreaDialog {

	private static final String TITLE = "Dashboard settings";

	private static final String DEFAULT_MESSAGE = "Change settings of this dashboard";

	private Text imageText;
	
	private IFile selectedImageFilename;
	
	public DashboardConfigWindow(Shell parentShell, Dashboard dashboard) {
		super(parentShell);

		Preconditions.checkNotNull(dashboard, "Dashboard to configure must not be null!");
		this.selectedImageFilename = dashboard.getBackgroundImageFilename();
	}

	public IFile getBackgroundImageFilename() {
		return selectedImageFilename;
	}
	
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle(TITLE);
		setMessage(DEFAULT_MESSAGE);
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableComposite.setLayout(new GridLayout(3, false));

		Label imageLabel = new Label(tableComposite, SWT.NONE);
		imageLabel.setText("Background Image");

		imageText = new Text(tableComposite, SWT.SINGLE | SWT.BORDER);
		if( selectedImageFilename != null ) {
			imageText.setText(selectedImageFilename.getFullPath().toString());
		}
		GridData imageTextLayoutData = new GridData();
		imageTextLayoutData.grabExcessHorizontalSpace = true;
		imageTextLayoutData.minimumWidth = 200;
		imageText.setLayoutData(imageTextLayoutData);

		Button selectImageButton = new Button(tableComposite, SWT.PUSH);
		selectImageButton.setImage(DashboardPlugIn.getImageManager().get("selectImage"));
		selectImageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IFile selectedFile = selectImageFile(getShell());
				if( selectedFile != null ) {
					imageText.setText(selectedFile.getFullPath().toString());
					selectedImageFilename = selectedFile;
				} else {
					selectedImageFilename = null;
					imageText.setText("");
				}
			}
		});

		tableComposite.pack();

		return super.createDialogArea(parent);
	}

	private static IFile selectImageFile(Shell shell) {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider() {
			@Override
			public Object[] getChildren(Object element) {
				if (element instanceof IContainer) {
					try {
						IResource[] members = ((IContainer) element).members();

						List<IResource> result = Lists.newArrayList();
						for (int i = 0; i < members.length; i++) {
							IResource member = members[i];
							if (member instanceof IContainer) {
								result.add(member);
							} else if (member instanceof IFile) {
								IFile f = (IFile) member;
								String extension = f.getFileExtension();
								if ("png".equalsIgnoreCase(extension) || "jpg".equalsIgnoreCase(extension)) {
									result.add(member);
								}
							}
						}

						return result.toArray();
					} catch (CoreException e) {
						return new Object[0];
					}
				} 
				return super.getChildren(element);
			}
		});
		dialog.setTitle("Tree Selection");
		dialog.setMessage("Select the elements from the tree:");
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		if( dialog.open() == Window.OK ) {
			Object[] selectedResources = dialog.getResult();
			if( selectedResources != null ) {
				Object firstResource = selectedResources[0];
				if( firstResource instanceof IFile ) {
					return (IFile)firstResource;
				}
			}
		}
		return null;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button okButton = createButton(parent, IDialogConstants.OK_ID, "OK", true);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.OK);
				close();
			}

		});

		Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.CANCEL);
				close();
			}
		});
	}

}
