package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.buffer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;

/**
 * 
 * Composite for the map editor to filter incoming tuples with the associated
 * temporal information
 * 
 * @author Stefan Bothe
 * 
 */
public class TimeSliderComposite extends Composite implements PropertyChangeListener {
	// private TimeSliderControl timeSliderControl;

	private ScreenManager manager;

	private Label intervallLabel;
	private Label valueLabel;
	private Label timestampLabel;
	private Label timeLabel;
	private Label beginLabel;
	private Slider beginSlider;
	private Button equalButton;
	private Label beginValue;
	private Label beginTimestamp;
	private Label beginTime;
	private Label endLabel;
	private Slider endSlider;
	private Label endValue;
	private Label endTimestamp;
	private Label endTime;

	private Boolean isEqual;
	private Boolean isActive = false;

	private Button btnActivate;

	public TimeSliderComposite(Composite parent, int style) {

		super(parent, style);
		this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.setLayout(new GridLayout(6, false));

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

		beginSlider = new Slider(this, SWT.NONE);
		beginSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		beginSlider.setValues(0, 0, 1, 1, 1, 1);
		beginSlider.setEnabled(isActive);
		beginSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isActive) {
					if (isEqual){
						endSlider.setSelection(beginSlider.getSelection());
						manager.setIntervalStart(manager.getMaxIntervalStart().plus(beginSlider.getSelection()));
						manager.setIntervalEnd(manager.getMaxIntervalStart().plus(endSlider.getSelection()+1));
					} else if (beginSlider.getSelection() == beginSlider.getMinimum())
						manager.setIntervalStart(manager.getMaxIntervalStart().clone());
					else {
						if (beginSlider.getSelection() > endSlider.getSelection())
							beginSlider.setSelection(endSlider.getSelection());
						manager.setIntervalStart(manager.getMaxIntervalStart().plus(beginSlider.getSelection()));
					}
				}
			}
		});

		btnActivate = new Button(this, SWT.CHECK);
		btnActivate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnActivate.setText("activate");
		btnActivate.setSelection(false);
		btnActivate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Slider " + beginSlider.getMinimum() + " " + beginSlider.getMaximum());
				ITimeInterval t = new TimeInterval(manager.getMaxIntervalStart());
				manager.setInterval(t);
				beginSlider.setEnabled(btnActivate.getSelection());
				endSlider.setEnabled(btnActivate.getSelection());
				beginValue.setEnabled(btnActivate.getSelection());
				beginTimestamp.setEnabled(btnActivate.getSelection());
				beginTime.setEnabled(btnActivate.getSelection());
				endValue.setEnabled(btnActivate.getSelection());
				endTimestamp.setEnabled(btnActivate.getSelection());
				endTime.setEnabled(btnActivate.getSelection());
				equalButton.setEnabled(btnActivate.getSelection());
				isActive = btnActivate.getSelection();
			}
		});

		beginValue = new Label(this, SWT.BORDER);
		GridData gd_beginValue = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_beginValue.widthHint = 100;
		beginValue.setLayoutData(gd_beginValue);
		beginValue.setText(Integer.toString(beginSlider.getSelection()));
		beginValue.setEnabled(isActive);

		beginTimestamp = new Label(this, SWT.BORDER);
		GridData gd_beginTimestamp = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_beginTimestamp.widthHint = 100;
		beginTimestamp.setLayoutData(gd_beginTimestamp);
		beginTimestamp.setText("");
		beginTimestamp.setEnabled(isActive);

		beginTime = new Label(this, SWT.BORDER);
		GridData gd_beginTime = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_beginTime.widthHint = 100;
		beginTime.setLayoutData(gd_beginTime);
		beginTime.setText("");
		beginTime.setEnabled(isActive);

		endLabel = new Label(this, SWT.NONE);
		endLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		endLabel.setText("End:");

		endSlider = new Slider(this, SWT.NONE);
		endSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		endSlider.setValues(0, 0, 1, 1, 1, 1);
		endSlider.setEnabled(isActive);
		endSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isActive) {
					if (isEqual)
						beginSlider.setSelection(endSlider.getSelection());

					if (endSlider.getSelection() == endSlider.getMaximum() - 1)
						manager.setIntervalEnd(PointInTime.getInfinityTime());
					else {
						if (endSlider.getSelection() <= beginSlider.getSelection()) {
							beginSlider.setSelection(endSlider.getSelection());
							manager.setIntervalStart(manager.getMaxIntervalStart().plus(beginSlider.getSelection()));
						}
						manager.setIntervalEnd(manager.getMaxIntervalStart().plus(endSlider.getSelection() + 1));
					}
				}
			}
		});

		equalButton = new Button(this, SWT.CHECK);
		equalButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		equalButton.setText("Equal");
		equalButton.setEnabled(isActive);
		equalButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isActive) {
					if (equalButton.getSelection()) {
						endSlider.setSelection(beginSlider.getSelection());
					}
					isEqual = equalButton.getSelection();
				}
			}
		});

		endValue = new Label(this, SWT.BORDER);
		endValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		endValue.setText(Integer.toString(endSlider.getSelection()));
		endValue.setEnabled(isActive);

		endTimestamp = new Label(this, SWT.BORDER);
		endTimestamp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		endTimestamp.setText("");
		endTimestamp.setEnabled(isActive);

		endTime = new Label(this, SWT.BORDER);
		endTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		endTime.setText("");
		endTime.setEnabled(isActive);

		isEqual = false;
	}

	public void setScreenmanager(ScreenManager screenManager) {
		this.manager = screenManager;
		this.manager.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if ("maxInterval".equals(evt.getPropertyName())) {
					ITimeInterval interval = ((ITimeInterval) evt.getNewValue());
					if (!evt.getNewValue().equals(evt.getOldValue())) {
						int i = (int) interval.getEnd().minus(interval.getStart()).getMainPoint();
//						beginValue.setText(interval.getStart().toString());
//						endValue.setText(interval.getEnd().toString());
						beginSlider.setMaximum(i);
						endSlider.setMaximum(i);
						if (manager.getIntervalEnd().isInfinite())
							endSlider.setSelection(endSlider.getMaximum());
						// beginSlider.setMinimum((int)
						// interval.getStart().getMainPoint());
					}
				}else if ("interval".equals(evt.getPropertyName())) {
					ITimeInterval interval = ((ITimeInterval) evt.getNewValue());
					if (!evt.getNewValue().equals(evt.getOldValue())) {
						if (interval.getEnd().isInfinite()) {
							endSlider.setSelection(endSlider.getMaximum() - 1);
						}
						{
							int i = (int) interval.getEnd().minus(interval.getStart()).getMainPoint();
							beginSlider.setSelection(i);
							endSlider.setSelection(i);
						}
						
						 beginValue.setText(Integer.toString(beginSlider.getSelection()));
						 endValue.setText(Integer.toString(endSlider.getSelection()));
						 
						 beginTimestamp.setText(interval.getStart().toString());						
						 endTimestamp.setText(interval.getEnd().toString());
						 
						 beginTime.setText(new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss.SSS")
						 .format(new Timestamp(interval.getEnd().getMainPoint())));
						 if (!interval.getEnd().isInfinite())
						 endTime.setText(new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss.SSS")
						 .format(new Timestamp(interval.getEnd().getMainPoint())));
						 else
							 endTime.setText("none");
						
					}
				}
			}
		});

	}
}
