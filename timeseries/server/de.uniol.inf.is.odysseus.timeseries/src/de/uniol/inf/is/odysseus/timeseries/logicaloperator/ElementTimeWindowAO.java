package de.uniol.inf.is.odysseus.timeseries.logicaloperator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * This logical operator defines a non blocking time window by elements. This
 * requires, that the elements have constant time intervals (e. g. set by the
 * {@link RegularTimeSeriesAO}).
 * 
 * Therefore, elements in a TimeWindow could be count and the time window can be
 * determined.
 * 
 * @author Christoph Schröer
 *
 */
@LogicalOperator(name = "ELEMENTTIMEWINDOW", minInputPorts = 1, maxInputPorts = 1, category = {
		LogicalOperatorCategory.MINING }, doc = "This logical operator defines a non blocking time window by elements. This requires, that the elements have constant time intervals.")
public class ElementTimeWindowAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 7752589543824364696L;

	private TimeValueItem regularWindowSize = null;

	private int elementSize;

	/**
	 * How long is defined day
	 */
	private TimeValueItem dayDuration = null;

	/**
	 * How long is defined night
	 * 
	 */
	private TimeValueItem nightDuration = null;

	/**
	 * Hour, on which a defined day starts, e.g. 09:00
	 */
	private String timeStart;

	/**
	 * Hour, on which a defined day ends, e.g. 17:30
	 */
	private String timeEnd;

	private TimeValueItem weekDuration = null;

	private TimeValueItem weekendDuration = null;

	private Integer dayStart;

	private Integer dayEnd;

	public ElementTimeWindowAO(final ElementTimeWindowAO elementTimeWindowAO) {
		super(elementTimeWindowAO);
		this.regularWindowSize = elementTimeWindowAO.getRegularWindowSize();
		this.elementSize = elementTimeWindowAO.getElementSize();
		this.dayDuration = elementTimeWindowAO.getDayDuration();
		this.nightDuration = elementTimeWindowAO.getNightDuration();
		this.timeStart = elementTimeWindowAO.getTimeStart();
		this.timeEnd = elementTimeWindowAO.getTimeEnd();

		this.dayStart = elementTimeWindowAO.getDayStart();
		this.dayEnd = elementTimeWindowAO.getDayEnd();
		this.weekDuration = elementTimeWindowAO.getWeekDuration();
		this.weekendDuration = elementTimeWindowAO.getWeekendDuration();

	}

	public ElementTimeWindowAO() {
		super();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ElementTimeWindowAO(this);
	}

	public TimeValueItem getRegularWindowSize() {
		return this.regularWindowSize;
	}

	@Parameter(name = "regular_window_size", type = TimeParameter.class, doc = "Window size of regular time series.")
	public void setRegularWindowSize(final TimeValueItem regularWindowSize) {
		this.regularWindowSize = regularWindowSize;
	}

	public int getElementSize() {
		return elementSize;
	}

	@Parameter(name = "element_size", type = IntegerParameter.class, doc = "Number of elements in time window.")
	public void setElementSize(int elementSize) {
		this.elementSize = elementSize;
	}

	public String getTimeStart() {
		return timeStart;
	}

	@Parameter(name = "time_start", type = StringParameter.class, doc = "Hour, on which a defined day starts, e.g. 09:00")
	public void setTimeStart(String timeStart) throws ParseException {
		this.timeStart = timeStart;

		if (this.timeEnd != null) {
			this.dayDuration = this.calcDayDuration(this.timeStart, timeEnd);
			this.nightDuration = this.calcNightDuration(this.timeStart, timeEnd);
		}
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	@Parameter(name = "time_end", type = StringParameter.class, doc = "Hour, on which a defined day ends, e.g. 17:30")
	public void setTimeEnd(String timeEnd) throws ParseException {
		this.timeEnd = timeEnd;

		if (this.timeStart != null) {
			this.dayDuration = this.calcDayDuration(this.timeStart, timeEnd);
			this.nightDuration = this.calcNightDuration(this.timeStart, timeEnd);
		}

	}

	@Parameter(name = "day_start", type = IntegerParameter.class, doc = "Day, on which a defined week starts, e.g. 1 for monday.", optional = true)
	public void setDayStart(int dayStart) throws ParseException {
		this.dayStart = dayStart;

		if (this.dayEnd != null) {
			this.weekDuration = this.calcWeekDuration(dayStart, dayEnd, this.timeStart, this.timeEnd);
			this.weekendDuration = this.calcWeekendDuration(dayStart, dayEnd, this.timeStart, this.timeEnd);
		}
	}

	public Integer getDayEnd() {
		return dayEnd;
	}

	@Parameter(name = "day_end", type = IntegerParameter.class, doc = "Day, on which a defined week ends, e.g. 5 for friday.", optional = true)
	public void getDayEnd(int dayEnd) throws ParseException {
		this.dayEnd = dayEnd;

		if (this.dayStart != null) {
			this.weekDuration = this.calcWeekDuration(dayStart, dayEnd, this.timeStart, this.timeEnd);
			this.weekendDuration = this.calcWeekendDuration(dayStart, dayEnd, this.timeStart, this.timeEnd);
		}

	}

	public Integer getDayStart() {
		return this.dayStart;
	}

	private TimeValueItem calcWeekDuration(int dayStart, int dayEnd, String timeStart, String timeEnd)
			throws java.text.ParseException {

		// arbitrary week-date

		String[] weekDays = new String[7];
		weekDays[0] = "04.01.2016";
		weekDays[1] = "05.01.2016";
		weekDays[2] = "06.01.2016";
		weekDays[3] = "07.01.2016";
		weekDays[4] = "08.01.2016";
		weekDays[5] = "09.01.2016";
		weekDays[6] = "10.01.2016";

		String weekDayStart = weekDays[dayStart - 1] + timeStart;
		String weekDayEnd = weekDays[dayEnd - 1] + timeEnd;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

		Date dStart = null;
		Date dEnd = null;

		dStart = dateFormat.parse(weekDayStart);
		dEnd = dateFormat.parse(weekDayEnd);

		long diff = dEnd.getTime() - dStart.getTime();

		if (diff < 0) {
			throw new IllegalArgumentException("Start day after end day.");
		}

		long diffMilliseconds = diff;

		return new TimeValueItem(diffMilliseconds, TimeUnit.MICROSECONDS);
	}

	private TimeValueItem calcWeekendDuration(int dayStart, int dayEnd, String timeStart, String timeEnd)
			throws java.text.ParseException {

		// arbitrary week-date

		TimeValueItem weekDuration = this.calcWeekendDuration(dayStart, dayEnd, timeStart, timeEnd);
		long weekDurationLong = weekDuration.getTime();

		// 24 h * 60 min * 60 s * 1000 ms
		long completeWeekDurationLong = 86400000;

		return new TimeValueItem(completeWeekDurationLong - weekDurationLong, TimeUnit.MICROSECONDS);
	}

	private TimeValueItem calcDayDuration(String timeStart, String timeEnd) throws java.text.ParseException {

		// arbitrary day-date
		String dateStart = "01.01.2016 " + timeStart;
		String dateEnd = "01.01.2016 " + timeEnd;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

		Date dStart = null;
		Date dEnd = null;

		dStart = dateFormat.parse(dateStart);
		dEnd = dateFormat.parse(dateEnd);

		long diff = dEnd.getTime() - dStart.getTime();

		if (diff < 0) {
			throw new IllegalArgumentException("Start time after end time.");
		}

		//long diffMinutes = diff / (60 * 1000) % 60;
		long diffMilliseconds = diff;

		return new TimeValueItem(diffMilliseconds, TimeUnit.MICROSECONDS);
	}

	private TimeValueItem calcNightDuration(String timeStart, String timeEnd) throws java.text.ParseException {

		// arbitrary day-date
		String dateStart = "01.01.2016 " + timeEnd;
		String dateEnd = "02.01.2016 " + timeStart;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

		Date dStart = null;
		Date dEnd = null;

		dStart = dateFormat.parse(dateStart);
		dEnd = dateFormat.parse(dateEnd);

		long diff = dEnd.getTime() - dStart.getTime();

		if (diff < 0) {
			throw new IllegalArgumentException("Start time after end time.");
		}

		long diffMilliseconds = diff;

		return new TimeValueItem(diffMilliseconds, TimeUnit.MICROSECONDS);
	}

	public TimeValueItem getDayDuration() {
		return dayDuration;
	}

	public TimeValueItem getNightDuration() {
		return nightDuration;
	}

	public TimeValueItem getWeekDuration() {
		return weekDuration;
	}

	public TimeValueItem getWeekendDuration() {
		return weekendDuration;
	}

}
