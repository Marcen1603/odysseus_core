package de.uniol.inf.is.odysseus.evaluation.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.evaluation.Activator;

public class EvaluationSettingsDialog extends TitleAreaDialog implements ICellModifier, IStructuredContentProvider, ITableLabelProvider {

	private static final String MEASURING_OPTIONS = "MEASURING_OPTIONS";

	private class VariableEntry {
		public String name;
		public boolean activated = true;
		public String value;
	}

	private enum Option {
		ActivateThroughputMeasuring, AddThroughputMeasureOperators, ActivateLatencyMeasuring, AddLatencyCalculationOperators, AddFileSinkOperators, UseTimestamp
	}

	private Map<Option, Boolean> options = new HashMap<EvaluationSettingsDialog.Option, Boolean>();

	private List<VariableEntry> variables = new ArrayList<VariableEntry>();
	private IDialogSettings store;
	private Button saveAll;
	private Table table;
	private TableViewer tableViewer;
	private List<String> columnNames = new ArrayList<String>(Arrays.asList("On", "Variable Name", "Variable Value"));
	private Spinner spinner;
	private Composite tableComposite_1;
	private Composite execuctionContainer;
	private Text dataFolderText;
	private EvaluationSetting evaluationSetting = new EvaluationSetting();

	protected EvaluationSettingsDialog(Shell parentShell) {
		super(parentShell);
		this.store = Activator.getDefault().getDialogSettings();
		loadOptions();
	}

	private void loadOptions() {
		// load options
		for (Option o : Option.values()) {
			String key = o.toString();
			boolean value = true;
			IDialogSettings settings = this.store.getSection(MEASURING_OPTIONS);
			if (settings != null) {
				if (settings.get(key) != null) {
					value = settings.getBoolean(key);
				}
			}
			options.put(o, value);
		}
	}

	private void saveOptions(){
		IDialogSettings settings = this.store.getSection(MEASURING_OPTIONS);
		if(settings==null){
			settings = this.store.addNewSection(MEASURING_OPTIONS);
		}
		for(Entry<Option, Boolean> o : this.options.entrySet()){
			settings.put(o.getKey().toString(), o.getValue());			
		}
	}

	@Override
	protected Point getInitialSize() {
		Point size = super.getInitialSize();
		return new Point(Math.max(400, size.x), Math.max(500, size.y));
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite mainarea = (Composite) super.createDialogArea(parent);

		TabFolder tabFolder = new TabFolder(mainarea, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TabItem tbtmExecutionSettings = new TabItem(tabFolder, SWT.NONE);
		tbtmExecutionSettings.setText("Execution Settings");

		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Measurement Settings");

		Composite measurementContainer = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(measurementContainer);
		measurementContainer.setLayout(new GridLayout(1, false));

		Group grpThroughput = new Group(measurementContainer, SWT.NONE);
		grpThroughput.setText("Throughput");
		grpThroughput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpThroughput.setLayout(new GridLayout(1, false));

		Button btnCheckButton = new Button(grpThroughput, SWT.CHECK);
		btnCheckButton.setSelection(true);
		btnCheckButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnCheckButton.setText("Activate throughput measuring");
		btnCheckButton.addSelectionListener(new CheckBoxSelectionListener(Option.ActivateThroughputMeasuring));

		Button btnCheckButton_1 = new Button(grpThroughput, SWT.CHECK);
		btnCheckButton_1.setSelection(true);
		GridData gd_btnCheckButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCheckButton_1.horizontalIndent = 20;
		btnCheckButton_1.setLayoutData(gd_btnCheckButton_1);
		btnCheckButton_1.setText("Add throughput measure operators automaically to each source");
		btnCheckButton_1.addSelectionListener(new CheckBoxSelectionListener(Option.AddThroughputMeasureOperators));

		Group grpLatency = new Group(measurementContainer, SWT.NONE);
		grpLatency.setLayout(new GridLayout(1, false));
		grpLatency.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpLatency.setText("Latency");

		Button btnActivateLatencyMeasuring = new Button(grpLatency, SWT.CHECK);
		btnActivateLatencyMeasuring.setSelection(true);
		btnActivateLatencyMeasuring.setText("Activate latency measuring");
		btnActivateLatencyMeasuring.addSelectionListener(new CheckBoxSelectionListener(Option.ActivateLatencyMeasuring));
		
		Button btnAddLatencyCalculation = new Button(grpLatency, SWT.CHECK);
		GridData gd_btnAddLatencyCalculation = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAddLatencyCalculation.horizontalIndent = 20;
		btnAddLatencyCalculation.setLayoutData(gd_btnAddLatencyCalculation);
		btnAddLatencyCalculation.setSelection(true);
		btnAddLatencyCalculation.setText("Add latency calculation operators automatically to each sink");
		btnAddLatencyCalculation.addSelectionListener(new CheckBoxSelectionListener(Option.AddLatencyCalculationOperators));

		Button btnAddFileSink = new Button(grpLatency, SWT.CHECK);
		GridData gd_btnAddFileSink = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAddFileSink.horizontalIndent = 20;
		btnAddFileSink.setLayoutData(gd_btnAddFileSink);
		btnAddFileSink.setSelection(true);
		btnAddFileSink.setText("Add file sink operators automatically");
		btnAddFileSink.addSelectionListener(new CheckBoxSelectionListener(Option.AddFileSinkOperators));

		Button btnUseCurrentTimestamp = new Button(grpLatency, SWT.CHECK);
		GridData gd_btnUseCurrentTimestamp = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnUseCurrentTimestamp.horizontalIndent = 20;
		btnUseCurrentTimestamp.setLayoutData(gd_btnUseCurrentTimestamp);
		btnUseCurrentTimestamp.setSelection(true);
		btnUseCurrentTimestamp.setText("Use current timestamp to separate runs");
		btnUseCurrentTimestamp.addSelectionListener(new CheckBoxSelectionListener(Option.UseTimestamp));

		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Diagramm Settings");

		execuctionContainer = new Composite(tabFolder, SWT.NONE);
		execuctionContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tbtmExecutionSettings.setControl(execuctionContainer);
		executionTab(execuctionContainer);

		return mainarea;
	}

	private Composite executionTab(Composite parent) {
		execuctionContainer.setLayout(new GridLayout(1, false));

		Composite container = new Composite(parent, SWT.NONE);
		FillLayout fl_container = new FillLayout(SWT.HORIZONTAL);
		fl_container.marginWidth = 10;
		container.setLayout(fl_container);
		GridData gd_container = new GridData(GridData.FILL_BOTH);
		gd_container.grabExcessHorizontalSpace = false;
		gd_container.horizontalAlignment = SWT.LEFT;
		gd_container.verticalAlignment = SWT.CENTER;
		container.setLayoutData(gd_container);

		Label lblDefineAndActivatedeactive = new Label(container, SWT.NONE);
		lblDefineAndActivatedeactive.setText("Define and activate/deactive variables and there values that should be run through");

		String[] names = store.getArray("EVALUATION_NAMES");
		if (names != null) {
			for (String name : names) {
				VariableEntry e = new VariableEntry();
				e.name = name;
				e.value = store.get(name);
				e.activated = store.getBoolean(name + "__ACTIVATE");
				this.variables.add(e);
			}
		}

		super.setTitle("Start Evaluation");
		super.setMessage("Change or activate/deactive settings for this evaluation!");

		tableComposite_1 = new Composite(parent, SWT.NONE);
		GridData gd_tableComposite_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_tableComposite_1.widthHint = 603;
		tableComposite_1.setLayoutData(gd_tableComposite_1);
		createTable(tableComposite_1);

		// Create and setup the TableViewer
		createTableViewer();
		tableViewer.setContentProvider(this);
		tableViewer.setLabelProvider(this);
		tableViewer.setInput(variables);

		Composite buttonComposite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		buttonComposite.setLayoutData(gridData);
		FillLayout fl_buttonComposite = new FillLayout(SWT.HORIZONTAL);
		fl_buttonComposite.marginWidth = 10;
		buttonComposite.setLayout(fl_buttonComposite);

		Button newLineButton = new Button(buttonComposite, SWT.PUSH);
		newLineButton.setText("Add new constant");

		newLineButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				VariableEntry e = new VariableEntry();
				e.activated = true;
				e.name = "NEW_VARIBALE";
				e.value = "1, 2";
				variables.add(e);
				refreshTable();
			}
		});

		Button removeLineButton = new Button(buttonComposite, SWT.PUSH);
		removeLineButton.setText("Remove constant");
		removeLineButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				for (TableItem ti : tableViewer.getTable().getSelection()) {
					if (ti.getData() instanceof VariableEntry) {
						VariableEntry e = (VariableEntry) ti.getData();
						variables.remove(e);
						refreshTable();
					}
				}

			}
		});

		// other settings
		final Composite othersettingsComp = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(3, false);
		gl.marginRight = 10;
		gl.marginLeft = 10;
		gl.marginTop = 10;
		othersettingsComp.setLayout(gl);
		int times = 1;
		if (store.get("TIMES") != null) {
			times = store.getInt("TIMES");
		}
		Label lblNumberOfTimes = new Label(othersettingsComp, SWT.NONE);
		lblNumberOfTimes.setText("Number of times to run the evaluation:");

		spinner = new Spinner(othersettingsComp, SWT.BORDER);
		spinner.setMinimum(0);
		spinner.setSelection(times);
		spinner.setMaximum(Integer.MAX_VALUE);
		spinner.setIncrement(1);
		new Label(othersettingsComp, SWT.NONE);

		Label lblWhereToStore = new Label(othersettingsComp, SWT.NONE);
		lblWhereToStore.setText("Where to store the results:");

		dataFolderText = new Text(othersettingsComp, SWT.BORDER);
		GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
		gd_dataFolderText.widthHint = 287;
		dataFolderText.setLayoutData(gd_dataFolderText);
		String folder = this.store.get("STORE_PATH");
		if(folder==null){
			folder = "";
		}
		dataFolderText.setText(folder);
		Button button = new Button(othersettingsComp, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(othersettingsComp.getShell());

				// Set the initial filter path according
				// to anything they've selected or typed in
				dlg.setFilterPath(dataFolderText.getText());

				// Change the title bar text
				dlg.setText("Folder where to store the measurements");

				// Customizable message displayed in the dialog
				dlg.setMessage("Select a directory");

				// Calling open() will open and run the dialog.
				// It will return the selected directory, or
				// null if user cancels
				String dir = dlg.open();
				if (dir != null) {
					// Set the text box to the new selection
					dataFolderText.setText(dir);
				}
			}
		});
		Label label2 = new Label(othersettingsComp, SWT.NONE);
		label2.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		label2.setText("Save all settings");

		saveAll = new Button(othersettingsComp, SWT.CHECK);
		saveAll.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1));
		saveAll.setSelection(true);
		new Label(othersettingsComp, SWT.NONE);
		return parent;
	}

	private void createTable(Composite parent) {
		GridLayout gl_tableComposite_1 = new GridLayout(1, false);
		gl_tableComposite_1.marginRight = 10;
		gl_tableComposite_1.marginLeft = 10;
		gl_tableComposite_1.marginBottom = 10;
		gl_tableComposite_1.verticalSpacing = 0;
		gl_tableComposite_1.marginWidth = 0;
		tableComposite_1.setLayout(gl_tableComposite_1);
		Composite tableComposite = new Composite(parent, SWT.NONE);
		FormData fd_tableComposite = new FormData();
		fd_tableComposite.right = new FormAttachment(0, 557);
		fd_tableComposite.top = new FormAttachment(0, 5);
		fd_tableComposite.left = new FormAttachment(0, 5);
		tableComposite.setLayoutData(fd_tableComposite);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.minimumHeight = 400;
		tableComposite.setLayoutData(gd);

		table = new Table(tableComposite, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 3;
		gridData.minimumHeight = 400;
		table.setLayoutData(gridData);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// 1st column with checkboxes
		TableColumn column = new TableColumn(table, SWT.CENTER, 0);
		column.setText(columnNames.get(0));
		tableColumnLayout.setColumnData(column, new ColumnWeightData(10, 50, false));
		// 2nd column
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText(columnNames.get(1));
		tableColumnLayout.setColumnData(column, new ColumnWeightData(30, 150, true));

		// 3rd column
		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText(columnNames.get(2));
		tableColumnLayout.setColumnData(column, new ColumnWeightData(60, 300, true));

	}

	private void createTableViewer() {

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(columnNames.toArray(new String[0]));

		// Create the cell editors
		CellEditor[] editors = new CellEditor[columnNames.size()];
		editors[0] = new CheckboxCellEditor(table);
		editors[1] = new TextCellEditor(table);
		editors[2] = new TextCellEditor(table);

		((Text) editors[1].getControl()).addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = validate();
			}

		});

		tableViewer.setCellEditors(editors);
		tableViewer.setCellModifier(this);
	}

	protected boolean validate() {
		setErrorMessage(null);
		boolean ok = true;
		VariableEntry[] values = variables.toArray(new VariableEntry[0]);
		for (int i = 0; i < values.length - 1; i++) {
			for (int j = i + 1; j < values.length; j++) {
				String first = values[i].name;
				String second = values[j].name;
				if (i != j && first.equalsIgnoreCase(second)) {
					setErrorMessage("Names of variables must be unique and " + first + " exists twice.");
					ok = false;
				}
			}
		}
		getButton(IDialogConstants.OK_ID).setEnabled(ok);
		return ok;
	}

	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		// rebuild new evaluationsetting

		evaluationSetting.setVariables(getActiveValues());
		evaluationSetting.setActivateThroughput(options.get(Option.ActivateThroughputMeasuring));
		evaluationSetting.setAddThroughputOperators(options.get(Option.AddThroughputMeasureOperators));		
		
		evaluationSetting.setActivateLatency(options.get(Option.ActivateLatencyMeasuring));		
		evaluationSetting.setAddLatencyCalculationOperators(options.get(Option.AddLatencyCalculationOperators));
		evaluationSetting.setAddFileSinkOperators(options.get(Option.AddFileSinkOperators));
		evaluationSetting.setUseTimestamps(options.get(Option.UseTimestamp));
		
		evaluationSetting.setStoringPath(dataFolderText.getText());
		evaluationSetting.setNumberOfTimes(this.spinner.getSelection());
		
		// store defaults!
		if (saveAll.getSelection()) {
			List<String> names = new ArrayList<String>();
			for (VariableEntry e : this.variables) {
				names.add(e.name);
				this.store.put(e.name + "__ACTIVATE", e.activated);
				this.store.put(e.name, e.value);
			}
			this.store.put("EVALUATION_NAMES", names.toArray(new String[0]));
			this.store.put("TIMES", evaluationSetting.getNumberOfTimes());
			this.store.put("STORE_PATH", evaluationSetting.getStoringPath());
			// save options
			saveOptions();
		}
		super.okPressed();
	}

	public EvaluationSetting getSetting() {
		return evaluationSetting;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private Map<String, List<String>> getActiveValues() {
		Map<String, List<String>> erg = new TreeMap<>();
		for (VariableEntry e : this.variables) {
			if (e.activated) {
				String[] splits = e.value.split(",");
				List<String> values = new ArrayList<>();
				for (String v : splits) {
					values.add(v.trim());
				}
				erg.put(e.name, values);
			}
		}
		return erg;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		int columnIndex = columnNames.indexOf(property);
		VariableEntry e = (VariableEntry) element;
		switch (columnIndex) {
		case 0:
			return new Boolean(e.activated);
		case 1:
			return e.name;
		case 2:
			return e.value;
		default:
			return "";
		}
	}

	@Override
	public void modify(Object element, String property, Object value) {
		int columnIndex = columnNames.indexOf(property);
		TableItem item = (TableItem) element;
		VariableEntry e = (VariableEntry) item.getData();
		switch (columnIndex) {
		case 0:
			e.activated = ((Boolean) value);
			break;
		case 1:
			e.name = ((String) value).trim();
			break;
		case 2:
			e.value = ((String) value).trim();
			break;
		default:

		}
		refreshTable();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return variables.toArray();
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		VariableEntry v = (VariableEntry) element;
		switch (columnIndex) {
		case 0:
			return Boolean.toString(v.activated);
		case 1:
			return v.name;
		case 2:
			return v.value;
		}
		return null;
	}

	private void refreshTable() {
		this.tableViewer.setInput(variables);
	}

	class CheckBoxSelectionListener extends SelectionAdapter {
		private Option option;

		public CheckBoxSelectionListener(Option option) {
			this.option = option;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (((Button) e.widget).getSelection()) {
				options.put(option, true);
			}else{
				options.put(option, false);
			}
		}

	}
}
