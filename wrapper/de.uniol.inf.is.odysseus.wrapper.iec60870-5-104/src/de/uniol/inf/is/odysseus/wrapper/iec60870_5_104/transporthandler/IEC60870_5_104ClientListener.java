package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.transporthandler;

import java.io.IOException;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.ConnectionEventListener;

//TODO javaDoc
//TODO implement
public class IEC60870_5_104ClientListener implements ConnectionEventListener {

	@Override
	public void connectionClosed(IOException e) {
		IEC60870_5_104TransportHandler.log.error("Received connection closed signal!", e);
	}

	@Override
	public void newASdu(ASdu asdu) {
		// TODO Auto-generated method stub
		IEC60870_5_104TransportHandler.log.debug("new ASDU: {}", asdu);
	}

}