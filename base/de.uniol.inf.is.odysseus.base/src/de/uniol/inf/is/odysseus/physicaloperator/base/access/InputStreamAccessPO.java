package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.net.Socket;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class InputStreamAccessPO<In, Out extends IMetaAttributeContainer<?>> extends
		AbstractInputStreamAccessPO<In, Out>{

	private String host;
	private int port;

	public InputStreamAccessPO(String host, int port,
			IDataTransformation<In, Out> transformation) {
		super(transformation);
		this.host = host;
		this.port = port;
	}

	@Override
	final protected void process_open() throws OpenFailedException {
		done = false;
		if (this.iStream == null) {
			Socket s;
			try {
				s = new Socket(host, port);
				this.iStream = new ObjectInputStream(s.getInputStream());
			} catch (Exception e) {
				throw new OpenFailedException(e.getMessage());
			}
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	final synchronized public boolean hasNext() {
		if (buffer != null) {
			return true;
		}
		try {
			In inElem = (In) this.iStream.readObject();
			if (inElem == null) {
				// TODO: Darf eigentlich gar nicht sein ...
				throw new Exception("null element from socket");
			}
			buffer = this.transformation.transform(inElem);
			return true;
		} catch (EOFException e) {
			propagateDone();
			return false;
		} catch (Exception e) {
			// e.printStackTrace();
			// TODO fehlerzustand irgendwie signalisieren?
			propagateDone();
			return false;
		}
	}


}
