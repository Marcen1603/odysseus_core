package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public class SPGeneratorPO<T extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<T, T> {

	static boolean secondStream = true;
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
		Tuple<?> obj = (Tuple<?>) object;

		String[] sps;
		if (temp) {
			sps = initSecondStream();

		} else {
			secondStream = true;
			sps = initFirstStream();

		}
//		zum kompletttest
//		if(counter%10==0&&counter%40!=0){
//			String[] sp = sps[spCounter % 5].split(";");
//		
//			
//			SecurityPunctuation spToSend = new SecurityPunctuation("*",sp[1], sp[2], true, true, new PointInTime(System.currentTimeMillis()));
//			sendPunctuation(spToSend);
//			spCounter++;
//		}if(counter%40==0){
//			String[] sp = sps[spCounter % 5].split(";");
//			long[] ts=new long[2];
//			ts[0]=System.currentTimeMillis()+900;
//			ts[1]=System.currentTimeMillis()+5100;
//			
//			SecurityPunctuation spToSend = new SecurityPunctuation(ts, sp[1], sp[2], true, true, new PointInTime(System.currentTimeMillis()));
//			sendPunctuation(spToSend);
//			spCounter++;
//		}
		if(counter%10==0){
			String[] sp = sps[spCounter % 3].split(";");
			long[] ts=new long[2];
			ts[0]=counter;
			ts[1]=counter+3;
			SecurityPunctuation spToSend = new SecurityPunctuation(ts, sp[1], sp[2], true, true, new PointInTime(System.currentTimeMillis()));
			sendPunctuation(spToSend);
			spCounter++;
		}
//		//zum test der Join-Funktion
//		if(counter%2==0&&counter!=8){
//			String[] sp = sps[spCounter % 3].split(";");
//			long[] ts=new long[2];
//			ts[0]=System.currentTimeMillis();
//			ts[1]=System.currentTimeMillis()+2100;
//			SecurityPunctuation spToSend = new SecurityPunctuation(sp[0], sp[1], sp[2], true, true, new PointInTime(System.currentTimeMillis()));
//			sendPunctuation(spToSend);
//			spCounter++;
//		}
//		if (counter % 8 == 0) {
//			String[] sp = sps[spCounter % 3].split(";");
//			long[] ts=new long[2];
//			ts[0]=System.currentTimeMillis()-100;
//			ts[1]=System.currentTimeMillis()+2200;
//			
//			SecurityPunctuation spToSend = new SecurityPunctuation(ts, sp[1], sp[2], true, true, new PointInTime(System.currentTimeMillis()));
//			
//
//		sendPunctuation(spToSend);
//		spCounter++;
//	}

	transfer(object);
		counter++;

	}
	//zum kompletttest
//	private String[] initSecondStream() {
//	String[] sps = new String[5];
//	sps[0] = "*;*;*;true;false";
//	sps[1] = "*;breathingRate,pulse;A,queryexecutor;true;false";
//	sps[2] = "*;patientID,breathingRate,pulse;A;true;false";
//	sps[3] = ";breathingRate,patientID,pulse;A,B;true;false";
//	sps[4] = "*;breathingRate,patientID,pulse;A,queryexecutor;true;false";
//	return sps;
//}
//
//private String[] initFirstStream() {
//	String[] sps = new String[5];
//	sps[0] = "*;pulse,patientID,breathingRate;nurse,doktor;true;false";
//	sps[1] = "*;pulse,patientID,breathingRate;doktor;true;false";
//	sps[2] = ";pulse,patientID,breathingRate;nurse,doktor;true;false";
//	sps[3] = "*;*;doctor,doktor;true;false";
//	sps[4] = "*;breathingRate,patientID,pulse;doktor;true;false";
//	return sps;
//}
	
//// 	Zum Test der Join-Funktion , nur jeweils die ersten 3 arrayeinträge genutzt
	private String[] initSecondStream() {
		String[] sps = new String[5];
		sps[0] = "*;*;*;true;false";
		sps[1] = "*;id,patientID,B;nurse,queryexecutor;true;false";
		sps[2] = "*;id,patientID,B;*;true;false";
		sps[3] = ";patientID,pulse;doctor,nurse,queryexecutor;true;false";
		sps[4] = "*;breathingRate,patientID,pulse,B;doctor,queryexecutor;true;false";
		return sps;
	}

	private String[] initFirstStream() {
		String[] sps = new String[5];
		sps[0] = "*;patientID;nurse,queryexecutor;true;false";
		sps[1] = "*;patientID;queryexecutor;true;false";
		sps[2] = ";patientID;nurse,queryexecutor;true;false";
		sps[3] = ";breathingRate,patientID,pulse;doctor,nurse,queryexecutor;true;false";
		sps[4] = ";breathingRate,patientID,pulse,A;queryexecutor;true;false";
		return sps;
	}
	
	

//	private String[] initSecondStream() {
//		String[] sps = new String[5];
//		sps[0] = ";pulse,patientID;nurse,doctor;true;false";
//		sps[1] = "1,1;pulse,patientID;doctor,queryexecutor;true;false";
//		sps[2] = "*;pulse;nurse,queryexecutor;true;false";
//		sps[3] = "*;pulse;doctor,nurse,queryexecutor;true;false";
//		sps[4] = "*;pulse,patientID;doctor,queryexecutor;true;false";
//		return sps;
//	}
//
//	private String[] initFirstStream() {
//		String[] sps = new String[5];
//		sps[0] = "*;pulse,breathingRate,patientID;nurse,queryexecutor,doctor;true;false";
//		sps[1] = "*;*;*;true;false";
//		sps[2] = ";breathingRate;nurse,queryexecutor;true;false";
//		sps[3] = "*;*;doctor,nurse,queryexecutor;true;false";
//		sps[4] = "*;breathingRate,patientID;doctor,queryexecutor;true;false";
//		return sps;
//	}

	// Security Shield Latenz
	// private String[] initSecondStream() {
	// String[] sps = new String[5];
	// sps[0] =
	// counter+1+","+counter2+";patientID,pulse,breathingRate;queryexecutor;true;false";
	// sps[1] =
	// "*;patientID,pulse,breathingRate;queryexecutor,doctor;true;false";
	// sps[2] = "*;patientID,pulse,breathingRate;nurse,doctor;true;false";
	// sps[3] =
	// counter+1+","+counter2+";patientID,pulse,breathingRate;*;true;false";
	// sps[4] =
	// counter+1+","+counter2+";patientID,pulse,breathingRate;queryexecutor,nurse;true;false";
	// return sps;
	// }
	//
	// private String[] initFirstStream() {
	// String[] sps = new String[5];
	// sps[0] =
	// counter+1+","+counter2+";patientID,pulse,breathingRate;queryexecutor;true;false";
	// sps[1] =
	// "*;patientID,pulse,breathingRate;queryexecutor,doctor;true;false";
	// sps[2] = "*;patientID,pulse,breathingRate;nurse,doctor;true;false";
	// sps[3] =
	// counter+1+","+counter2+";patientID,pulse,breathingRate;*;true;false";
	// sps[4] =
	// counter+1+","+counter2+";patientID,pulse,breathingRate;queryexecutor,nurse;true;false";
	// return sps;
	// }

	// private String[] initSecondStream() {
	// String[] sps = new String[5];
	// sps[0] = counter+1+","+counter2+";breathingRate;nurse;true;false";
	// sps[1] = counter+1+","+counter2+";breathingRate;doctor;true;false";
	// sps[2] = counter+1+","+counter2+";breathingRate;nurse;true;false";
	// sps[3] = counter+1+","+counter2+";pulse,breathingRate;nurse;true;false";
	// sps[4] = counter+1+","+counter2+";pulse,breathingRate;nurse;true;false";
	// return sps;
	// }
	//
	// private String[] initFirstStream() {
	// String[] sps = new String[5];
	// sps[0] =
	// counter+1+","+counter2+";patientID,pulse;nurse,doctor;true;false";
	// sps[1] = counter+1+","+counter2+";patientID;nurse,doctor;true;false";
	// sps[2] = counter+1+","+counter2+";patientID;nurse,doctor;true;false";
	// sps[3] = counter+1+","+counter2+";patientID;nurse,doctor;true;false";
	// sps[4] = counter+1+","+counter2+";patientID;nurse,doctor;true;false";
	// return sps;
	// }

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
