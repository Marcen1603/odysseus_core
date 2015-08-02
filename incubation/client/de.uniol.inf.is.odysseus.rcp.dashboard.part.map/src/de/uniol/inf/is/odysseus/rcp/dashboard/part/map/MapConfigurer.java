package de.uniol.inf.is.odysseus.rcp.dashboard.part.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.security.auth.callback.ConfirmationCallback;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
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
import de.uniol.inf.is.odysseus.rcp.dashboard.editors.Dashboard;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerTypes;

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
		topComposite.setLayout(new GridLayout(2, false));
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMaxDataControls(topComposite);
		createUpdateIntervalControls(topComposite);

		createLayerSettingsControls(topComposite);

		createAddButton(topComposite);
		createDeleteButton(topComposite);
		createConfigurateButton(topComposite);
		createOrderButtons(topComposite);

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

	private void createLayerSettingsControls(Composite topComposite) {

		DashboardPartUtil.createLabel(topComposite, "Layer Settings");
		this.layerTable = new Table(topComposite, SWT.CHECK | SWT.FULL_SELECTION | SWT.V_SCROLL);
		layerTable.setHeaderVisible(true);
		layerTable.setLayoutData(new GridData(GridData.FILL_BOTH));

		TableColumn visibilityColumn = new TableColumn(layerTable, SWT.NULL);
		visibilityColumn.setText("Visibility");
		TableColumn titleColumn = new TableColumn(layerTable, SWT.NULL);
		titleColumn.setText("Title");
		TableColumn typeColumn = new TableColumn(layerTable, SWT.NULL);
		typeColumn.setText("Type");

		mapDashboardPart.addLayerToMap(mapDashboardPart.getLayerCounter(),
				new LayerConfiguration(true, "Rasterschicht", "BasicLayer"));
		addTableItem(mapDashboardPart.getLayerCounter());
		mapDashboardPart.increaseLayerCounter();

		for (int i = 0; i < 3; i++) {
			layerTable.getColumn(i).pack();
		}
	}

	private void createAddButton(Composite topComposite) {

		final Button addingButton = new Button(topComposite, SWT.PUSH);
		addingButton.setText("Add");
		addingButton.setVisible(true);
		addingButton.setLayoutData(new GridData(50, 20));
		addingButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				createAddShell(topComposite);
			}
		});
	}

	private void createAddShell(Composite topComposite) {
		final Shell addingShell = new Shell(topComposite.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		addingShell.setText("Add Layer");
		addingShell.setLayout(new GridLayout(2, false));
		addingShell.setSize(400, 500);

		Label addingTitleLabel = DashboardPartUtil.createLabel(addingShell, "Title");
		Text addingTitleText = DashboardPartUtil.createText(addingShell, "");
		addingTitleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label addingTitleType = DashboardPartUtil.createLabel(addingShell, "Type");

		String[] layerTypes = { LayerTypes.BASICLAYER.toString(), LayerTypes.OSM_LAYER.toString() };
		Combo layerTypeCombo = DashboardPartUtil.createCombo(addingShell, layerTypes);

		layerTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite layerTypeComp = new Composite(addingShell, SWT.NONE);
		layerTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layerTypeComp.setLayout(new GridLayout(1, true));

		layerTypeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String layerCase = layerTypeCombo.getText();

				switch (layerCase) {
				case "BasicLayer":
					Label GridSizeLabel = DashboardPartUtil.createLabel(layerTypeCombo, "Grid Size");
					Text GridSizeText = DashboardPartUtil.createText(layerTypeComp, "");
					GridSizeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

					addingShell.update();
					break;

				case "OMS_LAYER":
					break;

				}
			}
		});

		Button confirmButton = new Button(addingShell, SWT.PUSH);
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

				addingShell.close();
				topComposite.update();
			}
		});

		Button cancelButton = new Button(addingShell, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addingShell.close();
			}
		});

		addingShell.open();

	}

	private void createDeleteButton(Composite topComposite) {

		Button deleteButton = new Button(topComposite, SWT.PUSH);
		deleteButton.setText("Delete");
		deleteButton.setLayoutData(new GridData(50, 20));
		deleteButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (layerTable.getSelectionIndex() < 0) {
					createWarningShell(topComposite, "Select row!");
				} else {
					createDeleteShell(topComposite, layerTable.getSelectionIndex());
				}
			}
		});
	}

	private void createDeleteShell(Composite topComposite, int layerId) {

		Shell deleteShell = new Shell(topComposite.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
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
				topComposite.update();
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
		Shell warningShell = new Shell(topComposite.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CENTER);
		warningShell.setText("Warning");
		warningShell.setSize(200, 100);
		warningShell.setLayout(new GridLayout(1, true));
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

	private void createConfigurateButton(Composite topComposite) {
		final Button configurateButton = new Button(topComposite, SWT.PUSH);
		configurateButton.setText("Configurate");
		configurateButton.setLayoutData(new GridData(50, 20));

		final Shell configurateShell = new Shell(topComposite.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		configurateShell.setText("Configurate Layer");
		configurateShell.setSize(300, 500);

		Label layerConfTitle = new Label(configurateShell, SWT.NONE);
		layerConfTitle.setText("Title");
		Text layerConfTitleText = new Text(configurateShell, SWT.BORDER);
		layerConfTitleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		LayerConfiguration layerToConfigurate = mapDashboardPart.getLayerList().get(layerTable.getSelectionIndex());

		configurateButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

	}

	private void createOrderButtons(Composite topComposite) {
		DashboardPartUtil.createLabel(topComposite, "UP");
		DashboardPartUtil.createLabel(topComposite, "DOWN");
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
		LayerConfiguration layerConf = mapDashboardPart.getLayerList().get(id);
		layerTable.remove(id);

	}

	@Override
	public void dispose() {

	}

}
