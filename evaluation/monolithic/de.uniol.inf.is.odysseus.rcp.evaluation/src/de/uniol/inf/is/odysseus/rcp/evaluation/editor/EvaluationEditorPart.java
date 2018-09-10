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
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.widgets.Combo;
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
import de.uniol.inf.is.odysseus.rcp.evaluation.plot.PlotBuilder.OutputType;

public class EvaluationEditorPart extends EditorPart implements IResourceChangeListener, IResourceDeltaVisitor {

    public static final String ID = "de.uniol.inf.is.odysseus.rcp.evaluation.editor.EvaluationEditorPart"; //$NON-NLS-1$
    private Text processingResultFolder;
    private Text plotFolder;
    private FileEditorInput input;
    private boolean dirty = false;
    private EvaluationModel evaluationModel;
    private Button btnActivateLatencyMeasurements;
    private Button btnUseMaxLatencyMeasurements;
    private Button btnActivateThroughputMeasurments;
    private Button btnActivateResourceMeasurments;
    private Text parameterValues;
    private Table parameterTable;
    private TableColumn tblclmnName;
    private CheckboxTableViewer checkboxTableViewer;
    private Text parameterName;
    private Color COLOR_RED;
    private Color COLOR_BLACK;
    private Text setupQueryText;
    private Text queryFileText;
    private Text tearDownQueryText;
    private Button btnSetupTearDownMode;
    private Spinner numberOfTimesSpinner;
    private Button btnStartEvaluation;
    private Composite latencyComposite;
    private Composite throughputComposite;
    private Composite resourceComposite;
    private Button btnCreatePlotsForThroughputs;
    private Spinner spinnerMeasureEachElements;
    private Button btnCreatePlotsForLatency;
    private Button btnCreatePlotsForCPU;
    private Button btnCreatePlotsForMemory;
    private Spinner spinnerWidth;
    private Spinner spinnerHeight;
    private Combo outputTypeCombo;

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
        ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        Composite container = new Composite(scrolledComposite, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        container.setLayout(gridLayout);

        Group grpGeneral = new Group(container, SWT.NONE);
        grpGeneral.setLayout(new GridLayout(4, false));
        // gd_grpGeneral.heightHint = 150;
        grpGeneral.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        grpGeneral.setText("General");

        Composite composite_1 = new Composite(grpGeneral, SWT.NONE);
        composite_1.setLayout(new GridLayout(3, false));
        composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

        Label lblQueryFile = new Label(composite_1, SWT.NONE);
        lblQueryFile.setText("Query File:");

        queryFileText = new Text(composite_1, SWT.BORDER);
        queryFileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        if (evaluationModel.getQueryFile() != null) {
            queryFileText.setText(evaluationModel.getQueryFile().getProjectRelativePath().toOSString());
        }
        queryFileText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        });

        Button btnBrowse_2 = new Button(composite_1, SWT.NONE);
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

        Label lblQuerySetup = new Label(composite_1, SWT.NONE);
        lblQuerySetup.setText("Query Setup File:");

        setupQueryText = new Text(composite_1, SWT.BORDER);
        setupQueryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        if (evaluationModel.getSetupQueryFile() != null) {
        	setupQueryText.setText(evaluationModel.getSetupQueryFile().getProjectRelativePath().toOSString());
        }
        setupQueryText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        });

        Button btnBrowse_3 = new Button(composite_1, SWT.NONE);
        btnBrowse_3.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                QueryTreeSelectionDialog dialog = new QueryTreeSelectionDialog(parent.getShell(), input.getFile());
                if (dialog.open() == Window.OK) {
                    IResource queryResource = (IResource) dialog.getFirstResult();
                    String text = queryResource.getProjectRelativePath().toString();
                    if (!text.equals(setupQueryText.getText())) {
                    	setupQueryText.setText(text);
                    }
                }
            }
        });
        btnBrowse_3.setText("Browse...");

        Label lblQueryTearDown = new Label(composite_1, SWT.NONE);
        lblQueryTearDown.setText("Query Tear Down File:");

        tearDownQueryText = new Text(composite_1, SWT.BORDER);
        tearDownQueryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        if (evaluationModel.getTearDownQueryFile() != null) {
        	tearDownQueryText.setText(evaluationModel.getTearDownQueryFile().getProjectRelativePath().toOSString());
        }
        tearDownQueryText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        });

        Button btnBrowse_4 = new Button(composite_1, SWT.NONE);
        btnBrowse_4.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                QueryTreeSelectionDialog dialog = new QueryTreeSelectionDialog(parent.getShell(), input.getFile());
                if (dialog.open() == Window.OK) {
                    IResource queryResource = (IResource) dialog.getFirstResult();
                    String text = queryResource.getProjectRelativePath().toString();
                    if (!text.equals(tearDownQueryText.getText())) {
                    	tearDownQueryText.setText(text);
                    }
                }
            }
        });
        btnBrowse_4.setText("Browse...");

        // empty label
        new Label(composite_1, SWT.NONE);

        btnSetupTearDownMode = new Button(composite_1, SWT.CHECK);
        btnSetupTearDownMode.setText("Setup/Tear down every Run");
        btnSetupTearDownMode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
            }
        });
        btnSetupTearDownMode.setSelection(evaluationModel.isRunSetupTearDownEveryRun());

        // empty label
        new Label(grpGeneral, SWT.NONE);


        Label lblDiagramFolder = new Label(grpGeneral, SWT.NONE);
        lblDiagramFolder.setText("Folder for processing results");
        new Label(grpGeneral, SWT.NONE);

        Label lblFolderToStore = new Label(grpGeneral, SWT.NONE);
        lblFolderToStore.setText("Folder for plots");
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
        gd_grpParameters.minimumHeight = 200;
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
                        }
                        else {
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

        latencyComposite = new Composite(grpLatency, SWT.NONE);
        latencyComposite.setLayout(new GridLayout(1, false));
        latencyComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        btnActivateLatencyMeasurements.setSelection(evaluationModel.isWithLatency());
        setEnabled(latencyComposite, evaluationModel.isWithLatency());
        new Label(grpLatency, SWT.NONE);

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

        btnUseMaxLatencyMeasurements = new Button(grpLatency, SWT.CHECK);
        btnUseMaxLatencyMeasurements.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        btnUseMaxLatencyMeasurements.setText("Use max latency");
        btnUseMaxLatencyMeasurements.setSelection(this.evaluationModel.isUseMaxLatency());
        btnUseMaxLatencyMeasurements.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
            }
        });

        btnActivateLatencyMeasurements.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
                setEnabled(latencyComposite, btnActivateLatencyMeasurements.getSelection());
                if (!btnActivateLatencyMeasurements.getSelection()) {
                    btnCreatePlotsForLatency.setSelection(btnActivateLatencyMeasurements.getSelection());
                }
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

        throughputComposite = new Composite(grpThroughput, SWT.NONE);
        throughputComposite.setLayout(new GridLayout(2, true));
        throughputComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        btnActivateThroughputMeasurments.setSelection(evaluationModel.isWithThroughput());
        setEnabled(throughputComposite, evaluationModel.isWithThroughput());
        new Label(grpThroughput, SWT.NONE);

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

        btnActivateThroughputMeasurments.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
                setEnabled(throughputComposite, btnActivateThroughputMeasurments.getSelection());
                if (!btnActivateThroughputMeasurments.getSelection()) {
                    btnCreatePlotsForThroughputs.setSelection(btnActivateThroughputMeasurments.getSelection());
                }
            }
        });

        Group grpResource = new Group(container, SWT.NONE);
        grpResource.setLayout(new GridLayout(3, false));
        grpResource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        grpResource.setText("Resource");

        btnActivateResourceMeasurments = new Button(grpResource, SWT.CHECK);
        GridData gd_btnActivateResourceMeasurments = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_btnActivateResourceMeasurments.widthHint = 150;
        btnActivateResourceMeasurments.setLayoutData(gd_btnActivateResourceMeasurments);
        btnActivateResourceMeasurments.setText("Measure Resources");

        resourceComposite = new Composite(grpResource, SWT.NONE);
        resourceComposite.setLayout(new GridLayout(1, true));
        resourceComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        btnActivateResourceMeasurments.setSelection(evaluationModel.isWithResource());
        setEnabled(resourceComposite, evaluationModel.isWithResource());
        new Label(grpResource, SWT.NONE);

        btnCreatePlotsForCPU = new Button(resourceComposite, SWT.CHECK);
        btnCreatePlotsForCPU.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        btnCreatePlotsForCPU.setText("Create Plots for CPU usage");
        btnCreatePlotsForCPU.setSelection(evaluationModel.isCreateCPUPlots());
        btnCreatePlotsForCPU.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
            }
        });

        btnCreatePlotsForMemory = new Button(resourceComposite, SWT.CHECK);
        btnCreatePlotsForMemory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        btnCreatePlotsForMemory.setText("Create Plots for memory");
        btnCreatePlotsForMemory.setSelection(evaluationModel.isCreateMemoryPlots());
        btnCreatePlotsForMemory.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
            }
        });
        btnActivateResourceMeasurments.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
                setEnabled(resourceComposite, btnActivateResourceMeasurments.getSelection());
                if (!btnActivateResourceMeasurments.getSelection()) {
                    btnCreatePlotsForCPU.setSelection(btnActivateResourceMeasurments.getSelection());
                    btnCreatePlotsForMemory.setSelection(btnActivateResourceMeasurments.getSelection());
                }
            }
        });

        Group grpPlotsettings = new Group(container, SWT.NONE);
        grpPlotsettings.setLayout(new GridLayout(6, true));
        grpPlotsettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        grpPlotsettings.setText("Plotsettings");

        Label lblExportType = new Label(grpPlotsettings, SWT.NONE);
        lblExportType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblExportType.setText("Export type:");

        outputTypeCombo = new Combo(grpPlotsettings, SWT.READ_ONLY);
        outputTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        outputTypeCombo.add(OutputType.PDF.toString());
        outputTypeCombo.add(OutputType.PNG.toString());
        outputTypeCombo.add(OutputType.JPEG.toString());
        outputTypeCombo.add(OutputType.GNUPLOT.toString());

        outputTypeCombo.select(outputTypeCombo.indexOf(evaluationModel.getOutputType()));
        outputTypeCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                setDirty(true);
            }
        });

        Label lblWidth = new Label(grpPlotsettings, SWT.NONE);
        lblWidth.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblWidth.setText("Plot width:");

        spinnerWidth = new Spinner(grpPlotsettings, SWT.BORDER);
        spinnerWidth.setMinimum(1);
        spinnerWidth.setMaximum(Integer.MAX_VALUE);
        spinnerWidth.setSelection(evaluationModel.getOutputWidth());
        spinnerWidth.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        });

        Label lblHeight = new Label(grpPlotsettings, SWT.NONE);
        lblHeight.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblHeight.setText("Plot height:");

        spinnerHeight = new Spinner(grpPlotsettings, SWT.BORDER);
        spinnerHeight.setMinimum(1);
        spinnerHeight.setMaximum(Integer.MAX_VALUE);
        spinnerHeight.setSelection(evaluationModel.getOutputHeight());
        spinnerHeight.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                setDirty(true);
            }
        });

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

        scrolledComposite.setContent(container);
        scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
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
            }
            else {
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
        String name = "NEW_PARAMETER";

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
        if (setupQueryText != null){
        	evaluationModel.setSetupQueryFile(input.getFile().getProject().findMember(setupQueryText.getText()));
        }
        if (tearDownQueryText != null){
        	evaluationModel.setTearDownQueryFile(input.getFile().getProject().findMember(tearDownQueryText.getText()));
        }

        evaluationModel.setPlotFilesPath(this.plotFolder.getText());
        evaluationModel.setProcessingResultsPath(this.processingResultFolder.getText());
        evaluationModel.setWithLatency(btnActivateLatencyMeasurements.getSelection());
        evaluationModel.setRunSetupTearDownEveryRun(btnSetupTearDownMode.getSelection());
        evaluationModel.setUseMaxLatency(btnUseMaxLatencyMeasurements.getSelection());
        evaluationModel.setWithThroughput(btnActivateThroughputMeasurments.getSelection());
        evaluationModel.setWithResource(btnActivateResourceMeasurments.getSelection());
        evaluationModel.setNumberOfRuns(numberOfTimesSpinner.getSelection());
        evaluationModel.setCreateLatencyPlots(btnCreatePlotsForLatency.getSelection());
        evaluationModel.setCreateThroughputPlots(btnCreatePlotsForThroughputs.getSelection());
        evaluationModel.setMeasureThrougputEach(spinnerMeasureEachElements.getSelection());
        evaluationModel.setCreateCPUPlots(btnCreatePlotsForCPU.getSelection());
        evaluationModel.setCreateMemoryPlots(btnCreatePlotsForMemory.getSelection());
        evaluationModel.setOutputHeight(spinnerHeight.getSelection());
        evaluationModel.setOutputWidth(spinnerWidth.getSelection());
        evaluationModel.setOutputType(outputTypeCombo.getText());
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
            }
            catch (CoreException e) {
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
