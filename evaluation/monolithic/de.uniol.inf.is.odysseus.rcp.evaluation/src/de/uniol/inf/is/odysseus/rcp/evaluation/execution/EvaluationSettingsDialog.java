package de.uniol.inf.is.odysseus.rcp.evaluation.execution;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.evaluation.Activator;

public class EvaluationSettingsDialog extends TitleAreaDialog implements ICellModifier, IStructuredContentProvider, ITableLabelProvider {

	private class VariableEntry {
		public String name;
		public boolean activated = true;
		public String value;

	}

	private List<VariableEntry> variables = new ArrayList<VariableEntry>();
	private IDialogSettings store;
	private int times = 3;
	private Button saveAll;
	private Table table;
	private Spinner timeText;
	private TableViewer tableViewer;
	private List<String> columnNames = new ArrayList<String>(Arrays.asList("On", "Variable Name", "Variable Value"));
	private Text locationText;
	private String location = "";

	protected EvaluationSettingsDialog(Shell parentShell) {
		super(parentShell);
		this.store = Activator.getDefault().getDialogSettings();
	}

	@Override
	protected Point getInitialSize() {
		Point size = super.getInitialSize();
		return new Point(Math.max(400, size.x), Math.max(500, size.y));
	}

	@Override
	protected Control createDialogArea(final Composite parent) {

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
		if(this.store.get("RESULT_LOCATION")!=null){
			this.location = store.get("RESULT_LOCATION");
		}else{
			this.location = "";
		}

		super.setTitle("Start Evaluation");
		super.setMessage("Change or activate/deactive settings for this evaluation!");

		// Set numColumns to 3 for the buttons
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 4;		
		parent.setLayout(layout);
		
		GridData gd = new GridData();
		//gd.grabExcessVerticalSpace = true;		
		gd.heightHint = 400;
		gd.minimumHeight = 400;
		// Create the table
		createTable(parent);
		table.setLayoutData(gd);

		// Create and setup the TableViewer
		createTableViewer();
		tableViewer.setContentProvider(this);
		tableViewer.setLabelProvider(this);
		
		tableViewer.setInput(variables);

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		Button newLineButton = new Button(parent, SWT.PUSH);
		newLineButton.setText("Add new constant");
		newLineButton.setLayoutData(gridData);
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
		
		Button removeLineButton = new Button(parent, SWT.PUSH);
		removeLineButton.setText("Remove constant");
		removeLineButton.setLayoutData(gridData);
		removeLineButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				for(TableItem ti : tableViewer.getTable().getSelection()){
					if(ti.getData() instanceof VariableEntry){
						VariableEntry e = (VariableEntry) ti.getData();						
						variables.remove(e);
						refreshTable();
					}
				}
				
			}
		});

		Label labelLocation = new Label(parent, SWT.NONE);
		labelLocation.setText("Choose location where to store the results");
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout compositeLayout = new GridLayout(2, false);
		compositeLayout.marginWidth = 0;
		compositeLayout.marginHeight = 0;
		composite.setLayout(compositeLayout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		locationText = new Text(composite, SWT.BORDER);
		GridData gd_dataFolderText = new GridData(GridData.FILL_HORIZONTAL);
		gd_dataFolderText.widthHint = 287;
		locationText.setLayoutData(gd_dataFolderText);
		locationText.setText(getLocation());
		locationText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				location = locationText.getText();
				
			}
		});

		Button button = new Button(composite, SWT.PUSH);
		button.setText("Browse...");

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {			
				DirectoryDialog dialog = new DirectoryDialog(getShell());												
				dialog.setFilterPath(locationText.getText());
				dialog.setText("Result directory");
				dialog.setMessage("Choose a directory");

				String dir = dialog.open();
				if (dir!=null) {					
					locationText.setText(dir);
				}			
			}
		});
		
		
		
		//********************************* OTHER STUFF ****************************
		
		Composite othersettingsComp = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		othersettingsComp.setLayout(gl);
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;		
		othersettingsComp.setLayoutData(gridData);

		
		timeText = new Spinner(othersettingsComp, SWT.BORDER);
		int times = 5;
		if (store.get("TIMES") != null) {
			times = store.getInt("TIMES");
		}
		timeText.setMinimum(0);
		timeText.setSelection(times);		
//		timeText.setSize(100, timeText.getSize().y);
		timeText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});

		Label label = new Label(othersettingsComp, SWT.NONE);
		label.setText("Number of times to run the evaluation!");

		saveAll = new Button(othersettingsComp, SWT.CHECK);
		saveAll.setSelection(true);
		Label label2 = new Label(othersettingsComp, SWT.NONE);
		label2.setText("Save all settings for next evaluation start!");
		
		
		
		
		return parent;
	}

	private void createTable(Composite parent) {

		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setSize(new Point(tableComposite.getSize().x, tableComposite.getSize().y + 200));
		tableComposite.setLayout(tableColumnLayout);
		GridData gd = new GridData();
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
		try {
			this.times = Integer.parseInt(this.timeText.getText());
		} catch (NumberFormatException ex) {
			setErrorMessage("Times must be an integer number!");
			ok = false;
		}				
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

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID, "Start", true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		
		try {
			this.times = Integer.parseInt(this.timeText.getText());
		} catch (NumberFormatException ex) {
			setErrorMessage("Times must be a number!");
			ex.printStackTrace();
		}
		// store defaults!
		if (saveAll.getSelection()) {
			List<String> names = new ArrayList<String>();
			for (VariableEntry e : this.variables) {				
				names.add(e.name);
				this.store.put(e.name + "__ACTIVATE", e.activated);
				this.store.put(e.name, e.value);
			}
			this.store.put("EVALUATION_NAMES", names.toArray(new String[0]));			
			this.store.put("TIMES", this.times);
			this.store.put("RESULT_LOCATION", this.getLocation());
		}
		super.okPressed();
	}

	public int getTimes() {
		return this.times;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	public Map<String, List<String>> getActiveValues() {
		Map<String, List<String>> erg = new TreeMap<>();
		for(VariableEntry e : this.variables){
			if(e.activated){
				String[] splits = e.value.split(",");
				List<String> values = new ArrayList<>();
				for(String v : splits){					
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
		default:
			return null;
		}
	}

	private void refreshTable() {
		this.tableViewer.setInput(variables);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
