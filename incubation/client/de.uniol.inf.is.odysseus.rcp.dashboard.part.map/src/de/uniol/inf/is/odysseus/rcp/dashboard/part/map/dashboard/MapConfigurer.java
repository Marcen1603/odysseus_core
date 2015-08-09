package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.dashboard;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerTypes;


/**
 * The view for the Setup
 *
 */
public class MapConfigurer extends AbstractDashboardPartConfigurer<MapDashboardPart> {

	private static final Logger LOG = LoggerFactory.getLogger(MapConfigurer.class);

	private MapDashboardPart mapDashboardPart;
	private Collection<IPhysicalOperator> roots;
	private Table layerTable;

	@Override
	public void init(MapDashboardPart dashboardPartToConfigure, Collection<IPhysicalOperator> roots) {
		this.mapDashboardPart = dashboardPartToConfigure;
		this.roots = roots;
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
	}

	private void createMaxDataControls(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "Max Data Value");
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
		DashboardPartUtil.createLabel(topComposite, "Upate Interval (ms)");
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

		createOrderButtons(orderButtonsComp);

	}

	private void createLayerTable(Composite parent) {
		this.layerTable = new Table(parent, SWT.CHECK | SWT.FULL_SELECTION | SWT.V_SCROLL);
		layerTable.setHeaderVisible(true);
		layerTable.setLayoutData(new GridData(GridData.FILL_BOTH));

		TableColumn visibilityColumn = new TableColumn(layerTable, SWT.NULL);
		visibilityColumn.setText("Visibility");
		TableColumn titleColumn = new TableColumn(layerTable, SWT.NULL);
		titleColumn.setText("Title");
		TableColumn typeColumn = new TableColumn(layerTable, SWT.NULL);
		typeColumn.setText("Type");

		//First layer as standard
		mapDashboardPart.addLayerToMap(mapDashboardPart.getLayerCounter(),
				new LayerConfiguration(true, "GridLayer", "BasicLayer"));
		addTableItem(mapDashboardPart.getLayerCounter());
		mapDashboardPart.increaseLayerCounter();

		for (int i = 0; i < 3; i++) {
			layerTable.getColumn(i).pack();
		}
	}

	private void createAddButton(final Composite parent) {

		final Button addingButton = new Button(parent, SWT.PUSH);
		addingButton.setText("Add");
		addingButton.setVisible(true);
		addingButton.setLayoutData(new GridData(50, 20));
		addingButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				createAddShell(parent);
			}
		});
	}

	@SuppressWarnings("unused")
	private void createAddShell(Composite topComposite) {
		final Shell addingShell = new Shell(topComposite.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		addingShell.setText("Add Layer");
		addingShell.setLayout(new GridLayout(1, false));
		addingShell.setLayoutData(new GridData(GridData.FILL_BOTH));
		addingShell.setSize(400, 500);

		Composite basisComp = new Composite(addingShell, SWT.NONE);
		basisComp.setLayout(new GridLayout(2, false));
		basisComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Composite layerDependedComp = new Composite(addingShell, SWT.NONE);
		layerDependedComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		layerDependedComp.setLayout(new GridLayout(2, false));

		Composite buttonComp = new Composite(addingShell, SWT.CENTER);
		buttonComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonComp.setLayout(new GridLayout(2, false));

		Label addingTitleLabel = DashboardPartUtil.createLabel(basisComp, "Title");
		final Text addingTitleText = DashboardPartUtil.createText(basisComp, "");
		addingTitleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label addingTitleType = DashboardPartUtil.createLabel(basisComp, "Type");

		// TODO MAKE IT GENERATED
		String[] layerTypes = { LayerTypes.BASICLAYER.toString(), LayerTypes.OSM_LAYER.toString() };
		final Combo layerTypeCombo = DashboardPartUtil.createCombo(basisComp, layerTypes);
		layerTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layerTypeCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String layerCase = layerTypeCombo.getText();

				for( Control c : layerDependedComp.getChildren()) {
					c.dispose();
				}
				
				switch (layerCase) {
				case "BASICLAYER":

					Label gridSizeLabel = DashboardPartUtil.createLabel(layerDependedComp, "Grid Size");
					Text gridSizeText = DashboardPartUtil.createText(layerDependedComp, "5");
					gridSizeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

					break;

				case "OMS_LAYER":
					break;
					
				default:
					LOG.error("Unknown layer type '{}'", layerCase);
					break;
				}
				
				addingShell.layout();
			}
		});

		Button confirmButton = new Button(buttonComp, SWT.PUSH | SWT.CENTER);
		confirmButton.setText("Confirm");
		confirmButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO CHECK IF EVERYTHING WAS FILLED CORRECTLY

				LayerConfiguration layerConf = new LayerConfiguration(true, addingTitleText.getText(),
						layerTypeCombo.getText());
				mapDashboardPart.addLayerToMap(mapDashboardPart.getLayerCounter(), layerConf);

				addTableItem(mapDashboardPart.getLayerCounter());
				mapDashboardPart.increaseLayerCounter();
				reprintLayerTable();
				addingShell.close();	
			}
		});

		Button cancelButton = new Button(buttonComp, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addingShell.close();
			}
		});

		addingShell.open();
	}

	private void createDeleteButton(final Composite parent) {

		Button deleteButton = new Button(parent, SWT.PUSH);
		deleteButton.setText("Delete");
		deleteButton.setLayoutData(new GridData(50, 20));
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

		Label deleteLabel = DashboardPartUtil.createLabel(deleteComp, "Delete Layer " + layerId + "?");

		Composite buttonComp = new Composite(deleteComp, SWT.NONE);
		buttonComp.setLayout(new GridLayout(2, false));
		buttonComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button confirmDeleteButton = new Button(buttonComp, SWT.PUSH);
		confirmDeleteButton.setText("Confirm");
		confirmDeleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mapDashboardPart.deleteLayerFromMap(layerId);
				deleteTableItem(layerId);
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

	private void createWarningShell(Composite topComposite, String text) {
		final Shell warningShell = new Shell(topComposite.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CENTER);
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

	private void createEditButton(final Composite parent) {
		final Button configurateButton = new Button(parent, SWT.PUSH);
		configurateButton.setText("Configurate");
		configurateButton.setLayoutData(new GridData(80, 20));
		configurateButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTable.getSelectionIndex() < 0) {
					createWarningShell(parent, "Select row!");
				} else {
					createEditShell(parent, layerTable.getSelectionIndex());
				}
			}
		});

	}

	// TODO Fill and change content depending on layertype
	private void createEditShell(Composite parent, int layerId) {

		Shell configurateShell = new Shell(parent.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		configurateShell.setText("Configurate Layer");
		configurateShell.setSize(300, 500);

		Label layerConfTitle = new Label(configurateShell, SWT.NONE);
		layerConfTitle.setText("Title");
		Text layerConfTitleText = new Text(configurateShell, SWT.BORDER);
		layerConfTitleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		LayerConfiguration layerToConfigurate = mapDashboardPart.getLayerList().get(layerTable.getSelectionIndex());

		configurateShell.open();
	}

	private void createOrderButtons(final Composite parent) {
		Button topButton = new Button(parent, SWT.PUSH);
		topButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		topButton.setText("Top");
		topButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTable.getSelectionIndex() < 0) {
					createWarningShell(parent, "Select row!");
				} else if(layerTable.getSelectionIndex() > 0){
					mapDashboardPart.changeLayerOrder(0, layerTable.getSelectionIndex());
					reprintLayerTable();
				} else{
					createWarningShell(parent, "Already zero layer!");
				}
			}
		});

		Button upButton = new Button(parent, SWT.PUSH);
		upButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		upButton.setText("Up");
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTable.getSelectionIndex() < 0) {
					createWarningShell(parent, "Select row!");
				} else if(layerTable.getSelectionIndex() > 0){
					mapDashboardPart.changeLayerOrder(layerTable.getSelectionIndex()-1, layerTable.getSelectionIndex());
					reprintLayerTable();
				} else{
					createWarningShell(parent, "Already zero layer!");
				}
			}
		});

		Button downButton = new Button(parent, SWT.PUSH);
		downButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		downButton.setText("Down");
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTable.getSelectionIndex() < 0) {
					createWarningShell(parent, "Select row!");
				} else if(layerTable.getSelectionIndex() < layerTable.getItemCount()-1){
					mapDashboardPart.changeLayerOrder(layerTable.getSelectionIndex()+1, layerTable.getSelectionIndex());
					reprintLayerTable();
				} else {
					createWarningShell(parent, "Already top layer");
				}
			}
		});

		Button bottomButton = new Button(parent, SWT.PUSH);
		bottomButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bottomButton.setText("Bottom");
		bottomButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTable.getSelectionIndex() < 0) {
					createWarningShell(parent, "Select row!");
				} else if(layerTable.getSelectionIndex() < layerTable.getItemCount()-1){
					mapDashboardPart.changeLayerOrder(layerTable.getItemCount()-1, layerTable.getSelectionIndex());
					reprintLayerTable();
				} else {
					createWarningShell(parent, "Already top layer");
				}
			}
		});
	}

	private void addTableItem(int id) {

		LayerConfiguration layerConf = mapDashboardPart.getLayerList().get(id);
		TableItem item = new TableItem(layerTable, SWT.NULL);
		item.setText(0, "");
		item.setText(1, layerConf.getTitle());
		item.setText(2, layerConf.getType());
		layerTable.getItem(id).setChecked(true);
	}

	private void deleteTableItem(int id) {
		layerTable.remove(id);
	}
	
	private void reprintLayerTable(){
		layerTable.removeAll();
		for(int i = 0; i < mapDashboardPart.getLayerCounter(); i++){
			addTableItem(i);
		}
	}

	@Override
	public void dispose() {

	}

}
