package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.buffer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;

/**
 * 
 * Composite for the map editor to filter incoming tuples with the associated
 * temporal information
 * 
 * @author Stefan Bothe
 * 
 */
public class TimeSliderComposite extends Composite implements
		PropertyChangeListener {

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

	private int savedMaxSlider; // Inaktive -> aktive (save what will be right
								// maximum value if slider is aktive again)

	private long previousUpperValue;

	public TimeSliderComposite(Composite parent, int style) {

		super(parent, style);
		this.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		this.setLayout(new GridLayout(4, false));

		// BeginTime
		beginTime = new Label(this, SWT.LEFT);
		beginTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		beginTime.setText("                                             ");

		// Slider
		rangeSlider = new RangeSlider(this, SWT.HORIZONTAL);
		rangeSlider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		rangeSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isActive) {

					// EndSlider (right one)
					if (rangeSlider.getSelection()[1] >= rangeSlider
							.getMaximum() - 1) {
						// Right slider is on the right end
						manager.setIntervalEnd(PointInTime.getInfinityTime());

					} else {
						manager.setIntervalEnd(manager.getMaxIntervalStart()
								.plus(rangeSlider.getSelection()[1]));
					}

					// BeginSlider (left one)
					if (!(isFixedTimeRange && rangeSlider.getSelection()[1] >= rangeSlider
							.getMaximum() - 1)) {
						if (rangeSlider.getSelection()[0] == rangeSlider
								.getMinimum()) {
							// left slider is on the left end
							manager.setIntervalStart(manager
									.getMaxIntervalStart().clone());
						} else {
							manager.setIntervalStart(manager
									.getMaxIntervalStart().plus(
											rangeSlider.getSelection()[0]));
						}
					} else {
						// Don't set the time, if the left slider can't move,
						// because the right one
						// is fixed on the right
						rangeSlider
								.setLowerValue((int) (previousUpperValue - fixedTimeRange));
					}

				}
			}
		});

		// EndTime
		endTime = new Label(this, SWT.NONE);
		endTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		endTime.setText("                                             ");

		// Active
		activeButton = new Button(this, SWT.CHECK);
		activeButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		activeButton.setText("Active");
		activeButton.setSelection(false);
		activeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ITimeInterval t = new TimeInterval(manager
						.getMaxIntervalStart());
				manager.setInterval(t);
				rangeSlider.setEnabled(activeButton.getSelection());
				beginTimestamp.setEnabled(activeButton.getSelection());
				beginTime.setEnabled(activeButton.getSelection());
				endTimestamp.setEnabled(activeButton.getSelection());
				endTime.setEnabled(activeButton.getSelection());
				timeRangeLabel.setEnabled(activeButton.getSelection());
				fixedTimeButton.setEnabled(activeButton.getSelection());
				isActive = activeButton.getSelection();

				// Set the begin-labels
				beginTimestamp.setText(Long.toString(manager.getIntervalStart()
						.getMainPoint()));
				beginTime.setText(new SimpleDateFormat(
						"dd/MM/yyyy - HH:mm:ss.SSS").format(new Timestamp(
						manager.getIntervalStart().getMainPoint())));

				// Start + selection -> better if end is infinity
				endTimestamp.setText(Long.toString(manager.getIntervalStart()
						.getMainPoint() + rangeSlider.getSelection()[1]));
				endTime.setText(new SimpleDateFormat(
						"dd/MM/yyyy - HH:mm:ss.SSS").format(new Timestamp(
						manager.getIntervalStart().getMainPoint()
								+ rangeSlider.getSelection()[1])));

				if (!activeButton.getSelection()) {
					// If it's set to inactive -> set the maximum interval
					manager.setIntervalStart(manager.getMaxIntervalStart()
							.clone());
					manager.setIntervalEnd(PointInTime.getInfinityTime());

				} else {
					// It's set to active -> set the values of the slider
					rangeSlider.setMaximum(savedMaxSlider);
				}
			}
		});

		// BeginTimeStamp
		beginTimestamp = new Label(this, SWT.LEFT);
		beginTimestamp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				true, 1, 1));
		beginTimestamp.setText("                                     ");

		// -----------------------------------
		// Box under slider
		Composite underSlider = new Composite(this, SWT.FILL);
		underSlider.setLayout(new GridLayout(2, false));
		underSlider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		// TimeRange
		timeRangeLabel = new Label(underSlider, SWT.LEFT);
		timeRangeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		timeRangeLabel.setText("                                     ");

		// Fixed time
		fixedTimeButton = new Button(underSlider, SWT.CHECK | SWT.RIGHT);
		fixedTimeButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1));
		fixedTimeButton.setText("Fixed timerange");
		fixedTimeButton.setSelection(false);
		fixedTimeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isFixedTimeRange = fixedTimeButton.getSelection();
				if (isFixedTimeRange) {
					fixedTimeRange = rangeSlider.getUpperValue()
							- rangeSlider.getLowerValue();
				}
			}
		});

		// -----------------------------------

		// EndTimeStamp
		endTimestamp = new Label(this, SWT.NONE);
		endTimestamp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		endTimestamp.setText("                                   ");

		// Options
		optionsButton = new Button(this, SWT.NONE);
		optionsButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		optionsButton.setText("Options ...");
		optionsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// New shell
				Shell s = new Shell(Display.getDefault(), SWT.PRIMARY_MODAL
						| SWT.SHEET);
				s.setLayout(new GridLayout(4, false));

				// Fill shell
				// Headings
				Label queryHead = new Label(s, SWT.NONE);
				queryHead.setText("Connection");
				Label numberHead = new Label(s, SWT.NONE);
				numberHead.setText("Puffer-size");
				Label numberViewedHead = new Label(s, SWT.NONE);
				numberViewedHead.setText("Number element now viewed");
				Label maxHead = new Label(s, SWT.NONE);
				maxHead.setText("Max-Puffer-size");

				ArrayList<LayerUpdater> connections = manager.getConnections();
				if (connections != null) {
					for (LayerUpdater conn : connections) {
						Label query = new Label(s, SWT.NONE);
						query.setText(conn.getQuery().getLogicalQuery()
								.getQueryText());

						Label number = new Label(s, SWT.NONE);
						number.setText(Long.toString(conn.getPufferSize()));

						Label numberViewed = new Label(s, SWT.NONE);
						numberViewed.setText(Long.toString(conn.getViewSize()));

						Spinner max = new Spinner(s, SWT.NONE);
						max.setValues(conn.getMaxPufferSize(), 0,
								Integer.MAX_VALUE, 0, 1, 1);

						max.addSelectionListener(new MaxTupleListener(conn, max));
					}
				}

				// Label test = new Label(s, SWT.NONE);
				// test.setText("Teeeest!");

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

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (isActive) {
					if (isFixedTimeRange) {

						if ((rangeSlider.getUpperValue() >= rangeSlider
								.getMaximum() - 1)
								|| (rangeSlider.getUpperValue() != previousUpperValue)) {
							// Set the left to the same distance as before
							rangeSlider.setLowerValue((int) (rangeSlider
									.getUpperValue() - fixedTimeRange));
							if (rangeSlider.getUpperValue() - fixedTimeRange < rangeSlider
									.getMinimum()) {
								rangeSlider.setLowerValue(rangeSlider
										.getMinimum());
								// left slider is on the left end
								manager.setIntervalStart(manager
										.getMaxIntervalStart().clone());
							}
							// And set the value of the manager to this new
							// value
							manager.setIntervalStart(manager
									.getMaxIntervalStart().plus(
											rangeSlider.getSelection()[0]));
						} else {
							// the lower value was changed (left slider) -> set
							// the right slider
							rangeSlider.setUpperValue((int) (rangeSlider
									.getLowerValue() + fixedTimeRange));
							if (rangeSlider.getLowerValue() + fixedTimeRange > rangeSlider
									.getMaximum()) {
								rangeSlider.setUpperValue(rangeSlider
										.getMaximum());
								// Right slider is on the right end
								manager.setIntervalEnd(PointInTime
										.getInfinityTime());
							}
							manager.setIntervalEnd(manager
									.getMaxIntervalStart().plus(
											rangeSlider.getSelection()[1]));
						}

					}

					if ("maxInterval".equals(evt.getPropertyName())) {
						ITimeInterval interval = ((ITimeInterval) evt
								.getNewValue());
						if (!evt.getNewValue().equals(evt.getOldValue())) {
							int i = (int) interval.getEnd()
									.minus(interval.getStart()).getMainPoint();
							boolean setBackToEnd = (rangeSlider.getSelection()[1] >= rangeSlider
									.getMaximum() - 1);

							rangeSlider.setMaximum(i);

							if (setBackToEnd) {
								// If the right slider is on the right side,
								// let is stay there (except the left one was
								// moved with fixed time range)
								rangeSlider.setUpperValue(rangeSlider
										.getMaximum());
							}

							// Set the begin-labels
							beginTimestamp.setText(Long.toString(interval
									.getStart().getMainPoint()));
							beginTime.setText(new SimpleDateFormat(
									"dd/MM/yyyy - HH:mm:ss.SSS")
									.format(new Timestamp(interval.getStart()
											.getMainPoint())));

							// Set the end-labels
							endTimestamp.setText(Long.toString(interval
									.getEnd().getMainPoint()));
							endTime.setText(new SimpleDateFormat(
									"dd/MM/yyyy - HH:mm:ss.SSS")
									.format(new Timestamp(interval.getEnd()
											.getMainPoint())));
						}
					} else if ("interval".equals(evt.getPropertyName())) {
						ITimeInterval interval = ((ITimeInterval) evt
								.getNewValue());
						if (!evt.getNewValue().equals(evt.getOldValue())) {

							int i = (int) interval.getEnd()
									.minus(interval.getStart()).getMainPoint();
							rangeSlider.setMaximum(i);

							if (interval.getEnd().isInfinite()) {
								rangeSlider.setUpperValue(rangeSlider
										.getMaximum());

								// Set the begin-labels
								beginTimestamp.setText(Long.toString(interval
										.getStart().getMainPoint()));
								beginTime.setText(new SimpleDateFormat(
										"dd/MM/yyyy - HH:mm:ss.SSS")
										.format(new Timestamp(interval
												.getStart().getMainPoint())));

								// Set the end-labels
								endTimestamp.setText(Long.toString(interval
										.getEnd().getMainPoint()));
								endTime.setText(new SimpleDateFormat(
										"dd/MM/yyyy - HH:mm:ss.SSS")
										.format(new Timestamp(interval.getEnd()
												.getMainPoint())));
							}
						}
					}

					// Set the begin-labels
					beginTimestamp.setText(Long.toString(manager
							.getIntervalStart().getMainPoint()));
					beginTime.setText(new SimpleDateFormat(
							"dd/MM/yyyy - HH:mm:ss.SSS").format(new Timestamp(
							manager.getIntervalStart().getMainPoint())));

					// Set the end-labels
					// Start + selection -> better if end is infinity
					endTimestamp.setText(Long.toString(manager
							.getIntervalStart().getMainPoint()
							+ rangeSlider.getSelection()[1]));
					endTime.setText(new SimpleDateFormat(
							"dd/MM/yyyy - HH:mm:ss.SSS").format(new Timestamp(
							manager.getIntervalStart().getMainPoint()
									+ rangeSlider.getSelection()[1])));

					// Set the time-range-label
					long timeInMs = rangeSlider.getUpperValue()
							- rangeSlider.getLowerValue();
					timeRangeLabel.setText(msToTimeString(timeInMs));

					// Save the values to compare, which one was dragged
					previousUpperValue = rangeSlider.getUpperValue();
				} else {
					if ("maxInterval".equals(evt.getPropertyName()) || "interval".equals(evt.getPropertyName())) {
						ITimeInterval interval = ((ITimeInterval) evt
								.getNewValue());
						int i = (int) interval.getEnd()
								.minus(interval.getStart()).getMainPoint();
						savedMaxSlider = i;
					}
				}
			}
		});
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
