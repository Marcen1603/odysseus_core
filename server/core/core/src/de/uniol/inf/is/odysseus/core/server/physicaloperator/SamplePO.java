package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SampleAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

public class SamplePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	final private int sampleRate;
	private int current = 0;
	private final TimeValueItem timeValue;
	private TimeValueItem startTime;
	private final TimeUnit baseTimeUnit;
	
	public SamplePO(SampleAO sampleAO){
		
		this.sampleRate = sampleAO.getSampleRate();
		this.baseTimeUnit = sampleAO.getBaseTimeUnit();
		if(sampleAO.getTimeValue() != null) {
			
			this.timeValue = new TimeValueItem(
					this.baseTimeUnit.convert(sampleAO.getTimeValue().getTime(), sampleAO.getTimeValue().getUnit()), 
					this.baseTimeUnit);
			
		} else this.timeValue = null;
		
	}
	
	public SamplePO(SamplePO<T> samplePO) {
		super(samplePO);
		this.sampleRate = samplePO.sampleRate;
		this.timeValue = samplePO.timeValue;
		this.baseTimeUnit = samplePO.baseTimeUnit;
		
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		
		if(this.timeValue != null) {
		
			final TimeValueItem currentTime = new TimeValueItem(
					((ITimeInterval) object.getMetadata()).getStart().getMainPoint(), 
					this.baseTimeUnit);
			
			if(this.startTime == null) {
				
				this.startTime = currentTime;
				this.transfer(object);
				
			} else if((currentTime.getTime() - this.startTime.getTime()) % this.timeValue.getTime() == 0)
				this.transfer(object);
			else this.transfer(object, 1);
			
		} else {
		
			current++;
			if (current == sampleRate) {
				transfer(object);
				current = 0;
			}else{
				transfer(object,1);
			}
			
		}
		
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		return new SamplePO<T>(this);
	}

}
