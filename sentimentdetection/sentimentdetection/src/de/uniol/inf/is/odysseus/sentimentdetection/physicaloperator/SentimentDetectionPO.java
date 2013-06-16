package de.uniol.inf.is.odysseus.sentimentdetection.physicaloperator;


import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

@SuppressWarnings({ "rawtypes" })
public class SentimentDetectionPO<T extends IStreamObject<?>> extends
		AbstractPipe<T, T> {
	
	int current = 0 ; 

	public SentimentDetectionPO() {
	}

	public SentimentDetectionPO(SentimentDetectionPO<T> senti) {
		super(senti);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
			
		if(current %2 == 0 ){
			System.out.println("Ausgabe an Port 0:");
			transfer(object,0);
		}else{
			System.out.println("Ausgabe an Port 1:");
			transfer(object,1);
		}
		current++;
		
	
	
	}

	@Override
	public SentimentDetectionPO<T> clone() {
		return new SentimentDetectionPO<T>(this);
	}

}
