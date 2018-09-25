package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.thematic.buffer;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.mihalis.opal.rangeSlider.RangeSlider;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Buffer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;

/**
 * 
 * Composite for the map editor to filter incoming tuples with the associated
 * temporal information
 * 
 * @author Stefan Bothe
 * 
 */
public class TimeSliderComposite extends Composite implements PropertyChangeListener {

	private ScreenManager manager;

	private Label timeRangeLabel;
	private Label beginTimestamp;
	private Label beginTime;
	private Label endTimestamp;
	private Label endTime;

	private Boolean isActive = false;
	private Boolean isFixedTimeRange = false;
	private long fixedTimeRange = 0;;

	private RangeSlider rangeSlider;
	private Button activeButton;
	private Button fixedTimeButton;
	private Button optionsButton;

	private int savedMaxSlider; // Inactive -> active (save what will be right
								// maximum value if slider is active again)
	private long previousUpperValue;
	private int sliderFactor; // If timeRange is higher than max-integer

	public TimeSliderComposite(Composite parent, int style) {

		super(parent, style);
		this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.setLayout(new GridLayout(4, false));

		// BeginTime
		beginTime = new Label(this, SWT.LEFT);
		beginTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		beginTime.setText("                                             ");

		// Slider
		rangeSlider = new RangeSlider(this, SWT.HORIZONTAL);
		rangeSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		rangeSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateInterval();
			}
		});

		// EndTime
		endTime = new Label(this, SWT.NONE);
		endTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		endTime.setText("                                             ");

		// Active
		activeButton = new Button(this, SWT.CHECK);
		activeButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		activeButton.setText("Active");
		activeButton.setSelection(false);
		activeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rangeSlider.setEnabled(activeButton.getSelection());
				beginTimestamp.setEnabled(activeButton.getSelection());
				beginTime.setEnabled(activeButton.getSelection());
				endTimestamp.setEnabled(activeButton.getSelection());
				endTime.setEnabled(activeButton.getSelection());
				timeRangeLabel.setEnabled(activeButton.getSelection());
				fixedTimeButton.setEnabled(activeButton.getSelection());
				isActive = activeButton.getSelection();

				if (!activeButton.getSelection()) {
					// If it's set to inactive -> set the maximum interval
					manager.setIntervalStart(manager.getMaxIntervalStart().clone());
					manager.setIntervalEnd(PointInTime.getInfinityTime());
				} else {
					// It's set to active -> set the values of the slider

					// Update the range (e.g. if a connection was deleted)
					Buffer connection = manager.getConnection();
					if (connection != null) {
						boolean first = true;
						boolean changedSomething = connection.updateIntervallInScreenManager(first);
						if (changedSomething)
							// If the first buffers have no tuples, they
							// don't affect anything -> Let the next one set
							// the initial values
							first = false;
						if (first) {
							// There is no connection
							manager.setMaxIntervalStart(new PointInTime(0));
							manager.setMaxIntervalEnd(new PointInTime(0));
						}
					}

					boolean setBackToEnd = false;
					if (rangeSlider.getUpperValue() >= rangeSlider.getMaximum() - 1) {
						// On the right end -> "glue"
						setBackToEnd = true;
					}
					rangeSlider.setMaximum(savedMaxSlider);
					if (setBackToEnd) {
						rangeSlider.setUpperValue(rangeSlider.getMaximum());
					}
					int tolerance = rangeSlider.getMaximum() / 20;
					int distance = rangeSlider.getUpperValue() - rangeSlider.getLowerValue();
					if (distance <= tolerance && (rangeSlider.getUpperValue() >= rangeSlider.getMaximum() - tolerance
							|| rangeSlider.getLowerValue() <= rangeSlider.getMinimum() + tolerance)) {
						// They are very near together (less or equal 5% of the
						// total range) and very near to an edge,
						// so maybe the user can't
						// slide them

						// Make a 10% gap between them
						if (rangeSlider.getLowerValue() - (rangeSlider.getMaximum() / 10) >= rangeSlider.getMinimum()) {
							rangeSlider.setLowerValue(rangeSlider.getLowerValue() - (rangeSlider.getMaximum() / 10));
						} else if (rangeSlider.getUpperValue() + (rangeSlider.getMaximum() / 10) <= rangeSlider
								.getMaximum()) {
							rangeSlider.setUpperValue(rangeSlider.getUpperValue() + (rangeSlider.getMaximum() / 10));
						}

					}
					// And update the selection
					updateInterval();

					// And update the labels
					updateLabels();

				}
			}
		});

		// BeginTimeStamp
		beginTimestamp = new Label(this, SWT.LEFT);
		beginTimestamp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		beginTimestamp.setText("                                     ");

		// -----------------------------------
		// Box under slider
		Composite underSlider = new Composite(this, SWT.FILL);
		underSlider.setLayout(new GridLayout(2, false));
		underSlider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// TimeRange
		timeRangeLabel = new Label(underSlider, SWT.LEFT);
		timeRangeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		timeRangeLabel.setText("                                     ");

		// Fixed time
		fixedTimeButton = new Button(underSlider, SWT.CHECK | SWT.RIGHT);
		fixedTimeButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		fixedTimeButton.setText("Fixed timerange");
		fixedTimeButton.setSelection(false);
		fixedTimeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isFixedTimeRange = fixedTimeButton.getSelection();
				if (isFixedTimeRange) {
					fixedTimeRange = rangeSlider.getUpperValue() - rangeSlider.getLowerValue();
				}
			}
		});

		// -----------------------------------

		// EndTimeStamp
		endTimestamp = new Label(this, SWT.NONE);
		endTimestamp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		endTimestamp.setText("                                   ");

		// Options
		optionsButton = new Button(this, SWT.NONE);
		optionsButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		optionsButton.setText("Options ...");
		optionsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// New shell
				Shell s = new Shell(Display.getDefault(), SWT.PRIMARY_MODAL | SWT.SHEET |SWT.ON_TOP);
				s.setLayout(new GridLayout(3, false));

				// Fill shell
				// Headings
				Label numberHead = new Label(s, SWT.NONE);
				numberHead.setText("Puffer-size");
				Label numberViewedHead = new Label(s, SWT.NONE);
				numberViewedHead.setText("Number element now viewed");
				Label timeHead = new Label(s, SWT.NONE);
				timeHead.setText("Time to save");

				
				Buffer connection = manager.getConnection();

				Label number = new Label(s, SWT.NONE);
				number.setText(Long.toString(connection.getPufferSize()));

				Label numberViewed = new Label(s, SWT.NONE);
				numberViewed.setText(Long.toString(connection.getViewSize()));

				// Set the time in the buffer by the user
				Composite timeContainer = new Composite(s, SWT.NONE);
				timeContainer.setLayout(new GridLayout(6, false));

				int prevSeconds = connection.getTimeRange();
				int nowSeconds = prevSeconds % 60;
				prevSeconds /= 60;
				int nowMinutes = prevSeconds % 60;
				prevSeconds /= 60;
				int nowHours = prevSeconds;

				Spinner hours = new Spinner(timeContainer, SWT.NONE);
				hours.setValues(nowHours, 0, Integer.MAX_VALUE, 0, 1, 1);
				Label h = new Label(timeContainer, SWT.NONE);
				h.setText("h    ");
				Spinner minutes = new Spinner(timeContainer, SWT.NONE);
				minutes.setValues(nowMinutes, 0, 60, 0, 1, 1);
				Label min = new Label(timeContainer, SWT.NONE);
				min.setText("m    ");
				Spinner seconds = new Spinner(timeContainer, SWT.NONE);
				seconds.setValues(nowSeconds, 0, 60, 0, 1, 1);
				Label sec = new Label(timeContainer, SWT.NONE);
				sec.setText("s");

				MaxTimeListener timeListener = new MaxTimeListener(connection, hours, minutes, seconds);
				
				// How can I add this later?
				Button okButton = new Button(s, SWT.NONE);
				okButton.setText("Save");

				okButton.addSelectionListener(timeListener);
				// This would make it "live" - but the moment you tip
				// "1 second"
				// nearly all old data (but the one second) is lost :(
				// hours.addSelectionListener(timeListener);
				// minutes.addSelectionListener(timeListener);
				// seconds.addSelectionListener(timeListener);

				// Open shell
				s.pack();
				s.open();
			}

		});

		// Set all the buttons active or inactive
		// normally inactive unti the active-button is clicked
		rangeSlider.setEnabled(activeButton.getSelection());
		beginTimestamp.setEnabled(activeButton.getSelection());
		beginTime.setEnabled(activeButton.getSelection());
		endTimestamp.setEnabled(activeButton.getSelection());
		endTime.setEnabled(activeButton.getSelection());
		timeRangeLabel.setEnabled(activeButton.getSelection());
		fixedTimeButton.setEnabled(activeButton.getSelection());

	}

	public void setScreenmanager(ScreenManager screenManager) {
		this.manager = screenManager;
		this.manager.addPropertyChangeListener(this);
	}

	/**
	 * Updates the interval in the manager to the selected range-slider-interval
	 */
	final private void updateInterval() {
		if (isActive) {

			// EndSlider (right one)
			if (rangeSlider.getUpperValue() >= rangeSlider.getMaximum() - 1) {
				// Right slider is on the right end
				manager.setIntervalEnd(PointInTime.getInfinityTime());

			} else {
				PointInTime newEnd = new PointInTime(manager.getMaxIntervalStart().getMainPoint()
						+ ((long) rangeSlider.getUpperValue() * sliderFactor));
				manager.setIntervalEnd(newEnd);
			}

			// BeginSlider (left one)
			if (!(isFixedTimeRange && rangeSlider.getUpperValue() >= rangeSlider.getMaximum() - 1)) {
				if (rangeSlider.getLowerValue() == rangeSlider.getMinimum()) {
					// left slider is on the left end
					manager.setIntervalStart(manager.getMaxIntervalStart().clone());
				} else {
					PointInTime newStart = new PointInTime(manager.getMaxIntervalStart().getMainPoint()
							+ ((long) rangeSlider.getLowerValue() * sliderFactor));
					manager.setIntervalStart(newStart);
				}
			} else {
				// Don't set the time, if the left slider can't move,
				// because the right one
				// is fixed on the right
				rangeSlider.setLowerValue((int) (previousUpperValue - fixedTimeRange));
			}

		}
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (isActive) {
					if (isFixedTimeRange) {

						if ((rangeSlider.getUpperValue() >= rangeSlider.getMaximum() - 1)
								|| (rangeSlider.getUpperValue() != previousUpperValue)) {
							// Set the left to the same distance as before
							rangeSlider.setLowerValue((int) (rangeSlider.getUpperValue() - fixedTimeRange));
							if (rangeSlider.getUpperValue() - fixedTimeRange < rangeSlider.getMinimum()) {
								rangeSlider.setLowerValue(rangeSlider.getMinimum());
								// left slider is on the left end
								manager.setIntervalStart(manager.getMaxIntervalStart().clone());
							}
							// And set the value of the manager to this new
							// value
							PointInTime newStart = new PointInTime(manager.getMaxIntervalStart().getMainPoint()
									+ ((long) rangeSlider.getLowerValue() * sliderFactor));
							manager.setIntervalStart(newStart);
						} else {
							// the lower value was changed (left slider) -> set
							// the right slider
							rangeSlider.setUpperValue((int) (rangeSlider.getLowerValue() + fixedTimeRange));
							if (rangeSlider.getLowerValue() + fixedTimeRange > rangeSlider.getMaximum()) {
								rangeSlider.setUpperValue(rangeSlider.getMaximum());
								// Right slider is on the right end
								manager.setIntervalEnd(PointInTime.getInfinityTime());
							}
							PointInTime newEnd = new PointInTime(manager.getMaxIntervalStart().getMainPoint()
									+ ((long) rangeSlider.getUpperValue() * sliderFactor));
							manager.setIntervalEnd(newEnd);
						}

					}

					if ("maxInterval".equals(evt.getPropertyName())) {
						ITimeInterval interval = ((ITimeInterval) evt.getNewValue());
						if (!evt.getNewValue().equals(evt.getOldValue())) {
							long tempMaxSlider = interval.getEnd().minus(interval.getStart()).getMainPoint();
							sliderFactor = 1;
							while (tempMaxSlider > Integer.MAX_VALUE) {
								// Has to fit in integer, cause the rangeSlider
								// can't have more than integer
								tempMaxSlider /= 10;
								sliderFactor *= 10;
							}

							boolean setBackToEnd = (rangeSlider.getUpperValue() >= rangeSlider.getMaximum() - 1);

							rangeSlider.setMaximum((int) tempMaxSlider);

							if (setBackToEnd) {
								// If the right slider is on the right side,
								// let is stay there (except the left one was
								// moved with fixed time range)
								rangeSlider.setUpperValue(rangeSlider.getMaximum());
							}
						}
					} else if ("interval".equals(evt.getPropertyName())) {
						ITimeInterval interval = ((ITimeInterval) evt.getNewValue());
						if (!evt.getNewValue().equals(evt.getOldValue())) {

							long tempMaxSlider = interval.getEnd().minus(interval.getStart()).getMainPoint();
							sliderFactor = 1;
							while (tempMaxSlider > Integer.MAX_VALUE) {
								// Has to fit in integer, cause the rangeSlider
								// can't have more than integer
								tempMaxSlider /= 10;
								sliderFactor *= 10;
							}

							rangeSlider.setMaximum((int) tempMaxSlider);

							if (interval.getEnd().isInfinite()) {
								rangeSlider.setUpperValue(rangeSlider.getMaximum());
							}
						}
					}

					updateLabels();

					// Save the values to compare, which one was dragged
					previousUpperValue = rangeSlider.getUpperValue();
				} else {
					// Is not active
					if ("maxInterval".equals(evt.getPropertyName()) || "interval".equals(evt.getPropertyName())) {
						ITimeInterval interval = ((ITimeInterval) evt.getNewValue());
						long tempMaxSlider = interval.getEnd().minus(interval.getStart()).getMainPoint();
						sliderFactor = 1;
						while (tempMaxSlider > Integer.MAX_VALUE) {
							// Has to fit in integer, cause the rangeSlider
							// can't have more than integer
							tempMaxSlider /= 10;
							sliderFactor *= 10;
						}
						savedMaxSlider = (int) tempMaxSlider;
					}
				}
			}
		});
	}

	/**
	 * Fills the labels with the right content (timelabels)
	 */
	private void updateLabels() {
		// Set the begin-labels
		beginTimestamp.setText(Long.toString(manager.getIntervalStart().getMainPoint()));
		beginTime.setText(new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss.SSS")
				.format(new Timestamp(manager.getIntervalStart().getMainPoint())));

		// Set the end-labels
		// Start + selection -> better if end is infinity
		endTimestamp.setText(Long
				.toString(manager.getIntervalStart().getMainPoint() - (long) rangeSlider.getLowerValue() * sliderFactor
						+ (long) rangeSlider.getUpperValue() * sliderFactor));
		endTime.setText(new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss.SSS").format(new Timestamp(
				manager.getIntervalStart().getMainPoint() - (long) rangeSlider.getLowerValue() * sliderFactor
						+ (long) rangeSlider.getUpperValue() * sliderFactor)));

		// Set the time-range-label
		long timeInMs = ((long) rangeSlider.getUpperValue() - (long) rangeSlider.getLowerValue()) * sliderFactor;
		timeRangeLabel.setText(msToTimeString(timeInMs));
	}

	/**
	 * Creates a timestring from a timeRange in MS (Form: 1day 2h 3min 4s 5ms)
	 * 
	 * @param timeInMs
	 * @return
	 */
	public String msToTimeString(long timeInMs) {
		long ms = timeInMs;
		long sec = 0;
		long min = 0;
		long h = 0;
		long days = 0;
		String timeString = "";

		if (timeInMs > 999) {
			ms = timeInMs % 1000;
			sec = timeInMs / 1000;
		}
		if (sec > 59) {
			min = sec / 60;
			sec = sec % 60;
		}
		if (min > 59) {
			h = min / 60;
			min = min % 60;
		}
		if (h > 23) {
			days = h / 24;
			h = h % 24;
		}

		// Always a least 0ms
		timeString = ms + "ms";

		if (sec > 0) {
			timeString = sec + "s " + timeString;
		}
		if (min > 0) {
			timeString = min + "min " + timeString;
		}
		if (h > 0) {
			timeString = h + "h " + timeString;
		}
		if (days > 0) {
			timeString = days + "d " + timeString;
		}

		return timeString;
	}
}
