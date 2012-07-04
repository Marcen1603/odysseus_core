package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.XMLDashboardPartHandler;

public class DashboardPartEditor extends EditorPart {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartEditor.class);
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();

	private FileEditorInput input;
	private IDashboardPart dashboardPart;
	private boolean dirty;
	
	private TabFolder tabFolder;
	private TableViewer settingsTableViewer;
	private Label settingDescriptionLabel;
	private Button resetButton;

	public DashboardPartEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			DASHBOARD_PART_HANDLER.save(dashboardPart, input.getFile());
			setDirty(false);
			
		} catch (IOException e) {
			LOG.error("Could not save DashboardPart to file {}.", input.getFile().getName(), e);
		}
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (!(input instanceof FileEditorInput)) {
			LOG.error("Could not open editor because input is from type {} instead of {}!", input.getClass(), FileEditorInput.class);
			throw new PartInitException("Input is from type " + input.getClass() + " instead of " + FileEditorInput.class + "!");
		}
		this.input = (FileEditorInput) input;

		setSite(site);
		setInput(input);
		setPartName(this.input.getFile().getName());

		try {
			dashboardPart = DASHBOARD_PART_HANDLER.load(this.input.getFile());
		} catch (IOException e) {
			LOG.error("Could not load DashboardPart for editor from file {}!", this.input.getFile().getName(), e);
			throw new PartInitException("Could not load DashboardPart from file " + this.input.getFile().getName(), e);
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty( boolean dirty ) {
		if (dirty != this.dirty) {
			this.dirty = dirty;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					firePropertyChange(IEditorPart.PROP_DIRTY);
				}
			});
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());
		
		tabFolder = new TabFolder(parent, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new GridLayout());
		
		Composite presentationTab = createTabComposite(tabFolder, "Presentation");
		Composite settingsTab = createTabComposite(tabFolder, "Settings");
		createTabComposite(tabFolder, "XML");
		
		dashboardPart.createPartControl(presentationTab);
		
		settingsTab.setLayout(new GridLayout(2, true));	
		settingsTableViewer = createSettingsTableViewer(settingsTab);
		settingsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				refreshSettingDescription();
			}
			
		});
		settingsTableViewer.setInput(dashboardPart.getConfiguration().getSettings());
		
		Composite rightPart = new Composite(settingsTab, SWT.NONE);
		rightPart.setLayoutData(new GridData(GridData.FILL_BOTH));
		rightPart.setLayout(new GridLayout());
		
		settingDescriptionLabel = new Label(rightPart, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 80;
		settingDescriptionLabel.setLayoutData(gd);
		settingDescriptionLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		
		resetButton = createButton(rightPart, "Reset");
		resetButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Optional<? extends Setting<?>> optSetting = getSelectedSetting();
				if( optSetting.isPresent() ) {
					Setting<?> setting = optSetting.get();
					Object oldValue = setting.get();
					setting.reset();
					if( !Objects.equal(oldValue, setting.get())) {
						settingsTableViewer.update(setting, null);
						setDirty(true);
					}
					
				} else {
					LOG.warn("Tried to reset non-existing setting");
				}
			}
			
		});
		
		Button resetAllButton = createButton(rightPart, "Reset all");
		resetAllButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				dashboardPart.getConfiguration().resetAll();
				settingsTableViewer.refresh();
				setDirty(true);
			}
			
		});
		
		createButton(rightPart, "Apply");
	}

	@Override
	public void setFocus() {
		tabFolder.setFocus();
	}
	
	private void refreshSettingDescription() {
		String desc = determineSettingDescription();
		settingDescriptionLabel.setText(desc);
		resetButton.setEnabled(!Strings.isNullOrEmpty(desc));
	}
	
	private String determineSettingDescription() {
		Optional<? extends Setting<?>> optSetting = getSelectedSetting();
		if( optSetting.isPresent() ) { 
			Setting<?> setting = optSetting.get();
			StringBuilder sb = new StringBuilder();
			sb.append("(").append(setting.getSettingDescriptor().getType()).append(") ");
			sb.append(setting.getSettingDescriptor().getDescription());
			return sb.toString();
		}
		
		return "";
	}
	
	private Optional<? extends Setting<?>> getSelectedSetting() {
		IStructuredSelection selection = (IStructuredSelection) settingsTableViewer.getSelection();
		if( selection == null ) {
			return Optional.absent();
		}
		Setting<?> setting = (Setting<?>)selection.getFirstElement();
		if( setting != null ) {
			return Optional.of(setting);
		} 
		return Optional.absent();
	}
	
	private static Button createButton(Composite parent, String title) {
		Button resetAllButton = new Button(parent, SWT.PUSH);
		GridData gd = new GridData();
		gd.widthHint = 200;
		resetAllButton.setLayoutData(gd);
		resetAllButton.setText(title);
		return resetAllButton;
	}

	private TableViewer createSettingsTableViewer(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		final TableViewer tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableViewerColumn settingNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		settingNameColumn.getColumn().setText("Setting");
		tableColumnLayout.setColumnData(settingNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		settingNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Setting<?> setting = (Setting<?>)cell.getElement();
				cell.setText(setting.getSettingDescriptor().getName());
			}
		});
		
		TableViewerColumn settingValueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		settingValueColumn.getColumn().setText("Value");
		tableColumnLayout.setColumnData(settingValueColumn.getColumn(), new ColumnWeightData(5, 25, true));
		settingValueColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				Setting<?> setting = (Setting<?>)cell.getElement();
				cell.setText(setting.get().toString());
			}
		});

		tableViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				Setting<?> setting = (Setting<?>)element;
				return "settingValue".equals(property) && setting.getSettingDescriptor().isEditable();
			}

			@Override
			public Object getValue(Object element, String property) {
				return ((Setting<?>)element).get().toString();
			}

			@Override
			public void modify(Object element, String property, Object value) {
				TableItem item = (TableItem)element;
				Setting<?> pair = (Setting<?>)item.getData();
				
				Object oldValue = pair.get();
				pair.setAsString(value.toString());
				if( !Objects.equal(oldValue, pair.get())) {
					setDirty(true);
				}
				tableViewer.update(item.getData(), null);
			}
			
		});
		
		tableViewer.setColumnProperties(new String[] { "setting", "settingValue" });
		tableViewer.setCellEditors(new CellEditor[] { null, new TextCellEditor(tableViewer.getTable()) });
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		return tableViewer;
	}
	
	private static Composite createTabComposite( TabFolder tabFolder, String title ) {
		TabItem presentationTab = new TabItem(tabFolder, SWT.NULL);
		presentationTab.setText(title);
		
		Composite presentationTabComposite = new Composite(tabFolder, SWT.NONE);
		presentationTabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		presentationTabComposite.setLayout(new GridLayout());
		
		presentationTab.setControl(presentationTabComposite);
		
		return presentationTabComposite;
	}
}
