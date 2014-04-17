package de.uniol.inf.is.odysseus.rcp.evaluation.editor;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import de.uniol.inf.is.odysseus.rcp.evaluation.QueryTreeSelectionDialog;
import de.uniol.inf.is.odysseus.rcp.evaluation.execution.EvaluationJob;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationModel;
import de.uniol.inf.is.odysseus.rcp.evaluation.model.EvaluationVariable;

public class EvaluationEditorPart extends EditorPart implements IResourceChangeListener, IResourceDeltaVisitor {

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.evaluation.editor.EvaluationEditorPart"; //$NON-NLS-1$
	private Text processingResultFolder;
	private Text plotFolder;
	private FileEditorInput input;
	private boolean dirty = false;
	private EvaluationModel evaluationModel;
	private Button btnActivateLatencyMeasurements;
	private Button btnActivateThroughputMeasurments;
	private Text parameterValues;
	private Table parameterTable;
	private TableColumn tblclmnName;
	private CheckboxTableViewer checkboxTableViewer;
	private Text parameterName;
	private Color COLOR_RED;
	private Color COLOR_BLACK;
	private Text queryFileText;
	private Spinner numberOfTimesSpinner;
	private Button btnStartEvaluation;
	private Composite latencyComposite;
	private Composite throughputComposite;
	private Button btnCreatePlotsForThroughputs;
	private Spinner spinnerMeasureEachElements;
	private Button btnCreatePlotsForLatency;

	public EvaluationEditorPart() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(final Composite parent) {

		Display display = Display.getCurrent();
		if (display != null) {
			COLOR_RED = display.getSystemColor(SWT.COLOR_RED);
			COLOR_BLACK = display.getSystemColor(SWT.COLOR_BLACK);
		}

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		Group grpGeneral = new Group(container, SWT.NONE);
		grpGeneral.setLayout(new GridLayout(2, false));
		GridData gd_grpGeneral = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_grpGeneral.heightHint = 132;
		grpGeneral.setLayoutData(gd_grpGeneral);
		grpGeneral.setText("General");

		Composite composite_1 = new Composite(grpGeneral, SWT.NONE);
		composite_1.setLayout(new GridLayout(2, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblQueryFile = new Label(composite_1, SWT.NONE);
		lblQueryFile.setText("Query File:");

		queryFileText = new Text(composite_1, SWT.BORDER);
		queryFileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		queryFileText.setText(evaluationModel.getQueryFile().getProjectRelativePath().toOSString());
		queryFileText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		Button btnBrowse_2 = new Button(grpGeneral, SWT.NONE);
		btnBrowse_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				QueryTreeSelectionDialog dialog = new QueryTreeSelectionDialog(parent.getShell(), input.getFile());
				if (dialog.open() == Window.OK) {
					IResource queryResource = (IResource) dialog.getFirstResult();
					String text = queryResource.getProjectRelativePath().toString();
					if (!text.equals(queryFileText.getText())) {
						queryFileText.setText(text);
					}
				}
			}
		});
		btnBrowse_2.setText("Browse...");

		Label lblDiagramFolder = new Label(grpGeneral, SWT.NONE);
		lblDiagramFolder.setText("Folder for processing results");
		new Label(grpGeneral, SWT.NONE);

		processingResultFolder = new Text(grpGeneral, SWT.BORDER);
		processingResultFolder.setText(evaluationModel.getProcessingResultsPath());
		processingResultFolder.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		processingResultFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnBrowse = new Button(grpGeneral, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getFolder(processingResultFolder);
			}
		});
		btnBrowse.setText("Browse...");

		Label lblFolderToStore = new Label(grpGeneral, SWT.NONE);
		lblFolderToStore.setText("Folder for plots");
		new Label(grpGeneral, SWT.NONE);

		plotFolder = new Text(grpGeneral, SWT.BORDER);
		plotFolder.setText(evaluationModel.getPlotFilesPath());
		plotFolder.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		plotFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnBrowse_1 = new Button(grpGeneral, SWT.NONE);
		btnBrowse_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getFolder(plotFolder);
			}
		});
		btnBrowse_1.setText("Browse...");

		Group grpParameters = new Group(container, SWT.NONE);
		GridLayout gl_grpParameters = new GridLayout(1, false);
		grpParameters.setLayout(gl_grpParameters);
		GridData gd_grpParameters = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_grpParameters.heightHint = 101;
		grpParameters.setLayoutData(gd_grpParameters);
		grpParameters.setText("Parameters");

		SashForm sashForm = new SashForm(grpParameters, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite sashLeftCompo = new Composite(sashForm, SWT.NONE);
		sashLeftCompo.setLayout(new GridLayout(1, false));

		Composite tableContainer = new Composite(sashLeftCompo, SWT.NONE);
		tableContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableContainer.setLayout(new FillLayout(SWT.VERTICAL));

		checkboxTableViewer = CheckboxTableViewer.newCheckList(tableContainer, SWT.BORDER | SWT.FULL_SELECTION);
		parameterTable = checkboxTableViewer.getTable();

		parameterTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = parameterTable.getSelectionIndex();
				if (index >= 0) {
					EvaluationVariable var = evaluationModel.getVariables().get(index);
					showVariableValues(var);
				}

				for (TableItem item : parameterTable.getItems()) {
					EvaluationVariable var = ((EvaluationVariable) item.getData());
					if (!var.isActive() == item.getChecked()) {
						var.setActive(item.getChecked());
						setDirty(true);
					}
				}

			}
		});
		parameterTable.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				tblclmnName.setWidth(100);
				Table table = (Table) e.getSource();
				tblclmnName.setWidth(table.getSize().x - 5);
			}
		});

		tblclmnName = new TableColumn(parameterTable, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		checkboxTableViewer.setLabelProvider(new EvaluationVariableContentProvider());
		checkboxTableViewer.setContentProvider(new EvaluationVariableContentProvider());
		checkboxTableViewer.setInput(evaluationModel);
		checkboxTableViewer.setCheckStateProvider(new EvaluationVariableContentProvider());

		Composite sashRightCompo = new Composite(sashForm, SWT.NONE);
		sashRightCompo.setLayout(new GridLayout(1, false));

		Label lblVariableName = new Label(sashRightCompo, SWT.NONE);
		lblVariableName.setSize(187, 146);
		lblVariableName.setText("Name");
		sashForm.setWeights(new int[] { 50, 100 });

		parameterName = new Text(sashRightCompo, SWT.BORDER);
		parameterName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String newName = parameterName.getText();
				parameterName.setForeground(COLOR_BLACK);
				int index = parameterTable.getSelectionIndex();
				if (index >= 0) {
					EvaluationVariable var = evaluationModel.getVariables().get(index);
					if (!newName.equals(var.getName())) {
						if (evaluationModel.parameterNameExists(newName)) {
							parameterName.setForeground(COLOR_RED);
						} else {
							var.setName(newName);
							refreshParameterTables();
							setDirty(true);
						}
					}
				}
			}
		});
		parameterName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblVariableValues = new Label(sashRightCompo, SWT.NONE);
		lblVariableValues.setText("Values");

		parameterValues = new Text(sashRightCompo, SWT.BORDER | SWT.MULTI);
		parameterValues.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String[] content = parameterValues.getText().split(parameterValues.getLineDelimiter());
				int index = parameterTable.getSelectionIndex();
				if (index >= 0) {
					EvaluationVariable var = evaluationModel.getVariables().get(index);
					List<String> newValues = Arrays.asList(content);
					if (!newValues.equals(var.getValues())) {
						var.setValues(newValues);
						refreshParameterTables();
						setDirty(true);
					}
				}
			}
		});
		parameterValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite buttonsParameters = new Composite(grpParameters, SWT.NONE);
		GridLayout gl_buttonsParameters = new GridLayout(2, true);
		gl_buttonsParameters.marginHeight = 0;
		buttonsParameters.setLayout(gl_buttonsParameters);
		GridData gd_buttonsParameters = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_buttonsParameters.heightHint = 66;
		buttonsParameters.setLayoutData(gd_buttonsParameters);

		Button btnAdd = new Button(buttonsParameters, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addParameter();
			}
		});
		btnAdd.setBounds(0, 0, 75, 25);
		btnAdd.setText("Add");

		Button btnRemove = new Button(buttonsParameters, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = parameterTable.getSelectionIndex();
				if (index >= 0) {
					EvaluationVariable var = evaluationModel.getVariables().get(index);
					removeParamater(var);
				}
			}
		});
		btnRemove.setBounds(0, 0, 75, 25);
		btnRemove.setText("Remove");

		Label lblNewLabel = new Label(buttonsParameters, SWT.NONE);
		lblNewLabel.setText("How often repeat one evaluation setting?");

		numberOfTimesSpinner = new Spinner(buttonsParameters, SWT.BORDER);
		numberOfTimesSpinner.setMaximum(10000000);
		numberOfTimesSpinner.setMinimum(1);
		numberOfTimesSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		numberOfTimesSpinner.setSelection(evaluationModel.getNumberOfRuns());
		numberOfTimesSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		Group grpLatency = new Group(container, SWT.NONE);
		grpLatency.setLayout(new GridLayout(3, false));
		grpLatency.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpLatency.setText("Latency");

		btnActivateLatencyMeasurements = new Button(grpLatency, SWT.CHECK);
		GridData gd_btnActivateLatencyMeasurements = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnActivateLatencyMeasurements.widthHint = 150;
		btnActivateLatencyMeasurements.setLayoutData(gd_btnActivateLatencyMeasurements);
		btnActivateLatencyMeasurements.setText("Measure Latencies");
		btnActivateLatencyMeasurements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
				setEnabled(latencyComposite, btnActivateLatencyMeasurements.getSelection());
			}
		});
		btnActivateLatencyMeasurements.setSelection(evaluationModel.isWithLatency());
		new Label(grpLatency, SWT.NONE);

		latencyComposite = new Composite(grpLatency, SWT.NONE);
		latencyComposite.setLayout(new GridLayout(1, false));
		latencyComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		btnCreatePlotsForLatency = new Button(latencyComposite, SWT.CHECK);
		btnCreatePlotsForLatency.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnCreatePlotsForLatency.setText("Create plots for latencies");
		btnCreatePlotsForLatency.setSelection(this.evaluationModel.isCreateLatencyPlots());
		btnCreatePlotsForLatency.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
			}
		});

		Group grpThroughput = new Group(container, SWT.NONE);
		grpThroughput.setLayout(new GridLayout(3, false));
		grpThroughput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		grpThroughput.setText("Throughput");

		btnActivateThroughputMeasurments = new Button(grpThroughput, SWT.CHECK);
		GridData gd_btnActivateThroughputMeasurments = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnActivateThroughputMeasurments.widthHint = 150;
		btnActivateThroughputMeasurments.setLayoutData(gd_btnActivateThroughputMeasurments);
		btnActivateThroughputMeasurments.setText("Measure Throughputs");
		btnActivateThroughputMeasurments.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
				setEnabled(throughputComposite, btnActivateThroughputMeasurments.getSelection());
			}
		});
		btnActivateThroughputMeasurments.setSelection(evaluationModel.isWithThroughput());
		new Label(grpThroughput, SWT.NONE);

		throughputComposite = new Composite(grpThroughput, SWT.NONE);
		throughputComposite.setLayout(new GridLayout(2, true));
		throughputComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblMeasureEachElements = new Label(throughputComposite, SWT.NONE);
		lblMeasureEachElements.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblMeasureEachElements.setText("Measure each x elements");

		spinnerMeasureEachElements = new Spinner(throughputComposite, SWT.BORDER);
		spinnerMeasureEachElements.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		spinnerMeasureEachElements.setMinimum(1);
		spinnerMeasureEachElements.setMaximum(Integer.MAX_VALUE);
		spinnerMeasureEachElements.setSelection(this.evaluationModel.getMeasureThrougputEach());
		spinnerMeasureEachElements.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});

		btnCreatePlotsForThroughputs = new Button(throughputComposite, SWT.CHECK);
		btnCreatePlotsForThroughputs.setText("Create Plots for throughputs");
		btnCreatePlotsForThroughputs.setSelection(evaluationModel.isCreateThroughputPlots());
		btnCreatePlotsForThroughputs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDirty(true);
			}
		});

		new Label(throughputComposite, SWT.NONE);

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		btnStartEvaluation = new Button(composite, SWT.NONE);
		btnStartEvaluation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startEvaluation(parent.getShell());
			}
		});
		btnStartEvaluation.setText("Start Evaluation");

	}

	private void setEnabled(Composite composite, boolean enable) {
		for (Control control : composite.getChildren()) {
			control.setEnabled(enable);
		}
	}

	protected void startEvaluation(Shell shell) {
		if (isDirty()) {
			if (MessageDialog.openConfirm(shell, "Save before?", "Do you want to save before starting?")) {
				doSave(new NullProgressMonitor());
			} else {
				return;
			}
		}

		Job job = new EvaluationJob(evaluationModel);
		job.setUser(true);
		job.schedule();

	}

	protected void removeParamater(EvaluationVariable var) {
		Shell shell = parameterTable.getShell();
		if (MessageDialog.openConfirm(shell, "Delete?", "Do you really want to delete \"" + var.getName() + "\"?")) {
			evaluationModel.getVariables().remove(var);
			refreshParameterTables();
			setDirty(true);
			selectParameter(0);
		}

	}

	protected void showVariableValues(EvaluationVariable var) {
		StringBuilder sb = new StringBuilder();
		for (String value : var.getValues()) {
			sb.append(value);
			sb.append(System.lineSeparator());
		}
		parameterValues.setText(sb.toString());
		parameterName.setText(var.getName());
	}

	private void selectParameter(int index) {
		parameterTable.forceFocus();
		parameterTable.setSelection(index);
		parameterTable.notifyListeners(SWT.Selection, null);
	}

	protected void addParameter() {
		String name = "NEW_PARAMATER";

		if (evaluationModel.parameterNameExists(name)) {
			int i = 1;
			while (evaluationModel.parameterNameExists(name + "_" + i)) {
				i++;
			}
			name = name + "_" + i;
		}
		evaluationModel.getVariables().add(new EvaluationVariable(name));
		refreshParameterTables();
		setDirty(true);
		selectParameter(parameterTable.getItemCount() - 1);
	}

	private void refreshParameterTables() {
		// checkboxTableViewer.setInput(evaluationModel);
		checkboxTableViewer.refresh();
	}

	protected void getFolder(Text text) {
		DirectoryDialog dialog = new DirectoryDialog(text.getShell());
		dialog.setFilterPath(text.getText());
		dialog.setText("Directory");
		dialog.setMessage("Choose a directory");

		String dir = dialog.open();
		if (dir != null) {
			text.setText(dir);
		}

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		evaluationModel.setQueryFile(input.getFile().getProject().findMember(queryFileText.getText()));
		evaluationModel.setPlotFilesPath(this.plotFolder.getText());
		evaluationModel.setProcessingResultsPath(this.processingResultFolder.getText());
		evaluationModel.setWithLatency(btnActivateLatencyMeasurements.getSelection());
		evaluationModel.setWithThroughput(btnActivateThroughputMeasurments.getSelection());
		evaluationModel.setNumberOfRuns(numberOfTimesSpinner.getSelection());
		evaluationModel.setCreateLatencyPlots(btnCreatePlotsForLatency.getSelection());
		evaluationModel.setCreateThroughputPlots(btnCreatePlotsForThroughputs.getSelection());
		evaluationModel.setMeasureThrougputEach(spinnerMeasureEachElements.getSelection());		
		// variables are directly saved
		evaluationModel.save(this.input.getFile());
		setDirty(false);
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.input = (FileEditorInput) input;
		evaluationModel = EvaluationModel.load(this.input.getFile());
		setPartName(this.input.getName());
		setSite(site);
		setInput(input);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	private void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public FileEditorInput getEditorInput() {
		return (FileEditorInput) super.getEditorInput();
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			IResourceDelta delta = event.getDelta();
			try {
				delta.accept(this);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			closeEditor(event.getResource());
		}
	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			if (file.equals(getEditorInput().getFile())) {
				switch (delta.getKind()) {
				case IResourceDelta.REMOVED:
				case IResourceDelta.REPLACED:
					closeEditor(delta.getResource());
					break;
				default:
					break;
				}
				return false;
			}
		}
		return true;
	}

	private void closeEditor(final IResource resource) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
				for (int i = 0; i < pages.length; i++) {
					if ((getEditorInput()).getFile().equals(resource)) {
						IEditorPart editorPart = pages[i].findEditor(getEditorInput());
						pages[i].closeEditor(editorPart, true);
					}
				}
			}
		});
	}
}
