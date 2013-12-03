package de.uniol.inf.is.odysseus.rcp.dashboard.windows;

import java.util.List;

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
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.DashboardSettings;

public class DashboardConfigWindow extends TitleAreaDialog {

	private static final List<String> ACCEPTED_IMAGE_FILE_EXTENSIONS = new ImmutableList.Builder<String>()
			.add("png")
			.add("jpg")
			.add("gif")
			.build();
		
	private static final String WINDOW_TITLE = "Configure Dashboard";
	private static final String DISPLAY_TITLE = "Dashboard settings";
	private static final String DEFAULT_MESSAGE = "Change settings of";

	private final String dashboardName; 
	
	private Text imageText;
	private IFile selectedImageFile;
	private boolean isDashboardLocked;
	private boolean isBackgroundImageStretched;

	public DashboardConfigWindow(Shell parentShell, Dashboard dashboard, String dashboardName) {
		super(parentShell);

		Preconditions.checkNotNull(dashboard, "Dashboard to configure must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(dashboardName), "Name of dashboard to show must not be null or empty!");
		
		this.dashboardName = dashboardName;
		
		importDashboardSettings(dashboard.getSettings());
	}

	public IFile getBackgroundImageFile() {
		return selectedImageFile;
	}

	public boolean isDasboardLocked() {
		return isDashboardLocked;
	}
	
	public boolean isBackgroundImageStretched() {
		return isBackgroundImageStretched;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle(DISPLAY_TITLE + ": " + dashboardName);
		getShell().setText(WINDOW_TITLE);
		setMessage(DEFAULT_MESSAGE + " " + dashboardName);
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite tableComposite = createTopComposite(parent);
		
		createImageSettingContent(tableComposite);
		createImageStretchSettingContent(tableComposite);
		createLockSettingContent(tableComposite);
		
		tableComposite.pack();
		return tableComposite;
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

	private void createLockSettingContent(Composite tableComposite) {
		createLabel(tableComposite, "Locked");
		final Combo comboLocked = createBooleanComboDropDown(tableComposite, isDashboardLocked);
		comboLocked.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isDashboardLocked = comboLocked.getSelectionIndex() == 0;
			}
		});
	}

	private void createImageStretchSettingContent(Composite tableComposite) {
		createLabel(tableComposite, "Streched");
		final Combo comboStretched = createBooleanComboDropDown(tableComposite, isBackgroundImageStretched );
		comboStretched.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isBackgroundImageStretched = comboStretched.getSelectionIndex() == 0;
			}
		});
		createDummyComposite(tableComposite);
	}

	private void createImageSettingContent(Composite tableComposite) {
		createLabel(tableComposite, "Background Image");
		createImageText(tableComposite);
		createImageButtons(tableComposite);
	}

	private void importDashboardSettings(DashboardSettings dashboardSettings) {
		selectedImageFile = dashboardSettings.getBackgroundImageFile();
		isDashboardLocked = dashboardSettings.isLocked();
		isBackgroundImageStretched = dashboardSettings.isBackgroundImageStretched();
	}

	private void createImageButtons(Composite tableComposite) {
		Composite imageButtonsComposite = new Composite(tableComposite, SWT.NONE);
		imageButtonsComposite.setLayout(new GridLayout(2, true));

		createImageSelectButton(imageButtonsComposite);
		createImageResetButton(imageButtonsComposite);
	}

	private void createImageResetButton(Composite imageButtonsComposite) {
		Button clearImageButton = new Button(imageButtonsComposite, SWT.PUSH);
		clearImageButton.setImage(DashboardPlugIn.getImageManager().get("resetImage"));
		clearImageButton.setToolTipText("Clear image selection");
		clearImageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetImageSelection();
			}
		});
	}

	private void resetImageSelection() {
		imageText.setText("");
		selectedImageFile = null;
	}

	private void createImageSelectButton(Composite imageButtonsComposite) {
		Button selectImageButton = new Button(imageButtonsComposite, SWT.PUSH);
		selectImageButton.setImage(DashboardPlugIn.getImageManager().get("selectImage"));
		selectImageButton.setToolTipText("Select image from workspace");
		selectImageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectImageFile();
			}
		});
	}

	private void selectImageFile() {
		IFile selectedFile = selectImageFileWithDialog(getShell());
		if (selectedFile != null) {
			imageText.setText(selectedFile.getFullPath().toString());
			selectedImageFile = selectedFile;
		}
	}

	private void createImageText(Composite tableComposite) {
		imageText = new Text(tableComposite, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		if (selectedImageFile != null) {
			imageText.setText(selectedImageFile.getFullPath().toString());
		}
		imageText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private static void createDummyComposite(Composite tableComposite) {
		new Composite(tableComposite, SWT.NONE);
	}

	private static Combo createBooleanComboDropDown(Composite tableComposite, boolean isSetToTrue) {
		Combo comboDropDown = new Combo(tableComposite, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		comboDropDown.add("true");
		comboDropDown.add("false");
		comboDropDown.select(isSetToTrue ? 0 : 1);
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
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(shell, new WorkbenchLabelProvider(), new FileContentProvider(ACCEPTED_IMAGE_FILE_EXTENSIONS));
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
