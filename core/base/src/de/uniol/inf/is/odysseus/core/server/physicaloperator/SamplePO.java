package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public class SamplePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	final private int sampleRate;
	private int current = 0;
	
	public SamplePO(int sampleRate){
		this.sampleRate = sampleRate;
	}
	
	public SamplePO(SamplePO<T> samplePO) {
		super(samplePO);
		this.sampleRate = samplePO.sampleRate;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		current++;
		if (current == sampleRate) {
			transfer(object);
			current = 0;
		}else{
			transfer(object,1);
		}
		
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new SamplePO<T>(this);
	}

}
