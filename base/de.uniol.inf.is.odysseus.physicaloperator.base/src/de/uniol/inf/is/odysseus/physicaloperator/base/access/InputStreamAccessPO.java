package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;

public class InputStreamAccessPO<In, Out extends IMetaAttributeContainer<?>> extends
		AbstractSource<Out> implements IIterableSource<Out>{//, IP2PInputPO {

	private String host;
	private int port;
	private Out buffer;
	protected ObjectInputStream iStream;
	final private IDataTransformation<In, Out> transformation;
	private boolean p2p = false;
//	private boolean connectToPipe = false;

	private boolean done = false;

	public InputStreamAccessPO(ObjectInputStream iStream,
			IDataTransformation<In, Out> transformation) {

		this.iStream = iStream;
		this.transformation = transformation;
	}

	public InputStreamAccessPO(String host, int port,
			IDataTransformation<In, Out> transformation) {
		this.host = host;
		this.port = port;
		this.transformation = transformation;
	}

	@Override
	final protected void process_open() throws OpenFailedException {
		done = false;
		if (!p2p) {
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
	}

	@SuppressWarnings("unchecked")
	@Override
	final synchronized public boolean hasNext() {

//		if (p2p) {
//			if (!connectToPipe) {
//				return false;
//			}
//			if (this.iStream == null) {
//				Socket s;
//				while (true) {
//					try {
//						s = new Socket(host, port);
//						this.iStream = new ObjectInputStream(s.getInputStream());
//					} catch (Exception e) {
//						// throw new OpenFailedException(e.getMessage());
//						System.err
//								.println("Konnte Quelle nicht öffnen");
//						try {
//							Thread.sleep(5000);
//						} catch (InterruptedException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//						continue;
//					}
//					break;
//				}
//			}
//		}

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

	@Override
	protected void process_done() {
		done = true;
		try {
			this.iStream.close();
		} catch (IOException e) {
			// we are done, we don't care anymore for exceptions
		}
	}

	@Override
	final synchronized public void transferNext() {
		if (buffer == null) {
			if (!hasNext()) {
				propagateDone();// TODO wie soll mit diesem fehler umgegangen
				// werden
				throw new NoSuchElementException();
			}
		}
		transfer(buffer);
		buffer = null;
	}

	@Override
	public boolean isDone() {
		return done;
	}

//	@Override
//	public void setConnectToPipe(boolean connectToPipe) {
//		this.connectToPipe = connectToPipe;
//	}

//	public boolean isP2P() {
//		return p2p;
//	}
//
//	public void setP2P(boolean p2p) {
//		this.p2p = p2p;
//	}

}
