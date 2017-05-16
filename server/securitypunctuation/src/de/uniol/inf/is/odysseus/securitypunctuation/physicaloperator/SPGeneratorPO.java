package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public class SPGeneratorPO<T extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	static boolean secondStream=true;
	boolean temp;
	private static final Logger LOG = LoggerFactory.getLogger(SecurityShieldPO.class);
	int counterOne;
	int counterTwo;
	int counter;
	int spCounter;
	int counter2;

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);

	}

	public SPGeneratorPO() {
		super();
		this.temp=secondStream;
		counterOne = 0;
		counterTwo = 0;
		counter = 0;
		spCounter = 0;
		secondStream=false;
	}



	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
	Tuple<?> obj=(Tuple<?>)object;
	counter=(int)(long)obj.getAttribute(0);
	counter2=counter+5;
		String[] sps;
		if (temp) {
			sps = initSecondStream();
			
		} else {
			//sps = initFirstStream();
			sps=initFirstStream();
		
		}
		if (counter % 3 == 0) {
			String[] sp = sps[spCounter % 5].split(";");
			SecurityPunctuation spToSend = new SecurityPunctuation(sp[0], sp[1], sp[2], true, true,
					object.getMetadata().getStart());
			if(counter%12==0){
				SecurityPunctuation spToSend2 = new SecurityPunctuation("*", "patientID,pulse", "Krankenschwester", true, true,
						object.getMetadata().getStart());
				sendPunctuation(spToSend2);
			}
			sendPunctuation(spToSend);
			spCounter++;
		}
		transfer(object);
//		counter++;
		
	}

	private String[] initSecondStream() {
		String[] sps = new String[5];
		sps[0] = counter+1+","+counter2+";PatientenID,Atemfrequenz;queryexecutor;true;false";
		sps[1] = counter+1+","+counter2+";PatientID,Atemfrequenz;queryexecutor;true;false";
		sps[2] = counter+1+","+counter2+";PatientID,Atemfrequenz;queryexecutor;true;false";
		sps[3] = counter+1+","+counter2+";PatientID,Puls,Atemfrequenz;Krankenschwester;true;false";
		sps[4] = counter+1+","+counter2+";PatientID,Puls,Atemfrequenz;queryexecutor;true;false";
		return sps;
	}

	private String[] initFirstStream() {
		String[] sps = new String[5];
		sps[0] = "*;patientID,breathingRate;*;true;false";
		sps[1] = "*;patientID,pulse;*;queryexecutor;false";
		sps[2] = "*;patientID,pulse;queryexecutor;false;false";
		sps[3] = counter+1+","+counter2+";patientID,breathingRate;Krankenschwester;true;false";
		sps[4] = counter+1+","+counter2+";patientID,pulse;*;true;false";
		return sps;
	}

}
