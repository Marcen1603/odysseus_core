package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.EditDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.PropertyTitleDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;

/**
 * The MapConfigurer of the {@link MapDashboardPart}. This is what you can see
 * in settings
 *
 */
public class MapConfigurer extends AbstractDashboardPartConfigurer<MapDashboardPart> {

	private static final Logger LOG = LoggerFactory.getLogger(MapConfigurer.class);

	private IPhysicalOperator operator;

	private MapDashboardPart mapDashboardPart;
	private Collection<IPhysicalOperator> roots;
	private Table layerTable;

	private Button editButton;

	@Override
	public void init(MapDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots) {
		this.mapDashboardPart = dashboardPartToConfigure;
		this.roots = roots;
		this.mapDashboardPart.initMapModel();
		operator = determinePyhsicalRoot(roots);
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayout(new GridLayout(1, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite dashboardPartSettingComposite = new Composite(topComposite, SWT.NONE);
		dashboardPartSettingComposite.setLayout(new GridLayout(2, false));
		dashboardPartSettingComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite layerSettingComp = new Composite(topComposite, SWT.NONE);
		layerSettingComp.setLayout(new GridLayout(2, false));
		layerSettingComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMaxDataControls(dashboardPartSettingComposite);
		createUpdateIntervalControls(dashboardPartSettingComposite);

		createLayerSettingsControls(layerSettingComp);

		reprintLayerTable();
	}

	private void createMaxDataControls(Composite topComposite) {
		Label maxDataLabel = DashboardPartUtil.createLabel(topComposite, "Max. Data");
		maxDataLabel.setToolTipText("Max value of DataStreamElements are processed");
		final Text maxDataText = DashboardPartUtil.createText(topComposite,
				String.valueOf(mapDashboardPart.getMaxData()));
		maxDataText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		maxDataText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				mapDashboardPart.setMaxData(Integer.valueOf(maxDataText.getText()));
				fireListener();
			}
		});
	}

	private void createUpdateIntervalControls(Composite topComposite) {
		Label updateIntervall = DashboardPartUtil.createLabel(topComposite, "Upate Interval (ms)");
		updateIntervall.setToolTipText("Updates the map in the given time intervall");
		final Text updateIntervalText = DashboardPartUtil.createText(topComposite,
				String.valueOf(mapDashboardPart.getUpdateInterval()));
		updateIntervalText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		updateIntervalText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				mapDashboardPart.setUpdateInterval(Integer.valueOf(updateIntervalText.getText()));
				fireListener();
			}
		});
	}

	private void createLayerSettingsControls(Composite parent) {

		Composite tableSettingComp = new Composite(parent, SWT.NONE);
		tableSettingComp.setLayout(new GridLayout(1, true));
		tableSettingComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		DashboardPartUtil.createLabel(tableSettingComp, "Layer Settings");

		Composite tableComp = new Composite(tableSettingComp, SWT.NONE);
		tableComp.setLayout(new GridLayout(1, true));
		tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite buttonComp = new Composite(tableSettingComp, SWT.NONE);
		buttonComp.setLayout(new GridLayout(3, false));
		buttonComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite orderButtonsComp = new Composite(parent, SWT.CENTER);
		orderButtonsComp.setLayout(new GridLayout(1, true));
		orderButtonsComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		createLayerTable(tableComp);

		createAddButton(buttonComp);
		createDeleteButton(buttonComp);
		createEditButton(buttonComp);
		createRefreshButton(buttonComp);

		createOrderButtons(orderButtonsComp);

	}

	private void createLayerTable(final Composite parent) {
		this.layerTable = new Table(parent, SWT.CHECK | SWT.FULL_SELECTION | SWT.V_SCROLL);
		layerTable.setHeaderVisible(true);
		layerTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		layerTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = layerTable.getSelectionIndex();
				if (index >= 0) {
					ILayer layer = mapDashboardPart.getMapEditorModel().getLayers().get(index);
					if (layer instanceof BasicLayer) {
						editButton.setEnabled(false);
					} else {
						editButton.setEnabled(true);
					}
				}
			}

		});

		// layerTable.addListener(SWT.Selection, new Listener() {
		// @Override
		// public void handleEvent(Event event) {
		// System.out.println("GetLayers()" +
		// mapDashboardPart.getMapEditorModel().getLayers());
		// System.out.println("EventButton" + event.button);
		//
		// boolean checked = event.detail == SWT.CHECK ? true : false;
		// System.out.println(event.detail);
		// System.out.println(checked);
		// mapDashboardPart.setActive(mapDashboardPart.getMapEditorModel().getLayers().get(event.button),
		// checked);
		// }
		// });

		TableColumn visibilityColumn = new TableColumn(layerTable, SWT.NULL);
		visibilityColumn.setText("Visibility");
		TableColumn titleColumn = new TableColumn(layerTable, SWT.NULL);
		titleColumn.setText("Title");
		TableColumn typeColumn = new TableColumn(layerTable, SWT.NULL);
		typeColumn.setText("Type");

		for (int i = 0; i < 3; i++) {
			layerTable.getColumn(i).pack();
		}

	}

	private void createAddButton(final Composite parent) {

		final Button addingButton = createButton(parent, "Add");
		addingButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				PropertyTitleDialog addDialog = new PropertyTitleDialog(parent.getShell(), operator,
						mapDashboardPart.getMapEditorModel().getLayers());
				addDialog.create();
				addDialog.open();
				if (addDialog.getReturnCode() == Window.OK) {

					mapDashboardPart.addLayer(addDialog.getLayerConfiguration());

					reprintLayerTable();
					fireListener();
				} else {
					// I guess user knows that this happens if he clicked
					// "cancel"
					// MessageDialog.openInformation(shell, "Information", "No
					// layer added to the map.");
				}
			}
		});
	}

	private void createDeleteButton(final Composite parent) {

		Button deleteButton = createButton(parent, "Delete");
		deleteButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTable.getSelectionIndex() < 0) {
					createWarningShell(parent, "Select row!");
				} else {
					createDeleteShell(parent, layerTable.getSelectionIndex());
				}
			}
		});
	}

	@SuppressWarnings("unused")
	private void createDeleteShell(Composite parent, final int layerId) {

		final Shell deleteShell = new Shell(parent.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		deleteShell.setText("Delete Layer");
		deleteShell.setSize(200, 100);
		deleteShell.setLayout(new GridLayout(1, false));
		deleteShell.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite deleteComp = new Composite(deleteShell, SWT.NONE);
		deleteComp.setLayout(new GridLayout(1, true));
		deleteComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		String[] layerNames = mapDashboardPart.getMapEditorModel().getLayerNameList();
		Label deleteLabel = DashboardPartUtil.createLabel(deleteComp, "Delete Layer " + layerNames[layerId] + "?");

		Composite buttonComp = new Composite(deleteComp, SWT.NONE);
		buttonComp.setLayout(new GridLayout(2, false));
		buttonComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button confirmDeleteButton = new Button(buttonComp, SWT.PUSH);
		confirmDeleteButton.setText("Confirm");
		confirmDeleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				mapDashboardPart.getMapEditorModel().getLayers().get(layerId);
				LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

				mapDashboardPart.removeLayer(group.get(layerId));
				fireListener();
				reprintLayerTable();
				deleteShell.close();

			}
		});

		Button canceleleteButton = new Button(buttonComp, SWT.PUSH);
		canceleleteButton.setText("Cancel");
		canceleleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteShell.close();
			}
		});

		deleteShell.open();
	}

	private void createEditButton(final Composite parent) {
		final Button editButton = createButton(parent, "Edit");
		this.editButton = editButton;
		editButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTable.getSelectionIndex() < 0) {
					createWarningShell(parent, "Select row!");
				} else {
					LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();
					int index = layerTable.getSelectionIndex();
					EditDialog editDialog = new EditDialog(parent.getShell(), group,
							group.get(index).getConfiguration(), index);
					editDialog.create();
					editDialog.open();
					if (editDialog.getReturnCode() == Window.OK) {
						mapDashboardPart.editLayer(group.get(index), editDialog.getLayerConfiguration());
						reprintLayerTable();
						fireListener();
					}
				}
			}
		});

	}

	// TODO
	private void createRefreshButton(final Composite parent) {
		final Button refresButton = createButton(parent, "Refresh");
		refresButton.setToolTipText("If for any case the map is not shown");
		refresButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});

	}

	private void createWarningShell(Composite topComposite, String text) {
		final Shell warningShell = new Shell(topComposite.getShell(),
				SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CENTER);
		warningShell.setText("Warning");
		warningShell.setSize(200, 100);
		warningShell.setLayout(new GridLayout(1, false));
		warningShell.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(warningShell, SWT.CENTER);
		label.setText(text);

		Button confirmDeleteButton = new Button(warningShell, SWT.PUSH | SWT.CENTER);
		confirmDeleteButton.setText("Confirm");
		confirmDeleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				warningShell.close();
			}
		});

		warningShell.open();
	}

	// TODO BUTTONS AUSGRAUEN, FALLS DIE GEWAEHLTE SCHICHT SCHON TOP ODER BOTTOM
	// IST
	private void createOrderButtons(final Composite parent) {

		Label horizontalSpacer = new Label(parent, SWT.NONE);
		int height = 50;
		horizontalSpacer.setLayoutData(new GridData(1, height));

		Button bottomButton = createButton(parent, "Bottom");
		bottomButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tableIndex = layerTable.getSelectionIndex();
				LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

				if (tableIndex < 0) {
					createWarningShell(parent, "Select row!");
				} else if (tableIndex > 0) {
					mapDashboardPart.getMapEditorModel().getLayers().get(tableIndex);
					mapDashboardPart.layerBottom(group.get(tableIndex));
					reprintLayerTable();
					fireListener();
				}
			}
		});

		Button downButton = createButton(parent, "Down");
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tableIndex = layerTable.getSelectionIndex();
				LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

				if (tableIndex < 0) {
					createWarningShell(parent, "Select row!");
				} else if (tableIndex > 0) {
					mapDashboardPart.getMapEditorModel().getLayers().get(tableIndex);
					mapDashboardPart.layerDown(group.get(tableIndex));
					reprintLayerTable();
					fireListener();
				}
			}
		});

		Button upButton = createButton(parent, "Up");
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tableIndex = layerTable.getSelectionIndex();
				LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

				if (tableIndex < 0) {
					createWarningShell(parent, "Select row!");
				} else if (tableIndex < group.size() - 1) {
					mapDashboardPart.getMapEditorModel().getLayers().get(tableIndex);
					mapDashboardPart.layerUp(group.get(tableIndex));
					reprintLayerTable();
					fireListener();
				}
			}
		});

		Button topButton = createButton(parent, "Top");
		topButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tableIndex = layerTable.getSelectionIndex();
				LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

				if (tableIndex < 0) {
					createWarningShell(parent, "Select row!");
				} else if (tableIndex < group.size() - 1) {
					mapDashboardPart.getMapEditorModel().getLayers().get(tableIndex);
					mapDashboardPart.layerTop(group.get(tableIndex));
					reprintLayerTable();
					fireListener();
				}
			}
		});

	}

	private Button createButton(Composite parent, String name) {
		Button button = new Button(parent, SWT.PUSH);
		button.setLayoutData(new GridData(50, 20));
		button.setText(name);

		return button;
	}

	// TODO HEATMAP Anpassung
	private void reprintLayerTable() {
		LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();
		layerTable.removeAll();

		for (ILayer layer : group) {
			LayerConfiguration layerConf = layer.getConfiguration();

			TableItem item = new TableItem(layerTable, SWT.NULL);
			item.setText(0, "");

			if (layerConf instanceof RasterLayerConfiguration) {
				if (layer.isActive()) {
					item.setChecked(true);
				} else {
					item.setChecked(false);
				}
				item.setText(1, layerConf.getName());
				item.setText(2, "Raster Layer");
			} else if (layerConf instanceof HeatmapLayerConfiguration) {
				if (layer.isActive()) {
					item.setChecked(true);
				} else {
					item.setChecked(false);
				}
				item.setText(1, layerConf.getName());
				item.setText(2, "Heatmap Layer");
			} else {
				item.setText(1, layer.getName());
				item.setText(2, "Basic Layer");
			}

			if (layer.isActive()) {
				layerTable.getItem(group.indexOf(layer)).setChecked(true);
			}
		}

		// Resizes the table columns
		for (TableColumn tc : layerTable.getColumns())
			tc.pack();
		;
	}

	private IPhysicalOperator determinePyhsicalRoot(Collection<IPhysicalOperator> physicalRoots) {
		for (IPhysicalOperator p : physicalRoots) {
				return p;
		}
		LOG.info("Select first physical root.");
		return physicalRoots.iterator().next();
	}

	@Override
	public void dispose() {

	}

}
