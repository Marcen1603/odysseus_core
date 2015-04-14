package de.uniol.inf.is.odysseus.peer.transmission.socket;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public interface IReceivingDataThreadListener {

	public void onReceivingData(byte[] msg);
	public void onReceivingPunctuation(IPunctuation punc);
	public void onReceivingDoneEvent();
	public void onFinish();
	
}
