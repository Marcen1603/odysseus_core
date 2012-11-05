package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.buffer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;

public class TimeSliderComposite extends Composite {
	private TimeSliderControl timeSliderControl;
	
	private Label intervallLabel;
	private Label valueLabel;
	private Label timestampLabel;
	private Label timeLabel;
	private Label beginLabel;
	private Scale beginScale;
	private Button equalButton;
	private Label beginValue;
	private Label beginTimestamp;
	private Label beginTime;
	private Label endLabel;
	private Scale endScale;
	private Label endValue;
	private Label endTimestamp;
	private Label endTime;
	
	Boolean isEqual;
	Composite thisComposite;
	Composite parent;
	public TimeSliderComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.setLayout(new GridLayout(6, false));
		thisComposite = this;
		this.parent = parent;
		
		intervallLabel = new Label(this, SWT.NONE);
		intervallLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
		intervallLabel.setText("Interval");
		
		valueLabel = new Label(this, SWT.NONE);
		valueLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		valueLabel.setText("Value");
		
		timestampLabel = new Label(this, SWT.NONE);
		timestampLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		timestampLabel.setText("Timestamp");
		
		timeLabel = new Label(this, SWT.NONE);
		timeLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		timeLabel.setText("Time");
		
		beginLabel = new Label(this, SWT.NONE);
		beginLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		beginLabel.setText("Begin:");
		
		beginScale = new Scale(this, SWT.NONE);
		beginScale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		beginScale.setMinimum(0);
		beginScale.setMaximum(1);
		beginScale.setSelection(0);
		beginScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
                if(isEqual==true){
                	endScale.setSelection(beginScale.getSelection());
                }else{
                	if(beginScale.getSelection()>endScale.getSelection()){
                		beginScale.setSelection(endScale.getSelection());
                	}
                }
                updateView();
                updateCanvas();
            }
		});
		
		equalButton = new Button(this, SWT.CHECK);
		equalButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 2));
		equalButton.setText("Equal");
		equalButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
                if(equalButton.getSelection()==true){
                	isEqual=true;
                	beginScale.setSelection(endScale.getSelection());
                }else{
                	isEqual=false;
                }
                updateView();
                updateCanvas();
            }
		});
		
		beginValue = new Label(this, SWT.BORDER);
		beginValue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		beginValue.setText(Integer.toString(beginScale.getSelection()));
		
		beginTimestamp = new Label(this, SWT.BORDER);
		beginTimestamp.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		beginTimestamp.setText("");
		
		beginTime = new Label(this, SWT.BORDER);
		beginTime.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		beginTime.setText("");
		
		endLabel = new Label(this, SWT.NONE);
		endLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		endLabel.setText("End:");
		
		endScale = new Scale(this, SWT.NONE);
		endScale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		endScale.setMinimum(0);
		endScale.setMaximum(1);
		endScale.setSelection(1);
		endScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
                if(isEqual==true){
                	beginScale.setSelection(endScale.getSelection());
                }else{
                	if(endScale.getSelection()<beginScale.getSelection()){
                		endScale.setSelection(beginScale.getSelection());
                	}
                }
                updateView();
                updateCanvas();
            }
		});
		
		endValue = new Label(this, SWT.BORDER);
		endValue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		endValue.setText(Integer.toString(endScale.getSelection()));
		
		endTimestamp = new Label(this, SWT.BORDER);
		endTimestamp.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		endTimestamp.setText("");
		
		endTime = new Label(this, SWT.BORDER);
		endTime.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		endTime.setText("");
		
		isEqual = false;
	}
	public void setMaxValue(int size) {
		if(endScale.getSelection()==endScale.getMaximum()){
			beginScale.setMaximum(size);
			endScale.setMaximum(size);
			endScale.setSelection(size);
		}else{
			beginScale.setMaximum(size);
			endScale.setMaximum(size);
		}
		if(isEqual){
			beginScale.setSelection(endScale.getSelection());
		}
		updateView();
	}
	private void updateView(){
		int beginSelection = beginScale.getSelection();
		int endSelection = endScale.getSelection();
		beginValue.setText(Integer.toString(beginSelection));
		endValue.setText(Integer.toString(endSelection));

		if(timeSliderControl.getTimestampToString(beginSelection)!=null){
			long ts = timeSliderControl.getTimestampToString(beginSelection);
			beginTimestamp.setText(Long.toString(ts));
			beginTime.setText(new SimpleDateFormat("dd/MM/yyyy-H:m:s.S").format(new Timestamp(ts)));
		}else{
			beginTimestamp.setText("");
			beginTime.setText("");
		}
		if(timeSliderControl.getTimestampToString(endSelection)!=null){
			long ts = timeSliderControl.getTimestampToString(endSelection);
			endTimestamp.setText(Long.toString(ts));
			endTime.setText(new SimpleDateFormat("dd/MM/yyyy-H:m:s.S").format(new Timestamp(ts)));
		}else{
			endTimestamp.setText("");
			endTime.setText("");
		}
		
		
		
		beginValue.pack();
        endValue.pack();
        thisComposite.layout();
	}
	
	public int getIntervalMin(){
		return beginScale.getSelection();
	}
	public int getIntervalMax(){
		return endScale.getSelection();
	}
	public void setTimeSliderControl(TimeSliderControl timeSliderControl) {
		this.timeSliderControl = timeSliderControl;
	}
	public void updateCanvas(){
		timeSliderControl.updateCanvas();
	}
	
}
