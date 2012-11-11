package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.diagram;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth.SelectValueDialog;

public class EditDiagramStyleSettings extends Dialog {
	DiagramLayer layer;
	DiagramStyle style;
	
	Composite parent;
	Composite pufferComposite;
	Composite mainComposite;
	Composite diagramComposite;
	
	Label sizeLabel;
	Label sizeLabelNumber;
	Button sizeEdit;
	
	Label transparencyLabel;
	Label transparencyValueLabel;
	Button transparencyEdit;
	
	Label showValuesLabel;
	Button showValuesButton;
	
	Listener editSizeListener;
	Listener editTransparencyListener;
	public EditDiagramStyleSettings(Shell parentShell, DiagramLayer layer) {
		super(parentShell);
		this.layer = layer;
		setBlockOnOpen(true);
	}
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		this.parent = parent;
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, true);
		repack();
		repackParentShell();
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		pufferComposite = (Composite) super.createDialogArea(parent);
		init();
		return pufferComposite;
	}
	
	private void init() {
		pufferComposite.setLayout(new FillLayout());
		style = (DiagramStyle)layer.getStyle();
		
		getShell().setText(layer.getName());
		
		initListener();
		revertLegend();
	}
	
	private void revertLegend() {
		if(mainComposite!=null){
			mainComposite.dispose();
		}
		mainComposite = new Composite(pufferComposite, SWT.BORDER);
		mainComposite.setLayout(new GridLayout(4, false));
		
		sizeLabel = new Label(mainComposite, SWT.NONE);
		sizeLabel.setText("Size: ");
		sizeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		
		sizeLabelNumber = new Label(mainComposite, SWT.NONE);
		sizeLabelNumber.setText(Integer.toString(style.getSize()));
		sizeLabelNumber.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		sizeEdit = new Button(mainComposite, SWT.PUSH);
		sizeEdit.setText("Edit Size");
		sizeEdit.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		sizeEdit.addListener(SWT.Selection, editSizeListener);
		
		transparencyLabel = new Label(mainComposite, SWT.NONE);
		transparencyLabel.setText("Transparency: ");
		transparencyLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		
		transparencyValueLabel = new Label(mainComposite, SWT.NONE);
		transparencyValueLabel.setText(Integer.toString(style.getTransparency()));
		transparencyValueLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		transparencyEdit = new Button(mainComposite, SWT.PUSH);
		transparencyEdit.setText("Edit Transparency");
		transparencyEdit.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		transparencyEdit.addListener(SWT.Selection, editTransparencyListener);
		
		showValuesLabel = new Label(mainComposite, SWT.NONE);
		showValuesLabel.setText("Show Values: ");
		showValuesLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		
		@SuppressWarnings("unused")
		Label emptyLabel = new Label(mainComposite, SWT.NONE);
		
		showValuesButton = new Button(mainComposite, SWT.CHECK);
		showValuesButton.setSelection(style.getShowValues());
		showValuesButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		
		diagramComposite = new Composite(mainComposite, SWT.BORDER);
		diagramComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 4, 1));
		diagramComposite.setLayout(new RowLayout());
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(int i=0;i<layer.getVisualizationSDFAttributes().size();i++){
			dataset.setValue(layer.getVisualizationSDFAttributes().get(i).getAttributeName(), 1);
		}
		JFreeChart chart = ChartFactory.createPieChart("", dataset, false, true, false);
		PiePlot plot = (PiePlot)chart.getPlot();
		plot.setBackgroundPaint(new java.awt.Color(255,255,255,255));
		plot.setOutlineVisible(false);
		plot.setShadowPaint(null);
		ChartComposite chartComposite = new ChartComposite(diagramComposite, SWT.BORDER, chart);
		chartComposite.setLayoutData(new RowData(300, 300));		
		
		Button applyButton = new Button(mainComposite, SWT.NONE);
		applyButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		applyButton.setText("Apply");
		applyButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
	        	applyLegend();
	          }
		});
		Button revertButton = new Button(mainComposite, SWT.NONE);
		revertButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		revertButton.setText("Revert");
		revertButton.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent event) {
	        	revertLegend();
	          }
	        });		
		redraw();
		repackParentShell();
		
		
	}
	private void applyLegend() {
		style.setSize(Integer.parseInt(sizeLabelNumber.getText()));
		style.setTransparency(Integer.parseInt(transparencyValueLabel.getText()));
		if(showValuesButton.getSelection()==true){
			style.setShowValues(true);
		}else{
			style.setShowValues(false);
		}
		layer.updateStyle(style);
	}
	private void redraw() {
		repack();
		
	}
	private void initListener() {
		editSizeListener = new Listener() {
			public void handleEvent(Event event) {
				int value = Integer.parseInt(sizeLabelNumber.getText());
				SelectValueDialog dialog = new SelectValueDialog(Display.getCurrent().getActiveShell(), 1, 500, value);
			    int selectedValue = dialog.open();
			    if(selectedValue!=value){
			    	sizeLabelNumber.setText(Integer.toString(selectedValue));
			    }
			}
		};
		editTransparencyListener = new Listener() {
			public void handleEvent(Event event) {
				int value = Integer.parseInt(transparencyValueLabel.getText());
				SelectValueDialog dialog = new SelectValueDialog(Display.getCurrent().getActiveShell(), 0, 255, value);
			    int selectedValue = dialog.open();
			    if(selectedValue!=value){
			    	transparencyValueLabel.setText(Integer.toString(selectedValue));
			    }
			}
		};
	}
	private void repack(){
		mainComposite.pack();
		mainComposite.layout();
		pufferComposite.pack();
		pufferComposite.layout();
	}
	
	private void repackParentShell(){
		getShell().pack();
	}

}
