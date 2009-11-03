package de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IDataTransformation;

public class P2PSocketInputStreamAccessPO<In, Out extends IMetaAttributeContainer<?>> extends
		AbstractIterableSource<Out> implements IP2PInputPO{

	private String host;
	private int port;
	private Out buffer;
	protected ObjectInputStream iStream;
	final private IDataTransformation<In, Out> transformation;
	private boolean p2p = true;
	private boolean connectToPipe = false;

	private boolean done = false;

	public P2PSocketInputStreamAccessPO(ObjectInputStream iStream,
			IDataTransformation<In, Out> transformation) {
		this.iStream = iStream;
		this.transformation = transformation;
		System.out.println("anderer Konstruktor!!! InputStream ist leer? "+(this.iStream=null));
	}

	public P2PSocketInputStreamAccessPO(String host, int port,
			IDataTransformation<In, Out> transformation) {
		this.host = host;
		this.port = port;
		this.transformation = transformation;

		System.out.println("InputStream ist leer? "+(this.iStream=null));
		System.out.println("Erzeue P2P Access");
	}

	@Override
	final protected void process_open() throws OpenFailedException {
		done = false;
//		if (!p2p) {
//			if (this.iStream == null) {
//				Socket s;
//				try {
//					s = new Socket(host, port);
//					this.iStream = new ObjectInputStream(s.getInputStream());
//				} catch (Exception e) {
//					throw new OpenFailedException(e.getMessage());
//				}
//			}
//		}
	}

	@SuppressWarnings("unchecked")
	@Override
	final synchronized public boolean hasNext() {
		System.out.println("HASNEXT");
		if (p2p) {
			if (!connectToPipe) {
				return false;
			}
			if (this.iStream == null) {
				Socket s;
				while (true) {
					try {
						s = new Socket(host, port);
						System.out.println("Host: "+host+" Port: "+port);
						this.iStream = new ObjectInputStream(s.getInputStream());
					} catch (Exception e) {
						// throw new OpenFailedException(e.getMessage());
						System.err
								.println("Konnte Quelle nicht öffnen");
						e.printStackTrace();
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						continue;
					}
					break;
				}
			}
		}

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
	

	public boolean isP2P() {
		return p2p;
	}

	@Override
	public void setP2P(boolean p2p) {
		this.p2p = p2p;
	}

	@Override
	public void setConnectedToPipe(boolean connectToPipe) {
		System.out.println("setze connectedtoPipe im P2PSocketinputstream auf "+connectToPipe);
		this.connectToPipe = connectToPipe;
		
	}

}
