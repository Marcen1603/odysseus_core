/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import java.io.FileNotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.widgets.Link;
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

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardHandlerException;
import de.uniol.inf.is.odysseus.rcp.dashboard.IConfigurationListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.Setting;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;
import de.uniol.inf.is.odysseus.rcp.dashboard.handler.XMLDashboardPartHandler;
import de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider.ResourceFileQueryTextProvider;
import de.uniol.inf.is.odysseus.rcp.dashboard.util.FileUtil;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

public class DashboardPartEditor extends EditorPart implements IConfigurationListener, IDashboardPartListener {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPartEditor.class);
	private static final IDashboardPartHandler DASHBOARD_PART_HANDLER = new XMLDashboardPartHandler();

	private FileEditorInput input;
	private IDashboardPart dashboardPart;
	private DashboardPartController dashboardPartController;
	private boolean dirty;

	private TabFolder tabFolder;
	private TableViewer settingsTableViewer;
	private Label settingDescriptionLabel;
	private Button resetButton;

	private DashboardPartEditorToolBar dashboardPartToolBar;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout());

		if (!dashboardPart.getConfiguration().getSettings().isEmpty()) {
			tabFolder = new TabFolder(parent, SWT.NONE);
			tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
			tabFolder.setLayout(new GridLayout());

			final Composite presentationTab = createTabComposite(tabFolder, "Presentation");
			final Composite settingsTab = createTabComposite(tabFolder, "Settings");
			createSettingsTabContent(settingsTab);
			createPresentationTabContent(presentationTab);
		} else {
			final Composite comp = new Composite(parent, SWT.NONE);
			comp.setLayoutData(new GridData(GridData.FILL_BOTH));
			comp.setLayout(new GridLayout());

			createPresentationTabContent(comp);
		}

	}

	@Override
	public void dispose() {
		dashboardPartController.stop();

		dashboardPart.getConfiguration().removeListener(this);
		dashboardPart.dispose();

		super.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			FileUtil.write(DASHBOARD_PART_HANDLER.save(dashboardPart), this.input.getFile());

			setDirty(false);

		} catch (final DashboardHandlerException e) {
			LOG.error("Could not save DashboardPart to file {}.", input.getFile().getName(), e);
		} catch (final CoreException ex) {
			LOG.error("Could not save DashboardPart to file {}.", input.getFile().getName(), ex);
		}
	}

	@Override
	public void doSaveAs() {

	}

	public DashboardPartController getDashboardPartController() {
		return dashboardPartController;
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
			dashboardPart = DASHBOARD_PART_HANDLER.load(FileUtil.read(this.input.getFile()));
			dashboardPart.getConfiguration().addListener(this);
			dashboardPart.addListener(this);
			dashboardPartController = new DashboardPartController(dashboardPart);
		} catch (final DashboardHandlerException e) {
			LOG.error("Could not load DashboardPart for editor from file {}!", this.input.getFile().getName(), e);
			throw new PartInitException("Could not load DashboardPart from file " + this.input.getFile().getName(), e);
		} catch (final FileNotFoundException ex) {
			LOG.error("Could not load corresponding query file!", ex);
			throw new PartInitException("Could not load corresponding query file!", ex);
		} catch (final CoreException ex) {
			LOG.error("Could not load corresponding query file!", ex);
			throw new PartInitException("Could not load corresponding query file!", ex);
		}
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public void setDirty(boolean dirty) {
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
	public void setFocus() {
		if (tabFolder != null) {
			tabFolder.setFocus();
		}
	}

	public void setPartNameSuffix(String partNameSuffix) {
		if (Strings.isNullOrEmpty(partNameSuffix)) {
			super.setPartName(this.input.getFile().getName());
		} else {
			super.setPartName(this.input.getFile().getName() + " [" + partNameSuffix + "]");
		}
	}

	@Override
	public void settingChanged(String settingName, Object oldValue, Object newValue) {
		setDirty(true);
		settingsTableViewer.refresh();
	}

	private void createPresentationTabContent(Composite presentationTab) {
		dashboardPartToolBar = new DashboardPartEditorToolBar(presentationTab, this);

		final Composite comp = new Composite(presentationTab, SWT.NONE);
		comp.setLayout(new GridLayout());
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		comp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		try {
			dashboardPart.createPartControl(comp, dashboardPartToolBar.getToolBar());
			dashboardPartController.start();
		} catch (final Exception ex) {
			dashboardPartToolBar.setStatusToStopped();
			throw new RuntimeException(ex);
		}
	}

	private void createSettingsTabContent(Composite settingsTab) {
		settingsTab.setLayout(new GridLayout(2, true));
		settingsTableViewer = createSettingsTableViewer(settingsTab);
		settingsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				refreshSettingDescription();
			}

		});
		settingsTableViewer.setInput(dashboardPart.getConfiguration().getSettings());

		final Composite rightPart = new Composite(settingsTab, SWT.NONE);
		rightPart.setLayoutData(new GridData(GridData.FILL_BOTH));
		rightPart.setLayout(new GridLayout());

		settingDescriptionLabel = new Label(rightPart, SWT.BORDER | SWT.WRAP);
		final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 80;
		settingDescriptionLabel.setLayoutData(gd);
		settingDescriptionLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		resetButton = createButton(rightPart, "Reset");
		resetButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final Optional<? extends Setting<?>> optSetting = getSelectedSetting();
				if (optSetting.isPresent()) {
					resetSetting(optSetting.get());
				} else {
					LOG.warn("Tried to reset non-existing setting");
				}
			}

		});

		final Button resetAllButton = createButton(rightPart, "Reset all");
		resetAllButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (final Setting<?> setting : dashboardPart.getConfiguration().getSettings()) {
					resetSetting(setting);
				}
			}

		});

		final Composite informationComposite = new Composite(rightPart, SWT.NONE);
		informationComposite.setLayout(new GridLayout());
		informationComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		if (dashboardPart.getQueryTextProvider() instanceof ResourceFileQueryTextProvider) {
			final ResourceFileQueryTextProvider resFile = (ResourceFileQueryTextProvider) dashboardPart.getQueryTextProvider();
			final IFile queryFile = resFile.getFile();

			final Link queryFileLabel = new Link(informationComposite, SWT.NONE);
			queryFileLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			queryFileLabel.setText("Open corresponding query file: <a>" + queryFile.getFullPath().toString() + "</a>");
			queryFileLabel.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(queryFile), OdysseusRCPEditorTextPlugIn.ODYSSEUS_SCRIPT_EDITOR_ID);
					} catch (final PartInitException ex) {
						LOG.error("Could not open editor", ex);
					}
				}
			});
		}
	}

	private TableViewer createSettingsTableViewer(Composite parent) {
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		final TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);

		final TableViewer tableViewer = new TableViewer(tableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		final Table table = tableViewer.getTable();

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableViewerColumn settingNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		settingNameColumn.getColumn().setText("Setting");
		tableColumnLayout.setColumnData(settingNameColumn.getColumn(), new ColumnWeightData(5, 25, true));
		settingNameColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final Setting<?> setting = (Setting<?>) cell.getElement();
				final String txt = setting.getSettingDescriptor().isOptional() ? setting.getSettingDescriptor().getName() : setting.getSettingDescriptor().getName() + "*";
				cell.setText(txt);

				if (!setting.getSettingDescriptor().isEditable()) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				} else {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				}
			}
		});

		final TableViewerColumn settingValueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		settingValueColumn.getColumn().setText("Value");
		tableColumnLayout.setColumnData(settingValueColumn.getColumn(), new ColumnWeightData(5, 25, true));
		settingValueColumn.setLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				final Setting<?> setting = (Setting<?>) cell.getElement();
				cell.setText(setting.get().toString());

				if (!setting.getSettingDescriptor().isEditable()) {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
				} else {
					cell.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				}
			}
		});

		tableViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				final Setting<?> setting = (Setting<?>) element;
				return "settingValue".equals(property) && setting.getSettingDescriptor().isEditable();
			}

			@Override
			public Object getValue(Object element, String property) {
				return ((Setting<?>) element).get().toString();
			}

			@Override
			public void modify(Object element, String property, Object value) {
				final TableItem item = (TableItem) element;
				final Setting<?> setting = (Setting<?>) item.getData();

				// use configuration to invoke listeners
				dashboardPart.getConfiguration().setAsString(setting.getSettingDescriptor().getName(), value.toString());
			}

		});

		tableViewer.setColumnProperties(new String[] { "setting", "settingValue" });
		tableViewer.setCellEditors(new CellEditor[] { null, new TextCellEditor(tableViewer.getTable()) });
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		return tableViewer;
	}

	private String determineSettingDescription() {
		final Optional<? extends Setting<?>> optSetting = getSelectedSetting();
		if (optSetting.isPresent()) {
			final Setting<?> setting = optSetting.get();
			final StringBuilder sb = new StringBuilder();
			sb.append("(").append(setting.getSettingDescriptor().getType()).append(") ");
			sb.append(setting.getSettingDescriptor().getDescription());
			return sb.toString();
		}

		return "";
	}

	private Optional<? extends Setting<?>> getSelectedSetting() {
		final IStructuredSelection selection = (IStructuredSelection) settingsTableViewer.getSelection();
		if (selection == null) {
			return Optional.absent();
		}
		final Setting<?> setting = (Setting<?>) selection.getFirstElement();
		if (setting != null) {
			return Optional.of(setting);
		}
		return Optional.absent();
	}

	private void refreshSettingDescription() {
		final String desc = determineSettingDescription();
		settingDescriptionLabel.setText(desc);
		resetButton.setEnabled(!Strings.isNullOrEmpty(desc));
	}

	private static Button createButton(Composite parent, String title) {
		final Button resetAllButton = new Button(parent, SWT.PUSH);
		final GridData gd = new GridData();
		gd.widthHint = 200;
		resetAllButton.setLayoutData(gd);
		resetAllButton.setText(title);
		return resetAllButton;
	}

	private static Composite createTabComposite(TabFolder tabFolder, String title) {
		final TabItem presentationTab = new TabItem(tabFolder, SWT.NULL);
		presentationTab.setText(title);

		final Composite presentationTabComposite = new Composite(tabFolder, SWT.NONE);
		presentationTabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		presentationTabComposite.setLayout(new GridLayout());

		presentationTab.setControl(presentationTabComposite);

		return presentationTabComposite;
	}

	private static void resetSetting(Setting<?> setting) {
		if (setting.getSettingDescriptor().isEditable()) {
			setting.reset();
		}
	}

	@Override
	public void dashboardPartChanged() {
		setDirty(true);	
	}
}
