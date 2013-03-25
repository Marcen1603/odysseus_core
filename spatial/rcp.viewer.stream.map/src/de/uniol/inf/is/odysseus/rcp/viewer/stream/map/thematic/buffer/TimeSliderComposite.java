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
import org.eclipse.swt.widgets.Slider;

/**
 * 
 * Composite for the map editor to filter incoming tuples with the associated
 * temporal information
 * 
 * @author Stefan Bothe
 * 
 */
public class TimeSliderComposite extends Composite {
	private TimeSliderControl timeSliderControl;

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
	private Composite thisComposite;

	private Long startValueDifference;
	private Button btnActivate;

	public TimeSliderComposite(Composite parent, int style) {
		super(parent, style);
		this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.setLayout(new GridLayout(6, false));
		thisComposite = this;

		intervallLabel = new Label(this, SWT.NONE);
		intervallLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 3, 1));
		intervallLabel.setText("Interval");

		valueLabel = new Label(this, SWT.NONE);
		valueLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		valueLabel.setText("Value");

		timestampLabel = new Label(this, SWT.NONE);
		timestampLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1));
		timestampLabel.setText("Timestamp");

		timeLabel = new Label(this, SWT.NONE);
		timeLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		timeLabel.setText("Time");

		beginLabel = new Label(this, SWT.NONE);
		beginLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		beginLabel.setText("Begin:");

		beginSlider = new Slider(this, SWT.NONE);
		beginSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		beginSlider.setValues(0, 0, 1, 1, 1, 1);
		beginSlider.setEnabled(isActive);
		beginSlider.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (isActive) {
					if (isEqual) {
						endSlider.setSelection(beginSlider.getSelection());
					} else {
						if (beginSlider.getSelection() > endSlider
								.getSelection()) {
							beginSlider.setSelection(endSlider.getSelection());
						}
					}
					updateView();
					updateCanvas();
				}
			}
		});

		btnActivate = new Button(this, SWT.CHECK);
		btnActivate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		btnActivate.setText("activate");
		btnActivate.setSelection(false);
		btnActivate.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				beginSlider.setEnabled(btnActivate.getSelection());
				endSlider.setEnabled(btnActivate.getSelection());
				beginValue.setEnabled(btnActivate.getSelection());
				beginTimestamp.setEnabled(btnActivate.getSelection());
				beginTime.setEnabled(btnActivate.getSelection());
				endValue.setEnabled(btnActivate.getSelection());
				endTimestamp.setEnabled(btnActivate.getSelection());
				endTime.setEnabled(btnActivate.getSelection());
				isActive = btnActivate.getSelection();
			}
		});

		beginValue = new Label(this, SWT.BORDER);
		beginValue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		beginValue.setText(Integer.toString(beginSlider.getSelection()));
		beginValue.setEnabled(isActive);

		beginTimestamp = new Label(this, SWT.BORDER);
		beginTimestamp.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1));
		beginTimestamp.setText("");
		beginTimestamp.setEnabled(isActive);

		beginTime = new Label(this, SWT.BORDER);
		beginTime.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		beginTime.setText("");
		beginTime.setEnabled(isActive);

		endLabel = new Label(this, SWT.NONE);
		endLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		endLabel.setText("End:");

		endSlider = new Slider(this, SWT.NONE);
		endSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		endSlider.setValues(0, 0, 1, 1, 1, 1);
		endSlider.setEnabled(isActive);
		endSlider.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (isActive) {
					if (isEqual == true) {
						beginSlider.setSelection(endSlider.getSelection());
					} else {
						if (endSlider.getSelection() < beginSlider
								.getSelection()) {
							endSlider.setSelection(beginSlider.getSelection());
						}
					}
					updateView();
					updateCanvas();
				}
			}
		});

		equalButton = new Button(this, SWT.CHECK);
		equalButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false, 1, 1));
		equalButton.setText("Equal");
		equalButton.setEnabled(isActive);
		equalButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (isActive) {
					if (equalButton.getSelection()) {
						beginSlider.setSelection(endSlider.getSelection());
					}
					isEqual = equalButton.getSelection();
					updateView();
					updateCanvas();
				}
			}
		});

		endValue = new Label(this, SWT.BORDER);
		endValue.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		endValue.setText(Integer.toString(endSlider.getSelection()));
		endValue.setEnabled(isActive);

		endTimestamp = new Label(this, SWT.BORDER);
		endTimestamp.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		endTimestamp.setText("");
		endTimestamp.setEnabled(isActive);

		endTime = new Label(this, SWT.BORDER);
		endTime.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		endTime.setText("");
		endTime.setEnabled(isActive);

		isEqual = false;
		startValueDifference = 0L;
	}

	private void updateView() {
		if (isActive) {
			int beginSelection = beginSlider.getSelection();
			int endSelection = endSlider.getSelection();
			beginValue.setText(Integer.toString(beginSelection));
			endValue.setText(Integer.toString(endSelection));

			long ts = beginSelection + startValueDifference;
			beginTimestamp.setText(Long.toString(ts));
			beginTime.setText(new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss.SSS")
					.format(new Timestamp(ts)));

			ts = endSelection + startValueDifference;
			endTimestamp.setText(Long.toString(ts));
			endTime.setText(new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss.SSS")
					.format(new Timestamp(ts)));

			beginValue.pack();
			endValue.pack();
			beginTimestamp.pack();
			endTimestamp.pack();
			beginTime.pack();
			endTime.pack();
			thisComposite.layout();
		}
	}

	public Long getIntervalMin() {
		return beginSlider.getSelection() + startValueDifference;
	}

	public Long getIntervalMax() {
		return endSlider.getSelection() + startValueDifference;
	}

	public void setTimeSliderControl(TimeSliderControl timeSliderControl) {
		this.timeSliderControl = timeSliderControl;
	}

	public void updateCanvas() {
		timeSliderControl.updateCanvas();
	}

	public Long getStartValueDifference() {
		return startValueDifference;
	}

	public void setStartValueDifference(Long startValueDifference) {
		this.startValueDifference = startValueDifference;
	}

	public void updateSliderInterval(long startPoint, long endPoint) {
		if (startValueDifference == 0 || startPoint < startValueDifference) {
			startValueDifference = startPoint;
		}

		if (endSlider.getSelection() == (endSlider.getMaximum() - 1)) {
			beginSlider.setMaximum((int) (endPoint - startValueDifference));
			endSlider.setMaximum((int) (endPoint - startValueDifference));
			endSlider.setSelection((int) (endPoint - startValueDifference));
		} else {
			beginSlider.setMaximum((int) (endPoint - startValueDifference));
			endSlider.setMaximum((int) (endPoint - startValueDifference));
		}
		if (isEqual) {
			beginSlider.setSelection(endSlider.getSelection());
		}
		updateView();
	}
}
