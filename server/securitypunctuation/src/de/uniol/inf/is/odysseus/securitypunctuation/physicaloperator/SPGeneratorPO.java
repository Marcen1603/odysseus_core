package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;


public  class SPGeneratorPO<T extends IStreamObject<?>> extends AbstractPipe<T,T>{


	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
		
	}
	public SPGeneratorPO(){
		super();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		int randomizer=(int)(Math.random()*5);
		if(randomizer==4){
			sendPunctuation(new SecurityPunctuation("Stream1","121,122","Heartbeat","Nurse",true,true,new Long(232)));
			
		}transfer(object);
		
	}
	
	



	
	
	


}
