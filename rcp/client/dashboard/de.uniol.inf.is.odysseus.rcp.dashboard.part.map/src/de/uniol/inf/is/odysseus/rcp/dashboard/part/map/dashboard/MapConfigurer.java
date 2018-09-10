package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPartConfigurer;
import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPartUtil;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.OwnProperties;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.AddMapLayerDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.EditDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.properties.HeatmapPropertiesDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dialog.properties.TracemapPropertiesDialog;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.BasicLayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.ILayer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.HeatmapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.NullConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.RasterLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.TracemapLayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.buffer.MaxTupleListener;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.heatmap.Heatmap;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.tracemap.TraceLayer;

/**
 * The configurer of the {@link MapDashboardPart}.
 *
 */
public class MapConfigurer extends AbstractDashboardPartConfigurer<MapDashboardPart> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(MapConfigurer.class);

	Collection<IPhysicalOperator> roots;

	private MapDashboardPart mapDashboardPart;

	// private TimeSliderComposite timeSliderComposite;
	private Table layerTable;

	private Button editButton, upButton, downButton, topButton, bottomButton;

	@Override
	public void init(MapDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots) {
		this.mapDashboardPart = dashboardPartToConfigure;
		this.mapDashboardPart.init();
		this.roots = roots;
		mapDashboardPart.setOperators(roots);
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

		createLayerOrderingControls(layerSettingComp);

		createStandardLayers();
		reprintLayerTable();

		// if (!mapDashboardPart.getWizardBoolean()) {
		// DashboardPartUtil.createLabel(topComposite, "CARE! The time slider is
		// not working properly!");
		// createTimeSliderComposite(topComposite);
		// }
	}

	/**
	 * Creates a standard set of layers. Makes it easier for the user to get a
	 * basic map. 
	 */
	private void createStandardLayers() {
		if (this.mapDashboardPart.getNumberOfLayers() > 0) {
			return;
		}

		// Here we get properties for the layer
		OwnProperties properties = new OwnProperties();

		// TODO A basic layer
		NullConfiguration basicConfiguration = new NullConfiguration();
		basicConfiguration.setName("Basic");
		this.mapDashboardPart.addLayer(basicConfiguration);
		
		// A layer that shows a simple OpenStreetMap
		RasterLayerConfiguration layerConfiguration = new RasterLayerConfiguration("Background");
		layerConfiguration.setSrid(3857);
		layerConfiguration.setUrl("http://otile2.mqcdn.com/tiles/1.0.0/osm/");
		properties.getTileServer(1, layerConfiguration);

		this.mapDashboardPart.addLayer(layerConfiguration);
	}

	private void createMaxDataControls(final Composite topComposite) {
		Label maxDataLabel = DashboardPartUtil.createLabel(topComposite, "Max. Data");
		maxDataLabel.setToolTipText("Max value of DataStreamElements are processed");
		final Spinner maxTuples = new Spinner(topComposite, SWT.NONE);
		maxTuples.setValues(mapDashboardPart.getMaxData(), 0, Integer.MAX_VALUE, 0, 1, 1);
		maxTuples.addSelectionListener(new MaxTupleListener(mapDashboardPart.getPuffer(), maxTuples));

		maxTuples.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (Integer.valueOf(maxTuples.getText()) < 0) {
					MessageDialog.openInformation(topComposite.getShell(), "Warning",
							"Negative numbers are forbidden!");
				} else {
					mapDashboardPart.setMaxData(Integer.valueOf(maxTuples.getText()));
					fireListener();
				}
			}
		});
	}

	private void createUpdateIntervalControls(final Composite topComposite) {
		Label updateIntervall = DashboardPartUtil.createLabel(topComposite, "Upate Interval (in s)");
		updateIntervall.setToolTipText("Updates the map in the given time intervall");
		final Text updateIntervalText = DashboardPartUtil.createText(topComposite,
				String.valueOf(mapDashboardPart.getUpdateInterval()));
		updateIntervalText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		updateIntervalText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (Integer.valueOf(updateIntervalText.getText()) < 0) {
					MessageDialog.openInformation(topComposite.getShell(), "Warning",
							"Negative numbers are forbidden!");
				} else {
					mapDashboardPart.setUpdateInterval(Integer.valueOf(updateIntervalText.getText()));
					fireListener();
				}
			}
		});
	}

	private void createLayerOrderingControls(Composite parent) {

		Composite tableOrderingComp = new Composite(parent, SWT.NONE);
		tableOrderingComp.setLayout(new GridLayout(1, true));
		tableOrderingComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label layerSettingsLabel = DashboardPartUtil.createLabel(tableOrderingComp, "Layer Ordering");
		layerSettingsLabel.setToolTipText("If no layer was added, a basic layer will be added");

		Composite tableComp = new Composite(tableOrderingComp, SWT.NONE);
		tableComp.setLayout(new GridLayout(1, true));
		tableComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite buttonComp = new Composite(tableOrderingComp, SWT.NONE);
		buttonComp.setLayout(new GridLayout(3, false));
		buttonComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite orderButtonsComp = new Composite(parent, SWT.CENTER);
		orderButtonsComp.setLayout(new GridLayout(1, true));
		orderButtonsComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		createLayerTable(tableComp);

		createAddButton(buttonComp);
		createDeleteButton(buttonComp);
		createEditButton(buttonComp);

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

					if (layerTable.getItemCount() == 1) {
						topButton.setEnabled(false);
						upButton.setEnabled(false);
						bottomButton.setEnabled(false);
						downButton.setEnabled(false);

					} else if (index == 0) {
						topButton.setEnabled(true);
						upButton.setEnabled(true);

						bottomButton.setEnabled(false);
						downButton.setEnabled(false);
					} else if (index == layerTable.getItemCount() - 1) {
						topButton.setEnabled(false);
						upButton.setEnabled(false);

						bottomButton.setEnabled(true);
						downButton.setEnabled(true);
					} else {
						topButton.setEnabled(true);
						upButton.setEnabled(true);

						bottomButton.setEnabled(true);
						downButton.setEnabled(true);
					}
				}
			}

		});

		layerTable.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (!mapDashboardPart.getWizardBoolean()) {
					LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

					for (int i = 0; i < group.size(); i++) {
						if (group.get(i).isActive() != layerTable.getItem(i).getChecked()) {
							mapDashboardPart.setActive(group.get(i), layerTable.getItem(i).getChecked());
							fireListener();
						}
					}
				}
			}
		});

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

				AddMapLayerDialog addDialog = new AddMapLayerDialog(parent.getShell(), roots,
						mapDashboardPart.getMapEditorModel().getLayers());
				addDialog.create();
				addDialog.open();
				if (addDialog.getReturnCode() == Window.OK) {

					mapDashboardPart.addLayer(addDialog.getLayerConfiguration());

					int layerPositionAfter = addDialog.getLayerPositionAfter();
					int layerSize = mapDashboardPart.getMapEditorModel().getLayers().size();
					if (!(layerPositionAfter == layerSize - 2)) {
						for (int index = layerSize - 1; index > layerPositionAfter; index--) {
							LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();
							mapDashboardPart.layerUp(group.get(index));
						}
					}

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
					MessageDialog.openInformation(parent.getShell(), "Warning", "No layer selected!");
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
		this.editButton = createButton(parent, "Edit");
		editButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTable.getSelectionIndex() < 0) {
					MessageDialog.openInformation(parent.getShell(), "Warning", "No layer selected!");
				} else {
					LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();
					int index = layerTable.getSelectionIndex();

					if (group.get(index) instanceof Heatmap) {
						HeatmapPropertiesDialog propertiesDialog = new HeatmapPropertiesDialog(parent.getShell(),
								mapDashboardPart, (Heatmap) group.get(index), roots);
						propertiesDialog.create();
						propertiesDialog.open();
						if (propertiesDialog.getReturnCode() == Window.OK) {
							mapDashboardPart.editLayer(group.get(index), propertiesDialog.getLayerConfiguration());
							reprintLayerTable();
							fireListener();
						}
					} else if (group.get(index) instanceof TraceLayer) {
						TracemapPropertiesDialog propertiesDialog = new TracemapPropertiesDialog(parent.getShell(),
								(TraceLayer) group.get(index), roots);
						propertiesDialog.create();
						propertiesDialog.open();
						if (propertiesDialog.getReturnCode() == Window.OK) {
							mapDashboardPart.editLayer(group.get(index), propertiesDialog.getLayerConfiguration());
							reprintLayerTable();
							fireListener();
						}
					} else {

						EditDialog editDialog = new EditDialog(parent.getShell(), group,
								group.get(index).getConfiguration(), index);
						editDialog.create();
						editDialog.open();
						if (editDialog.getReturnCode() == Window.OK) {
							mapDashboardPart.editLayer(group.get(index), editDialog.getLayerConfiguration());

							// TODO
							if (index > editDialog.getLayerPositionAfter()
									&& index != editDialog.getLayerPositionAfter() + 1) {
								for (int i = index; i > editDialog.getLayerPositionAfter() + 1; i--) {
									mapDashboardPart.layerDown(group.get(i));
								}
							} else if (index < editDialog.getLayerPositionAfter()) {
								for (int i = index; i < editDialog.getLayerPositionAfter(); i++) {
									mapDashboardPart.layerUp(group.get(i));
								}
							}

							reprintLayerTable();
							fireListener();
						}
					}
				}

			}
		});

	}

	private void createOrderButtons(final Composite parent) {
		Label horizontalSpacer = new Label(parent, SWT.NONE);
		int height = 50;
		horizontalSpacer.setLayoutData(new GridData(1, height));

		this.bottomButton = createButton(parent, "Bottom");
		bottomButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tableIndex = layerTable.getSelectionIndex();
				LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

				if (tableIndex < 0) {
					MessageDialog.openInformation(parent.getShell(), "Warning", "No layer selected!");
				} else if (tableIndex > 0) {
					mapDashboardPart.getMapEditorModel().getLayers().get(tableIndex);
					mapDashboardPart.layerBottom(group.get(tableIndex));
					reprintLayerTable();
					fireListener();
				}
			}
		});

		this.downButton = createButton(parent, "Down");
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tableIndex = layerTable.getSelectionIndex();
				LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

				if (tableIndex < 0) {
					MessageDialog.openInformation(parent.getShell(), "Warning", "No layer selected!");
				} else if (tableIndex > 0) {
					mapDashboardPart.getMapEditorModel().getLayers().get(tableIndex);
					mapDashboardPart.layerDown(group.get(tableIndex));
					reprintLayerTable();
					fireListener();
				}
			}
		});

		this.upButton = createButton(parent, "Up");
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tableIndex = layerTable.getSelectionIndex();
				LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

				if (tableIndex < 0) {
					MessageDialog.openInformation(parent.getShell(), "Warning", "No layer selected!");
				} else if (tableIndex < group.size() - 1) {
					mapDashboardPart.getMapEditorModel().getLayers().get(tableIndex);
					mapDashboardPart.layerUp(group.get(tableIndex));
					reprintLayerTable();
					fireListener();
				}
			}
		});

		this.topButton = createButton(parent, "Top");
		topButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int tableIndex = layerTable.getSelectionIndex();
				LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();

				if (tableIndex < 0) {
					MessageDialog.openInformation(parent.getShell(), "Warning", "No layer selected!");
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

	private void reprintLayerTable() {
		LinkedList<ILayer> group = mapDashboardPart.getMapEditorModel().getLayers();
		layerTable.removeAll();

		for (ILayer layer : group) {
			LayerConfiguration layerConf = layer.getConfiguration();

			TableItem item = new TableItem(layerTable, SWT.NULL);
			item.setText(0, "");

			if (layerConf instanceof HeatmapLayerConfiguration) {
				if (layer.isActive()) {
					item.setChecked(true);
				} else {
					item.setChecked(false);
				}
				item.setText(1, layerConf.getName());
				item.setText(2, "Heatmap Layer");
			} else if (layerConf instanceof TracemapLayerConfiguration) {
				if (layer.isActive()) {
					item.setChecked(true);
				} else {
					item.setChecked(false);
				}
				item.setText(1, layerConf.getName());
				item.setText(2, "Tracemap Layer");
			} else if (layerConf instanceof RasterLayerConfiguration) {
				if (layer.isActive()) {
					item.setChecked(true);
				} else {
					item.setChecked(false);
				}
				item.setText(1, layerConf.getName());
				item.setText(2, "Map Layer");
			} else {
				item.setText(1, layer.getName() == null ? "" : layer.getName());
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

	@Override
	public void dispose() {

	}

}
