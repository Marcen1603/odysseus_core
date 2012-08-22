package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.dialog;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;

///http://www.vogella.com/articles/EclipseDialogs/article.html

@Deprecated
public class LayerPropertyDialog extends PropertyDialog {

	private LinkedList<ILayer> layerOrder;
	private boolean raster = false;
	
	public LayerPropertyDialog(Shell parentShell, LinkedList<ILayer> layerOrder) {
		super(parentShell);
		this.layerOrder = layerOrder;
	}


	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout(2, false);
		
		
		Group layerConfiguration = new Group(container, SWT.FILL);
		layerConfiguration.setLayout(layout);
		layerConfiguration.setText("Basics");
		
		final Group rasterLayer = new Group(container, SWT.FILL);
		
			Label layerNameLabel = new Label (layerConfiguration, SWT.FLAT);
			layerNameLabel.setText("Name");	
		
			Text layerName = new Text(layerConfiguration, SWT.BORDER);
			
			Label layerTypelabel = new Label (layerConfiguration, SWT.FLAT);
			layerTypelabel.setText("Type");	
			
			CCombo layerType = new CCombo(layerConfiguration, SWT.BORDER);
			layerType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	
			layerType.add("Raster");
			layerType.add("Vector");
			
			layerType.setText("Raster");
	
			layerType.setOrientation(SWT.CENTER);
			
			layerType.addSelectionListener(new SelectionAdapter() {
				
				public void widgetSelected(SelectionEvent e) {
					
					rasterLayer.setVisible(!raster);
				};
			});
	
			Label layerPlaceLabel = new Label (layerConfiguration, SWT.FLAT);
			layerPlaceLabel.setText("Placing");	
			
			CCombo layerPlace = new CCombo(layerConfiguration, SWT.BORDER);
			layerPlace.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			
			for (ILayer layer: layerOrder) {
				layerPlace.add( layer.getName());
			}
			layerPlace.setText(layerOrder.getFirst().getName());
	
			layerPlace.setOrientation(SWT.CENTER);
			
			layerPlace.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					rasterLayer.setVisible(!raster);
				};
			});
			
		rasterLayer.setVisible(raster);
		rasterLayer.setLayout(layout);
		rasterLayer.setText("Raster");
		rasterLayer.setOrientation(SWT.CENTER);
		
			Label serverTypeLabel = new Label (rasterLayer, SWT.FLAT);
			serverTypeLabel.setText("Sever Type");	
			
			CCombo serverType = new CCombo(rasterLayer, SWT.BORDER);
			serverType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			
			serverType.add("RESTFUL Tile Server");
			serverType.add("WMS 1.0");
			serverType.add("WMS 1.1");
			serverType.add("WMS 1.1.1");

			serverType.setText("RESTFUL Tile Server");
	
			serverType.setOrientation(SWT.CENTER);
			
			serverType.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					System.out.println(e.data.toString());
				};
			});
		
			Label serverLabel = new Label (rasterLayer, SWT.FLAT);
			serverLabel.setText("URL");	
			
			CCombo server = new CCombo(rasterLayer, SWT.BORDER);
			server.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			
			
			server.add("http://tah.openstreetmap.org/Tiles/tile/");
			server.setText("http://tah.openstreetmap.org/Tiles/tile/");
	
			server.setOrientation(SWT.CENTER);
			
			server.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					System.out.println(e.data.toString());
				};
			});
		
		
		Group vectorLayer = new Group(container, SWT.FILL);
		vectorLayer.setLayout(layout);
		vectorLayer.setText("Vector");
		
		
		new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		
		return container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Layer Properties");
	}

}
