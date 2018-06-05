package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public class SPGeneratorPO<T extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	static boolean secondStream = true;
	boolean temp;
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
		this.temp = secondStream;
		counterOne = 0;
		counterTwo = 0;
		counter = 0;
		spCounter = 0;
		secondStream = false;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {

		String[] sps;
		if (temp) {
			sps = initSecondStream();

		} else {
			secondStream = true;
			sps = initFirstStream();

		}


		// zum test der Join-Funktion
		if (counter % 10 == 0) {
			String[] sp = sps[spCounter % 5].split(";");
			long[] ts = new long[2];
			ts[0] = System.currentTimeMillis()-1;
			ts[1] = System.currentTimeMillis() + 8;
			SecurityPunctuation spToSend = new SecurityPunctuation(ts, sp[1], sp[2], true, true,
					new PointInTime(System.currentTimeMillis()));
			sendPunctuation(spToSend);
			spCounter++;
		}


		transfer(object);
		counter++;

	}

	// zum kompletttest
	private String[] initFirstStream() {
		String[] sps = new String[5];
		sps[0] = "*;pulse,patientID;queryexecutor,doctor,nurse;true;false";
		sps[1] = ";pulse,patientID,breathingRate;nurse,queryexecutor;true;false";
		sps[2] = "*;patientID,pulse,breathingRate;*;true;false";
		sps[3] = "*;*;*;true;false";
		sps[4] = "*;*;queryexecutor;true;false";
		return sps;
	}

	private String[] initSecondStream() {
		String[] sps = new String[5];
		sps[0] = "*;breathingRate,patientID,pulse;queryexecutor,nurse,doctir;true;false";
		sps[1] = "*;breathingRate,patientID,pulse;*;true;false";
		sps[2] = ";A,C,D,patientID;*;true;false";
		sps[3] = "*;*;queryexecutor,nurse;true;false";
		sps[4] = "*;A,B,C,D,patientID,breathingRate;*;true;false";
		return sps;
	}



	// Security Shield test:
	// private String[] initSecondStream() {
	// String[] sps = new String[5];
	// sps[0] =
	// counter+1+","+counter2+";PatientenID,Atemfrequenz;queryexecutor;true;false";
	// sps[1] =
	// counter+1+","+counter2+";PatientID,Atemfrequenz;queryexecutor;true;false";
	// sps[2] =
	// counter+1+","+counter2+";PatientID,Atemfrequenz;queryexecutor;true;false";
	// sps[3] =
	// counter+1+","+counter2+";PatientID,Puls,Atemfrequenz;Krankenschwester;true;false";
	// sps[4] =
	// counter+1+","+counter2+";PatientID,Puls,Atemfrequenz;queryexecutor;true;false";
	// return sps;
	// }
	//
	// private String[] initFirstStream() {
	// String[] sps = new String[5];
	// sps[0] = "*;patientID,breathingRate;*;true;false";
	// sps[1] = "*;patientID,pulse;*;queryexecutor;false";
	// sps[2] = "*;patientID,pulse;queryexecutor;false;false";
	// sps[3] =
	// counter+1+","+counter2+";patientID,breathingRate;Krankenschwester;true;false";
	// sps[4] = counter+1+","+counter2+";patientID,pulse;*;true;false";
	// return sps;
	// }

}
