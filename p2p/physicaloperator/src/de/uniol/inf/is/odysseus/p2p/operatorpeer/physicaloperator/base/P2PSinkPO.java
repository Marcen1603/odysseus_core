package de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.jxta.peergroup.PeerGroup;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.MessageTool;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;

public class P2PSinkPO<T> extends AbstractSink<T> {
	
	static private Logger logger = LoggerFactory.getLogger(P2PSinkPO.class);
	
	private String queryId;
	private PipeAdvertisement adv;
	public ArrayList<StreamHandler> subscribe = new ArrayList<StreamHandler>();
	private PeerGroup peerGroup;

	class ConnectionListener extends Thread {

		@Override
		public void run() {
			JxtaServerSocket server = null;
			try {
				server = new JxtaServerSocket(getPeerGroup(), adv, 10);
				server.setSoTimeout(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (true) {
				Socket socket = null;
				try {
					logger.debug("Waiting for Server to connect");
					socket = server.accept();
					logger.debug("Connection from "+socket.getRemoteSocketAddress());
					if (socket != null) {
						logger.debug("Adding Handler");
						StreamHandler temp = new StreamHandler(socket);
						temp.start();
						synchronized (subscribe) {
							subscribe.add(temp);
						}
						logger.debug("Adding Handler done");

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class StreamHandler extends Thread{
		ObjectInputStream oin;
		OutputStream outS;
		ObjectOutputStream dout;
		Socket socket;

		public StreamHandler(Socket socket) {
			this.socket = socket;
			try {
				outS = socket.getOutputStream();
				dout = new ObjectOutputStream(outS);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while(getSubscribedToSource().get(0)==null){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void transfer(Object o) {
			try {
				// TEST
				dout.writeObject(o);
				dout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void done() {
			try {
				//Signalisieren, dass dieser Peer fertig ist.
				dout.writeObject(new Integer(0));
				dout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
				dout.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		@Override
		public void run() {
			while (!interrupted()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		logger.debug("Sending object "+object);
		synchronized (subscribe) {
			for (StreamHandler sh : subscribe) {
				logger.debug("to "+sh.socket.getRemoteSocketAddress());
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

	@Override
	public P2PSinkPO<T> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}


	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		throw new RuntimeException("process punctuation not implemented");
	}




}
