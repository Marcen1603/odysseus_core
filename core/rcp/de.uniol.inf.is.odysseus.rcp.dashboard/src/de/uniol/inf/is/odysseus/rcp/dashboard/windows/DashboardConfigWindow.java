package de.uniol.inf.is.odysseus.rcp.dashboard.windows;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;

public class DashboardConfigWindow extends TitleAreaDialog {

	private static final String TITLE = "Dashboard settings";
	private static final String DEFAULT_MESSAGE = "Change settings of this dashboard";

	private Text imageText;
	private IFile selectedImageFile;
	private boolean isDashboardLocked;

	public DashboardConfigWindow(Shell parentShell, Dashboard dashboard) {
		super(parentShell);

		Preconditions.checkNotNull(dashboard, "Dashboard to configure must not be null!");
		selectedImageFile = dashboard.getBackgroundImageFilename();
		isDashboardLocked = dashboard.isLocked();
	}

	public IFile getBackgroundImageFile() {
		return selectedImageFile;
	}
	
	public boolean isDasboardLocked() {
		return isDashboardLocked;
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
		Composite tableComposite = createTopComposite(parent);
		
		createLabel(tableComposite, "Background Image");
		createImageText(tableComposite);
		createSelectImageButton(tableComposite);
		
		createLabel(tableComposite, "Locked");
		final Combo comboDropDown = createComboDropDown(tableComposite, isDashboardLocked);
		comboDropDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isDashboardLocked = comboDropDown.getSelectionIndex() == 0;
			}
		});

		tableComposite.pack();
		return super.createDialogArea(parent);
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

	private void createSelectImageButton(Composite tableComposite) {
		Button selectImageButton = new Button(tableComposite, SWT.PUSH);
		selectImageButton.setImage(DashboardPlugIn.getImageManager().get("selectImage"));
		selectImageButton.setToolTipText("Select image from workspace");
		selectImageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectImageFile();
			}
		});
	}

	private void createImageText(Composite tableComposite) {
		imageText = new Text(tableComposite, SWT.SINGLE | SWT.BORDER);
		if (selectedImageFile != null) {
			imageText.setText(selectedImageFile.getFullPath().toString());
		}
		imageText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private void selectImageFile() {
		IFile selectedFile = selectImageFileWithDialog(getShell());
		if (selectedFile != null) {
			imageText.setText(selectedFile.getFullPath().toString());
			selectedImageFile = selectedFile;
		} else {
			selectedImageFile = null;
			imageText.setText("");
		}
	}
	
	private static Combo createComboDropDown(Composite tableComposite, boolean isLocked) {
		Combo comboDropDown = new Combo(tableComposite, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		comboDropDown.add("true");
		comboDropDown.add("false");
		comboDropDown.select(isLocked ? 0 : 1);
		comboDropDown.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return comboDropDown;
	}

	private static Composite createTopComposite(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableComposite.setLayout(new GridLayout(3, false));
		return tableComposite;
	}

	private static void createLabel(Composite tableComposite, String text) {
		Label imageLabel = new Label(tableComposite, SWT.NONE);
		imageLabel.setText(text);
	}

	private static IFile selectImageFileWithDialog(Shell shell) {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(), new ImageContentProvider());
		dialog.setTitle("Tree Selection");
		dialog.setMessage("Select the elements from the tree:");
		dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
		if (dialog.open() == Window.OK) {
			return getFirstSelectedImageFile(dialog.getResult());
		}
		return null;
	}

	private static IFile getFirstSelectedImageFile(Object[] selectedResources) {
		if (selectedResources != null) {
			Object firstResource = selectedResources[0];
			if (firstResource instanceof IFile) {
				return (IFile) firstResource;
			}
		}
		return null;
	}
}
