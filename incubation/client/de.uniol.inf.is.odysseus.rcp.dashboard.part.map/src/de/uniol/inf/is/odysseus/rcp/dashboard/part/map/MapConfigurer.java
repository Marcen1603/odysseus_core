package de.uniol.inf.is.odysseus.rcp.dashboard.part.map;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.security.auth.callback.ConfirmationCallback;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerTypes;


public class MapConfigurer extends AbstractDashboardPartConfigurer<MapDashboardPart>{
	
	private static final Logger LOG = LoggerFactory.getLogger(MapConfigurer.class);
	
	private MapDashboardPart mapDashboardPart;
	private Collection<IPhysicalOperator> roots;
	private Table layerTable;
	

	@Override
	public void init(MapDashboardPart dashboardPartToConfigure,
			Collection<IPhysicalOperator> roots) {
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
	
	private void createMaxDataControls(Composite topComposite){
		DashboardPartUtil.createLabel(topComposite, "Max Data Value");
		final Text maxDataText = DashboardPartUtil.createText(topComposite, String.valueOf(mapDashboardPart.getMaxData()));
		maxDataText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		maxDataText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				mapDashboardPart.setMaxData(Integer.valueOf(maxDataText.getText()));
				fireListener();
				}
		});
	}
	
	private void createUpdateIntervalControls(Composite topComposite){
		DashboardPartUtil.createLabel(topComposite, "Upate Interval (ms)");
		final Text updateIntervalText = DashboardPartUtil.createText(topComposite, String.valueOf(mapDashboardPart.getUpdateInterval()));
		updateIntervalText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		updateIntervalText.addModifyListener(new ModifyListener(){
			@Override
			public void modifyText(ModifyEvent e){
				mapDashboardPart.setUpdateInterval(Integer.valueOf(updateIntervalText.getText()));
				fireListener();
			}
		});
	}

	private void createLayerSettingsControls(Composite topComposite){
			
		DashboardPartUtil.createLabel(topComposite, "Layer Settings");
		this.layerTable = new Table(topComposite, SWT.CHECK | SWT.FULL_SELECTION | SWT.V_SCROLL);
		layerTable.setHeaderVisible(true);
		layerTable.setBounds(25, 25, 220, 200);
		
		TableColumn visibilityColumn = new TableColumn(layerTable, SWT.NULL);
		visibilityColumn.setText("Visibility");
		TableColumn titleColumn = new TableColumn(layerTable, SWT.NULL);
		titleColumn.setText("Title");
		TableColumn typeColumn = new TableColumn(layerTable, SWT.NULL);
		typeColumn.setText("Type");
		
		TableItem item = new TableItem(layerTable, SWT.NULL);
		item.setText(0, "");
		item.setText(1, "Rasterschicht");
		item.setText(2, "BasicLayer");
		layerTable.getItem(0).setChecked(true);
		
		mapDashboardPart.addLayerToMap(1, new LayerConfiguration(true, "Rasterschicht", "BasicLayer"));
		
		for (int i = 0; i < 3; i++) {
		     layerTable.getColumn(i).pack();
		}
	}
	
	private void createAddButton(Composite topComposite){
		
		final Shell addingShell = new Shell (topComposite.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		addingShell.setText("Add Layer");
		addingShell.setSize(200, 300);
		
		final Button addingButton = new Button(topComposite, SWT.PUSH);
		addingButton.setText("Add");
		addingButton.setVisible(true);
		addingButton.setLayoutData(new GridData(50,20));	
		addingButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				createAddingShell(addingShell);
				addingShell.open();	
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	
	private void createAddingShell(Composite parent){
		
		
		final Shell addingShell = new Shell (parent.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		addingShell.setText("Add Layer");
		addingShell.setSize(200, 300);
		
		final Label addingTitleLabel = new Label(addingShell, SWT.NONE);
		addingTitleLabel.setText("Title: ");
		addingTitleLabel.setSize(30, 20);
		final Text addingTitleText = new Text (addingShell, SWT.BORDER);
		addingTitleText.setText("");
		addingTitleText.setSize(160, 20);
		addingTitleText.setLocation(30,0);
		
		final Label addingTypeLabel = new Label(addingShell, SWT.NONE);
		addingTypeLabel.setText("Type :");
		addingTypeLabel.setSize(30, 20);
		addingTypeLabel.setLocation(0, 25);
		
		//TODO HERAUSFINDEN WARUM DAS NICHT ANGEZEIGT WIRD
		ArrayList <String> layerStringList = new ArrayList<String>(); 
		for(LayerTypes i : LayerTypes.values()){
			layerStringList.add(i.toString());
		}
		
		final Combo layerTypeCombo = new Combo (addingShell, SWT.DROP_DOWN | SWT.READ_ONLY);
		layerTypeCombo.setItems((String[]) layerStringList.toArray());
		layerTypeCombo.setBounds(30, 25, 160, 20);
		layerTypeCombo.setVisible(true); 
		
		layerTypeCombo.addSelectionListener(new SelectionListener () {

			@Override
			public void widgetSelected(SelectionEvent e) {
				
				//TODO REMOVE NOT NEEDED PARTS?
				
				switch(layerTypeCombo.getText()){
				case "BasisLayer": 	Label sizeLabel = new Label(addingShell, SWT.NONE);
									sizeLabel.setText("Size");
									Text sizeText = new Text(addingShell, SWT.BORDER);
									sizeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
									break;
				case "OSM_Layer": 	Label urlLabel = new Label(addingShell, SWT.NONE);
									urlLabel.setText("URL");
									Text urlText = new Text (addingShell, SWT.BORDER);
									urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				
					
				}
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//TODO Create new Layer with parameters
		final Button confirmAddingButton = new Button(addingShell, SWT.PUSH);
		confirmAddingButton.setText("Confirm");
		confirmAddingButton.setSize(40, 20);
		confirmAddingButton.addSelectionListener(new SelectionListener () {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Integer count = mapDashboardPart.getLayerList().entrySet().size();
				String layerTitle = addingTitleText.getText();
				String layerType = layerTypeCombo.getText();
				
				//TODO DEPENDS ON SELECTED LAYERTYPE
				mapDashboardPart.addLayerToMap(count+1, new LayerConfiguration(true, layerTitle, layerType));
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}		
		});
	}
	
	private void createDeleteButton(Composite topComposite){
		
		final Table tempTableInstance = this.layerTable;
		final Button deleteButton = new Button (topComposite, SWT.PUSH);
		deleteButton.setText("Delete");
		deleteButton.setLayoutData(new GridData(50,20));
		
		final Shell deleteShell = new Shell(topComposite.getShell() , SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		deleteShell.setText("Delete Layer");
		deleteShell.setSize(200, 200);
		
		deleteButton.addSelectionListener(new SelectionListener() {
			
			int tableindex = tempTableInstance.getSelectionIndex();
			
			
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				
				
				
				
				
				deleteShell.setVisible(true);
				deleteShell.open();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
	}
	
	private void createConfigurateButton(Composite topComposite){
		final Button configurateButton = new Button (topComposite, SWT.PUSH);
		configurateButton.setText("Configurate");
		configurateButton.setLayoutData(new GridData(50,20));
		
		final Shell configurateShell = new Shell (topComposite.getShell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		configurateShell.setText("Configurate Layer");
		configurateShell.setSize(300, 500);
		
		Label layerConfTitle = new Label (configurateShell, SWT.NONE);
		layerConfTitle.setText("Title"); 
		Text layerConfTitleText = new Text(configurateShell, SWT.BORDER);
		layerConfTitleText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL)); 
		
		
		LayerConfiguration layerToConfigurate = mapDashboardPart.getLayerList().get(layerTable.getSelectionIndex());
		
		
		configurateButton.addSelectionListener(new SelectionListener(){
			
			@Override
			public void widgetSelected(SelectionEvent e) {

				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {		
			}
			
			
		
		});
		
	}
	
	private void createOrderButtons(Composite topComposite){
		DashboardPartUtil.createLabel(topComposite, "UP");
		DashboardPartUtil.createLabel(topComposite, "DOWN");
	}
	
	
	@Override
	public void dispose() {
		
	}

}
