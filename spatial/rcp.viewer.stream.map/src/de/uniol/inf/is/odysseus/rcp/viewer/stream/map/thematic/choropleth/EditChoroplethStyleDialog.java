package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.choropleth;

import java.util.LinkedList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.misc.Operator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.misc.Predicate;

public class EditChoroplethStyleDialog extends Dialog{
	ChoroplethLayer layer;
	ChoroplethLegend legend;
	
	Composite parent;
	
	Composite pufferComposite;
	
	Composite mainComposite;
	
	Combo comboStyleChooser;
	final String[] comboItems = {"User defined style","Guided style"};
	
	Spinner spinnerNumberOfClasses;
	
	Label baseStyleLabel;
	Label baseStyleImageLabel;
	ChoroplethStyle choroplethStyleBaseStyle;
	
	Label labelElse;
	Label imageLabelElse;
	ChoroplethStyle choroplethStyleElse;
	
	int numberOfClasses;
	
	LinkedList<Label> valueLabelList;
	LinkedList<Button> operatorButtonList;
	LinkedList<Text> valueTextList;
	LinkedList<Label> imageLabelList;
	LinkedList<ChoroplethStyle> choroplethStyleList;
	
	public EditChoroplethStyleDialog(Shell parentShell, ChoroplethLayer layer) {
		super(parentShell);
		this.layer = layer;
		setBlockOnOpen(true);
	}
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		this.parent = parent;
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, true);
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
		choroplethStyleList = new LinkedList<>();
		
		pufferComposite.setLayout(new FillLayout());
		legend = layer.getLegend();
		
		getShell().setText(layer.getName());
		
		revertLegend();
	}
	
	public void revertLegend(){
		valueLabelList = new LinkedList<>();
		operatorButtonList = new LinkedList<>();
		valueTextList = new LinkedList<>();
		imageLabelList = new LinkedList<>();
		choroplethStyleList = new LinkedList<>();
		
		if(mainComposite!=null){
			mainComposite.dispose();
		}

		mainComposite = new Composite(pufferComposite, SWT.BORDER);
		mainComposite.setLayout(new GridLayout(4, false));
		
		
		SelectionListener selectionListener = new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				redraw();
				repack();
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		};
		
		Button importButton = new Button(mainComposite, SWT.NONE);
		importButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		importButton.setText("Import");
		Button exportButton = new Button(mainComposite, SWT.NONE);
		exportButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		exportButton.setText("Export");
		
		
		comboStyleChooser = new Combo(mainComposite, SWT.READ_ONLY);
		comboStyleChooser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		comboStyleChooser.setItems(comboItems);
		comboStyleChooser.select(0);
		comboStyleChooser.addSelectionListener(selectionListener);
		
		Label numClassesLabel = new Label(mainComposite, SWT.READ_ONLY);
		numClassesLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
		numClassesLabel.setText("Number of classes: ");
		
		
		spinnerNumberOfClasses = new Spinner(mainComposite, SWT.READ_ONLY);
		spinnerNumberOfClasses.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		spinnerNumberOfClasses.setMinimum(1);
		spinnerNumberOfClasses.setSelection(legend.legendList.size());
		spinnerNumberOfClasses.addSelectionListener(selectionListener);
		numberOfClasses = spinnerNumberOfClasses.getSelection();
		

		
		if(comboStyleChooser.getSelectionIndex()==0){
			
		}else if(comboStyleChooser.getSelectionIndex()==1){
			
		}
		
		
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
			valueTextList.add(valueText);
			Label imageLabel = new Label(mainComposite, SWT.BORDER);
			imageLabel.setImage(legend.legendList.get(i).getStyle().getImage());
			imageLabelList.add(imageLabel);
			choroplethStyleList.add(legend.legendList.get(i).getStyle());
			
				
		}
		labelElse = new Label(mainComposite, SWT.NONE);
		labelElse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
		labelElse.setText("ELSE");
		choroplethStyleElse = new ChoroplethStyle(ChoroplethStyle.defaultChoroplethStyle);
		imageLabelElse = new Label(mainComposite, SWT.BORDER);
		imageLabelElse.setImage(choroplethStyleElse.getImage());
		
		Button saveButton = new Button(mainComposite, SWT.NONE);
		saveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		saveButton.setText("Apply");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
	        	applyLegend();
	          }
		});
		Button resetButton = new Button(mainComposite, SWT.NONE);
		resetButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		resetButton.setText("Revert");
		resetButton.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent event) {
	        	revertLegend();
	          }
	        });		
		redraw();
	}
	
	public void redraw(){
		if(comboStyleChooser.getSelectionIndex()==0){
			if(baseStyleLabel!=null && baseStyleImageLabel!=null){
				baseStyleLabel.dispose();
				baseStyleImageLabel.dispose();
			}
		}else if(comboStyleChooser.getSelectionIndex()==1){
			if(baseStyleLabel!=null && baseStyleImageLabel!=null){
				baseStyleLabel.dispose();
				baseStyleImageLabel.dispose();
			}
			baseStyleLabel = new Label(mainComposite, SWT.NONE);
			baseStyleLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 3, 1));
			baseStyleLabel.setText("BaseStyle: ");
			baseStyleLabel.moveBelow(spinnerNumberOfClasses);
			baseStyleImageLabel = new Label(mainComposite, SWT.BORDER);
			choroplethStyleBaseStyle = new ChoroplethStyle(ChoroplethStyle.defaultChoroplethStyle);
			baseStyleImageLabel.setImage(choroplethStyleBaseStyle.getImage());
			baseStyleImageLabel.moveBelow(baseStyleLabel);
		}
		if(spinnerNumberOfClasses.getSelection()>numberOfClasses){
			numberOfClasses = spinnerNumberOfClasses.getSelection();
			
			Label valueLabel = new Label(mainComposite, SWT.NONE);
			valueLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			valueLabel.setText("attribute");
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
			valueText.addListener(SWT.Verify, new Listener() {
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
			      }
			    });
			valueTextList.add(valueText);
			Label imageLabel = new Label(mainComposite, SWT.BORDER);
			imageLabel.moveAbove(labelElse);
			imageLabelList.add(imageLabel);
			choroplethStyleList.add(new ChoroplethStyle(ChoroplethStyle.defaultChoroplethStyle));
			imageLabel.setImage(choroplethStyleList.getLast().getImage());
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
				choroplethStyleList.removeLast();
			}
		}
		
		mainComposite.pack();
		mainComposite.layout();
		pufferComposite.pack();
		pufferComposite.layout();
	}
	
	private void repack(){
		getShell().pack();
	}
	
	private void applyLegend(){
		ChoroplethLegend newLegend = new ChoroplethLegend();
		newLegend.legendList = new LinkedList<>();
		for(int i=0;i<valueLabelList.size();i++){
			Predicate predicate = new Predicate(valueLabelList.get(i).getText(),operatorButtonList.get(i).getText(),Integer.parseInt(valueTextList.get(i).getText()));
			ChoroplethLegendEntry legendEntry = new ChoroplethLegendEntry(predicate,choroplethStyleList.get(i));
			newLegend.legendList.add(legendEntry);
		}
		
		Predicate predicate = new Predicate(null,Operator.ELSE,null);
		ChoroplethLegendEntry defaultLegendEntry = new ChoroplethLegendEntry(predicate,choroplethStyleElse);
		newLegend.legendList.add(defaultLegendEntry);	
		
		legend = newLegend;
		layer.updateLegend(legend);
		revertLegend();
		
		
	}
	private void loadLegend(){
		
	}
	private void saveLegend(){
		
	}

}
