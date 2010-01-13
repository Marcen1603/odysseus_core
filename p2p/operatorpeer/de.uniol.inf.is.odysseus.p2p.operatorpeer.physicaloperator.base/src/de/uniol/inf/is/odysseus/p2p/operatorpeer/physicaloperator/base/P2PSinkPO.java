package de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSink;

public class P2PSinkPO<T> extends AbstractSink<T> {
	
	private String queryId;
	private PipeAdvertisement adv;
	public ArrayList<StreamHandler> subscribe = new ArrayList<StreamHandler>();
	private PeerGroup peerGroup;

	class ConnectionListener extends Thread {

		@Override
		public void run() {
			JxtaServerSocket server = null;
			try {
				server = new JxtaServerSocket(getPeerGroup(), adv, 1000);
				server.setSoTimeout(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (true) {
				Socket socket = null;
				try {
					socket = server.accept();

					if (socket != null) {
						synchronized (subscribe) {
							StreamHandler temp = new StreamHandler(socket);
							subscribe.add(temp);
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class StreamHandler {
		InputStream in;
		ObjectInputStream oin;
		ObjectOutputStream oout;
		OutputStream out;
		Socket socket;

		public StreamHandler(Socket socket) {
			this.socket = socket;
			try {
				out = socket.getOutputStream();
				in = socket.getInputStream();
				oout = new ObjectOutputStream(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while(getSubscribedTo().get(0)==null){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void transfer(Object o) {
			try {
				oout.writeObject(o);
				oout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void done() {
			try {
				//Signalisieren, dass dieser Peer fertig ist.
				oout.writeObject(new Integer(0));
				oout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
				oout.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}


	public P2PSinkPO(String adv, PeerGroup peerGroup) {
		super();
		this.peerGroup = peerGroup;
		this.adv = MessageTool.createPipeAdvertisementFromXml(adv);
		ConnectionListener listener = new ConnectionListener();
		listener.start();
	}


	@Override
	protected void process_next(T object, int port, boolean isReadOnly) {
		synchronized (subscribe) {
			for (StreamHandler sh : subscribe) {
				sh.transfer(object);
			}
		}
	}


	
	protected void process_done() {
		synchronized (subscribe) {
			for (StreamHandler sh : subscribe) {
				sh.done();
			}
		}
	}

	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	
	public void setAdv(PipeAdvertisement adv) {
		this.adv = adv;
	}
	
	public PipeAdvertisement getAdv() {
		return adv;
	}
	

	public PeerGroup getPeerGroup() {
		return peerGroup;
	}

	public void setPeerGroup(PeerGroup peerGroup) {
		this.peerGroup = peerGroup;
	}





}
