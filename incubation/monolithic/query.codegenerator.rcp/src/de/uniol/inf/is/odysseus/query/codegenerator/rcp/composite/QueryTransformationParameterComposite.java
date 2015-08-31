package de.uniol.inf.is.odysseus.query.codegenerator.rcp.composite;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.query.codegenerator.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.codegenerator.executor.registry.ExecutorRegistry;
import de.uniol.inf.is.odysseus.query.codegenerator.rcp.window.QueryTransformationWindow;
import de.uniol.inf.is.odysseus.query.codegenerator.target.platform.registry.TargetPlatformRegistry;

public class QueryTransformationParameterComposite extends
		AbstractParamterComposite {

	private Text txtTragetDirectory;
	private Text txtTempDirectory;
	private Text txtOdysseusCode;

	private Button btnChooseTargetDirectory;
	private Button btnChooseTempDirectory;
	private Combo targetPlatform;
	private Combo comboExecutor;

	private Composite inputDirectoryComposite;
	private Composite inputTwoGridComposite;
	private Composite buttonComposite;
	private QueryTransformationWindow window;
	private Shell parentShell;

	public QueryTransformationParameterComposite(
			final QueryTransformationWindow window, Composite parent,
			int style, int windowWidth) {
		super(parent, style);
		this.window = window;
		this.parentShell = parent.getShell();

		GridData contentGridData = new GridData(GridData.FILL_BOTH);
		contentGridData.horizontalAlignment = SWT.FILL;
		contentGridData.widthHint = windowWidth;
		this.setLayoutData(contentGridData);
		setLayout(new GridLayout(1, false));

		inputDirectoryComposite = new Composite(this, SWT.FILL);
		inputDirectoryComposite.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, true, false, 1, 1));

		inputTwoGridComposite = new Composite(this, SWT.FILL);
		inputTwoGridComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		createContent();

		// Create a horizontal separator
		Label separator = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		buttonComposite = new Composite(this, SWT.NONE);
		GridData griDDatabuttonComposite = new GridData(SWT.BEGINNING);
		griDDatabuttonComposite.horizontalAlignment = SWT.FILL;
		buttonComposite.setLayoutData(griDDatabuttonComposite);
		buttonComposite.setLayout(new GridLayout(3, false));

		createButton();

		parent.pack();
		parent.setVisible(true);
	}

	private void createContent() {

		inputDirectoryComposite.setLayout(new GridLayout(3, false));
		inputTwoGridComposite.setLayout(new GridLayout(2, false));

		txtTragetDirectory = createTextFieldWithLabel(inputDirectoryComposite,
				"C:\\Users\\Marc\\Desktop\\target", "Zielverzeichnis");

		btnChooseTargetDirectory = new Button(inputDirectoryComposite, SWT.NONE);
		btnChooseTargetDirectory.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		btnChooseTargetDirectory.setText("...");
		btnChooseTargetDirectory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog dlg = new DirectoryDialog(
						btnChooseTargetDirectory.getShell(), SWT.OPEN);
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null)
					return;
				txtTragetDirectory.setText(path);
			}
		});

		txtTempDirectory = createTextFieldWithLabel(inputDirectoryComposite,
				"C:\\Users\\Marc\\Desktop\\tmp", "Tempverzeichnis");

		btnChooseTempDirectory = new Button(inputDirectoryComposite, SWT.NONE);
		btnChooseTempDirectory.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		btnChooseTempDirectory.setText("...");

		btnChooseTempDirectory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog dlg = new DirectoryDialog(
						btnChooseTargetDirectory.getShell(), SWT.OPEN);
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null)
					return;
				txtTempDirectory.setText(path);
			}
		});

		txtOdysseusCode = createTextFieldWithLabel(inputDirectoryComposite,
				"F:\\Studium\\odysseus", "Odysseus Code");

		Button buttonOdysseusCore = new Button(inputDirectoryComposite,
				SWT.NONE);
		buttonOdysseusCore.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		buttonOdysseusCore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog dlg = new DirectoryDialog(
						btnChooseTargetDirectory.getShell(), SWT.OPEN);
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null)
					return;
				txtOdysseusCode.setText(path);
			}
		});
		buttonOdysseusCore.setText("...");

		targetPlatform = createComboWithLabel(inputTwoGridComposite,
				"Sprache:", TargetPlatformRegistry.getAllTargetPlatform());

		comboExecutor = createComboWithLabel(inputTwoGridComposite,
				"Executor:", ExecutorRegistry.getAllExecutor("JRE"));

	}

	private void createButton() {

		Button oklButton = new Button(buttonComposite, SWT.PUSH);
		oklButton.setText("OK");
		oklButton.setAlignment(SWT.CENTER);
		oklButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		oklButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					checkInputFields();

					TransformationParameter parameter = new TransformationParameter(
							targetPlatform.getText(), txtTempDirectory
									.getText(), txtTragetDirectory.getText(),
							window.getQueryId(), txtOdysseusCode.getText(),
							true, comboExecutor.getText());
					window.startQueryTransformation(parameter);
				} catch (IllegalArgumentException ae) {
					createErrorDialog(ae);
				}
			}
		});

		Button cancelButton = new Button(buttonComposite, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setAlignment(SWT.CENTER);
		cancelButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(buttonComposite, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (!window.getWindow().isDisposed()) {
					window.getWindow().dispose();
				}

			}
		});

	}

	private void checkInputFields() throws IllegalArgumentException {

		if (!checkDirecotry(txtTempDirectory.getText())) {
			throw new IllegalArgumentException(
					"Please check your input\nNo access to the temp folder: "
							+ txtTempDirectory.getText());
		}

		if (!checkDirecotry(txtTragetDirectory.getText())) {
			throw new IllegalArgumentException(
					"Please check your input\nNo access to the target folder: "
							+ txtTragetDirectory.getText());
		}

		if (!checkDirecotry(txtOdysseusCode.getText())) {
			throw new IllegalArgumentException(
					"Please check your input\nNo access to the Odysseus code folder: "
							+ txtOdysseusCode.getText());
		}

	}

	private boolean checkDirecotry(String folder) {
		File folderFile = new File(folder);

		if (folderFile.exists() && folderFile.canRead()) {
			return true;
		} else {
			return false;
		}
	}

	private void createErrorDialog(Throwable t) {
		MessageDialog dialog = new MessageDialog(parentShell, "Error", null,
				t.getLocalizedMessage(), MessageDialog.ERROR,
				new String[] { "ok" }, 0);
		dialog.open();
	}

}
