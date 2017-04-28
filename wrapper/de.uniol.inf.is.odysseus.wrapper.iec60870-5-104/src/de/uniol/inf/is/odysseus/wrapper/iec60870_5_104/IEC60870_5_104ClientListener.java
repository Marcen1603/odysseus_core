package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104;

import java.io.IOException;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.ConnectionEventListener;

//TODO javaDoc
//TODO implement
public class IEC60870_5_104ClientListener implements ConnectionEventListener {

	public IEC60870_5_104ClientListener(IEC60870_5_104TransportHandler iec60870_5_104TransportHandler) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void connectionClosed(IOException arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newASdu(ASdu arg0) {
		// TODO Auto-generated method stub

	}

}