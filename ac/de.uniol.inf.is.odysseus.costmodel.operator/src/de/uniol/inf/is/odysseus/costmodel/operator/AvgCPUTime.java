package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;

public class AvgCPUTime extends AbstractMonitoringData<Double> implements IPOEventListener {

	public static final String METADATA_TYPE_NAME = "avgCPUTime";
	private static double granularity;
	private static final int MAX_DATA = 50;
	private long start1 = 0;
	private long end1 = 0;
	private long start2 = 0;
	private long end2 = 0;
	
	private long runSum = 0;
	private long runCount = 0;
	
//	boolean output = false;
	
	static {
		long first = System.nanoTime();
		while( true ) {
			double second = System.nanoTime();
			if( second > first ) {
				granularity = second - first;
//				System.out.println("Granulariry of nanoTime(): " + granularity);
				break;
			}
		}
	}
	
	private List<Long> runTimes = new LinkedList<Long>();
	private List<Long> sorted = new LinkedList<Long>();

	public AvgCPUTime(IPhysicalOperator target) {
		super(target, METADATA_TYPE_NAME);
		setTarget(target);
		
//		if( target.getName().contains("Join")) {
//			output = true;
//		}
	}

	public AvgCPUTime(AvgCPUTime avgProcessingTime) {
		super(avgProcessingTime);
		this.start1 = avgProcessingTime.start1;
		this.start2 = avgProcessingTime.start2;
		this.end1 = avgProcessingTime.end1;
		this.end2 = avgProcessingTime.end2;
		this.runSum = avgProcessingTime.runSum;
		this.runTimes = avgProcessingTime.runTimes;
		this.runCount = avgProcessingTime.runCount;
	}

	public void setTarget(IPhysicalOperator target) {
		super.setTarget(target);
		target.subscribe(this, POEventType.ProcessInit);
		target.subscribe(this, POEventType.ProcessDone);
		target.subscribe(this, POEventType.PushInit);
		target.subscribe(this, POEventType.PushDone);
	}

	@Override
	public void eventOccured(IEvent<?, ?> poEvent, long eventNanoTime) {
		if (poEvent.getEventType().equals(POEventType.ProcessInit)) {
			start1 = System.nanoTime();
		} else if (poEvent.getEventType().equals(POEventType.PushInit)) {
			end1 = System.nanoTime();
		} else if (poEvent.getEventType().equals(POEventType.PushDone)) {
			start2 = System.nanoTime();
		} else if (poEvent.getEventType().equals(POEventType.ProcessDone)){
			end2 = System.nanoTime();
			
			Long lastRun = new Long( ( end2 - start2 ) + ( end1 - start1) );
			if( lastRun < granularity )
				lastRun = (long) granularity;
			
			runTimes.add(lastRun);
			runSum += lastRun;
			runCount++;

			if (!sorted.isEmpty()) {
				ListIterator<Long> iterator = sorted.listIterator();
				boolean added = false;
				while (iterator.hasNext()) {
					Long v = iterator.next();
					if (v >= lastRun) {
						iterator.previous();
						iterator.add(lastRun);
						iterator.next();
						added = true;
						break;
					}
				}
				// end of list?
				if (!added && !iterator.hasNext())
					sorted.add(lastRun);

			} else {
				// list is empty
				sorted.add(lastRun);
			}

			
			if( runTimes.size() > MAX_DATA ) {
				Long v = runTimes.remove(0);
				runSum -= v;
				sorted.remove(v);
			}
//			
//			if( output ) {
//				System.out.print(lastRun + ", ");
//				System.out.println(getValue());
//			}
		}
	}

	@Override
	public Double getValue() {
		if ( !sorted.isEmpty() && runTimes.size() > MAX_DATA / 2 )
			return new Double(sorted.get(sorted.size() / 2));
		return null;
	}

	@Override
	public void reset() {
		start1 = 0;
		start2 = 0;
		end1 = 0;
		end2 = 0;
		runCount = 0;
		runTimes = new LinkedList<Long>();
	}

	@Override
	public AvgCPUTime clone() {
		return new AvgCPUTime(this);
	}

}
