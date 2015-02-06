package de.uniol.inf.is.odysseus.sports.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.sports.logicaloperator.ReduceLoadAO;

/**
 * Another implementation of the SamplePO to reduce load in Sports Queries.
 * @author Carsten Cordes
 *
 * @param <T>
 */
public class ReduceLoadPO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ReduceLoadPO.class);
	
	/**
	 * Sample Rate (if we are sampling per Element)
	 */
	final private int sampleRate;
	/**
	 * Time Value (to sample with time)
	 */
	private final TimeValueItem timeValue;
	
	/**
	 * Counter for elements used in elemt wise sampling
	 */
	private int current = 0;
	/**
	 * Next (Time) Interval after which a tuple can pass. 
	 */
	private long nextIntervalTime;
	
	/**
	 * Base Time Unit used in Operator.
	 */
	private final TimeUnit baseTimeUnit;
	
	/**
	 * HashMaps for Grouped Sampling.
	 */
	private Map<Long, Integer> currentGroups = new HashMap<>();
	private Map<Long, Long> timeIntervalGroups = new HashMap<>();
	
	/**
	 * Grouping Processor
	 */
	private IGroupProcessor<T,T> groupProcessor = null;
	
	/**
	 * Constructor
	 * @param ao Logical Operator
	 */
	public ReduceLoadPO(ReduceLoadAO ao){
		
		this.sampleRate = ao.getSampleRate();
		this.baseTimeUnit = ao.getBaseTimeUnit();

		if(ao.getTimeValue() != null) {
			
			this.timeValue = new TimeValueItem(
					this.baseTimeUnit.convert(ao.getTimeValue().getTime(), ao.getTimeValue().getUnit()), 
					this.baseTimeUnit);
			
		} else this.timeValue = null;
		
	}
	
	/**
	 * Sets GroupProcessor.
	 * @param groupProcessor
	 */
	public void setGroupProcessor(IGroupProcessor<T, T> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}
	
	/***
	 * Construcot
	 * @param samplePO Physical Operator
	 */
	public ReduceLoadPO(ReduceLoadPO<T> samplePO) {
		super(samplePO);
		this.sampleRate = samplePO.sampleRate;
		this.timeValue = samplePO.timeValue;
		this.baseTimeUnit = samplePO.baseTimeUnit;
		this.groupProcessor = samplePO.groupProcessor;
	}

	@Override
	/**
	 * Gets output mode.
	 */
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	
	@Override
	/**
	 * Process open
	 */
	protected void process_open() throws OpenFailedException {
		if (groupProcessor != null) {
			groupProcessor.init();
		}
	}

	@Override
	/**
	 * Process next evaulates tupels and let's those pass who are after a certain time or after a certain elemnt count depending on sampling method.
	 */
	protected void process_next(T object, int port) {
		
		///Time based Sampling.
		if(this.timeValue != null) {
			
			if(this.groupProcessor!=null) {
				this.nextIntervalTime = getGroupTimeValue(object);
				evaluateTime(object);
				putGroupTimeValue(object,this.nextIntervalTime);
			} else {
				evaluateTime(object);
			}
		} 
		
		///Element based Sampling
		else {
		
			if(groupProcessor!=null) {
				//Grouped sampling.
				current = getGroupElementValue(object);
				evaluateElementCount(object);
				putGroupElementValue(object,current);
				
			}
			else {
				///Ungrouped sampling.
				evaluateElementCount(object);
			}
		}
		
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	/**
	 * Gets element-value when grouping
	 * @param object Stream Object
	 * @return Element 
	 */
	private int getGroupElementValue(T object) {
		long groupID = groupProcessor.getGroupID(object);
		
		if(!currentGroups.containsKey(groupID)) {
			currentGroups.put(groupID, 0);
		}
		return currentGroups.get(groupID);
	}
	
	/**
	 * Gets time-value when grouping
	 * @param object Stream Object
	 * @return TimeValue as Long
	 */
	private long getGroupTimeValue(T object) {
		long groupID = groupProcessor.getGroupID(object);
		if(!timeIntervalGroups.containsKey(groupID)) {
			timeIntervalGroups.put(groupID, 0L);
		}
		return timeIntervalGroups.get(groupID);
	}
	
	/**
	 * Puts Group TimeValue in Hashmap
	 * @param object
	 * @param value
	 */
	private void putGroupTimeValue(T object, long value) {
		long groupID = groupProcessor.getGroupID(object);
		timeIntervalGroups.put(groupID,value);
	}
	
	/**
	 * Puts Group Elemen Value in Hashmap
	 * @param object
	 * @param value
	 */
	private void putGroupElementValue(T object, int value) {
		long groupID = groupProcessor.getGroupID(object);
		currentGroups.put(groupID,value);
	}
	
	/**
	 * Tests if tuple can pass and passes it to output 0 or 1.
	 * @param object
	 */
	private void evaluateElementCount(T object) {
		current++;
		if (current == sampleRate) {
			transfer(object);
			current = 0;
		}else{
			transfer(object,1);
		}
	}
	
	/**
	 * Tests if tuple can pass and passes it to output 0 or 1.
	 * @param object
	 */
	private void evaluateTime(T object) {
		final TimeValueItem currentTime = new TimeValueItem(
				((ITimeInterval) object.getMetadata()).getStart().getMainPoint(), 
				this.baseTimeUnit);
		
		if(currentTime.getTime() > this.nextIntervalTime) {
			this.nextIntervalTime = ((currentTime.getTime()/this.timeValue.getTime())*this.timeValue.getTime())+timeValue.getTime();
			this.transfer(object);
		} 
		else this.transfer(object, 1);
	}
	
	@Override
	/**
	 * Clone Operator.
	 */
	public AbstractPipe<T, T> clone() {
		return new ReduceLoadPO<T>(this);
	}

}
