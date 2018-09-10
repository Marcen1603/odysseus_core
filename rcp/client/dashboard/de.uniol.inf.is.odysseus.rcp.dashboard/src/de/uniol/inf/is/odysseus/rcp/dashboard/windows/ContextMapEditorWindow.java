package de.uniol.inf.is.odysseus.rcp.dashboard.windows;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;

public class ContextMapEditorWindow extends TitleAreaDialog {

	private static class TableEntry {
		public String key;
		public String value;
		public boolean fixed;
		
		public TableEntry( String key, String value, boolean fixed ) {
			this.key = key;
			this.value = value;
			this.fixed = fixed;
		}
	}
	
	private static final String WINDOW_TITLE = "Configure context of DashboardPart";
	private static final String DISPLAY_TITLE = "DashboardPart context";
	private static final String DEFAULT_MESSAGE = "Change query context of ";

	private final IDashboardPart dashboardPart;
	private final String dashboardPartName;
	
	private final List<TableEntry> tableEntries = Lists.newLinkedList();
	private TableViewer tableViewer;
	private Button okButton;
	
	public ContextMapEditorWindow(Shell parentShell, IDashboardPart dashboardPart, String dashboardPartName) {
		super(parentShell);

		Preconditions.checkNotNull(dashboardPart, "DashboardPart to change context must not be null!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(dashboardPartName), "Name of dashboardPart must not be null or empty!");
		
		this.dashboardPart = dashboardPart;
		this.dashboardPartName = dashboardPartName;
		
		this.tableEntries.addAll(determineTableEntries(dashboardPart));
		scanForUndefinedReplacements(dashboardPart.getQueryTextProvider().getQueryText());
	}
	
	private static List<TableEntry> determineTableEntries(IDashboardPart part) {
		List<TableEntry> entries = Lists.newArrayList();
		for( String key : part.getContextKeys() ) {
			entries.add( new TableEntry(key, part.getContextValue(key).get(), false));
		}
		
		return entries;
	}
	
	private void scanForUndefinedReplacements(ImmutableList<String> queryText) {
		List<String> undefinedReplacements = Lists.newArrayList();
		List<String> foundDefines = Lists.newArrayList();
		List<String> preDefinedKeys = Lists.newArrayList();
		preDefinedKeys.add("WORKSPACE");
		preDefinedKeys.add("WORKSPACE/");
		preDefinedKeys.add("WORKSPACE\\");
		preDefinedKeys.add("PROJECT");
		preDefinedKeys.add("PROJECTPATH");
		preDefinedKeys.add("PROJECTPATH\\");
		preDefinedKeys.add("PROJECTPATH/");
		preDefinedKeys.add("WORKSPACEPROJECT");
		preDefinedKeys.add("WORKSPACEPROJECT\\");
		preDefinedKeys.add("WORKSPACEPROJECT/");
		preDefinedKeys.add("\\");
		preDefinedKeys.add("/");

		
		for( String line : queryText ) {
			line = line.trim();
			if( line.startsWith("#DEFINE")) {
				String[] defineParts = line.split("\\ ", 3);
				if( defineParts.length > 2 ) {
					foundDefines.add(defineParts[1]);
				}
			} else {
				
				int startPos = 0; 
				startPos = line.indexOf("${");
				
				while( startPos >= 0 ) {
					int posEnd = line.indexOf("}", startPos);
					if( posEnd != -1 ) {
						String undefinedReplacement = line.substring(startPos + 2, posEnd);
						
						if( !undefinedReplacements.contains(undefinedReplacement) && !foundDefines.contains(undefinedReplacement) && !preDefinedKeys.contains(undefinedReplacement)) {
							undefinedReplacements.add(undefinedReplacement);
						}
						startPos = line.indexOf("${", posEnd);
					} else {
						break;
					}
				}
			}
		}
		
		f1: for( String undefinedReplacement : undefinedReplacements ) {
			for( TableEntry e : tableEntries ) {
				if( e.key.equals(undefinedReplacement ) ) {
					e.fixed = true;
					continue f1;
				}
			}
			
			tableEntries.add(new TableEntry(undefinedReplacement, "", true));
		}
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle(DISPLAY_TITLE);
		getShell().setText(WINDOW_TITLE);
		setMessage(DEFAULT_MESSAGE + dashboardPartName);
		
		return contents;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite tableComposite = createTopComposite(parent);
		
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		tableViewer = new TableViewer(tableComposite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);

		TableViewerColumn keyColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		keyColumn.getColumn().setText("Key");
		tableColumnLayout.setColumnData(keyColumn.getColumn(), new ColumnWeightData(5, 25, true));
		keyColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				TableEntry entry = (TableEntry) cell.getElement();
				String keyName = entry.key;
				if( entry.fixed ) {
					keyName += "*";
				}
				cell.setText(keyName);
			}
		});

		TableViewerColumn valueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		valueColumn.getColumn().setText("Value");
		tableColumnLayout.setColumnData(valueColumn.getColumn(), new ColumnWeightData(5, 25, true));
		valueColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				TableEntry entry = (TableEntry) cell.getElement();
				cell.setText(entry.value);
			}
		});
		
		tableViewer.setCellEditors(new CellEditor[] { new TextCellEditor(tableViewer.getTable()), new TextCellEditor(tableViewer.getTable()) });
		tableViewer.setColumnProperties(new String[] {"key", "value"});
		tableViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				TableEntry entry = (TableEntry)element;
				if( "key".equals(property)) {
					return entry.key;
				}
				return entry.value;
			}

			@Override
			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem)element;
				TableEntry entry = (TableEntry)tableItem.getData();
				
				if( "key".equals(property)) {
					entry.key = (String)value;
					tableItem.setText(0, entry.key);
					
				} else {
					entry.value = (String)value;
					tableItem.setText(1, entry.value);
				}
				checkKeys();
			}
		});

		tableViewer.setInput(tableEntries);
		
		return tableComposite;
	}

	private void checkKeys() {
		for( int i = 0; i < tableEntries.size(); i++ ) {
			String key = tableEntries.get(i).key;
			
			for( int j = i + 1; j < tableEntries.size(); j++ ) {
				if( tableEntries.get(j).key.equals(key)) {
					okButton.setEnabled(false);
					setErrorMessage("Duplicate key '" + key + "'");
					return;
				}
			}
		}
		
		for( TableEntry e : tableEntries ) {
			if( e.fixed && Strings.isNullOrEmpty(e.value)) {
				okButton.setEnabled(false);
				setErrorMessage("Value for key '" + e.key + "' is needed to execute query!");
				return;
			}
		}

		setErrorMessage(null);
		okButton.setEnabled(true);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		((GridLayout) parent.getLayout()).numColumns++;	
		Button addButton = new Button(parent, SWT.PUSH);
		addButton.setText("Add");
		setButtonLayoutData(addButton);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableEntries.add(generateNewTableEntry());
				tableViewer.refresh();
			}
		});
		
		((GridLayout) parent.getLayout()).numColumns++;	
		Button removeButton = new Button(parent, SWT.PUSH);
		removeButton.setText("Remove");
		setButtonLayoutData(removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
				for( Object selectedObject : selection.toArray() ) {
					TableEntry entry = (TableEntry)selectedObject;
					if( !entry.fixed) {
						tableEntries.remove(entry);
					}
				}
				
				tableViewer.refresh();
			}
		});
		
		Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", true);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.CANCEL);
				close();
			}
		});
		
		okButton = createButton(parent, IDialogConstants.OK_ID, "OK", true);
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(Window.OK);
				insertInto( dashboardPart, tableEntries );
				close();
			}
		});
		
		checkKeys();
	}

	private TableEntry generateNewTableEntry() {
		int i = 0;
		wh: while(true) {
			String name = "key" + i;
			for( TableEntry e : tableEntries ) {
				if(e.key.equals(name)) {
					i++;
					continue wh;
				}
			}
			
			return new TableEntry(name, "value", false);
		}
	}
	
	private static void insertInto(IDashboardPart part, List<TableEntry> tableEntries) {
		ImmutableCollection<String> keys = part.getContextKeys();
		for( String key : keys ) {
			part.removeContext(key);
		}
		
		for( TableEntry entry : tableEntries ) {
			part.addContext(entry.key, entry.value);
		}
	}
	
	private static Composite createTopComposite(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		tableComposite.setLayout(new GridLayout(1, false));
		return tableComposite;
	}
}
