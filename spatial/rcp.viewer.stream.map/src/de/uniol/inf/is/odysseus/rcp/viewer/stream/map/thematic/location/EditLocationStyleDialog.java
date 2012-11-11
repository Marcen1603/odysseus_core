package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location;


import java.util.LinkedList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth.SelectValueDialog;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.misc.Operator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.misc.Predicate;

public class EditLocationStyleDialog extends Dialog{
	LocationLayer layer;
	LocationLegend legend;
	
	Composite parent;
	Composite pufferComposite;
	Composite mainComposite;
	
	Spinner spinnerNumberOfClasses;
	
	Label labelElse;
	Label imageLabelElse;
	LocationStyle locationStyleElse;
	
	int numberOfClasses;
	
	LinkedList<Label> valueLabelList;
	LinkedList<Button> operatorButtonList;
	LinkedList<Text> valueTextList;
	LinkedList<Label> imageLabelList;
	LinkedList<LocationStyle> locationStyleList;
	
	Listener valueTextListener;
	Listener imageLabelListener;
	
	public EditLocationStyleDialog(Shell parentShell, LocationLayer layer) {
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
	
	private void init(){
		valueLabelList = new LinkedList<>();
		operatorButtonList = new LinkedList<>();
		valueTextList = new LinkedList<>();
		imageLabelList = new LinkedList<>();
		locationStyleList = new LinkedList<>();
		
		pufferComposite.setLayout(new FillLayout());
		legend = layer.getLegend();
		
		getShell().setText(layer.getName());
		
		initListener();
		
		revertLegend();
	}
	private void initListener() {
		valueTextListener = new Listener() {
		      public void handleEvent(Event e) {
		        String string = e.text;
		        char[] chars = new char[string.length()];
		        string.getChars(0, chars.length, chars, 0);
		        for (int i = 0; i < chars.length; i++) {
		          if (!('0' <= chars[i] && chars[i] <= '9')) {
		            e.doit = false;
		            return;
		          }
		        }
		        Text valueText = ((Text)e.widget);
		        valueText.pack();
		        repack();
		        repackParentShell();
		      }
		};
			
		imageLabelListener = new Listener() {
			public void handleEvent(Event event) {
				MenuItem item = (MenuItem)event.widget;
				String text = item.getText();
				int ruleNumber = Integer.parseInt(text.substring(text.indexOf(" ")+1, text.indexOf(":")));
				String setting = text.substring(text.lastIndexOf(" ")+1);
				editSetting(ruleNumber, setting);
			}
		};
	}
	private void editSetting(int ruleNumber, String setting){
		ruleNumber -= 1;
		LocationStyle style;
		if(ruleNumber<locationStyleList.size()){
			style = locationStyleList.get(ruleNumber);
		}else{
			style = locationStyleElse;
		}
		
		if(setting.equals("Size")){
			int value = style.getSize();
			SelectValueDialog dialog = new SelectValueDialog(Display.getCurrent().getActiveShell(), 1, 50, value);
		    int selectedValue = dialog.open();
		    if(selectedValue!=value){
		    	style.setSize(selectedValue);
		    }
		}else if(setting.equals("LineColor")){
			ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
			RGB color = style.getLineColor().getRGB();
			colorDialog.setRGB(color);
			colorDialog.setText("ColorDialog");
			RGB selectedColor = colorDialog.open();
			if (selectedColor!= null && !selectedColor.equals(color)){
				style.setLineColor(ColorManager.getInstance().getColor(selectedColor));
			}
		}else if(setting.equals("FillColor")){
			ColorDialog colorDialog = new ColorDialog(Display.getCurrent().getActiveShell());
			RGB color = style.getFillColor().getRGB();
			colorDialog.setRGB(color);
			colorDialog.setText("ColorDialog");
			RGB selectedColor = colorDialog.open();
			if (selectedColor!= null && !selectedColor.equals(color)){
				style.setFillColor(ColorManager.getInstance().getColor(selectedColor));
			}
		}else if(setting.equals("Transparency")){
			int value = style.getTransparency();
			SelectValueDialog dialog = new SelectValueDialog(Display.getCurrent().getActiveShell(), 0, 255, value);
		    int selectedValue = dialog.open();
		    if(selectedValue!=value){
		    	style.setTransparency(selectedValue);
		    }
		}
		
		redraw();
	}
	public void revertLegend(){
		valueLabelList = new LinkedList<>();
		operatorButtonList = new LinkedList<>();
		valueTextList = new LinkedList<>();
		imageLabelList = new LinkedList<>();
		locationStyleList = new LinkedList<>();
		
		if(mainComposite!=null){
			mainComposite.dispose();
		}

		mainComposite = new Composite(pufferComposite, SWT.BORDER);
		mainComposite.setLayout(new GridLayout(4, false));
		
		
		SelectionListener selectionListener = new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				redraw();
				repackParentShell();
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		};
		
		
		Label numClassesLabel = new Label(mainComposite, SWT.READ_ONLY);
		numClassesLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
		numClassesLabel.setText("Number of classes: ");
		
		
		spinnerNumberOfClasses = new Spinner(mainComposite, SWT.READ_ONLY);
		spinnerNumberOfClasses.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		spinnerNumberOfClasses.setMinimum(1);
		spinnerNumberOfClasses.setSelection(legend.legendList.size());
		spinnerNumberOfClasses.addSelectionListener(selectionListener);
		numberOfClasses = spinnerNumberOfClasses.getSelection();
		
		
		
		for(int i=0;i<legend.getSize()-1;i++){
			Label valueLabel = new Label(mainComposite, SWT.NONE);
			valueLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			valueLabel.setText(legend.legendList.get(i).getPredicate().getValueName());
			valueLabelList.add(valueLabel);
			
			final Button operatorButton = new Button(mainComposite,SWT.NONE);
			operatorButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			operatorButton.setText(legend.legendList.get(i).getPredicate().getOperator());
			operatorButtonList.add(operatorButton);
			operatorButton.addSelectionListener(new SelectionAdapter() {
		        public void widgetSelected(SelectionEvent event) {
		            if(operatorButton.getText().equals(Operator.SMALLERTHAN)){
		            	operatorButton.setText(Operator.SMALLEREQUALTHAN);
		            }else if(operatorButton.getText().equals(Operator.SMALLEREQUALTHAN)){
		            	operatorButton.setText(Operator.EQUALTHAN);
		            }else if(operatorButton.getText().equals(Operator.EQUALTHAN)){
		            	operatorButton.setText(Operator.GREATERQUALTHAN);
		            }else if(operatorButton.getText().equals(Operator.GREATERQUALTHAN)){
		            	operatorButton.setText(Operator.GREATERTHAN);
		            }else if(operatorButton.getText().equals(Operator.GREATERTHAN)){
		            	operatorButton.setText(Operator.SMALLERTHAN);
		            }
		          }
		        });      
			Text valueText = new Text(mainComposite,SWT.NONE);
			valueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			valueText.setText(Integer.toString(legend.legendList.get(i).getPredicate().getValue()));
			valueText.addListener(SWT.Verify, valueTextListener);
			valueTextList.add(valueText);
			Label imageLabel = new Label(mainComposite, SWT.BORDER);
			imageLabel.setImage(legend.legendList.get(i).getStyle().getImage());
			imageLabelList.add(imageLabel);
			locationStyleList.add(new LocationStyle(legend.legendList.get(i).getStyle()));
				
		}
		labelElse = new Label(mainComposite, SWT.NONE);
		labelElse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
		labelElse.setText("ELSE");
		if(locationStyleElse==null){
			locationStyleElse = new LocationStyle(legend.legendList.getLast().getStyle());
		}
		imageLabelElse = new Label(mainComposite, SWT.BORDER);
		imageLabelElse.setImage(locationStyleElse.getImage());
		
		
		
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
	
	private void setRightClickMenu(int i, Label label){
		Menu popupMenu = new Menu(label);
	    MenuItem lineWidthItem = new MenuItem(popupMenu, SWT.NONE);
	    lineWidthItem.setText("Rule "+(i+1)+": Set Size");
	    lineWidthItem.addListener(SWT.Selection, imageLabelListener);
	    MenuItem lineColorItem = new MenuItem(popupMenu, SWT.NONE);
	    lineColorItem.setText("Rule "+(i+1)+": Set LineColor");
	    lineColorItem.addListener(SWT.Selection, imageLabelListener);
	    MenuItem fillColorItem = new MenuItem(popupMenu, SWT.NONE);
	    fillColorItem.setText("Rule "+(i+1)+": Set FillColor");
	    fillColorItem.addListener(SWT.Selection, imageLabelListener);
	    MenuItem transparencyItem = new MenuItem(popupMenu, SWT.NONE);
	    transparencyItem.setText("Rule "+(i+1)+": Set Transparency");
	    transparencyItem.addListener(SWT.Selection, imageLabelListener);
	    label.setMenu(popupMenu);
	}
	
	public void redraw(){
		if(spinnerNumberOfClasses.getSelection()>numberOfClasses){
			numberOfClasses = spinnerNumberOfClasses.getSelection();
			
			Label valueLabel = new Label(mainComposite, SWT.NONE);
			valueLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			valueLabel.setText(legend.getAttributeName());
			valueLabel.moveAbove(labelElse);
			valueLabelList.add(valueLabel);
			final Button operatorButton = new Button(mainComposite,SWT.NONE);
			operatorButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			operatorButton.setText(Operator.SMALLERTHAN);
			operatorButtonList.add(operatorButton);
			operatorButton.addSelectionListener(new SelectionAdapter() {
		        public void widgetSelected(SelectionEvent event) {
		            if(operatorButton.getText().equals(Operator.SMALLERTHAN)){
		            	operatorButton.setText(Operator.SMALLEREQUALTHAN);
		            }else if(operatorButton.getText().equals(Operator.SMALLEREQUALTHAN)){
		            	operatorButton.setText(Operator.EQUALTHAN);
		            }else if(operatorButton.getText().equals(Operator.EQUALTHAN)){
		            	operatorButton.setText(Operator.GREATERQUALTHAN);
		            }else if(operatorButton.getText().equals(Operator.GREATERQUALTHAN)){
		            	operatorButton.setText(Operator.GREATERTHAN);
		            }else if(operatorButton.getText().equals(Operator.GREATERTHAN)){
		            	operatorButton.setText(Operator.SMALLERTHAN);
		            }
		          }
		        });      
			operatorButton.moveAbove(labelElse);
			Text valueText = new Text(mainComposite,SWT.NONE);
			valueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			valueText.setText(Integer.toString(1000));
			valueText.moveAbove(labelElse);
			valueText.addListener(SWT.Verify, valueTextListener);
			valueTextList.add(valueText);
			Label imageLabel = new Label(mainComposite, SWT.BORDER);
			imageLabel.moveAbove(labelElse);
			imageLabelList.add(imageLabel);
			locationStyleList.add(new LocationStyle(LocationStyle.DEFAULTLOCATIONSTYLE));
			imageLabel.setImage(locationStyleList.getLast().getImage());
		}else if(spinnerNumberOfClasses.getSelection()<numberOfClasses){
			numberOfClasses = spinnerNumberOfClasses.getSelection();
			if(valueLabelList.size()>=0){
				Label label = valueLabelList.getLast();
				valueLabelList.removeLast();
				label.dispose();
				Button button = operatorButtonList.getLast();
				operatorButtonList.removeLast();
				button.dispose();
				Text text = valueTextList.getLast();
				valueTextList.removeLast();
				text.dispose();
				Label imageLabel = imageLabelList.getLast();
				imageLabelList.removeLast();
				imageLabel.dispose();
				locationStyleList.removeLast();
			}
		}
		
		for(int i=0;i<imageLabelList.size();i++){
			setRightClickMenu(i, imageLabelList.get(i));
			imageLabelList.get(i).setImage(locationStyleList.get(i).getImage());
		}
		setRightClickMenu(imageLabelList.size(), imageLabelElse);
		imageLabelElse.setImage(locationStyleElse.getImage());
		
		repack();
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
	
	private void applyLegend(){
		
		LocationLegend newLegend = new LocationLegend(layer.getVisualizationSDFAttribute().getQualName());
		newLegend.legendList = new LinkedList<>();
		for(int i=0;i<valueLabelList.size();i++){
			Predicate predicate = new Predicate(valueLabelList.get(i).getText(),operatorButtonList.get(i).getText(),Integer.parseInt(valueTextList.get(i).getText()));
			LocationLegendEntry legendEntry = new LocationLegendEntry(predicate,locationStyleList.get(i));
			newLegend.legendList.add(legendEntry);
		}
		
		Predicate predicate = new Predicate(null,Operator.ELSE,null);
		LocationLegendEntry defaultLegendEntry = new LocationLegendEntry(predicate,locationStyleElse);
		newLegend.legendList.add(defaultLegendEntry);	
		
		legend = newLegend;
		layer.updateLegend(legend);
		revertLegend();
		
		for (LocationLegendEntry element : legend.legendList) {
			System.out.println(element.toString());
		}
	}
//	private void loadLegend(){
//		
//	}
//	private void saveLegend(){
//		
//	}

}
